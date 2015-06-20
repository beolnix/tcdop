package io.cyberstock.tcdop.server.integration.teamcity.tasks;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Image;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.digitalocean.DOCallback;
import io.cyberstock.tcdop.server.integration.digitalocean.DOUtils;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudImage;

import java.util.HashMap;
import java.util.List;

/**
 * Created by beolnix on 26/05/15.
 */
public class ClientInitializationTask implements Runnable {

    private DigitalOceanClient doClient;
    private DOCallback<HashMap<String, TCCloudImage>, DOError> callback;

    public ClientInitializationTask(DigitalOceanClient doClient,
                                    DOCallback<HashMap<String, TCCloudImage>, DOError> callback) {
        this.doClient = doClient;
        this.callback = callback;
    }

    public void run() {
        try {
            List<Image> images = DOUtils.getImages(doClient);
            HashMap<String, TCCloudImage> result = new HashMap<String, TCCloudImage>();
            for (Image image : images) {
//                DOCloudImage doImage = new DOCloudImage();
            }


            callback.onSuccess(result);
        } catch (DOError e) {
            callback.onFailure(e);
        }
    }
}
