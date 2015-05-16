package io.cyberstock.tcdop.server.error;

/**
 * Created by beolnix on 16/05/15.
 */
public class DOCommunicationError extends RuntimeException {

    public static final String errMsg = "Can't send request to Digital Ocean, check if everything is fine with network.";

    public DOCommunicationError(Exception e) {
        super(errMsg, e);
    }
}
