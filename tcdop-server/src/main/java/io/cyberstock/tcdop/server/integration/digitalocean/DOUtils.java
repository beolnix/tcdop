package io.cyberstock.tcdop.server.integration.digitalocean;

import com.google.common.base.Optional;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Droplets;
import com.myjeeva.digitalocean.pojo.Image;
import com.myjeeva.digitalocean.pojo.Images;
import io.cyberstock.tcdop.model.error.DOError;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by beolnix on 24/05/15.
 */
public class DOUtils {

    public static Optional<Droplet> findDropletByName(DigitalOceanClient doClient, String dropletName) throws DigitalOceanException, RequestUnsuccessfulException {
        int pageNumber = 0;
        while (true) {
            Droplets droplets = doClient.getAvailableDroplets(pageNumber);
            List<Droplet> dropletList = droplets.getDroplets();
            if (dropletList.isEmpty()) {
                break;
            } else {
                for (Droplet droplet : dropletList) {
                    if (droplet.getName().equals(dropletName)) {
                        return Optional.of(droplet);
                    }
                }
            }

            ++pageNumber;
        }

        return Optional.absent();
    }

    public static Optional<Image> findImageByName(DigitalOceanClient doClient, String imageName) throws DigitalOceanException, RequestUnsuccessfulException {
        int pageNumber = 0;
        while (true) {
            Images images = doClient.getUserImages(pageNumber);
            List<Image> imageList = images.getImages();
            if (imageList.isEmpty()) {
                break;
            } else {
                for (Image image : imageList) {
                    if (image.getName().equals(imageName)) {
                        return Optional.of(image);
                    }
                }
            }

            ++pageNumber;
        }

        return Optional.absent();
    }

    public static List<Image> getImages(DigitalOceanClient doClient) throws DOError {
        List<Image> resultList = new LinkedList<Image>();
        int pageNumber = 0;
        try {
            while (true) {
                Images images = doClient.getUserImages(pageNumber);
                List<Image> imageList = images.getImages();
                if (imageList.isEmpty()) {
                    break;
                } else {
                    resultList.addAll(imageList);
                }

                ++pageNumber;
            }
        } catch (DigitalOceanException e) {
            throw new DOError("Can't get user images:" + e.getMessage(), e);
        } catch (RequestUnsuccessfulException e) {
            throw new DOError("Communication issue while: " + e.getMessage(), e);
        }

        return resultList;
    }
}
