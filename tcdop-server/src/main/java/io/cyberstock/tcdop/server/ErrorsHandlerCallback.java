package io.cyberstock.tcdop.server;

/**
 * Created by beolnix on 03/06/15.
 */
public interface ErrorsHandlerCallback {
    public void handleError(Throwable e, String msg);
}
