package io.cyberstock.tcdop.ssh;

import io.cyberstock.tcdop.ssh.error.FatalSshError;

/**
 * Created by beolnix on 17/08/15.
 */
public interface SSHClientServiceFactory {

    public SSHClientService createSSHClientService(String keyName, String privateKey, String ipv4, String user) throws FatalSshError;
}
