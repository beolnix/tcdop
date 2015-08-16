package io.cyberstock.tcdop.ssh.impl;

import com.jcraft.jsch.JSch;
import io.cyberstock.tcdop.ssh.SSHClientFactory;
import io.cyberstock.tcdop.ssh.SSHClientService;
import io.cyberstock.tcdop.ssh.SSHClientServiceFactory;
import io.cyberstock.tcdop.ssh.error.FatalSshError;

/**
 * Created by beolnix on 17/08/15.
 */
public class SSHClientServiceFactoryImpl implements SSHClientServiceFactory {

    // dependencies
    private final SSHClientFactory sshClientFactory;

    // state
    private Long connectionThreshold = 6 * 60 * 1000L;  // 6m default threshold
    private Long attemptDelay = 100L;                   // 100ms default attempt delay

    public SSHClientServiceFactoryImpl(SSHClientFactory sshClientFactory) {
        this.sshClientFactory = sshClientFactory;
    }

    public SSHClientService createSSHClientService(String keyName, String privateKey, String ipv4, String user) throws FatalSshError {
        JSch jSch = sshClientFactory.chreateSSHClient(keyName, privateKey);
        SSHClientService service = new SSHClientServiceImpl(jSch, connectionThreshold, attemptDelay, ipv4, user);
        return service;
    }

    public void setConnectionThreshold(Long connectionThreshold) {
        this.connectionThreshold = connectionThreshold;
    }

    public void setAttemptDelay(Long attemptDelay) {
        this.attemptDelay = attemptDelay;
    }
}
