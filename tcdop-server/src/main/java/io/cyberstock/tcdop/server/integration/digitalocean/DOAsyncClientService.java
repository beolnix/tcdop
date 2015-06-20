package io.cyberstock.tcdop.server.integration.digitalocean;

import com.intellij.openapi.diagnostic.Logger;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudClient;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudImage;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudInstance;
import io.cyberstock.tcdop.server.integration.teamcity.tasks.ClientInitializationTask;
import jetbrains.buildServer.clouds.CloudErrorInfo;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;

/**
 * Created by beolnix on 20/06/15.
 */
public class DOAsyncClientService {

    // dependencies
    private final ExecutorService executorService;
    private final DigitalOceanClient doClient;

    // constants
    private static final Logger LOG = Logger.getInstance(DOAsyncClientService.class.getName());

    public DOAsyncClientService(ExecutorService executorService, DigitalOceanClient doClient) {
        this.executorService = executorService;
        this.doClient = doClient;
    }

    public void preloadCloudImages(final TCCloudClient cloudClient) {
        executorService.execute(new ClientInitializationTask(doClient, new DOCallback<HashMap<String, TCCloudImage>, DOError>() {
            public void onSuccess(HashMap<String, TCCloudImage> result) {
                //cloudClient.setCloudImageMap(result);
                cloudClient.setReadyFlag(true);
            }

            public void onFailure(DOError error) {
                cloudClient.setReadyFlag(false);

                CloudErrorInfo cloudErrorInfo = new CloudErrorInfo(error.getMessage());
                cloudClient.setCloudErrorInfo(cloudErrorInfo);
            }
        }));
    }

    public void restartInstance(TCCloudInstance cloudInstance) {
        //TODO
    }

    public void terminateInstance(TCCloudInstance cloudInstance) {
        //TODO
    }

    public void findOrCreateDroplet(TCCloudInstance cloudInstance) {
        LOG.debug("findOrCreateDroplet triggered: " + cloudInstance.toString());
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
