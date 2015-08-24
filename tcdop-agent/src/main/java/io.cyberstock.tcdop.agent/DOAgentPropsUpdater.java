/* The MIT License
 *
 * Copyright (c) 2010-2015 Danila A. (atmakin.dv@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
package io.cyberstock.tcdop.agent;

import com.intellij.openapi.diagnostic.Logger;
import io.cyberstock.tcdop.model.DOConfigConstants;

import java.net.InetAddress;
import java.net.UnknownHostException;

import jetbrains.buildServer.agent.*;
import jetbrains.buildServer.util.EventDispatcher;
import org.jetbrains.annotations.NotNull;


/**
 * This one doesn't seem to work
 * Using buildAgent.properties to set env properties for agent identification
 */
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
                agentConfig.addConfigurationParameter(DOConfigConstants.IDENTITY_KEY, DOConfigConstants.IDENTITY_VALUE);
            }
        });
    }

    private String getIPv4() {
        InetAddress ip;
        try {
            ip = InetAddress.getLocalHost();
            LOG.debug("IPv4 address is: " + ip);
            return ip.getHostAddress();
        } catch (UnknownHostException e) {
            LOG.error("Can't get ipv4 address: " + e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
