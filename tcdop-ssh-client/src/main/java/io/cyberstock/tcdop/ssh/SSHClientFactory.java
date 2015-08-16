package io.cyberstock.tcdop.ssh;

import com.jcraft.jsch.JSch;
import io.cyberstock.tcdop.ssh.error.FatalSshError;

/**
 * Created by beolnix on 17/08/15.
 */
public interface SSHClientFactory {

    SSHClientWrapper chreateSSHClient(String keyName, String privateKey) throws FatalSshError;
}
