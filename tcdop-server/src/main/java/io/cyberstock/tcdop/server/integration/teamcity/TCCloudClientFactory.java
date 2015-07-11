package io.cyberstock.tcdop.server.integration.teamcity;

import com.intellij.openapi.diagnostic.Logger;
import io.cyberstock.tcdop.model.DOConfigConstants;
import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.server.error.UnsupportedDOModeError;
import io.cyberstock.tcdop.server.integration.digitalocean.CloudImageStorage;
import io.cyberstock.tcdop.server.integration.digitalocean.CloudImageStorageFactory;
import io.cyberstock.tcdop.server.integration.digitalocean.DOAsyncClientServiceWrapper;
import io.cyberstock.tcdop.server.integration.digitalocean.DOAsyncClientServiceFactory;
import io.cyberstock.tcdop.server.integration.teamcity.web.TCDOPSettingsController;
import jetbrains.buildServer.clouds.*;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.InvalidProperty;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static io.cyberstock.tcdop.util.SettingsUtils.convertClientParametersToDOSettings;

import static io.cyberstock.tcdop.server.service.ConfigurationValidator.validateConfiguration;

import java.util.*;

/**
 * Created by beolnix on 08/05/15.
 */
public class TCCloudClientFactory implements CloudClientFactory {

    // dependencies
    private final DOAsyncClientServiceFactory asyncClientServiceFactory;
    private final CloudImageStorageFactory cloudImageStorageFactory;

    // state
    private final String doProfileHtmlPath;

    // constants
    private static final Logger LOG = Logger.getInstance(TCCloudClientFactory.class.getName());

    private final static String DISPLAY_NAME = "Digital ocean type";

    public TCCloudClientFactory(@NotNull final CloudRegistrar cloudRegistrar,
                                @NotNull final PluginDescriptor pluginDescriptor,
                                @NotNull final DOAsyncClientServiceFactory asyncClientServiceFactory,
                                @NotNull final CloudImageStorageFactory cloudImageStorageFactory) {
        this.asyncClientServiceFactory = asyncClientServiceFactory;
        this.cloudImageStorageFactory = cloudImageStorageFactory;

        this.doProfileHtmlPath = pluginDescriptor.getPluginResourcesPath(TCDOPSettingsController.HTML_PAGE_NAME);
        cloudRegistrar.registerCloudFactory(this);

        LOG.info("Digital Ocean client factory initialized. Settings HTML path: " + doProfileHtmlPath);
    }

    @NotNull
    public CloudClientEx createNewClient(CloudState cloudState, CloudClientParameters cloudClientParameters) {
        DOSettings settings = convertClientParametersToDOSettings(cloudClientParameters);

        if (!settings.isPreparedInstanceMode()) {
            throw new UnsupportedDOModeError(settings.getMode());
        }

        DOAsyncClientServiceWrapper client = asyncClientServiceFactory.createClient(settings.getToken());
        CloudImageStorage imageStorage = cloudImageStorageFactory.getStorage(settings.getToken());

        TCCloudClient cloudClient = new TCCloudClient(settings, client, imageStorage);
        cloudClient.setReadyFlag(true);

        return cloudClient;
    }

    @NotNull
    public String getCloudCode() {
        return DOConfigConstants.CLOUD_CODE;
    }

    @NotNull
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Nullable
    public String getEditProfileUrl() {
        return doProfileHtmlPath;
    }

    @NotNull
    public Map<String, String> getInitialParameterValues() {
        return Collections.<String, String>emptyMap();
    }

    @NotNull
    public PropertiesProcessor getPropertiesProcessor() {
        return new PropertiesProcessor() {
            public Collection<InvalidProperty> process(Map<String, String> stringStringMap) {
                DOSettings doSettings = new DOSettings(stringStringMap);
                return validateConfiguration(doSettings);
            }
        };
    }

    public boolean canBeAgentOfType(AgentDescription agentDescription) {
        final Map<String, String> configParams = agentDescription.getAvailableParameters();
        boolean result = configParams.containsKey(DOConfigConstants.ENV_AGENT_TYPE)
                && DOConfigConstants.IDENTITY_VALUE.equals(configParams.get(DOConfigConstants.ENV_AGENT_TYPE));

        if (LOG.isDebugEnabled()) {
            String agentName = agentDescription.getConfigurationParameters().get(DOConfigConstants.AGENT_NAME_PROP);
            LOG.debug("Agent with name: " + agentName + " can be managed by DO plugin: " + result);
        }

        return result;
    }
}
