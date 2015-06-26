package io.cyberstock.tcdop.server.integration.digitalocean;

import com.intellij.openapi.diagnostic.Logger;

import java.util.concurrent.Executor;

/**
 * Created by beolnix on 27/06/15.
 */
public class CloudImageStorage {

    private final DOClientService clientService;
    private final Executor executor;
    private final static Integer CHECK_INTERVAL = 60 * 1000;

    private static final Logger LOG = Logger.getInstance(CloudImageStorage.class.getName());

    public CloudImageStorage(DOClientService clientService, Executor executor) {
        this.clientService = clientService;
        this.executor = executor;
    }

    public void init() {
        executor.execute(new CloudImagesChecker());
    }

    private class CloudImagesChecker implements Runnable {



        public void run() {
            while (true) {


                try {
                    Thread.sleep(CHECK_INTERVAL);
                } catch (InterruptedException e) {
                    LOG.error("Abort background images checks, setup new checker.", e);
                    init();
                    return;
                }
            }

        }
    }
}
