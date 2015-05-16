package io.cyberstock.tcdop.server;

import jetbrains.buildServer.clouds.CloudErrorInfo;
import jetbrains.buildServer.clouds.CloudImage;
import jetbrains.buildServer.clouds.CloudInstance;
import jetbrains.buildServer.clouds.InstanceStatus;
import jetbrains.buildServer.serverSide.AgentDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * Created by beolnix on 16/05/15.
 */
public class DOCloudInstance implements CloudInstance {
    @NotNull
    public String getInstanceId() {
        return null;
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
        return null;
    }

    @Nullable
    public CloudErrorInfo getErrorInfo() {
        return null;
    }

    public boolean containsAgent(AgentDescription agentDescription) {
        return false;
    }
}
