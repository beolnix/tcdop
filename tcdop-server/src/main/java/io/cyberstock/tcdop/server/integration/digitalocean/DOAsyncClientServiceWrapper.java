package io.cyberstock.tcdop.server.integration.digitalocean;

import com.intellij.openapi.diagnostic.Logger;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudInstance;

import java.util.concurrent.ExecutorService;

/**
 * Created by beolnix on 20/06/15.
 */
public class DOAsyncClientServiceWrapper {

    // dependencies
    private final ExecutorService executorService;
    private final DOClientService clientService;

    // constants
    private static final Logger LOG = Logger.getInstance(DOAsyncClientServiceWrapper.class.getName());

    public DOAsyncClientServiceWrapper(ExecutorService executorService, DOClientService clientService) {
        this.executorService = executorService;
        this.clientService = clientService;
    }

    public void restartInstance(final TCCloudInstance cloudInstance) {
        executorService.execute(new Runnable() {
            public void run() {
                clientService.restartInstance(cloudInstance);
            }
        });
    }

    public void terminateInstance(final TCCloudInstance cloudInstance) {
        executorService.execute(new Runnable() {
            public void run() {
                clientService.terminateInstance(cloudInstance);
            }
        });
    }

    public void initializeInstance(final TCCloudInstance cloudInstance) {
        executorService.execute(new Runnable() {
            public void run() {
                clientService.initializeInstance(cloudInstance);
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public DOClientService getClientService() {
        return clientService;
    }
}
