package io.cyberstock.tcdop.server.integration.digitalocean;

import com.intellij.openapi.diagnostic.Logger;
import freemarker.ext.beans.HashAdapter;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudImage;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
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

    // constants
    private final static Integer CHECK_INTERVAL = 60 * 1000;
    private static final Logger LOG = Logger.getInstance(CloudImageStorage.class.getName());

    public CloudImageStorage(DOClientService clientService, Executor executor) {
        this.clientService = clientService;
        this.executor = executor;
    }

    public void init() {
        updateImages();
        executor.execute(new CloudImagesChecker());
    }

    private class CloudImagesChecker implements Runnable {
        public void run() {
            while (true) {
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

    synchronized private void updateImages() {
        List<TCCloudImage> images = clientService.getImages();
        Map<String, TCCloudImage> newImageMap = new HashMap<String, TCCloudImage>();
        for (TCCloudImage image : images) {
            newImageMap.put(image.getId(), image);
        }
        imageMap = newImageMap;
    }

    public Collection<TCCloudImage> getImagesList() {
        return imageMap.values();
    }

    public TCCloudImage getImageById(String imageId) {
        return imageMap.get(imageId);
    }
}
