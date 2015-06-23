package io.cyberstock.tcdop.model.error;

/**
 * Created by beolnix on 18/06/15.
 */
public class DOError extends Exception {
    public DOError(String message, Throwable cause) {
        super(message, cause);
    }

    public DOError(String message) {
        super(message);
    }
}
