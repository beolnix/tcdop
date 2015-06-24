package io.cyberstock.tcdop.server.integration.teamcity;

import com.intellij.openapi.diagnostic.Logger;
import io.cyberstock.tcdop.model.AgentParamKey;
import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.server.integration.digitalocean.DOAsyncClientServiceWrapper;
import jetbrains.buildServer.clouds.*;
import jetbrains.buildServer.serverSide.AgentDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Created by beolnix on 24/05/15.
 */
public class TCCloudClient implements CloudClientEx {

    // dependencies
    @NotNull private final DOSettings settings;
    @NotNull private DOAsyncClientServiceWrapper client;

    // State
    private Boolean readyFlag = true;
    private CloudErrorInfo cloudErrorInfo = null;
    private List<TCCloudImage> cloudImages = new ArrayList<TCCloudImage>();

    // constants
    private static final Logger LOG = Logger.getInstance(TCCloudClient.class.getName());


    TCCloudClient(@NotNull DOSettings settings,
                  @NotNull DOAsyncClientServiceWrapper client) {
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

        enrichCloudInstanceUserData(cloudInstanceUserData);

        TCCloudImage tcCloudImage = new TCCloudImage(cloudImage, settings);
        TCCloudInstance instance = new TCCloudInstance(tcCloudImage, cloudInstanceUserData, settings);
        tcCloudImage.addInstance(instance);

        cloudImages.add(tcCloudImage);

        client.initializeInstance(instance);
        return instance;
    }

    public void enrichCloudInstanceUserData(CloudInstanceUserData cloudInstanceUserData) {
        if (!cloudInstanceUserData.getCustomAgentConfigurationParameters().containsKey(AgentParamKey.AGENT_ID)) {
            cloudInstanceUserData.addAgentConfigurationParameter(AgentParamKey.AGENT_ID, "DO_AGENT_" + UUID.randomUUID());
        }
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
        return cloudImages;
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
        return "testAgent";
    }
}
