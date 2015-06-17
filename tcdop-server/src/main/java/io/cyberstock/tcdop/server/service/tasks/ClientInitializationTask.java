package io.cyberstock.tcdop.server.service.tasks;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Image;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.DOCloudImage;

import java.util.HashMap;
import java.util.List;

/**
 * Created by beolnix on 26/05/15.
 */
public class ClientInitializationTask implements Runnable {

    private DigitalOceanClient doClient;
    private AsyncTaskCallback<HashMap<String, DOCloudImage>, DOError> callback;

    public ClientInitializationTask(DigitalOceanClient doClient,
                                    AsyncTaskCallback<HashMap<String, DOCloudImage>, DOError> callback) {
        this.doClient = doClient;
        this.callback = callback;
    }

    public void run() {
        try {
            List<Image> images = DOUtils.getImages(doClient);
            HashMap<String, DOCloudImage> result = new HashMap<String, DOCloudImage>();
            for (Image image : images) {
//                DOCloudImage doImage = new DOCloudImage();
            }


            callback.onSuccess(result);
        } catch (DOError e) {
            callback.onFailure(e);
        }
    }
}
