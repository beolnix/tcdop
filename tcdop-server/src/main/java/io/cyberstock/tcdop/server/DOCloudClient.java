package io.cyberstock.tcdop.server;


import io.cyberstock.tcdop.server.service.DOIntegrationService;
import jetbrains.buildServer.clouds.*;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.BuildServerAdapter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Date;

/**
 * Created by beolnix on 08/05/15.
 */
public class DOCloudClient implements CloudClientEx {

    @NotNull private final DOSettings settings;
    @NotNull private final DOIntegrationService doService;

    public DOCloudClient(@NotNull final DOSettings settings) {
        this.settings = settings;

        this.doService = new DOIntegrationService(settings.getToken());
    }

    @NotNull
    public CloudInstance startNewInstance(@NotNull CloudImage cloudImage, @NotNull CloudInstanceUserData cloudInstanceUserData) throws QuotaException {
        return null;
    }

    public void restartInstance(@NotNull CloudInstance cloudInstance) {

    }

    public void terminateInstance(@NotNull CloudInstance cloudInstance) {

    }

    public void dispose() {

    }

    public boolean isInitialized() {
        return false;
    }

    @Nullable
    public CloudImage findImageById(@NotNull String s) throws CloudException {
        return null;
    }

    @Nullable
    public CloudInstance findInstanceByAgent(@NotNull AgentDescription agentDescription) {
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

    public boolean canStartNewInstance(@NotNull CloudImage cloudImage) {
        return false;
    }

    @Nullable
    public String generateAgentName(@NotNull AgentDescription agentDescription) {
        return null;
    }
}
