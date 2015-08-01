package io.cyberstock.tcdop.server.integration.digitalocean.storage;

import io.cyberstock.tcdop.server.integration.digitalocean.storage.impl.CloudImageStorageImpl;

import java.util.concurrent.Executor;

/**
 * Created by beolnix on 01/08/15.
 */
public interface CloudImageStorageFactory {
    CloudImageStorage getStorage(Executor executor, String token);
}
