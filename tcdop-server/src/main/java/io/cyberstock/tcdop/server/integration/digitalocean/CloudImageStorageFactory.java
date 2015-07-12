package io.cyberstock.tcdop.server.integration.digitalocean;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;

import java.util.concurrent.Executor;

/**
 * Created by beolnix on 27/06/15.
 */
public class CloudImageStorageFactory {

    private final DOClientServiceFactory clientServiceFactory;

    public CloudImageStorageFactory(DOClientServiceFactory clientServiceFactory) {
        this.clientServiceFactory = clientServiceFactory;
    }

    public CloudImageStorage getStorage(Executor executor, String token) {
        DOClientService clientService = clientServiceFactory.createClient(token);
        CloudImageStorage cloudImageStorage = new CloudImageStorage(clientService, executor);
        cloudImageStorage.init();
        return cloudImageStorage;
    }
}
