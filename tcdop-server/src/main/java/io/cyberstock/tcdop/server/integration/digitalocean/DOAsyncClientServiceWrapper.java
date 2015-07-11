package io.cyberstock.tcdop.server.integration.digitalocean;

import com.intellij.openapi.diagnostic.Logger;
import com.sun.javafx.iio.ImageStorage;
import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudImage;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudInstance;
import jetbrains.buildServer.clouds.InstanceStatus;

import java.awt.*;
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

    public void restartInstance(final TCCloudInstance cloudInstance) {
        cloudInstance.updateStatus(InstanceStatus.RESTARTING);
        executorService.execute(new Runnable() {
            public void run() {
                clientService.restartInstance(cloudInstance);
            }
        });
    }

    public TCCloudInstance initializeInstance(TCCloudImage cloudImage, DOSettings doSettings) throws DOError {
        final TCCloudInstance cloudInstance = clientService.createInstance(cloudImage, doSettings);
        executorService.execute(new Runnable() {
            public void run() {
                clientService.waitInstanceInitialization(cloudInstance);
            }
        });

        return cloudInstance;
    }

    public void terminateInstance(final TCCloudInstance cloudInstance) {
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
