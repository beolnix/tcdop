package io.cyberstock.tcdop.server.integration.digitalocean;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;

import java.util.concurrent.Executor;

/**
 * Created by beolnix on 27/06/15.
 */
public class CloudImageStorageFactory {

    private final DOClientServiceFactory clientServiceFactory;
    private final Executor executor;

    public CloudImageStorageFactory(DOClientServiceFactory clientServiceFactory, Executor executor) {
        this.clientServiceFactory = clientServiceFactory;
        this.executor = executor;
    }

    public CloudImageStorage getStorage(String token) {
        DOClientService clientService = clientServiceFactory.createClient(token);
        return new CloudImageStorage(clientService, executor);
    }
}
