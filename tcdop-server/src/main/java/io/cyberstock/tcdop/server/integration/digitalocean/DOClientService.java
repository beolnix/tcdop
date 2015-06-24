package io.cyberstock.tcdop.server.integration.digitalocean;

import com.google.common.base.Optional;
import com.intellij.openapi.diagnostic.Logger;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Droplet;
import io.cyberstock.tcdop.model.DropletConfig;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudInstance;
import jetbrains.buildServer.clouds.CloudErrorInfo;
import jetbrains.buildServer.clouds.InstanceStatus;

import java.util.Date;


/**
 * Created by beolnix on 24/06/15.
 */
public class DOClientService {

    private final DigitalOceanClient doClient;

    // constants
    private static final Logger LOG = Logger.getInstance(DOAsyncClientServiceWrapper.class.getName());

    public DOClientService(DigitalOceanClient doClient) {
        this.doClient = doClient;
    }

    public void initializeInstance(TCCloudInstance cloudInstance) {
        if (cloudInstance.getInstanceId() != null) {
            findOrCreateInstance(cloudInstance);
        } else {
            createInstance(cloudInstance);
        }

        startInstance(cloudInstance);
    }

    public void findOrCreateInstance(TCCloudInstance cloudInstance) {
        Integer instanceId = Integer.parseInt(cloudInstance.getInstanceId());
        try {
            Optional<Droplet> dropletOpt = DOUtils.findDropletById(doClient, instanceId);
            if (!dropletOpt.isPresent()) {
                createInstance(cloudInstance);
            } else {
                cloudInstance.setDroplet(dropletOpt.get());
            }
        } catch (DOError e) {
            cloudInstance.updateStatus(InstanceStatus.ERROR);
            cloudInstance.updateErrorInfo(new CloudErrorInfo("Can't find instance by id: " + instanceId, e.getMessage()));
        }
    }

    public void startInstance(TCCloudInstance cloudInstance) {
        Integer instanceId = Integer.parseInt(cloudInstance.getInstanceId());
        try {
            Date startTime = DOUtils.startInstance(doClient, instanceId);
            cloudInstance.setStartTime(startTime);
            cloudInstance.updateStatus(InstanceStatus.RUNNING);
        } catch (DOError e) {
            cloudInstance.updateStatus(InstanceStatus.ERROR);
            cloudInstance.updateErrorInfo(new CloudErrorInfo("Can't start instance with id: " + instanceId, e.getMessage()));
        }
    }

    public void restartInstance(TCCloudInstance cloudInstance) {
        Integer instanceId = Integer.parseInt(cloudInstance.getInstanceId());
        cloudInstance.updateStatus(InstanceStatus.RESTARTING);
        try {
            Date startTime = DOUtils.startInstance(doClient, instanceId);
            cloudInstance.setStartTime(startTime);
            cloudInstance.updateStatus(InstanceStatus.RUNNING);
        } catch (DOError e) {
            cloudInstance.updateStatus(InstanceStatus.ERROR);
            cloudInstance.updateErrorInfo(new CloudErrorInfo("Can't start instance with id: " + instanceId, e.getMessage()));
        }
    }

    public void terminateInstance(final TCCloudInstance cloudInstance) {
        Integer instanceId = Integer.parseInt(cloudInstance.getInstanceId());
        cloudInstance.updateStatus(InstanceStatus.SCHEDULED_TO_STOP);
        try {
            Date startTime = DOUtils.startInstance(doClient, instanceId);
            cloudInstance.setStartTime(startTime);
            cloudInstance.updateStatus(InstanceStatus.STOPPED);
        } catch (DOError e) {
            cloudInstance.updateStatus(InstanceStatus.ERROR_CANNOT_STOP);
            cloudInstance.updateErrorInfo(new CloudErrorInfo("Can't start instance with id: " + instanceId, e.getMessage()));
        }
    }

    public void createInstance(TCCloudInstance cloudInstance) {
        DropletConfig dropletConfig = cloudInstance.getDoSettings().getDropletConfig();
        try {
            Droplet droplet = DOUtils.createInstance(doClient, dropletConfig);
            cloudInstance.setDroplet(droplet);
        } catch (DOError e) {
            cloudInstance.updateStatus(InstanceStatus.ERROR);
            cloudInstance.updateErrorInfo(new CloudErrorInfo("Can't create new instance with config: " + dropletConfig.toString(), e.getMessage()));
        }
    }

}
