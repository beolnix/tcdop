package io.cyberstock.tcdop.server.integration.digitalocean;

import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.digitalocean.impl.DOClientServiceImpl;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudInstance;

import java.util.concurrent.ExecutorService;

/**
 * Created by beolnix on 20/06/15.
 */
public class DOAsyncClientServiceWrapper {

    // dependencies
    private final ExecutorService executorService;
    private final DOClientService clientService;

    public DOAsyncClientServiceWrapper(ExecutorService executorService,
                                       DOClientService clientService) {
        this.executorService = executorService;
        this.clientService = clientService;
    }

    public void restartInstance(final DOCloudInstance cloudInstance) {
        executorService.execute(new Runnable() {
            public void run() {
                clientService.restartInstance(cloudInstance);
            }
        });
    }

    public DOCloudInstance initializeInstance(DOCloudImage cloudImage, DOSettings doSettings) throws DOError {
        final DOCloudInstance cloudInstance = clientService.createInstance(cloudImage, doSettings);
        executorService.execute(new Runnable() {
            public void run() {
                clientService.waitInstanceInitialization(cloudInstance);
            }
        });

        return cloudInstance;
    }

    public void terminateInstance(final DOCloudInstance cloudInstance) {
        executorService.execute(new Runnable() {
            public void run() {
                clientService.terminateInstance(cloudInstance);
            }
        });
    }

    public DOClientService getClientService() {
        return clientService;
    }
}
