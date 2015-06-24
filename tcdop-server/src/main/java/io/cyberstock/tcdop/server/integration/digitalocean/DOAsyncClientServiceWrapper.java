package io.cyberstock.tcdop.server.integration.digitalocean;

import com.intellij.openapi.diagnostic.Logger;
import com.myjeeva.digitalocean.pojo.Droplet;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudInstance;
import io.cyberstock.tcdop.server.integration.digitalocean.tasks.InstanceInitializationTask;
import io.cyberstock.tcdop.server.integration.digitalocean.tasks.StartInstanceTask;
import jetbrains.buildServer.clouds.CloudErrorInfo;
import jetbrains.buildServer.clouds.InstanceStatus;

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

    public void restartInstance(TCCloudInstance cloudInstance) {
        //TODO
    }

    public void terminateInstance(TCCloudInstance cloudInstance) {
        //TODO
    }

    public void initializeInstance(final TCCloudInstance cloudInstance) {
        executorService.execute(new InstanceInitializationTask(clientService, cloudInstance, new DOCallback<Droplet, DOError>() {
            public void onSuccess(Droplet droplet) {
                cloudInstance.setDroplet(droplet);
                cloudInstance.updateStatus(InstanceStatus.RUNNING);
            }

            public void onFailure(DOError error) {
                cloudInstance.updateStatus(InstanceStatus.ERROR);
                cloudInstance.updateErrorInfo(new CloudErrorInfo(error.getMessage()));
            }
        }));
    }



    public void shutdown() {
        executorService.shutdown();
    }
}
