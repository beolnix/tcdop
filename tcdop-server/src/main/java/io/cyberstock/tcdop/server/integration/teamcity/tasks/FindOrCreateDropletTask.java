package io.cyberstock.tcdop.server.integration.teamcity.tasks;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Droplet;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.digitalocean.DOCallback;

/**
 * Created by beolnix on 20/06/15.
 */
public class FindOrCreateDropletTask implements Runnable {

    private final DigitalOceanClient digitalOceanClient;
    private final String dropletName;
    private final DOCallback<Droplet, DOError> callback;

    public FindOrCreateDropletTask(DigitalOceanClient digitalOceanClient, String dropletName, DOCallback<Droplet, DOError> callback) {
        this.digitalOceanClient = digitalOceanClient;
        this.dropletName = dropletName;
        this.callback = callback;
    }

    public void run() {
        //TODO
    }
}
