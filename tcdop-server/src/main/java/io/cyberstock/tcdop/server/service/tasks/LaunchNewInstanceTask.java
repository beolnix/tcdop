package io.cyberstock.tcdop.server.service.tasks;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Droplets;
import com.myjeeva.digitalocean.pojo.Image;
import com.myjeeva.digitalocean.pojo.Images;
import io.cyberstock.tcdop.server.DOCloudInstance;
import jetbrains.buildServer.clouds.CloudErrorInfo;
import jetbrains.buildServer.clouds.InstanceStatus;

import java.util.List;

/**
 * Created by beolnix on 24/05/15.
 */
public class LaunchNewInstanceTask implements Runnable {

    private DOCloudInstance cloudInstance;
    private DigitalOceanClient doClient;

    public LaunchNewInstanceTask(DOCloudInstance cloudInstance, DigitalOceanClient doClient) {
        this.cloudInstance = cloudInstance;
        this.doClient = doClient;
    }

    public void run() {
        Droplet droplet = null;

        try {
            droplet = DOUtils.findDropletByName(doClient, cloudInstance.getName());
        } catch (Exception e) {
            cloudInstance.updateStatus(InstanceStatus.ERROR);
            cloudInstance.updateErrorInfo(new CloudErrorInfo(e.getMessage(), "Can't get list of droplets from DO", e));
            return;
        }
        if (droplet != null) {
            startDroplet(droplet);
        } else {
            createAndStartDroplet();
        }
    }

    private void createAndStartDroplet() {

        Image image = null;

        try {
            image = findImageByName(cloudInstance.getImageId());
        } catch (Exception e) {
            cloudInstance.updateStatus(InstanceStatus.ERROR);
            cloudInstance.updateErrorInfo(new CloudErrorInfo(e.getMessage(), "Can't find image by name: " + cloudInstance.getImageId(), e));
            return;
        }

        Droplet droplet = new Droplet();
        droplet.setName(cloudInstance.getName());
        droplet.setImage(image);

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

    private Image findImageByName(String imageName) throws Exception {
        int pageNumber = 0;
        while (true) {
            Images images = doClient.getAvailableImages(pageNumber);
            List<Image> imageList = images.getImages();
            if (imageList.isEmpty()) {
                break;
            } else {
                for (Image image : imageList) {
                    if (image.getName().equals(imageName)) {
                        return image;
                    }
                }
            }

            ++pageNumber;
        }

        return null;
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
