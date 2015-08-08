package io.cyberstock.tcdop.server.integration.digitalocean.storage.impl;

import com.intellij.openapi.diagnostic.Logger;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService;
import io.cyberstock.tcdop.server.integration.digitalocean.storage.CloudImageStorage;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * Created by beolnix on 27/06/15.
 */
public class CloudImageStorageImpl implements CloudImageStorage {

    // dependencies
    private final DOClientService clientService;
    private final Executor executor;

    // state
    private volatile Map<String, DOCloudImage> imageMap = new HashMap<String, DOCloudImage>();
    private volatile Integer instancesCount = 0;
    private volatile CloudImagesChecker checker = new CloudImagesChecker();
    private volatile Long lastUpdateTimestamp;
    private volatile boolean stop = false;
    private volatile boolean initialized = false;

    // constants
    private final static Integer UPDATE_INTERVAL = 30 * 1000;
    private final static Integer INITIALIZATION_CHECK_INTERVAL = 50;
    private final static Integer INITIALIZATION_THRESHOLD = 90 * 1000;

    private static final Logger LOG = Logger.getInstance(CloudImageStorageImpl.class.getName());

    public CloudImageStorageImpl(DOClientService clientService, Executor executor) {
        this.clientService = clientService;
        this.executor = executor;
    }

    void init() {
        executor.execute(checker);
    }

    private class CloudImagesChecker implements Runnable {
        public void run() {
            while (!stop) {
                if (shouldUpdate()) {
                    updateImages();
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOG.error("Abort background images checks, setup new checker.", e);
                    init();
                    return;
                }
            }

        }

        private boolean shouldUpdate() {
            Long currentTimestamp = System.currentTimeMillis();
            return lastUpdateTimestamp == null ||
                    (currentTimestamp - lastUpdateTimestamp) >= UPDATE_INTERVAL;
        }
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void waitInitialization() throws DOError {
        long initializationDelay = 0;
        while (!initialized) {

            if (initializationDelay > INITIALIZATION_THRESHOLD) {
                String msg = "initialization took too long. Cant retrieve images from Digital Ocean in 1m 30s, something wrong.";
                LOG.error(msg);
                throw new DOError(msg);
            }

            try {
                initializationDelay += INITIALIZATION_CHECK_INTERVAL;
                Thread.sleep(INITIALIZATION_CHECK_INTERVAL);
            } catch (InterruptedException e) {
                return;
            }
        }
    }

    synchronized private void updateImages() {
        LOG.debug("updating images cache.");

        Map<String, DOCloudImage> newImageMap = getImagesMapFromServer();

        instancesCount = calculateInstances(newImageMap);
        LOG.debug(newImageMap.size() + " images contains: " + instancesCount);

        mergeOldImagesToNewImagesMap(newImageMap, imageMap);

        lastUpdateTimestamp = System.currentTimeMillis();
        initialized = true;
    }


    private Integer calculateInstances(Map<String, DOCloudImage> newImageMap) {
        int instancesCounter = 0;

        for (DOCloudImage image : newImageMap.values()) {
            instancesCounter += image.getInstances().size();
        }

        return instancesCounter;
    }

    private Map<String, DOCloudImage> getImagesMapFromServer() {
        List<DOCloudImage> images = clientService.getImages();
        LOG.debug(images.size() + " images got.");

        Map<String, DOCloudImage> newImageMap = new HashMap<String, DOCloudImage>();

        for (DOCloudImage image : images) {
            newImageMap.put(image.getId(), image);
        }

        return newImageMap;
    }

    /**
     * Merges state of already downloaded images with downloaded images from the server
     * It is important do not lose state of images which are already downloaded because they consist of
     * TeamCity specific status info like InstanceStatus or CloudErrorInfo
     * @param newImageMap
     * @param oldMap
     */
    private void mergeOldImagesToNewImagesMap(Map<String, DOCloudImage> newImageMap, Map<String, DOCloudImage> oldMap) {
        for (DOCloudImage image : oldMap.values()) {
            if (newImageMap.containsKey(image.getId())) {
                newImageMap.put(image.getId(), image);
            }
        }
        imageMap = newImageMap;
    }

    public Collection<DOCloudImage> getImagesList() {
        return imageMap.values();
    }

    public Integer getInstancesCount() {
        return instancesCount;
    }

    public DOCloudImage getImageById(String imageId) {
        return imageMap.get(imageId);
    }

    public void shutdownStorage() {
        this.stop = true;
    }
}
