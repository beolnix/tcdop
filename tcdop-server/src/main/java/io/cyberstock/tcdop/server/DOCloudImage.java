package io.cyberstock.tcdop.server;

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

    public DOCloudImage(CloudImage cloudImage) {
        this.cloudImage = cloudImage;
    }

    @NotNull
    public String getId() {
        return null;
    }

    @NotNull
    public String getName() {
        return null;
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
