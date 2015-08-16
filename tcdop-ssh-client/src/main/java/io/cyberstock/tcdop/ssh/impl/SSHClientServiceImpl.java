package io.cyberstock.tcdop.ssh.impl;

import com.jcraft.jsch.*;
import io.cyberstock.tcdop.ssh.SSHChannelWrapper;
import io.cyberstock.tcdop.ssh.SSHClientService;
import io.cyberstock.tcdop.ssh.SSHClientWrapper;
import io.cyberstock.tcdop.ssh.SSHSessionWrapper;
import io.cyberstock.tcdop.ssh.error.FatalSshError;

import java.io.InputStream;

/**
 * Created by beolnix on 17/08/15.
 */
public class SSHClientServiceImpl implements SSHClientService {

    // dependencies
    private final SSHClientWrapper jsch;
    private final Long connectionThreshold;
    private final Long attemptDelay;
    private final String ipv4;
    private final String user;

    // state
    private SSHSessionWrapper session;

    SSHClientServiceImpl(SSHClientWrapper jsch, Long connectionThreshold, Long attemptDelay, String ipv4, String user) {
        this.jsch = jsch;
        this.connectionThreshold = connectionThreshold;
        this.attemptDelay = attemptDelay;
        this.ipv4 = ipv4;
        this.user = user;
    }

    private void configureSession() throws FatalSshError {
        try {
            session = jsch.getSession(user, ipv4);

            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
        } catch (JSchException e) {
            throw new FatalSshError("Can't configure ssh session.", e);
        }
    }

    private void establishConnection() throws FatalSshError {
        long start = System.currentTimeMillis();
        int attempts = 0;
        while (!session.isConnected()) {
            ++attempts;
            if ((System.currentTimeMillis() - start) > connectionThreshold) {
                throw new FatalSshError("Can't establish connection with " + ipv4 + " in " + connectionThreshold + " millis within " + attempts);
            }

            try {
                session.connect();
            } catch (JSchException e) {
                connectionDelay();
            }
        }
    }

    private void connectionDelay() {
        try {
            Thread.sleep(attemptDelay);
        } catch (InterruptedException ee) {
            //nop
        }
    }

    private void initIfRequired() throws FatalSshError {
        if (session == null) {
            configureSession();
        }

        if (!session.isConnected()) {
            session.disconnect();
            establishConnection();
        }
    }

    public String executeRemote(String command) throws FatalSshError {
        initIfRequired();

        String result = runCommand(command, session);

        return result;
    }

    private String runCommand(String command, SSHSessionWrapper session) {
        try {
            SSHChannelWrapper channel = session.openChannel("exec");
            channel.setCommand(command);

            channel.connect();

            InputStream inc = channel.getInputStream();

            byte[] tmp = new byte[1024];
            StringBuilder resultBuilder = new StringBuilder();
            while (true) {
                while (inc.available() > 0) {
                    int i = inc.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    resultBuilder.append(new String(tmp, 0, i));
                }
                if (channel.isClosed()) {
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ee) {
                    //nop
                }
            }
            channel.disconnect();
            return resultBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
