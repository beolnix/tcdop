package io.cyberstock.tcdop.ssh.impl;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.cyberstock.tcdop.ssh.SSHChannelWrapper;
import io.cyberstock.tcdop.ssh.SSHSessionWrapper;

import java.util.Properties;

/**
 * Created by beolnix on 17/08/15.
 */
public class SSHSessionWrapperImpl implements SSHSessionWrapper {

    private final Session session;

    public SSHSessionWrapperImpl(Session session) {
        this.session = session;
    }

    public void setConfig(Properties newconf) {
        this.session.setConfig(newconf);
    }

    public boolean isConnected() {
        return this.session.isConnected();
    }

    public void connect() throws JSchException {
        this.session.connect();
    }

    public void disconnect() {
        this.session.disconnect();
    }

    public SSHChannelWrapper openChannel(String type) throws JSchException {
        return new SSHChannelWrapperImpl(session.openChannel(type));
    }
}
