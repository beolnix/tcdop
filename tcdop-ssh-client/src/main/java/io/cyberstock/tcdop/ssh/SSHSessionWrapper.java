package io.cyberstock.tcdop.ssh;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSchException;

import java.util.Properties;

/**
 * Created by beolnix on 17/08/15.
 */
public interface SSHSessionWrapper {

    void setConfig(Properties newconf);

    boolean isConnected();

    void connect() throws JSchException;

    void disconnect();

    SSHChannelWrapper openChannel(String type) throws JSchException;
}
