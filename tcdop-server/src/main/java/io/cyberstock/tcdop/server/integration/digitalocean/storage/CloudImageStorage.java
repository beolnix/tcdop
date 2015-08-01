package io.cyberstock.tcdop.server.integration.digitalocean.storage;

import io.cyberstock.tcdop.server.integration.teamcity.DOCloudImage;

import java.util.Collection;

/**
 * Created by beolnix on 01/08/15.
 */
public interface CloudImageStorage {

    void forceUpdate();

    Collection<DOCloudImage> getImagesList();

    Integer getInstancesCount();

    DOCloudImage getImageById(String imageId);

    void shutdownStorage();
}
