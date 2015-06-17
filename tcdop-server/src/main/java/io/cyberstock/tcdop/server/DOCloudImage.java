package io.cyberstock.tcdop.server;

import com.myjeeva.digitalocean.pojo.Image;
import jetbrains.buildServer.clouds.CloudErrorInfo;
import jetbrains.buildServer.clouds.CloudImage;
import jetbrains.buildServer.clouds.CloudInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created by beolnix on 16/05/15.
 */
public class DOCloudImage implements CloudImage {

    private CloudImage cloudImage;
    private String id;
    private String name;

    public DOCloudImage(CloudImage cloudImage) {
        this.cloudImage = cloudImage;
    }

    public DOCloudImage(Image image) {
        this.id = image.getId().toString();
        this.name = image.getName();
    }

    @NotNull
    public String getId() {
        return id;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public Collection<? extends CloudInstance> getInstances() {
        return null;
    }

    @Nullable
    public CloudInstance findInstanceById(String s) {
        return null;
    }

    @Nullable
    public CloudErrorInfo getErrorInfo() {
        return null;
    }
}
