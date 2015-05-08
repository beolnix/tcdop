package io.cyberstock.tcdop.server;

import io.cyberstock.tcdop.api.DOConfigConstants;
import io.cyberstock.tcdop.util.DOMessagesHelper;
import jetbrains.buildServer.clouds.CloudType;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.PropertiesProcessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.Map;

/**
 * Created by beolnix on 06/05/15.
 */
public class DOCloudType implements CloudType {

    private DOMessagesHelper msg;

    public DOCloudType(DOMessagesHelper msg) {
        this.msg = msg;
    }

    @NotNull
    public String getCloudCode() {
        return DOConfigConstants.CLOUD_CODE;
    }

    @NotNull
    public String getDisplayName() {
        return msg.getMsg("tcdop.server.display_name");
    }

    @Nullable
    public String getEditProfileUrl() {
        return "do-profile.jsp";
    }

    @NotNull
    public Map<String, String> getInitialParameterValues() {
        //TODO: implement this
        throw new NotImplementedException();
    }

    @NotNull
    public PropertiesProcessor getPropertiesProcessor() {
        //TODO: implement this
        throw new NotImplementedException();
    }

    public boolean canBeAgentOfType(AgentDescription agentDescription) {
        final Map<String, String> configParams = agentDescription.getConfigurationParameters();

        return configParams.containsKey(DOConfigConstants.IDENTITY_KEY)
                && DOConfigConstants.IDENTITY_VALUE.equals(configParams.get(DOConfigConstants.IDENTITY_KEY));
    }
}
