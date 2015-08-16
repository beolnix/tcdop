package io.cyberstock.tcdop.server.integration.digitalocean.adapter.impl;

import com.myjeeva.digitalocean.common.DropletStatus;
import com.myjeeva.digitalocean.pojo.Droplet;
import io.cyberstock.tcdop.model.error.DOError;
import jetbrains.buildServer.clouds.InstanceStatus;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.IllegalFormatException;

/**
 * Created by beolnix on 16/08/15.
 */
public class DOAdapterUtils {
    private DOAdapterUtils() {}

    public static String modifySSHKeyName(String name) {
        String[] nameRaw = name.split("_");
        String indexStr = nameRaw[nameRaw.length - 1];

        Integer index = 2;
        if (StringUtils.isNumeric(indexStr)) {
            try {
                index = Integer.parseInt(indexStr) + 1;
                nameRaw = Arrays.copyOf(nameRaw, nameRaw.length - 1);
            } catch (IllegalFormatException e) {
                //nop
            }
        }

        return StringUtils.join(nameRaw) + "_" + index;
    }

    public static DOError dropletCreationRequestError(Exception e) {
        String errMsg = "Can't create droplet: " + e.getMessage();
        return new DOError(errMsg, e);
    }

    public static boolean isThresholdReached(long start, long actionWaitThreshold) {
        long current = System.currentTimeMillis();
        long duration = current - start;
        return duration > actionWaitThreshold;
    }

    public static DOError createActionError(Integer actionId, Exception e) {
        String errMsg = "Can't get actionInfo with id: " + actionId;
        return new DOError(errMsg, e);
    }

    public static DOError waitThresholdReached(String operationName) {
        String errMsg = operationName + ": result wait threshold reached.";
        return new DOError(errMsg);
    }

    public static boolean isDropletActive(Droplet droplet) {
        if (droplet == null) {
            return false;
        }
        return DropletStatus.ACTIVE.equals(droplet.getStatus());
    }

    public static InstanceStatus transformStatusOfDroplet(DropletStatus dropletStatus) {
        switch (dropletStatus) {
            case NEW:
                return InstanceStatus.STARTING;
            case ACTIVE:
                return InstanceStatus.RUNNING;
            case ARCHIVE:
                return InstanceStatus.STOPPED;
            case OFF:
                return InstanceStatus.STOPPED;
            default:
                return InstanceStatus.UNKNOWN;
        }
    }


}
