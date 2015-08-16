package io.cyberstock.tcdop.ssh;

import io.cyberstock.tcdop.ssh.error.FatalSshError;

/**
 * Created by beolnix on 17/08/15.
 */
public interface SSHClientService {

    public String executeRemote(String command) throws FatalSshError;
}
