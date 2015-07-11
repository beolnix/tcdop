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
    private final Map<String, DOCloudInstance> instances = new HashMap<String, DOCloudInstance>();

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
        return instances.values();
    }

    public void addInstance(DOCloudInstance cloudInstance) {
        instances.put(cloudInstance.getInstanceId(), cloudInstance);
        LOG.debug("New instance " + cloudInstance.getInstanceId() + " has been added to the image: " + this.getId());
    }

    public void removeInstance(DOCloudInstance cloudInstance) {
        instances.remove(cloudInstance.getInstanceId());
        LOG.info("Instance " + cloudInstance.getInstanceId() + " has been removed from the image: " + this.getId());
    }

    @Nullable
    public CloudInstance findInstanceById(String s) {
        return instances.get(s);
    }

    @Nullable
    public CloudErrorInfo getErrorInfo() {
        return cloudErrorInfo;
    }

    public void setCloudErrorInfo(CloudErrorInfo cloudErrorInfo) {
        this.cloudErrorInfo = cloudErrorInfo;
    }
}
