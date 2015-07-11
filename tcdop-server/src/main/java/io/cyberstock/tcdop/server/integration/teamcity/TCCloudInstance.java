package io.cyberstock.tcdop.server.integration.teamcity;

import com.intellij.openapi.diagnostic.Logger;
import com.myjeeva.digitalocean.pojo.Droplet;
import io.cyberstock.tcdop.model.AgentParamKey;
import io.cyberstock.tcdop.model.DOConfigConstants;
import io.cyberstock.tcdop.model.DOSettings;
import jetbrains.buildServer.clouds.*;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.impl.auth.SecuredBuildAgent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.Map;

/**
 * Created by beolnix on 16/05/15.
 */
public class TCCloudInstance implements CloudInstance {

    // dependencies
    private final TCCloudImage cloudImage;
    private final String instanceId;
    private final String instanceName;
    private final Droplet droplet;

    // constants
    private static final Logger LOG = Logger.getInstance(TCCloudInstance.class.getName());
    private static final InstanceStatus DEFAULT_STATE = InstanceStatus.UNKNOWN;

    // state
    private InstanceStatus instanceStatus = DEFAULT_STATE;
    private CloudErrorInfo cloudErrorInfo;
    private Date startTime;

    public TCCloudInstance(@NotNull TCCloudImage cloudImage,
                           @NotNull Droplet droplet) {
        this.cloudImage = cloudImage;
        this.droplet = droplet;
        this.instanceId = droplet.getId().toString();
        this.instanceName = droplet.getName();

        switch (droplet.getStatus()) {
            case NEW:
                this.instanceStatus = InstanceStatus.SCHEDULED_TO_START;
                break;
            case ACTIVE:
                this.instanceStatus = InstanceStatus.RUNNING;
                break;
            case OFF:
                this.instanceStatus = InstanceStatus.STOPPED;
                break;
            case ARCHIVE:
                this.instanceStatus = InstanceStatus.STOPPED;
                break;
            default:
                this.instanceStatus = DEFAULT_STATE;
                break;
        }
    }

    @NotNull
    public String getInstanceId() {
        return instanceId;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void updateStatus(InstanceStatus newStatus) {
        LOG.debug("new status of " + instanceId + " is: " + newStatus.getName());
        this.instanceStatus = newStatus;
    }

    public void updateErrorInfo(CloudErrorInfo errorInfo) {
        LOG.error("instance error: " + errorInfo.getMessage());
        cloudErrorInfo = errorInfo;
    }

    @NotNull
    public String getName() {
        return instanceName;
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
        LOG.debug("Contains agent is triggered in instance: " + instanceId + " for agent: " + agentDescription.toString());

        String agentIPv4 = ((SecuredBuildAgent)agentDescription).getHostAddress();
        String instanceIPv4 = getNetworkIdentity();

        boolean result = instanceIPv4.equals(agentIPv4);
        if (result) {
            LOG.debug("Instance " + instanceId + " contains agent: " + agentDescription.toString());
        } else {
            LOG.debug("Instance " + instanceId + " doesn't contain agent: " + agentDescription.toString());
        }

        return result;
    }

    @Override
    public String toString() {
        return "TCCloudInstance{" +
                "cloudImage=" + cloudImage +
                ", instanceId='" + instanceId + '\'' +
                ", instanceName='" + instanceName + '\'' +
                ", instanceStatus=" + instanceStatus +
                ", cloudErrorInfo=" + cloudErrorInfo +
                ", startTime=" + startTime +
                '}';
    }
}
