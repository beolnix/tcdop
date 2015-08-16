package io.cyberstock.tcdop.ssh;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * Created by beolnix on 17/08/15.
 */
public interface SSHClientWrapper {
    SSHSessionWrapper getSession(String user, String host) throws JSchException;
}
