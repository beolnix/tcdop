package io.cyberstock.tcdop.server.integration.teamcity;

import com.myjeeva.digitalocean.pojo.Droplet;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudImage;
import jetbrains.buildServer.clouds.*;
import jetbrains.buildServer.serverSide.AgentDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * Created by beolnix on 16/05/15.
 */
public class TCCloudInstance implements CloudInstance {

    private TCCloudImage cloudImage;
    private CloudInstanceUserData userData;
    private InstanceStatus instanceStatus = InstanceStatus.UNKNOWN;
    private CloudErrorInfo cloudErrorInfo;
    private Droplet droplet;

    public TCCloudInstance(@NotNull TCCloudImage cloudImage,
                           @NotNull CloudInstanceUserData cloudInstanceUserData) {
        this.cloudImage = cloudImage;
        this.userData = cloudInstanceUserData;
    }

    @NotNull
    public String getInstanceId() {
        return null;
    }

    public void updateStatus(InstanceStatus newStatus) {
        this.instanceStatus = newStatus;
    }

    public void updateErrorInfo(CloudErrorInfo errorInfo) {
        cloudErrorInfo = errorInfo;
    }

    @NotNull
    public String getName() {
        return null;
    }

    @NotNull
    public String getImageId() {
        return null;
    }

    @NotNull
    public CloudImage getImage() {
        return null;
    }

    @NotNull
    public Date getStartedTime() {
        return null;
    }

    @Nullable
    public String getNetworkIdentity() {
        return null;
    }

    @NotNull
    public InstanceStatus getStatus() {
        return instanceStatus;
    }

    @Nullable
    public CloudErrorInfo getErrorInfo() {
        return null;
    }

    public boolean containsAgent(AgentDescription agentDescription) {
        return false;
    }

    public void setDroplet(Droplet droplet) {
        this.droplet = droplet;
    }

    public Droplet getDroplet() {
        return this.droplet;
    }
}
