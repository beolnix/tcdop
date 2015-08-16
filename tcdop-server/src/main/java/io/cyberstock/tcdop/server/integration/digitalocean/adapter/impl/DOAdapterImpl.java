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
import jetbrains.buildServer.clouds.InstanceStatus;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import static io.cyberstock.tcdop.server.integration.digitalocean.adapter.impl.DOAdapterUtils.*;

import javax.annotation.Nullable;
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
    public Droplet createInstance(@NotNull DOSettings doSettings,
                                  @Nullable DOCloudImage cloudImage) throws DOError {
        Droplet droplet = new Droplet();

        configureDropletCommon(droplet, doSettings);
        configureDropletImage(droplet, cloudImage);
        configureRsaKey(droplet, doSettings);

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

    private void configureRsaKey(Droplet droplet, DOSettings doSettings) throws DOError {
        if (StringUtils.isNotBlank(doSettings.getRsaKeyName()) &&
                StringUtils.isNotBlank(doSettings.getPublicRsaKey())) {
            Key key = createSSHKeyOrGet(doSettings.getRsaKeyName(), doSettings.getPublicRsaKey());
            droplet.setKeys(Collections.singletonList(key));
        }
    }

    private void configureDropletImage(Droplet droplet, DOCloudImage cloudImage) {
        if (cloudImage != null) {
            droplet.setRegion(new Region(cloudImage.getImage().getRegions().iterator().next()));
            droplet.setImage(cloudImage.getImage());
        }
    }

    private void configureDropletCommon(Droplet droplet, DOSettings doSettings) {
        droplet.setName(doSettings.getDropletNamePrefix() + "-" + UUID.randomUUID());
        droplet.setSize(doSettings.getSize().getSlug());

        if (StringUtils.isNotBlank(doSettings.getOsImageName())) {
            droplet.setImage(new Image(doSettings.getOsImageName()));
        }

        if (StringUtils.isNotBlank(doSettings.getRegion())) {
            droplet.setRegion(new Region(doSettings.getRegion()));
        }
    }

    public Droplet createInstance(DOSettings doSettings) throws DOError {
        return createInstance(doSettings, null);
    }

    public Key createSSHKeyOrGet(String name, String pubKey) throws DOError {
        Optional<Key> keyOpt = getSSHKeyByName(name);
        if (keyOpt.isPresent() &&
                keyOpt.get().getPublicKey().equals(pubKey)) {
            return keyOpt.get();
        }

        if (keyOpt.isPresent()) {
            name = modifySSHKeyName(name);
        }

        Key key = createSSHKey(name, pubKey);
        return key;
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
            while (!isThresholdReached(start, actionWaitThreshold)) {
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

    private Date waitForActionResult(Action actionInfo) throws DOError {
        long start = System.currentTimeMillis();
        Integer actionId = actionInfo.getId();

        try {
            boolean completed = false;

            while (!isThresholdReached(start, actionWaitThreshold) && !completed) {
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

    public Key createSSHKey(String name, String publicKey) throws DOError {
        Key key = new Key(name, publicKey);
        try {
            Key createdKey = doClient.createKey(key);
            return createdKey;
        } catch (Exception e) {
            throw new DOError("Can't create key: " + key.toString(), e);
        }
    }

    public Optional<Key> getSSHKeyByName(String name) throws DOError {
        List<Key> keys = getKeys();
        for (Key key : keys) {
            if (name.equals(key.getName())) {
                return Optional.of(key);
            }
        }

        return Optional.absent();
    }

    public List<Key> getKeys() throws DOError {
        List<Key> resultList = new LinkedList<Key>();
        int pageNumber = 0;
        try {
            while (true) {
                Keys keys = doClient.getAvailableKeys(pageNumber);
                List<Key> keyList = keys.getKeys();
                if (keyList.isEmpty()) {
                    break;
                } else {
                    resultList.addAll(keyList);
                }

                ++pageNumber;
            }
        } catch (Exception e) {
            throw new DOError("Can't get available keys:" + e.getMessage(), e);
        }

        return resultList;
    }

    public InstanceStatus transformStatus(DropletStatus dropletStatus) {
        return transformStatusOfDroplet(dropletStatus);
    }
}
