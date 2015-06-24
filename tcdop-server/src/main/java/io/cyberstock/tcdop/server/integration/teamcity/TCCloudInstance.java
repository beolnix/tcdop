package io.cyberstock.tcdop.server.integration.teamcity;

import com.myjeeva.digitalocean.pojo.Droplet;
import io.cyberstock.tcdop.model.AgentParamKey;
import io.cyberstock.tcdop.model.DOSettings;
import jetbrains.buildServer.clouds.*;
import jetbrains.buildServer.serverSide.AgentDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * Created by beolnix on 16/05/15.
 */
public class TCCloudInstance implements CloudInstance {

    // dependencies
    private final TCCloudImage cloudImage;
    private final CloudInstanceUserData userData;
    private final DOSettings doSettings;

    // state
    private InstanceStatus instanceStatus = InstanceStatus.SCHEDULED_TO_START;
    private CloudErrorInfo cloudErrorInfo;
    private Droplet droplet;
    private Date startTime;

    public static final String INSTANCE_ID_PARAM_KEY = "instance_id_param_key";

    public TCCloudInstance(@NotNull TCCloudImage cloudImage,
                           @NotNull CloudInstanceUserData cloudInstanceUserData,
                           @NotNull DOSettings doSettings) {
        this.doSettings = doSettings;
        this.cloudImage = cloudImage;
        this.userData = cloudInstanceUserData;
    }

    @NotNull
    public String getInstanceId() {
        if (droplet != null) {
            return droplet.getId().toString();
        } else {
            return userData.getCustomAgentConfigurationParameters().get(INSTANCE_ID_PARAM_KEY);
        }
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void updateStatus(InstanceStatus newStatus) {
        this.instanceStatus = newStatus;
    }

    public void updateErrorInfo(CloudErrorInfo errorInfo) {
        cloudErrorInfo = errorInfo;
    }

    @NotNull
    public String getName() {
        if (droplet != null) {
            return droplet.getName();
        } else {
            return null;
        }

    }

    @NotNull
    public String getImageId() {
        return cloudImage.getId();
    }

    @NotNull
    public CloudImage getImage() {
        return cloudImage;
    }

    @NotNull
    public Date getStartedTime() {
        return startTime;
    }

    @Nullable
    public String getNetworkIdentity() {
        if (droplet != null) {
            return droplet.getNetworks().getVersion4Networks().get(0).getIpAddress();
        } else {
            return null;
        }
    }

    @NotNull
    public InstanceStatus getStatus() {
        return instanceStatus;
    }

    @Nullable
    public CloudErrorInfo getErrorInfo() {
        return cloudErrorInfo;
    }

    public boolean containsAgent(AgentDescription agentDescription) {
        String agentId = agentDescription.getAvailableParameters().get(AgentParamKey.AGENT_ID);
        if (agentId != null) {
            return agentId.equals(userData.getCustomAgentConfigurationParameters().get(AgentParamKey.AGENT_ID));
        } else {
            return false;
        }
    }

    public void setDroplet(Droplet droplet) {
        this.droplet = droplet;
        userData.getCustomAgentConfigurationParameters().put(INSTANCE_ID_PARAM_KEY, droplet.getId().toString());
        cloudImage.setImage(droplet.getImage());
    }

    public Droplet getDroplet() {
        return this.droplet;
    }

    public DOSettings getDoSettings() {
        return doSettings;
    }

    @Override
    public String toString() {
        return "TCCloudInstance{" +
                "cloudImage=" + cloudImage +
                ", userData=" + userData +
                ", instanceStatus=" + instanceStatus +
                ", cloudErrorInfo=" + cloudErrorInfo +
                ", droplet=" + droplet +
                '}';
    }
}
