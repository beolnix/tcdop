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
package io.cyberstock.tcdop.server.integration.digitalocean.storage;

import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage;

import java.util.Collection;

/**
 * Storage is used to cache images available in Digital Ocean.
 * Cache is automatically updated in a background.
 *
 * Created by beolnix on 01/08/15.
 */
public interface CloudImageStorage {

    /**
     * Returns is storage initialized yet or not.
     * @return initialization flag
     */
    boolean isInitialized();

    /**
     * Blocks the thread till the storage is initialized
     * @throws DOError throws an error in case of any issue
     */
    void waitInitialization() throws DOError;

    /**
     * Method returns cached images list
     * @return cached images list
     */
    Collection<DOCloudImage> getImagesList();

    /**
     * Method returns currently available droplets client on a Digital Ocean
     * @return user droplets counter
     */
    Integer getInstancesCount();

    /**
     * Method returns cached image details by imageId
     * @param imageId image id to get the image
     * @return image details in TeamCity format
     */
    DOCloudImage getImageById(String imageId);

    /**
     * Shuts down the storage and all background cache refresh processes of it.
     */
    void shutdownStorage();

    /**
     * Method forces recalculation of newly created instances.
     */
    void countNewInstance();
}
