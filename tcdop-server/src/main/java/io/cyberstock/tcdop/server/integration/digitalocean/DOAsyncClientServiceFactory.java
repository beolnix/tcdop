package io.cyberstock.tcdop.server.integration.digitalocean;

import io.cyberstock.tcdop.server.integration.digitalocean.impl.DOClientServiceImpl;

import java.util.concurrent.ExecutorService;

/**
 * Created by beolnix on 01/08/15.
 */
public interface DOAsyncClientServiceFactory {

    DOAsyncClientService createClient(ExecutorService executorService, String token);

}
