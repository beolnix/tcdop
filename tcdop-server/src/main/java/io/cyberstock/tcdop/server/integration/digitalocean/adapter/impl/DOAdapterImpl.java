package io.cyberstock.tcdop.server.integration.digitalocean.adapter.impl;

import com.google.common.base.Optional;
import com.intellij.openapi.diagnostic.Logger;
import com.myjeeva.digitalocean.DigitalOcean;
import com.myjeeva.digitalocean.common.ActionStatus;
import com.myjeeva.digitalocean.common.DropletStatus;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.pojo.*;
import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.DOAdapter;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudInstance;
import jetbrains.buildServer.clouds.InstanceStatus;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Created by beolnix on 24/05/15.
 */
public class DOAdapterImpl implements DOAdapter {

    // dependencies
    private DigitalOcean doClient;
    private final Integer actionResultCheckInterval;
    private final Long actionWaitThreshold;

    // constants
    private static final Logger LOG = Logger.getInstance(DOAdapterImpl.class.getName());

    public DOAdapterImpl(DigitalOcean doClient, Integer actionResultCheckInterval, Long actionWaitThreshold) {
        this.doClient = doClient;
        this.actionResultCheckInterval = actionResultCheckInterval;
        this.actionWaitThreshold = actionWaitThreshold;
    }

    @NotNull
    public Droplet createInstance(DOSettings doSettings, DOCloudImage cloudImage) throws DOError {
        Droplet droplet = new Droplet();
        droplet.setName(doSettings.getDropletNamePrefix() + "-" + UUID.randomUUID());
        droplet.setRegion(new Region(cloudImage.getImage().getRegions().iterator().next()));
        droplet.setSize(doSettings.getSize().getSlug());
        droplet.setImage(cloudImage.getImage());

        try {
            Droplet createdDroplet = doClient.createDroplet(droplet);
            LOG.info("Droplet creation request sent successfully: " + droplet.getId());
            return createdDroplet;
        } catch (DigitalOceanException e) {
            throw dropletCreationRequestError(e);
        } catch (RequestUnsuccessfulException e) {
            throw dropletCreationRequestError(e);
        }
    }

    private static DOError dropletCreationRequestError(Exception e) {
        String errMsg = "Can't create droplet: " + e.getMessage();
        return new DOError(errMsg, e);
    }

    public Account checkAccount() throws DOError {
        try {
            return doClient.getAccountInfo();
        } catch (DigitalOceanException e) {
            throw new DOError("Token isn't valid.", e);
        } catch (RequestUnsuccessfulException e) {
            throw new DOError("Can't reach Digital Ocean in order to verify token.", e);
        }
    }

    public String waitForDropletInitialization(Integer dropletId) throws DOError {
        long start = System.currentTimeMillis();

        try {
            while (!isThresholdReached(start)) {
                Droplet droplet = doClient.getDropletInfo(dropletId);
                String ipv4 = getIpv4(droplet);
                if (isDropletActive(droplet) && StringUtils.isNotEmpty(ipv4)) {
                    return ipv4;
                } else {
                    Thread.sleep(actionResultCheckInterval);
                }
            }

            throw waitThresholdReached("createDroplet");

        } catch (Exception e) {
            throw new DOError("Can't get dropletInfo of dropletId: " + dropletId, e);
        }
    }

    private boolean isThresholdReached(long start) {
        long current = System.currentTimeMillis();
        long duration = current - start;
        return duration > actionWaitThreshold;
    }

    private String getIpv4(Droplet droplet) {
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

    private Date waitForActionResult(Action actionInfo) throws DOError {
        long start = System.currentTimeMillis();
        Integer actionId = actionInfo.getId();

        try {
            boolean completed = false;

            while (!isThresholdReached(start) && !completed) {
                actionInfo = doClient.getActionInfo(actionId);
                if (ActionStatus.IN_PROGRESS.equals(actionInfo.getStatus())) {
                    Thread.sleep(actionResultCheckInterval);
                } else {
                    completed = true;
                }
            }

            if (!completed) {
                throw waitThresholdReached("waitForActionResult");
            }

            if (ActionStatus.COMPLETED.equals(actionInfo.getStatus())) {
                return actionInfo.getCompletedAt();
            } else {
                String errMsg = "Action hasn't been completed successfully. Action id: " + actionId;
                throw new DOError(errMsg);
            }

        } catch (InterruptedException e) {
            throw createActionError(actionId, e);
        } catch (DigitalOceanException e) {
            throw createActionError(actionId, e);
        } catch (RequestUnsuccessfulException e) {
            throw createActionError(actionId, e);
        }
    }

    private static DOError createActionError(Integer actionId, Exception e) {
        String errMsg = "Can't get actionInfo with id: " + actionId;
        return new DOError(errMsg, e);
    }

    private static DOError waitThresholdReached(String operationName) {
        String errMsg = operationName + ": result wait threshold reached.";
        return new DOError(errMsg);
    }

    public Boolean terminateInstance(Integer instanceId) throws DOError {
        try {
            Delete delete = doClient.deleteDroplet(instanceId);
            return delete.getIsRequestSuccess();
        } catch (Exception e) {
            throw new DOError("Can't stop instance with id: " + instanceId, e);
        }
    }

    public Date restartInstance(Integer instanceId) throws DOError {
        try {
            Action action = doClient.rebootDroplet(instanceId);
            Date completedAt = waitForActionResult(action);
            return completedAt;
        } catch (Exception e) {
            throw new DOError("Can't restart instance with id: " + instanceId, e);
        }
    }

    public Optional<Image> findImageByName(String imageName) throws DOError {
        List<Image> imageList = getImages();
        if (!imageList.isEmpty()) {
            for (Image image : imageList) {
                if (image.getName().equals(imageName)) {
                    return getImageById(image.getId());
                }
            }
        }

        return Optional.absent();
    }

    public Optional<Image> getImageById(Integer imageId) throws DOError {
        try {
            Image image = doClient.getImageInfo(imageId);
            if (image != null) {
                return Optional.of(image);
            } else {
                return Optional.absent();
            }
        } catch (Exception e) {
            throw new DOError("Can't get image info by id: " + imageId, e);
        }
    }

    public InstanceStatus transformStatus(DropletStatus dropletStatus) {
        switch (dropletStatus) {
            case NEW:
                return InstanceStatus.STARTING;
            case ACTIVE:
                return InstanceStatus.RUNNING;
            case ARCHIVE:
                return InstanceStatus.STOPPED;
            case OFF:
                return InstanceStatus.STOPPED;
            default:
                return InstanceStatus.UNKNOWN;
        }
    }

    public List<Image> getImages() throws DOError {
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
            throw new DOError("Can't get user images:" + e.getMessage(), e);
        }

        return resultList;
    }

    public List<Droplet> getDroplets() throws DOError {
        List<Droplet> resultList = new LinkedList<Droplet>();
        int pageNumber = 0;
        try {
            while (true) {
                Droplets droplets = doClient.getAvailableDroplets(pageNumber);
                List<Droplet> dropletsList = droplets.getDroplets();
                if (dropletsList.isEmpty()) {
                    break;
                } else {
                    resultList.addAll(dropletsList);
                }

                ++pageNumber;
            }
        } catch (Exception e) {
            throw new DOError("Can't get available droplets:" + e.getMessage(), e);
        }

        return resultList;
    }
}
