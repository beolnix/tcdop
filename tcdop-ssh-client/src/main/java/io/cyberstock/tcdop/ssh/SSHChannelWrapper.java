package io.cyberstock.tcdop.ssh;

import com.jcraft.jsch.JSchException;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by beolnix on 17/08/15.
 */
public interface SSHChannelWrapper {

    void setCommand(String command);

    void connect() throws JSchException;

    InputStream getInputStream() throws IOException;

    boolean isClosed();

    void disconnect();
}
