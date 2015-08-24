/* The MIT License
 *
 * Copyright (c) 2010-2015 Danila A. (atmakin.dv@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package io.cyberstock.tcdop.server.integration.digitalocean.storage.impl;

import com.intellij.openapi.diagnostic.Logger;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.model.error.DOFatalError;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService;
import io.cyberstock.tcdop.server.integration.digitalocean.storage.CloudImageStorage;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by beolnix on 27/06/15.
 */
public class CloudImageStorageImpl implements CloudImageStorage {

    // dependencies
    private final DOClientService clientService;
    private final Executor executor;
    private final Long initThreashold;

    // state
    private volatile Map<String, DOCloudImage> imageMap = new HashMap<String, DOCloudImage>();
    private volatile CloudImagesChecker checker = new CloudImagesChecker();
    private volatile Long lastUpdateTimestamp;
    private volatile boolean stop = false;
    private volatile boolean initialized = false;

    private AtomicInteger instancesCount = new AtomicInteger(0);

    // constants
    private final static Integer UPDATE_INTERVAL = 30 * 1000;
    private final static Integer INITIALIZATION_CHECK_INTERVAL = 50;

    private static final Logger LOG = Logger.getInstance(CloudImageStorageImpl.class.getName());

    CloudImageStorageImpl(DOClientService clientService,
                                 Executor executor,
                                 Long initThreashold) {
        this.clientService = clientService;
        this.executor = executor;
        this.initThreashold = initThreashold;
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

    public void countNewInstance() {
        instancesCount.incrementAndGet();
    }

    public void waitInitialization() throws DOError {
        long initializationDelay = 0;
        while (!initialized) {

            if (initializationDelay > initThreashold) {
                String msg = "initialization took too long. Cant retrieve images from Digital Ocean in 1m 30s, something wrong.";
                LOG.error(msg);
                throw new DOError(msg);
            }

            try {
                initializationDelay += INITIALIZATION_CHECK_INTERVAL;
                Thread.sleep(INITIALIZATION_CHECK_INTERVAL);
            } catch (InterruptedException e) {
                String msg = "interrupted exception during initialization.";
                throw new DOError(msg);
            }
        }
    }

    synchronized private void updateImages() {
        LOG.debug("updating images cache.");

        Map<String, DOCloudImage> newImageMap = getImagesMapFromServer();
        int newInstancesCount = calculateInstances(newImageMap);
        instancesCount.getAndSet(newInstancesCount);

        LOG.debug(newImageMap.size() + " images contains: " + instancesCount.get() + " instances.");

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
        Map<String, DOCloudImage> newImageMap = new HashMap<String, DOCloudImage>();
        try {
            List<DOCloudImage> images = clientService.getImages();
            LOG.debug(images.size() + " images got.");

            for (DOCloudImage image : images) {
                newImageMap.put(image.getId(), image);
            }

        } catch (DOError e) {
            LOG.error("Can't update images list from server.", e);
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
        return instancesCount.get();
    }

    public DOCloudImage getImageById(String imageId) {
        return imageMap.get(imageId);
    }

    public void shutdownStorage() {
        this.stop = true;
    }
}
