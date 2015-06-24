package io.cyberstock.tcdop.server.integration.digitalocean;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;

import java.util.concurrent.ExecutorService;

/**
 * Created by beolnix on 20/06/15.
 */
public class DOAsyncClientServiceFactory {

    private final ExecutorService executor;

    public DOAsyncClientServiceFactory(ExecutorService executor) {
        this.executor = executor;
    }

    public DOAsyncClientServiceWrapper createClient(String token) {
        DigitalOceanClient doClient = new DigitalOceanClient(token);
        DOClientService clientService = new DOClientService(doClient);
        return new DOAsyncClientServiceWrapper(executor, clientService);
    }

}
