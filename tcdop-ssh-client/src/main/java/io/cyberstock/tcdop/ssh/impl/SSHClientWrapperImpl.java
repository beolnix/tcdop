package io.cyberstock.tcdop.ssh.impl;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import io.cyberstock.tcdop.ssh.SSHClientWrapper;
import io.cyberstock.tcdop.ssh.SSHSessionWrapper;

/**
 * Created by beolnix on 17/08/15.
 */
public class SSHClientWrapperImpl implements SSHClientWrapper {
    private final JSch jSch;

    public SSHClientWrapperImpl(JSch jSch) {
        this.jSch = jSch;
    }

    public SSHSessionWrapper getSession(String user, String host) throws JSchException {
        return new SSHSessionWrapperImpl(jSch.getSession(user, host));
    }
}
