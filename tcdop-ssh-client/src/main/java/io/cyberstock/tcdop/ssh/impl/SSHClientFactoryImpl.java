package io.cyberstock.tcdop.ssh.impl;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import io.cyberstock.tcdop.ssh.SSHClientFactory;
import io.cyberstock.tcdop.ssh.SSHClientWrapper;
import io.cyberstock.tcdop.ssh.error.FatalSshError;

/**
 * Created by beolnix on 17/08/15.
 */
public class SSHClientFactoryImpl implements SSHClientFactory {

    public SSHClientFactoryImpl() {}

    public SSHClientWrapper chreateSSHClient(String keyName, String privateKey) throws FatalSshError {
        try {
            JSch jsch = new JSch();
            jsch.addIdentity("test", privateKey.getBytes(), null, null);

            return new SSHClientWrapperImpl(jsch);
        } catch (JSchException e) {
            throw new FatalSshError("Can't init ssh connection.", e);
        }
    }
}
