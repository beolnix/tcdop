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
package io.cyberstock.tcdop.server.integration.teamcity;

import com.intellij.openapi.diagnostic.Logger;
import com.myjeeva.digitalocean.pojo.Image;
import jetbrains.buildServer.clouds.CloudErrorInfo;
import jetbrains.buildServer.clouds.CloudImage;
import jetbrains.buildServer.clouds.CloudInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Created by beolnix on 16/05/15.
 */
public class DOCloudImage implements CloudImage {

    // Dependencies
    private final Image image;
    private final String id;
    private final String name;
    private final Map<String, DOCloudInstance> instancesMap = new HashMap<String, DOCloudInstance>();

    // State
    private CloudErrorInfo cloudErrorInfo;

    // constants
    private static final Logger LOG = Logger.getInstance(DOCloudImage.class.getName());

    public DOCloudImage(Image image) {
        this.id = image.getId().toString();
        this.name = image.getName();
        this.image = image;
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    @NotNull
    public Collection<? extends CloudInstance> getInstances() {
        return instancesMap.values();
    }

    public void addInstance(DOCloudInstance cloudInstance) {
        instancesMap.put(cloudInstance.getInstanceId(), cloudInstance);
        LOG.debug("New instance " + cloudInstance.getInstanceId() + " has been added to the image: " + this.getId());
    }

    public void removeInstance(DOCloudInstance cloudInstance) {
        instancesMap.remove(cloudInstance.getInstanceId());
        LOG.info("Instance " + cloudInstance.getInstanceId() + " has been removed from the image: " + this.getId());
    }

    @Nullable
    public CloudInstance findInstanceById(String s) {
        return instancesMap.get(s);
    }

    @Nullable
    public CloudErrorInfo getErrorInfo() {
        return cloudErrorInfo;
    }

    public void setCloudErrorInfo(CloudErrorInfo cloudErrorInfo) {
        this.cloudErrorInfo = cloudErrorInfo;
    }
}
