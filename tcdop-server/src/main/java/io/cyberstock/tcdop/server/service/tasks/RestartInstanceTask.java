package io.cyberstock.tcdop.server.service.tasks;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Droplet;
import io.cyberstock.tcdop.server.DOCloudInstance;
import jetbrains.buildServer.clouds.CloudErrorInfo;
import jetbrains.buildServer.clouds.InstanceStatus;

/**
 * Created by beolnix on 24/05/15.
 */
public class RestartInstanceTask implements Runnable {

    private DOCloudInstance instance;
    private DigitalOceanClient doClient;

    public RestartInstanceTask(DOCloudInstance instance, DigitalOceanClient doClient) {
        this.instance = instance;
        this.doClient = doClient;
    }

    public void run() {
        instance.updateStatus(InstanceStatus.RESTARTING);
        try {
            doClient.rebootDroplet(instance.getDroplet().getId());
        } catch (Exception e) {
            instance.updateStatus(InstanceStatus.ERROR);
            instance.updateErrorInfo(new CloudErrorInfo(e.getMessage(), "can't restart droplet", e));
            return;
        }
        instance.updateStatus(InstanceStatus.RUNNING);
    }
}
