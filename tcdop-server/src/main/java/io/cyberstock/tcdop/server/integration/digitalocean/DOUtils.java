package io.cyberstock.tcdop.server.integration.digitalocean;

import com.google.common.base.Optional;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.*;
import io.cyberstock.tcdop.model.DropletConfig;
import io.cyberstock.tcdop.model.error.DOError;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by beolnix on 24/05/15.
 */
public class DOUtils {

    public static Optional<Droplet> findDropletById(DigitalOceanClient doClient, Integer dropletId) throws DOError {
        Droplet dropletInfo = null;
        try {
            dropletInfo = doClient.getDropletInfo(dropletId);
        } catch (DigitalOceanException e) {
            throw new DOError("Can't find droplet with id: " + dropletId, e);
        } catch (RequestUnsuccessfulException e) {
            throw new DOError("Can't find droplet with id: " + dropletId, e);
        }
        return Optional.fromNullable(dropletInfo);
    }

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

    @NotNull
    public static Droplet createInstance(DigitalOceanClient doClient, DropletConfig dropletConfig) throws DOError {
        Droplet droplet = new Droplet();
        droplet.setDiskSize(dropletConfig.getDiskSize());
        droplet.setMemorySizeInMb(dropletConfig.getMemorySizeInMb());
        droplet.setName(dropletConfig.getDropletName());
        droplet.setRegion(dropletConfig.getRegion());
        droplet.setKeys(dropletConfig.getKeys());

        Optional<Image> imageOpt = DOUtils.findImageByName(doClient, dropletConfig.getImageName());
        if (!imageOpt.isPresent()) {
            throw new DOError("There is no image with name \"" + dropletConfig.getImageName() + "\" in user images.");
        }
        droplet.setImage(imageOpt.get());

        try {
            Droplet createdDroplet = doClient.createDroplet(droplet);
            return createdDroplet;
        } catch (DigitalOceanException e) {
            throw new DOError("Can't create droplet.", e);
        } catch (RequestUnsuccessfulException e) {
            throw new DOError("Can't create droplet.", e);
        }
    }

    public static Date startInstance(DigitalOceanClient doClient, Integer instanceId) throws DOError {
        try {
            Action action = doClient.powerOnDroplet(instanceId);
            return action.getCompletedAt();
        } catch (DigitalOceanException e) {
            throw new DOError("Can't start instance with id: " + instanceId , e);
        } catch (RequestUnsuccessfulException e) {
            throw new DOError("Can't start instance with id: " + instanceId , e);
        }
    }

    public static Date stopInstance(DigitalOceanClient doClient, Integer instanceId) throws DOError {
        try {
            Action action = doClient.powerOffDroplet(instanceId);
            return action.getCompletedAt();
        } catch (DigitalOceanException e) {
            throw new DOError("Can't stop instance with id: " + instanceId , e);
        } catch (RequestUnsuccessfulException e) {
            throw new DOError("Can't stop instance with id: " + instanceId , e);
        }
    }

    public static Date restartInstance(DigitalOceanClient doClient, Integer instanceId) throws DOError {
        try {
            Action action = doClient.rebootDroplet(instanceId);
            return action.getCompletedAt();
        } catch (DigitalOceanException e) {
            throw new DOError("Can't restart instance with id: " + instanceId , e);
        } catch (RequestUnsuccessfulException e) {
            throw new DOError("Can't restart instance with id: " + instanceId , e);
        }
    }

    public static Optional<Image> findImageByName(DigitalOceanClient doClient, String imageName) throws DOError{
        int pageNumber = 0;
        while (true) {
            Images images = null;
            try {
                images = doClient.getUserImages(pageNumber);
            } catch (DigitalOceanException e) {
                throw new DOError("Can't find image by name \"" + imageName + "\".", e);
            } catch (RequestUnsuccessfulException e) {
                throw new DOError("Can't find image by name \"" + imageName + "\".", e);
            }
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

    public static List<Droplet> getDroplets(DigitalOceanClient doClient) throws DOError {
        List<Droplet> resultList = new LinkedList<Droplet>();
        int pageNumber = 0;
        try {
            while (true) {
                Droplets droplets = doClient.getAvailableDroplets(pageNumber);
                List<Droplet> imageList = droplets.getDroplets();
                if (imageList.isEmpty()) {
                    break;
                } else {
                    resultList.addAll(imageList);
                }

                ++pageNumber;
            }
        } catch (DigitalOceanException e) {
            throw new DOError("Can't get available droplets:" + e.getMessage(), e);
        } catch (RequestUnsuccessfulException e) {
            throw new DOError("Communication issue while: " + e.getMessage(), e);
        }

        return resultList;
    }
}
