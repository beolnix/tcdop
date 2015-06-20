package io.cyberstock.tcdop.server.integration.digitalocean;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * Created by beolnix on 20/06/15.
 */
public class DOAsyncClientServiceFactory {

    private final ExecutorService executor;

    public DOAsyncClientServiceFactory(ExecutorService executor) {
        this.executor = executor;
    }

    public DOAsyncClientService createClient(String token) {
        DigitalOceanClient doCleint = new DigitalOceanClient(token);

        return new DOAsyncClientService(executor, doCleint);
    }

}
