package io.cyberstock.tcdop.server.integration.digitalocean.storage.impl;

import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientServiceFactory;
import io.cyberstock.tcdop.server.integration.digitalocean.storage.CloudImageStorage;
import io.cyberstock.tcdop.server.integration.digitalocean.storage.CloudImageStorageFactory;
import io.cyberstock.tcdop.server.integration.digitalocean.DOAsyncClientServiceFactory;

import java.util.concurrent.Executor;

/**
 * Created by beolnix on 27/06/15.
 */
public class CloudImageStorageFactoryImpl implements CloudImageStorageFactory {

    // dependencies
    private final DOClientServiceFactory doClientServiceFactory;

    // state
    private Long initThreashold = 90 * 1000L; // default init threashold

    public CloudImageStorageFactoryImpl(DOClientServiceFactory doClientServiceFactory) {
        this.doClientServiceFactory = doClientServiceFactory;
    }

    public void setInitThreashold(Long initThreashold) {
        this.initThreashold = initThreashold;
    }

    public CloudImageStorage createStorage(Executor executor, String token) {
        DOClientService doClientService = doClientServiceFactory.createClient(token);
        CloudImageStorageImpl cloudImageStorageImpl = new CloudImageStorageImpl(doClientService, executor, initThreashold);
        cloudImageStorageImpl.init();
        return cloudImageStorageImpl;
    }
}
