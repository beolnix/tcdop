package io.cyberstock.tcdop.server.integration.digitalocean.tasks;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Droplet;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.digitalocean.DOCallback;

/**
 * Created by beolnix on 24/06/15.
 */
public class StartInstanceTask implements Runnable {

    private final DigitalOceanClient doClient;
    private final DOCallback<Droplet, DOError> callback;

    public StartInstanceTask(DigitalOceanClient doClient, DOCallback<Droplet, DOError> callback) {
        this.doClient = doClient;
        this.callback = callback;
    }

    public void run() {

    }
}
