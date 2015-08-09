package io.cyberstock.tcdop.server.integration.digitalocean.impl;

import com.myjeeva.digitalocean.DigitalOcean;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import io.cyberstock.tcdop.server.integration.digitalocean.DOAsyncClientService;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService;
import io.cyberstock.tcdop.server.integration.digitalocean.DOAsyncClientServiceFactory;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientServiceFactory;
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.DOAdapter;
import io.cyberstock.tcdop.server.integration.digitalocean.adapter.impl.DOAdapterImpl;

import java.util.concurrent.ExecutorService;

/**
 * Created by beolnix on 27/06/15.
 */
public class DOAsyncClientServiceFactoryImpl implements DOAsyncClientServiceFactory {

    // dependencies
    private final DOClientServiceFactory doClientServiceFactory;

    public DOAsyncClientServiceFactoryImpl(DOClientServiceFactory doClientServiceFactory) {
        this.doClientServiceFactory = doClientServiceFactory;
    }

    public DOAsyncClientService createClient(ExecutorService executorService, String token) {

        DOClientService clientService = doClientServiceFactory.createClient(token);
        DOAsyncClientServiceWrapper client = new DOAsyncClientServiceWrapper(executorService, clientService);
        return client;
    }

}
