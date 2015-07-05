package io.cyberstock.tcdop.agent;

import com.intellij.openapi.diagnostic.Logger;
import io.cyberstock.tcdop.model.DOConfigConstants;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.util.EventDispatcher;
import org.jetbrains.annotations.NotNull;


public class DOAgentPropsUpdater {

    // dependencies
    private final BuildAgentConfiguration agentConfig;

    // constants
    private static final Logger LOG = Logger.getInstance(DOAgentPropsUpdater.class.getName());


    public DOAgentPropsUpdater(final BuildAgentConfiguration agentConfiguration,
                               @NotNull EventDispatcher<AgentLifeCycleListener> events) {
        LOG.info("DO plugin initializing...");
        agentConfig = agentConfiguration;

        events.addListener(new AgentLifeCycleAdapter() {
            @Override
            public void afterAgentConfigurationLoaded(@NotNull final BuildAgent agent) {
                final String ipv4 = getIPv4();
                agentConfig.addConfigurationParameter(DOConfigConstants.AGENT_IPV4_PROP_KEY, ipv4);
            }
        });
    }

    private String getIPv4() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException e) {
            LOG.error("Can't get ipv4 address: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
