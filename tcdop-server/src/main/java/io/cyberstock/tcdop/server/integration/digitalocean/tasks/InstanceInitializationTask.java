package io.cyberstock.tcdop.server.integration.digitalocean.tasks;

import com.myjeeva.digitalocean.pojo.Droplet;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.digitalocean.DOCallback;
import io.cyberstock.tcdop.server.integration.digitalocean.DOClientService;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudInstance;

/**
 * Created by beolnix on 26/05/15.
 */
public class InstanceInitializationTask extends AsyncTask<TCCloudInstance, Droplet> {

    public InstanceInitializationTask(DOClientService clientService, TCCloudInstance cloudInstance, DOCallback<Droplet, DOError> callback) {
        super(clientService, cloudInstance, callback);
    }

    @Override
    protected Droplet execute(TCCloudInstance cloudInstance) throws DOError {
        return clientService.initializeInstance(cloudInstance);
    }
}
