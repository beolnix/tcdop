package io.cyberstock.tcdop.server.integration.digitalocean;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import jetbrains.buildServer.clouds.CloudImage;

import java.util.concurrent.ExecutorService;

/**
 * Created by beolnix on 20/06/15.
 */
public class DOAsyncClientServiceFactory {


    private final DOClientServiceFactory clientServiceFactory;

    public DOAsyncClientServiceFactory(DOClientServiceFactory clientServiceFactory) {
        this.clientServiceFactory = clientServiceFactory;
    }

    public DOAsyncClientServiceWrapper createClient(ExecutorService executor, String token) {
        DOClientService clientService = clientServiceFactory.createClient(token);
        return new DOAsyncClientServiceWrapper(executor, clientService);
    }

}
