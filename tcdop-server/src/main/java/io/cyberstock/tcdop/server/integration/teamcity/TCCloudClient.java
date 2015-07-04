package io.cyberstock.tcdop.server.integration.teamcity;

import com.intellij.openapi.diagnostic.Logger;
import io.cyberstock.tcdop.model.AgentParamKey;
import io.cyberstock.tcdop.model.DOIntegrationMode;
import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.server.integration.digitalocean.CloudImageStorage;
import io.cyberstock.tcdop.server.integration.digitalocean.DOAsyncClientServiceWrapper;
import jetbrains.buildServer.clouds.*;
import jetbrains.buildServer.serverSide.AgentDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by beolnix on 24/05/15.
 */
public class TCCloudClient implements CloudClientEx {

    // dependencies
    @NotNull private final DOSettings settings;
    @NotNull private final DOAsyncClientServiceWrapper client;
    @NotNull private final CloudImageStorage imageStorage;

    // State
    private Boolean readyFlag = true;
    private CloudErrorInfo cloudErrorInfo = null;


    // constants
    private static final Logger LOG = Logger.getInstance(TCCloudClient.class.getName());


    TCCloudClient(@NotNull DOSettings settings,
                  @NotNull DOAsyncClientServiceWrapper client,
                  @NotNull CloudImageStorage imageStorage) {
        this.settings = settings;
        this.client = client;
        this.imageStorage = imageStorage;
    }

    public void setReadyFlag(Boolean readyFlag) {
        this.readyFlag = readyFlag;
    }

    public void setCloudErrorInfo(CloudErrorInfo cloudErrorInfo) {
        this.cloudErrorInfo = cloudErrorInfo;
    }

    @NotNull
    public CloudInstance startNewInstance(@NotNull CloudImage cloudImage, @NotNull CloudInstanceUserData cloudInstanceUserData) throws QuotaException {

        TCCloudImage tcCloudImage = (TCCloudImage) cloudImage;
        TCCloudInstance instance = new TCCloudInstance(tcCloudImage);
        tcCloudImage.addInstance(instance);

        client.initializeInstance(instance, settings);
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
        if (DOIntegrationMode.PREPARED_IMAGE.equals(settings.getMode())) {
            TCCloudImage cloudImage = imageStorage.getImageById(s);
            if (cloudImage != null && settings.getImageName().equals(cloudImage.getName())) {
                return cloudImage;
            }
        } else {
            throw new NotImplementedException();
        }
        return null;
    }

    @Nullable
    public CloudInstance findInstanceByAgent(@NotNull AgentDescription agentDescription) {
        String agentId = agentDescription.getConfigurationParameters().get(AgentParamKey.AGENT_ID);

        return null;
    }

    @NotNull
    public Collection<? extends CloudImage> getImages() throws CloudException {
        if (DOIntegrationMode.PREPARED_IMAGE.equals(settings.getMode())) {
            for (TCCloudImage cloudImage : imageStorage.getImagesList()) {
                if (cloudImage != null && settings.getImageName().equals(cloudImage.getName())) {
                    return Collections.singleton(cloudImage);
                }
            }
        } else {
            throw new NotImplementedException();
        }
        return Collections.EMPTY_LIST;
    }

    @Nullable
    public CloudErrorInfo getErrorInfo() {
        return cloudErrorInfo;
    }

    public boolean canStartNewInstance(@NotNull CloudImage cloudImage) {
        return true;
    }

    @Nullable
    public String generateAgentName(@NotNull AgentDescription agentDescription) {
        return "DO_AGENT_" + UUID.randomUUID();
    }
}
