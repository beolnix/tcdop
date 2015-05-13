package io.cyberstock.tcdop.server;

import com.intellij.openapi.diagnostic.Logger;
import io.cyberstock.tcdop.api.DOConfigConstants;
import io.cyberstock.tcdop.util.DOMessagesHelper;
import jetbrains.buildServer.clouds.*;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import jetbrains.buildServer.web.openapi.PluginDescriptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by beolnix on 08/05/15.
 */
public class DOCloudClientFactory implements CloudClientFactory {

    private static final Logger LOG = Logger.getInstance(DOCloudClientFactory.class.getName());

    @NotNull private final String doProfileJspPath;
    @NotNull private final PropertiesProcessor doPropertiesProcessor = new DOPropertiesProcessor();

    private final static String DO_SETTINGS_PAGE_NAME = "do-profile-settings.jsp";

    public DOCloudClientFactory(@NotNull final CloudRegistrar cloudRegistrar,
                                @NotNull final PluginDescriptor pluginDescriptor) {
        this.doProfileJspPath = pluginDescriptor.getPluginResourcesPath(DO_SETTINGS_PAGE_NAME);

        cloudRegistrar.registerCloudFactory(this);
    }

    @NotNull
    public CloudClientEx createNewClient(CloudState cloudState, CloudClientParameters cloudClientParameters) {
        //TODO: implement this
        throw new NotImplementedException();
    }

    @NotNull
    public String getCloudCode() {
        return DOConfigConstants.CLOUD_CODE;
    }

    @NotNull
    public String getDisplayName() {
        return "Digital Ocean cloud";
    }

    @Nullable
    public String getEditProfileUrl() {
        return doProfileJspPath;
    }

    @NotNull
    public Map<String, String> getInitialParameterValues() {
        final HashMap<String, String> initialParamsMap = new HashMap<String, String>();

        //TODO: Add initial params here if required

        return initialParamsMap;
    }

    @NotNull
    public PropertiesProcessor getPropertiesProcessor() {
        //TODO: What is this??
        return doPropertiesProcessor;
    }

    public boolean canBeAgentOfType(AgentDescription agentDescription) {
        final Map<String, String> configParams = agentDescription.getConfigurationParameters();

        return configParams.containsKey(DOConfigConstants.IDENTITY_KEY)
                && DOConfigConstants.IDENTITY_VALUE.equals(configParams.get(DOConfigConstants.IDENTITY_KEY));
    }
}
