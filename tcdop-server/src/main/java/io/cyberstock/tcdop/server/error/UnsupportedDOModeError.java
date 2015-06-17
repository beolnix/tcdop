package io.cyberstock.tcdop.server.error;

import io.cyberstock.tcdop.model.DOIntegrationMode;

/**
 * Created by beolnix on 24/05/15.
 */
public class UnsupportedDOModeError extends RuntimeException {
    public UnsupportedDOModeError(String message) {
        super(message);
    }

    public UnsupportedDOModeError(DOIntegrationMode mode) {
        super("Mode " + mode.name() + " isn't supported yet.");
    }
}
