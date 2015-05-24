package io.cyberstock.tcdop.server.error;

/**
 * Created by beolnix on 24/05/15.
 */
public class ConfigurationValidationError extends RuntimeException {

    public ConfigurationValidationError(String msg) {
        super(msg);
    }
}
