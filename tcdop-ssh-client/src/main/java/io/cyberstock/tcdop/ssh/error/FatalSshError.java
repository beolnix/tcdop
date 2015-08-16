package io.cyberstock.tcdop.ssh.error;

/**
 * Created by beolnix on 17/08/15.
 */
public class FatalSshError extends Exception {

    public FatalSshError(String message) {
        super(message);
    }

    public FatalSshError(String message, Throwable cause) {
        super(message, cause);
    }
}
