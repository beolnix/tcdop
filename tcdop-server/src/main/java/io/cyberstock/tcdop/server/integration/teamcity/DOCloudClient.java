package io.cyberstock.tcdop.server.integration.teamcity;

import com.intellij.openapi.diagnostic.Logger;
import io.cyberstock.tcdop.model.DOIntegrationMode;
import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.digitalocean.CloudImageStorage;
import io.cyberstock.tcdop.server.integration.digitalocean.DOAsyncClientServiceWrapper;
import jetbrains.buildServer.clouds.*;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.impl.auth.SecuredBuildAgent;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

/**
 * Created by beolnix on 24/05/15.
 */
public class DOCloudClient implements CloudClientEx {

    // dependencies
    @NotNull private final DOSettings settings;
    @NotNull private final DOAsyncClientServiceWrapper client;
    @NotNull private final CloudImageStorage imageStorage;
    @NotNull private final ExecutorService executorService;

    // State
    private Boolean readyFlag = true;
    private CloudErrorInfo cloudErrorInfo = null;


    // constants
    private static final Logger LOG = Logger.getInstance(DOCloudClient.class.getName());

    DOCloudClient(@NotNull DOSettings settings,
                  @NotNull DOAsyncClientServiceWrapper client,
                  @NotNull CloudImageStorage imageStorage,
                  @NotNull ExecutorService executorService) {
        this.settings = settings;
        this.client = client;
        this.imageStorage = imageStorage;
        this.executorService = executorService;
    }

    public void setReadyFlag(Boolean readyFlag) {
        LOG.debug("Cloud client for DO ready flag updated: " + readyFlag);
        this.readyFlag = readyFlag;
    }

    public void setCloudErrorInfo(CloudErrorInfo cloudErrorInfo) {
        LOG.error("Error occured: " + cloudErrorInfo.getMessage());
        this.cloudErrorInfo = cloudErrorInfo;
    }

    @NotNull
    public CloudInstance startNewInstance(@NotNull CloudImage cloudImage, @NotNull CloudInstanceUserData cloudInstanceUserData) throws QuotaException {
        LOG.info("Launch new instance in Digital Ocean with cloudImage: " + cloudImage.getName());
        DOCloudImage DOCloudImage = (DOCloudImage) cloudImage;
        try {
            DOCloudInstance instance = client.initializeInstance(DOCloudImage, settings);
            imageStorage.forceUpdate();
            return instance;
        } catch (DOError e) {
            setCloudErrorInfo(new CloudErrorInfo("Can't create new instance", e.getMessage(), e));
            throw new RuntimeException(e);
        }
    }

    public void restartInstance(@NotNull CloudInstance cloudInstance) {
        LOG.info("DO Instance restart is triggered for: " + cloudInstance.toString());
        client.restartInstance((DOCloudInstance) cloudInstance);
    }

    public void terminateInstance(@NotNull CloudInstance cloudInstance) {
        LOG.info("DO Instance termination is triggered for: " + cloudInstance.toString());
        client.terminateInstance((DOCloudInstance) cloudInstance);
    }

    public void dispose() {
        imageStorage.shutdownStorage();
        executorService.shutdown();
    }

    public boolean isInitialized() {
        return readyFlag;
    }

    @Nullable
    public CloudImage findImageById(@NotNull String s) throws CloudException {
        LOG.debug("DO find image by id is triggered: " + s);
        if (DOIntegrationMode.PREPARED_IMAGE.equals(settings.getMode())) {
            DOCloudImage cloudImage = imageStorage.getImageById(s);

            if (cloudImage == null) {
                LOG.debug("DO cloud image not found for id: " + s);
                return null;
            } else {
                LOG.debug("DO cloud image found: " + cloudImage.toString());
            }

            if (settings.getImageName().equals(cloudImage.getName())) {
                LOG.debug("DO cloud image is correct. Find Image by id returns successful result: " + cloudImage.toString());
                return cloudImage;
            } else {
                LOG.error("DO cloud image name isn't equal to pre-configured: " + cloudImage.getName());
                return null;
            }
        } else {
            LOG.error("DO Mode: " + settings.getMode() + " isn't supported yet.");
            throw new NotImplementedException();
        }
    }

    @Nullable
    public CloudInstance findInstanceByAgent(@NotNull AgentDescription agentDescription) {
        LOG.debug("Find instance by agent is triggered.");
        String agentIPv4 = ((SecuredBuildAgent)agentDescription).getHostAddress();
        if (StringUtils.isEmpty(agentIPv4)) {
            LOG.error("Agent ipv4 is empty.");
            return null;
        } else {
            LOG.debug("Agent ipv4 is: " + agentIPv4);
        }

        Collection<DOCloudImage> images = imageStorage.getImagesList();
        for (DOCloudImage image : images) {
            for (CloudInstance instance : image.getInstances()) {
                if (instance.getNetworkIdentity().equals(agentIPv4)) {
                    LOG.debug("Instance with the same ipv4 has been found: " + instance.toString());
                    return instance;
                } else {
                    LOG.debug("Instance " + instance.getInstanceId() + " has another ipv4: " + instance.getNetworkIdentity());
                }
            }
        }

        LOG.error("Instance with ipv4: " + agentIPv4 + " hasn't been found.");
        return null;
    }

    @NotNull
    public Collection<? extends CloudImage> getImages() throws CloudException {
        LOG.debug("DO get images triggered");
        if (DOIntegrationMode.PREPARED_IMAGE.equals(settings.getMode())) {
            Collection<DOCloudImage> images = imageStorage.getImagesList();
            LOG.debug(images.size() + " images found, trying to identify image with name: " + settings.getImageName());
            for (DOCloudImage cloudImage : images) {
                if (cloudImage != null && settings.getImageName().equals(cloudImage.getName())) {
                    LOG.debug("Image found: " + cloudImage.toString());
                    return Collections.singleton(cloudImage);
                } else {
                    LOG.debug("Image " + cloudImage.getName() + " skipped.");
                }

            }
        } else {
            LOG.error(settings.getMode() + " Mode isn't supported yet");
            throw new NotImplementedException();
        }

        LOG.debug("Image with name " + settings.getImageName() + " hasn't been found.");
        return Collections.EMPTY_LIST;
    }

    @Nullable
    public CloudErrorInfo getErrorInfo() {
        return cloudErrorInfo;
    }

    public boolean canStartNewInstance(@NotNull CloudImage cloudImage) {
        LOG.debug("Can start new instance? is triggered");
        if (settings.getInstancesLimit() > imageStorage.getInstancesCount() &&
                cloudErrorInfo == null) {
            LOG.debug("new instance can be started. Limit: " + settings.getInstancesLimit() +
                    "; current: " + imageStorage.getInstancesCount());
            return true;
        } else {
            LOG.error("new instance can NOT be started. Limit: " + settings.getInstancesLimit() +
                    "; current: " + imageStorage.getInstancesCount());
            if (cloudErrorInfo != null) {
                LOG.error("Cloud error info: " + cloudErrorInfo.getDetailedMessage());
            }
            return false;
        }
    }

    @Nullable
    public String generateAgentName(@NotNull AgentDescription agentDescription) {
        String agentName = settings.getDropletNamePrefix() + "_AGENT_" + UUID.randomUUID();
        LOG.debug("Agent name generation is triggered. Generated name is: " + agentName);
        return agentName;
    }
}
