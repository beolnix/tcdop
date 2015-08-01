package io.cyberstock.tcdop.server.integration.digitalocean;

import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudInstance;

import java.util.List;

/**
 * Created by beolnix on 01/08/15.
 */
public interface DOClientService {

    List<DOCloudImage> getImages();

    void waitInstanceInitialization(DOCloudInstance cloudInstance);

    void restartInstance(DOCloudInstance cloudInstance);

    void terminateInstance(final DOCloudInstance cloudInstance);

    DOCloudInstance createInstance(DOCloudImage cloudImage, DOSettings doSettings) throws DOError;

    void accountCheck() throws DOError;

    DOCloudImage findImageByName(String imageName) throws DOError;
}
