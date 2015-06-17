package io.cyberstock.tcdop.server.service.tasks;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import io.cyberstock.tcdop.model.DOConfigConstants;
import io.cyberstock.tcdop.model.DOIntegrationMode;
import io.cyberstock.tcdop.server.DOCloudInstance;
import jetbrains.buildServer.clouds.CloudClientParameters;
import jetbrains.buildServer.clouds.CloudErrorInfo;
import jetbrains.buildServer.clouds.InstanceStatus;

/**
 * Created by beolnix on 24/05/15.
 */
public class TerminateTask implements Runnable {


    private DOCloudInstance instance;
    private DigitalOceanClient doClient;

    public TerminateTask(DOCloudInstance instance, DigitalOceanClient doClient) {
        this.instance = instance;
        this.doClient = doClient;
    }

    public void run() {
        try {
            instance.updateStatus(InstanceStatus.STOPPING);
            doClient.powerOffDroplet(instance.getDroplet().getId());
        } catch (Exception e) {
            instance.updateStatus(InstanceStatus.ERROR_CANNOT_STOP);
            instance.updateErrorInfo(new CloudErrorInfo(e.getMessage(), "can't terminate instance", e));
        }
        instance.updateStatus(InstanceStatus.STOPPED);
    }
}
