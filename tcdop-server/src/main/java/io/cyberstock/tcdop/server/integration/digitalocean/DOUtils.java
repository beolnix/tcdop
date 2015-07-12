package io.cyberstock.tcdop.server.integration.digitalocean;

import com.google.common.base.Optional;
import com.intellij.openapi.diagnostic.Logger;
import com.myjeeva.digitalocean.common.ActionStatus;
import com.myjeeva.digitalocean.common.DropletStatus;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.*;
import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by beolnix on 24/05/15.
 */
public class DOUtils {

    // constants
    private static final Logger LOG = Logger.getInstance(DOUtils.class.getName());
    private final static Integer ACTION_RESULT_CHECK_INTERVAL = 2 * 1000;

    @NotNull
    public static Droplet createInstance(DigitalOceanClient doClient, DOSettings doSettings, DOCloudImage cloudImage) throws DOError {
        Droplet droplet = new Droplet();
        droplet.setName(doSettings.getDropletNamePrefix() + "-" + UUID.randomUUID());
        droplet.setRegion(new Region(cloudImage.getImage().getRegions().iterator().next()));
        droplet.setSize(doSettings.getSize().getSlug());
        droplet.setImage(cloudImage.getImage());

        try {
            Droplet createdDroplet = doClient.createDroplet(droplet);
            LOG.info("Droplet created successfully: " + droplet.getId());
            return createdDroplet;
        } catch (Exception e) {
            LOG.error("Can't create droplet:" + e.getMessage(), e);
            throw new DOError("Can't create droplet:" + e.getMessage(), e);
        }
    }

    public static String waitForDropletInitialization(DigitalOceanClient doClient, Integer dropletId) throws DOError {
        try {
            while(true) {
                Droplet droplet = doClient.getDropletInfo(dropletId);
                String ipv4 = getIpv4(droplet);
                if (isDropletActive(droplet) && StringUtils.isNotEmpty(ipv4)) {
                    return ipv4;
                } else {
                    Thread.sleep(ACTION_RESULT_CHECK_INTERVAL);
                }

            }
        } catch (Exception e) {
            LOG.error("Can't get dropletInfo of dropletId: " + dropletId , e);
            throw new DOError("Can't get dropletInfo of dropletId: " + dropletId , e);
        }
    }

    private static String getIpv4(Droplet droplet) {
        if (droplet != null &&
                droplet.getNetworks() != null &&
                droplet.getNetworks().getVersion4Networks() != null &&
                droplet.getNetworks().getVersion4Networks().size() > 0 &&
                droplet.getNetworks().getVersion4Networks().get(0) != null
                ) {
            return droplet.getNetworks().getVersion4Networks().get(0).getIpAddress();
        }

        return null;
    }

    private static boolean isDropletActive(Droplet droplet) {
        if (droplet == null) {
            return false;
        }
        return DropletStatus.ACTIVE.equals(droplet.getStatus());
    }

    private static Date waitForActionResult(DigitalOceanClient doClient, Action actionInfo) throws DOError {
        Integer actionId = actionInfo.getId();
        try {
            while (ActionStatus.IN_PROGRESS.equals(actionInfo.getStatus())) {
                actionInfo = doClient.getActionInfo(actionId);
                Thread.sleep(ACTION_RESULT_CHECK_INTERVAL);
            }

            if (ActionStatus.COMPLETED.equals(actionInfo.getStatus())) {
                return actionInfo.getCompletedAt();
            } else {
                LOG.error("Action hasn't been completed successfully");
                throw new DOError("Action hasn't been completed successfully");
            }

        } catch (Exception e) {
            LOG.error("Can't get actionInfo with id: " + actionId , e);
            throw new DOError("Can't get actionInfo with id: " + actionId , e);
        }
    }

    public static Boolean terminateInstance(DigitalOceanClient doClient, Integer instanceId) throws DOError {
        try {
            Delete delete = doClient.deleteDroplet(instanceId);
            return delete.getIsRequestSuccess();
        } catch (Exception e) {
            LOG.error("Can't stop instance with id: " + instanceId + " because: " + e.getMessage(), e);
            throw new DOError("Can't stop instance with id: " + instanceId , e);
        }
    }

    public static Date restartInstance(DigitalOceanClient doClient, Integer instanceId) throws DOError {
        try {
            Action action = doClient.rebootDroplet(instanceId);
            Date completedAt = waitForActionResult(doClient, action);
            return completedAt;
        } catch (Exception e) {
            LOG.error("Can't restart instance with id: " + instanceId , e);
            throw new DOError("Can't restart instance with id: " + instanceId , e);
        }
    }

    public static Optional<Image> findImageByName(DigitalOceanClient doClient, String imageName) throws DOError{
        int pageNumber = 0;
        while (true) {
            Images images = null;
            try {
                images = doClient.getUserImages(pageNumber);
            } catch (Exception e) {
                LOG.error("Can't find image by name \"" + imageName + "\".", e);
                throw new DOError("Can't find image by name \"" + imageName + "\".", e);
            }
            List<Image> imageList = images.getImages();
            if (imageList.isEmpty()) {
                break;
            } else {
                for (Image image : imageList) {
                    if (image.getName().equals(imageName)) {
                        return getImageById(doClient, image.getId());
                    }
                }
            }

            ++pageNumber;
        }

        return Optional.absent();
    }

    public static Optional<Image> getImageById(DigitalOceanClient doClient, Integer imageId) throws DOError {
        try {
            Image image = doClient.getImageInfo(imageId);
            if (image != null) {
                return Optional.of(image);
            } else {
                return Optional.absent();
            }
        } catch (Exception e) {
            LOG.error("Can't get image info by id: " + imageId, e);
            throw new DOError("Can't get image info by id: " + imageId, e);
        }
    }

    public static List<Image> getImages(DigitalOceanClient doClient) throws DOError {
        List<Image> resultList = new LinkedList<Image>();
        int pageNumber = 0;
        try {
            while (true) {
                Images images = doClient.getUserImages(pageNumber);
                List<Image> imageList = images.getImages();
                if (imageList.isEmpty()) {
                    break;
                } else {
                    resultList.addAll(imageList);
                }

                ++pageNumber;
            }
        } catch (Exception e) {
            LOG.error("Can't get user images:" + e.getMessage(), e);
            throw new DOError("Can't get user images:" + e.getMessage(), e);
        }

        return resultList;
    }

    public static List<Droplet> getDroplets(DigitalOceanClient doClient) throws DOError {
        List<Droplet> resultList = new LinkedList<Droplet>();
        int pageNumber = 0;
        try {
            while (true) {
                Droplets droplets = doClient.getAvailableDroplets(pageNumber);
                List<Droplet> imageList = droplets.getDroplets();
                if (imageList.isEmpty()) {
                    break;
                } else {
                    resultList.addAll(imageList);
                }

                ++pageNumber;
            }
        } catch (Exception e) {
            LOG.error("Can't get available droplets:" + e.getMessage(), e);
            throw new DOError("Can't get available droplets:" + e.getMessage(), e);
        }

        return resultList;
    }
}
