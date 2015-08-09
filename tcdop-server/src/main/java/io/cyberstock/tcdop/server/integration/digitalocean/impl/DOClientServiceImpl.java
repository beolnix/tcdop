package io.cyberstock.tcdop.server.integration.digitalocean.impl;

import com.google.common.base.Optional;
import com.intellij.openapi.diagnostic.Logger;
import com.myjeeva.digitalocean.DigitalOcean;
import com.myjeeva.digitalocean.common.DropletStatus;
import com.myjeeva.digitalocean.pojo.Account;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Image;
import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService;
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.DOAdapter;
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.impl.DOAdapterImpl;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudInstance;
import jetbrains.buildServer.clouds.CloudErrorInfo;
import jetbrains.buildServer.clouds.InstanceStatus;

import java.util.*;


/**
 * Created by beolnix on 24/06/15.
 */
public class DOClientServiceImpl implements DOClientService {

    // dependencies
    private final DOAdapter doAdapter;

    // constants
    private static final Logger LOG = Logger.getInstance(DOClientServiceImpl.class.getName());

    // state
    private volatile Boolean denyNewInstancesCreation = false;

    DOClientServiceImpl(DOAdapter doAdapter) {
        this.doAdapter = doAdapter;
    }

    public void waitInstanceInitialization(DOCloudInstance cloudInstance) {
        Integer dropletId = Integer.parseInt(cloudInstance.getInstanceId());
        cloudInstance.updateStatus(InstanceStatus.STARTING);
        LOG.debug("Starting instance: " + dropletId);
        try {
            String ipv4 = doAdapter.waitForDropletInitialization(dropletId);
            cloudInstance.updateNetworkIdentity(ipv4);
            cloudInstance.updateStatus(InstanceStatus.RUNNING);
            cloudInstance.setStartTime(new Date());
            LOG.info("Instance " + dropletId + " has been started successfully.");
        } catch (DOError e) {
            LOG.error("Instance can't be initializated " + dropletId + " because of: " + e.getMessage());
            cloudInstance.updateStatus(InstanceStatus.ERROR);
            cloudInstance.updateErrorInfo(new CloudErrorInfo("Instance can't be initializated " + dropletId + " because of: " + e.getMessage()));
            denyNewInstancesCreation = true;
        }
    }

    public void restartInstance(DOCloudInstance cloudInstance) {
        Integer instanceId = Integer.parseInt(cloudInstance.getInstanceId());
        cloudInstance.updateStatus(InstanceStatus.RESTARTING);
        LOG.debug("Restarting instance " + instanceId);
        try {
            Date restartTime = doAdapter.restartInstance(instanceId);
            cloudInstance.setStartTime(restartTime);
            cloudInstance.updateStatus(InstanceStatus.RUNNING);
            LOG.info("Instance " + instanceId + " has been restarted successfully");
        } catch (DOError e) {
            cloudInstance.updateStatus(InstanceStatus.ERROR);
            cloudInstance.updateErrorInfo(new CloudErrorInfo("Can't restart instance with id: " + instanceId, e.getMessage()));
            LOG.error("Can't restart instance " + instanceId + " because of: " + e.getMessage());
        }
    }

    public void terminateInstance(final DOCloudInstance cloudInstance) {
        Integer instanceId = Integer.parseInt(cloudInstance.getInstanceId());
        cloudInstance.updateStatus(InstanceStatus.SCHEDULED_TO_STOP);
        try {
            boolean successFlag = doAdapter.terminateInstance(instanceId);
            if (successFlag) {
                ((DOCloudImage)cloudInstance.getImage()).removeInstance(cloudInstance);
                cloudInstance.updateStatus(InstanceStatus.STOPPED);
                LOG.info("Cloud instance " + cloudInstance.getName() + " has been stopped successfully.");
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

    public DOCloudInstance createInstance(DOCloudImage cloudImage, DOSettings doSettings) throws DOError {

        if (denyNewInstancesCreation) {
            throw new DOError("New Instances creation is denied because of previous errors.");
        }

        Droplet droplet = doAdapter.createInstance(doSettings, cloudImage);
        DOCloudInstance cloudInstance = new DOCloudInstance(cloudImage, droplet.getId().toString(), droplet.getName());
        cloudImage.addInstance(cloudInstance);
        return cloudInstance;
    }

    public void accountCheck() throws DOError {
        Account account = doAdapter.checkAccount();
        if (account == null) {
            throw new DOError("Can't get account info from the server.");
        }
    }

    public DOCloudImage findImageByName(String imageName) throws DOError {
        Optional<Image> imageOpt = doAdapter.findImageByName(imageName);
        if (!imageOpt.isPresent()) {
            throw new DOError("Image with name \"" + imageName + "\" not found in user images.");
        }

        return new DOCloudImage(imageOpt.get());
    }

    public List<DOCloudImage> getImages() throws DOError {
        List<Image> images = doAdapter.getImages();
        List<Droplet> droplets = doAdapter.getDroplets();
        List<DOCloudImage> result = new ArrayList<DOCloudImage>(images.size());
        for (Image image : images) {
            DOCloudImage cloudImage = new DOCloudImage(image);

            for (Droplet droplet : droplets) {
                if (droplet.getImage() != null && droplet.getImage().getId().equals(image.getId())) {
                    DOCloudInstance cloudInstance = new DOCloudInstance(cloudImage, droplet.getId().toString(), droplet.getName());
                    cloudInstance.updateStatus(doAdapter.transformStatus(droplet.getStatus()));
                    cloudInstance.updateNetworkIdentity(droplet.getNetworks().getVersion4Networks().get(0).getIpAddress());
                    cloudImage.addInstance(cloudInstance);
                }
            }

            result.add(cloudImage);

        }
        return result;
    }

}
