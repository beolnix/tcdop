package io.cyberstock.tcdop.server.integration.teamcity;

import com.intellij.openapi.diagnostic.Logger;
import jetbrains.buildServer.clouds.*;
import jetbrains.buildServer.serverSide.AgentDescription;
import jetbrains.buildServer.serverSide.impl.auth.SecuredBuildAgent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * Created by beolnix on 16/05/15.
 */
public class DOCloudInstance implements CloudInstance {

    // dependencies
    private final DOCloudImage cloudImage;
    private final String instanceId;
    private final String instanceName;

    // constants
    private static final Logger LOG = Logger.getInstance(DOCloudInstance.class.getName());

    // state
    private InstanceStatus instanceStatus = InstanceStatus.SCHEDULED_TO_START;
    private String ipv4;
    private CloudErrorInfo cloudErrorInfo;
    private Date startTime;

    public DOCloudInstance(@NotNull DOCloudImage cloudImage,
                           @NotNull String instanceId,
                           @NotNull String instanceName) {
        this.cloudImage = cloudImage;
        this.instanceId = instanceId;
        this.instanceName = instanceName;
    }

    @NotNull
    public String getInstanceId() {
        return instanceId;
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

    public void updateNetworkIdentity(String ipv4) {
        this.ipv4 = ipv4;
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
        return ipv4;
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
