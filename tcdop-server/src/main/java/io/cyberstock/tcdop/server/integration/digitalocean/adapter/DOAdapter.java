package io.cyberstock.tcdop.server.integration.digitalocean.adapter;

import com.google.common.base.Optional;
import com.myjeeva.digitalocean.DigitalOcean;
import com.myjeeva.digitalocean.common.DropletStatus;
import com.myjeeva.digitalocean.pojo.Account;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Image;
import io.cyberstock.tcdop.model.DOSettings;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage;
import jetbrains.buildServer.clouds.InstanceStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.List;

/**
 * Created by beolnix on 09/08/15.
 */
public interface DOAdapter {

    @NotNull
    Droplet createInstance(DOSettings doSettings, DOCloudImage cloudImage) throws DOError;
    Account checkAccount() throws DOError;
    String waitForDropletInitialization(Integer dropletId) throws DOError;
    Boolean terminateInstance(Integer instanceId) throws DOError;
    Date restartInstance(Integer instanceId) throws DOError;
    Optional<Image> findImageByName(String imageName) throws DOError;
    Optional<Image> getImageById(Integer imageId) throws DOError;

    InstanceStatus transformStatus(DropletStatus dropletStatus);
    List<Image> getImages() throws DOError;
    List<Droplet> getDroplets() throws DOError;

}
