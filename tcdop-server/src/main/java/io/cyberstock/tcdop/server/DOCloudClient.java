package io.cyberstock.tcdop.server;


import jetbrains.buildServer.clouds.*;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.BuildServerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

/**
 * Created by beolnix on 08/05/15.
 */
public class DOCloudClient extends BuildServerAdapter implements CloudClientEx {

    @NotNull private final DOSettings settings;

    public DOCloudClient(@NotNull final DOSettings settings) {
        this.settings = settings;
    }

    @NotNull
    public CloudInstance startNewInstance(CloudImage cloudImage, CloudInstanceUserData cloudInstanceUserData) throws QuotaException {
        return null;
    }

    public void restartInstance(CloudInstance cloudInstance) {

    }

    public void terminateInstance(CloudInstance cloudInstance) {

    }

    public void dispose() {

    }

    public boolean isInitialized() {
        return false;
    }

    @Nullable
    public CloudImage findImageById(String s) throws CloudException {
        return null;
    }

    @Nullable
    public CloudInstance findInstanceByAgent(AgentDescription agentDescription) {
        return null;
    }

    @NotNull
    public Collection<? extends CloudImage> getImages() throws CloudException {
        return null;
    }

    @Nullable
    public CloudErrorInfo getErrorInfo() {
        return null;
    }

    public boolean canStartNewInstance(CloudImage cloudImage) {
        return false;
    }

    @Nullable
    public String generateAgentName(AgentDescription agentDescription) {
        return null;
    }
}
