package io.cyberstock.tcdop.ssh.impl;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import io.cyberstock.tcdop.ssh.SSHChannelWrapper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by beolnix on 17/08/15.
 */
public class SSHChannelWrapperImpl implements SSHChannelWrapper {
    private final Channel channel;

    public SSHChannelWrapperImpl(Channel channel) {
        this.channel = channel;
    }

    public void setCommand(String command) {
        ((ChannelExec) channel).setCommand(command);
    }

    public void connect() throws JSchException {
        channel.connect();
    }

    public InputStream getInputStream() throws IOException {
        return channel.getInputStream();
    }

    public boolean isClosed() {
        return channel.isClosed();
    }

    public void disconnect() {
        channel.disconnect();
    }
}
