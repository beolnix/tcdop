package io.cyberstock.tcdop.server.integration.digitalocean.tasks;

import com.google.common.base.Optional;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Image;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.digitalocean.DOCallback;
import io.cyberstock.tcdop.server.integration.digitalocean.DOUtils;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudInstance;
import jetbrains.buildServer.clouds.CloudErrorInfo;
import jetbrains.buildServer.clouds.InstanceStatus;



/**
 * Created by beolnix on 24/05/15.
 */
public class LaunchNewInstanceTask implements Runnable {

    private DOCallback<Droplet, DOError> callback;
    private DigitalOceanClient doClient;
    private TCCloudInstance cloudInstance;

    public LaunchNewInstanceTask(DigitalOceanClient doClient, DOCallback<Droplet, DOError> callback) {
        this.callback = callback;
        this.doClient = doClient;
    }

    public void run() {
        Optional<Droplet> dropletOpt;

        try {
            dropletOpt = DOUtils.findDropletByName(doClient, cloudInstance.getName());
        } catch (Exception e) {
            cloudInstance.updateStatus(InstanceStatus.ERROR);
            cloudInstance.updateErrorInfo(new CloudErrorInfo(e.getMessage(), "Can't get list of droplets from DO", e));
            return;
        }
        if (dropletOpt.isPresent()) {
            startDroplet(dropletOpt.get());
        } else {

            try {
                createAndStartDroplet();
            } catch (DOError e) {
                cloudInstance.updateStatus(InstanceStatus.ERROR);
                cloudInstance.updateErrorInfo(new CloudErrorInfo(e.getMessage(), "DO communication error.", e));
                return;
            }
        }
    }

    private void createAndStartDroplet() throws DOError {

        Optional<Image> imageOpt = null;

        try {
            imageOpt = DOUtils.findImageByName(doClient, cloudInstance.getImageId());
        } catch (Exception e) {
            cloudInstance.updateStatus(InstanceStatus.ERROR);
            cloudInstance.updateErrorInfo(new CloudErrorInfo(e.getMessage(), "Can't find image by name: " + cloudInstance.getImageId(), e));
            return;
        }

        if (!imageOpt.isPresent()) {
            throw new DOError("Image with id \"" + cloudInstance.getImageId() + "\" isn't found in user images.");
        }

        Droplet droplet = new Droplet();
        droplet.setName(cloudInstance.getName());
        droplet.setImage(imageOpt.get());

        Droplet createdDroplet = null;
        try {
            createdDroplet = doClient.createDroplet(droplet);
            return;
        } catch (Exception e) {
            cloudInstance.updateStatus(InstanceStatus.ERROR);
            cloudInstance.updateErrorInfo(new CloudErrorInfo(e.getMessage(), "Can't create droplet using image: " + cloudInstance.getImageId(), e));
        }

        startDroplet(createdDroplet);
    }

    private void startDroplet(Droplet droplet) {
        if (droplet.isOff()) {
            cloudInstance.updateStatus(InstanceStatus.SCHEDULED_TO_START);
            try {
                doClient.powerOnDroplet(droplet.getId());
            } catch (Exception e) {
                cloudInstance.updateStatus(InstanceStatus.ERROR);
                cloudInstance.updateErrorInfo(new CloudErrorInfo(e.getMessage(), "Can't launch droplet", e));
            }
            cloudInstance.updateStatus(InstanceStatus.STARTING);
        } else {
            cloudInstance.updateStatus(InstanceStatus.RUNNING);
        }
    }

}
