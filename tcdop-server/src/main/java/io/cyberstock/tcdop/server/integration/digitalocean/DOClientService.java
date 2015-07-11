package io.cyberstock.tcdop.server.integration.digitalocean;

import com.google.common.base.Optional;
import com.intellij.openapi.diagnostic.Logger;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Image;
import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.DropletConfig;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudImage;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudInstance;
import jetbrains.buildServer.clouds.CloudErrorInfo;
import jetbrains.buildServer.clouds.InstanceStatus;

import java.util.*;


/**
 * Created by beolnix on 24/06/15.
 */
public class DOClientService {

    //  dependencies
    private final DigitalOceanClient doClient;

    // constants
    private static final Logger LOG = Logger.getInstance(DOAsyncClientServiceWrapper.class.getName());

    public DOClientService(DigitalOceanClient doClient) {
        this.doClient = doClient;
    }

    public List<TCCloudImage> getImages() {
        try {
            List<Image> images = DOUtils.getImages(doClient);
            List<Droplet> droplets = DOUtils.getDroplets(doClient);
            List<TCCloudImage> result = new ArrayList<TCCloudImage>(images.size());
            for (Image image : images) {
                TCCloudImage cloudImage = new TCCloudImage(image);

                for (Droplet droplet : droplets) {
                    if (droplet.getImage() != null && droplet.getImage().getId().equals(image.getId())) {
                        TCCloudInstance cloudInstance = new TCCloudInstance(cloudImage, droplet);
                        cloudImage.addInstance(cloudInstance);

                    }
                }

                result.add(cloudImage);

            }
            return result;
        } catch (DOError e) {
            LOG.error("Can't download DO images: " + e.getMessage(), e);
            return Collections.EMPTY_LIST;
        }
    }

    public void restartInstance(TCCloudInstance cloudInstance) {
        Integer instanceId = Integer.parseInt(cloudInstance.getInstanceId());
        cloudInstance.updateStatus(InstanceStatus.RESTARTING);
        try {
            Date restartTime = DOUtils.restartInstance(doClient, instanceId);
            cloudInstance.setStartTime(restartTime);
            cloudInstance.updateStatus(InstanceStatus.RUNNING);
            LOG.info("Instance " + instanceId + " restarted successfully");
        } catch (DOError e) {
            cloudInstance.updateStatus(InstanceStatus.ERROR);
            cloudInstance.updateErrorInfo(new CloudErrorInfo("Can't restart instance with id: " + instanceId, e.getMessage()));
            LOG.error("Can't restart instance " + instanceId + " because of: " + e.getMessage(), e);
        }
    }

    public void terminateInstance(final TCCloudInstance cloudInstance) {
        Integer instanceId = Integer.parseInt(cloudInstance.getInstanceId());
        cloudInstance.updateStatus(InstanceStatus.SCHEDULED_TO_STOP);
        try {
            boolean successFlag = DOUtils.terminateInstance(doClient, instanceId);
            if (successFlag) {
                ((TCCloudImage)cloudInstance.getImage()).removeInstance(cloudInstance);
                cloudInstance.updateStatus(InstanceStatus.STOPPED);
                LOG.info("Cloud instance " + cloudInstance.getName() + " stopped successfully.");
            } else {
                cloudInstance.updateStatus(InstanceStatus.ERROR_CANNOT_STOP);
                cloudInstance.updateErrorInfo(new CloudErrorInfo("Can't terminate instance with id: " + instanceId));
                LOG.error("Cloud instance " + cloudInstance.getName() + " can't be stopped.");
            }
        } catch (DOError e) {
            cloudInstance.updateStatus(InstanceStatus.ERROR_CANNOT_STOP);
            cloudInstance.updateErrorInfo(new CloudErrorInfo("Can't stop instance with id: " + instanceId, e.getMessage()));
            LOG.error("Cloud instance " + cloudInstance.getName() + " can't be stopped: " + e.getMessage());
        }
    }

    public TCCloudInstance createInstance(TCCloudImage cloudImage, DOSettings doSettings) throws DOError {
        DropletConfig dropletConfig = doSettings.getDropletConfig();

        Droplet droplet = DOUtils.createInstance(doClient, dropletConfig, cloudImage);
        TCCloudInstance cloudInstance = new TCCloudInstance(cloudImage, droplet);
        cloudImage.addInstance(cloudInstance);
        return cloudInstance;
    }

}
