package io.cyberstock.tcdop.server.integration.digitalocean;

import com.intellij.openapi.diagnostic.Logger;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudImage;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * Created by beolnix on 27/06/15.
 */
public class CloudImageStorage {

    // dependencies
    private final DOClientService clientService;
    private final Executor executor;

    // state
    private volatile Map<String, TCCloudImage> imageMap = new HashMap<String, TCCloudImage>();
    private volatile Integer instancesCount = 0;
    private volatile CloudImagesChecker checker = new CloudImagesChecker();
    private volatile boolean stop = false;

    // constants
    private final static Integer CHECK_INTERVAL = 30 * 1000;
    private static final Logger LOG = Logger.getInstance(CloudImageStorage.class.getName());

    public CloudImageStorage(DOClientService clientService, Executor executor) {
        this.clientService = clientService;
        this.executor = executor;
    }

    public void init() {
        updateImages();
        executor.execute(checker);
    }

    private class CloudImagesChecker implements Runnable {
        public void run() {
            while (!stop) {
                updateImages();
                try {
                    Thread.sleep(CHECK_INTERVAL);
                } catch (InterruptedException e) {
                    LOG.error("Abort background images checks, setup new checker.", e);
                    init();
                    return;
                }
            }

        }
    }

    public void forceUpdate() {
        updateImages();
    }

    synchronized private void updateImages() {
        LOG.debug("updating images cache.");

        Map<String, TCCloudImage> newImageMap = getImagesMapFromServer();

        instancesCount = calculateInstances(newImageMap);
        LOG.debug(newImageMap.size() + " images contains: " + instancesCount);

        mergeOldImagesToNewImagesMap(newImageMap, imageMap);
    }

    private Integer calculateInstances(Map<String, TCCloudImage> newImageMap) {
        int instancesCounter = 0;

        for (TCCloudImage image : newImageMap.values()) {
            instancesCounter += image.getInstances().size();
        }

        return instancesCounter;
    }

    private Map<String, TCCloudImage> getImagesMapFromServer() {
        List<TCCloudImage> images = clientService.getImages();
        LOG.debug(images.size() + " images got.");

        Map<String, TCCloudImage> newImageMap = new HashMap<String, TCCloudImage>();

        for (TCCloudImage image : images) {
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
    private void mergeOldImagesToNewImagesMap(Map<String, TCCloudImage> newImageMap, Map<String, TCCloudImage> oldMap) {
        for (TCCloudImage image : oldMap.values()) {
            if (newImageMap.containsKey(image.getId())) {
                newImageMap.put(image.getId(), image);
            }
        }
        imageMap = newImageMap;
    }

    public Collection<TCCloudImage> getImagesList() {
        return imageMap.values();
    }

    public Integer getInstancesCount() {
        return instancesCount;
    }

    public TCCloudImage getImageById(String imageId) {
        return imageMap.get(imageId);
    }

    public void shutdownStorage() {
        this.stop = true;
    }
}
