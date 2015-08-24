/* The MIT License
 *
 * Copyright (c) 2010-2015 Danila A. (atmakin.dv@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
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
