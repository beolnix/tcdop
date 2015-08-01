package io.cyberstock.tcdop.server.integration.digitalocean.storage.impl;

import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService;
import io.cyberstock.tcdop.server.integration.digitalocean.storage.CloudImageStorage;
import io.cyberstock.tcdop.server.integration.digitalocean.storage.CloudImageStorageFactory;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientServiceFactory;
import io.cyberstock.tcdop.server.integration.digitalocean.impl.DOClientServiceImpl;

import java.util.concurrent.Executor;

/**
 * Created by beolnix on 27/06/15.
 */
public class CloudImageStorageFactoryImpl implements CloudImageStorageFactory {

    private final DOClientServiceFactory clientServiceFactory;

    public CloudImageStorageFactoryImpl(DOClientServiceFactory clientServiceFactory) {
        this.clientServiceFactory = clientServiceFactory;
    }

    public CloudImageStorage getStorage(Executor executor, String token) {
        DOClientService clientService = clientServiceFactory.createClient(token);
        CloudImageStorageImpl cloudImageStorageImpl = new CloudImageStorageImpl(clientService, executor);
        cloudImageStorageImpl.init();
        return cloudImageStorageImpl;
    }
}
