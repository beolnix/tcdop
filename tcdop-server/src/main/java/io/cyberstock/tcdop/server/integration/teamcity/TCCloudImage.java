package io.cyberstock.tcdop.server.integration.teamcity;

import com.intellij.openapi.diagnostic.Logger;
import com.myjeeva.digitalocean.pojo.Image;
import io.cyberstock.tcdop.model.DOSettings;
import jetbrains.buildServer.clouds.CloudErrorInfo;
import jetbrains.buildServer.clouds.CloudImage;
import jetbrains.buildServer.clouds.CloudInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

/**
 * Created by beolnix on 16/05/15.
 */
public class TCCloudImage implements CloudImage {

    // Dependencies
    private final Image image;
    private final String id;
    private final String name;
    private final Map<String, TCCloudInstance> instances = new HashMap<String, TCCloudInstance>();

    // State
    private CloudErrorInfo cloudErrorInfo;

    // constants
    private static final Logger LOG = Logger.getInstance(TCCloudImage.class.getName());

    public TCCloudImage(Image image) {
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

    public void addInstance(TCCloudInstance cloudInstance) {
        instances.put(cloudInstance.getInstanceId(), cloudInstance);
        LOG.debug("New instance " + cloudInstance.getInstanceId() + " has been added to the image: " + this.getId());
    }

    public void removeInstance(TCCloudInstance cloudInstance) {
        instances.remove(cloudInstance.getInstanceId());
        LOG.debug("Instance " + cloudInstance.getInstanceId() + " has been removed from the image: " + this.getId());
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
