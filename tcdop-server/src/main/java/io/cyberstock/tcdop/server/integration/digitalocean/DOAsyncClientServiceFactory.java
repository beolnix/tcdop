package io.cyberstock.tcdop.server.integration.digitalocean;

import io.cyberstock.tcdop.server.integration.digitalocean.impl.DOClientServiceImpl;

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
