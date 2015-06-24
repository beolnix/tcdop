package io.cyberstock.tcdop.server.integration.teamcity;

import com.myjeeva.digitalocean.pojo.Image;
import io.cyberstock.tcdop.model.DOSettings;
import jetbrains.buildServer.clouds.CloudErrorInfo;
import jetbrains.buildServer.clouds.CloudImage;
import jetbrains.buildServer.clouds.CloudInstance;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by beolnix on 16/05/15.
 */
public class TCCloudImage implements CloudImage {

    // Dependencies
    private Image image = null;
    private String id;
    private String name;
    private DOSettings doSettings;

    // State
    private List<TCCloudInstance> instances = new ArrayList<TCCloudInstance>();
    private CloudErrorInfo cloudErrorInfo;

    public TCCloudImage(CloudImage cloudImage, DOSettings doSettings) {
        this.id = cloudImage.getId();
        this.name = cloudImage.getName();
        this.doSettings = doSettings;
    }


    @NotNull
    public String getId() {
        if (image != null) {
            return image.getId().toString();
        } else if (id != null) {
            return id;
        } else {
            return null;
        }
    }

    @NotNull
    public String getName() {
        if (image != null) {
            return image.getName();
        } else if (name != null){
            return name;
        } else {
            return doSettings.getImageName();
        }
    }

    public Image getDOImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
        this.id = image.getId().toString();
        this.name = image.getName();
    }

    @NotNull
    public Collection<? extends CloudInstance> getInstances() {
        return instances;
    }

    public void addInstance(TCCloudInstance cloudInstance) {
        instances.add(cloudInstance);
    }

    @Nullable
    public CloudInstance findInstanceById(String s) {
        for (TCCloudInstance cloudInstance : instances) {
            if (s.equals(cloudInstance.getInstanceId())) {
                return cloudInstance;
            }
        }

        return null;
    }

    @Nullable
    public CloudErrorInfo getErrorInfo() {
        return cloudErrorInfo;
    }

    public void setCloudErrorInfo(CloudErrorInfo cloudErrorInfo) {
        this.cloudErrorInfo = cloudErrorInfo;
    }
}
