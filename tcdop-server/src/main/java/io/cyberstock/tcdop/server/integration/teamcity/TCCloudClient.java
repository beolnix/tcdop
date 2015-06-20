package io.cyberstock.tcdop.server.integration.teamcity;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.server.integration.digitalocean.DOAsyncClientService;
import jetbrains.buildServer.clouds.*;
import jetbrains.buildServer.serverSide.AgentDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by beolnix on 24/05/15.
 */
public class TCCloudClient implements CloudClientEx {

    // dependencies
    @NotNull private final DOSettings settings;
    @NotNull private DOAsyncClientService client;

    // State
    private Boolean readyFlag = false;
    private CloudErrorInfo cloudErrorInfo = null;


    TCCloudClient(@NotNull DOSettings settings,
                  @NotNull DOAsyncClientService client) {
        this.settings = settings;
        this.client = client;
    }

    public void setReadyFlag(Boolean readyFlag) {
        this.readyFlag = readyFlag;
    }

    public void setCloudErrorInfo(CloudErrorInfo cloudErrorInfo) {
        this.cloudErrorInfo = cloudErrorInfo;
    }

    @NotNull
    public CloudInstance startNewInstance(@NotNull CloudImage cloudImage, @NotNull CloudInstanceUserData cloudInstanceUserData) throws QuotaException {

        TCCloudImage tcCloudImage = new TCCloudImage(cloudImage);
        TCCloudInstance instance = new TCCloudInstance(tcCloudImage, cloudInstanceUserData);

        client.findOrCreateDroplet();
        return instance;
    }

    public void restartInstance(@NotNull CloudInstance cloudInstance) {
        client.restartInstance((TCCloudInstance) cloudInstance);
    }

    public void terminateInstance(@NotNull CloudInstance cloudInstance) {
        client.terminateInstance((TCCloudInstance) cloudInstance);
    }

    public void dispose() {
        client.shutdown();
    }

    public boolean isInitialized() {
        return readyFlag;
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
        return cloudErrorInfo;
    }

    public boolean canStartNewInstance(@NotNull CloudImage cloudImage) {
        return false;
    }

    @Nullable
    public String generateAgentName(@NotNull AgentDescription agentDescription) {
        return null;
    }
}
