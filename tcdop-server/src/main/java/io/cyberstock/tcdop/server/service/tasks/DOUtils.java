package io.cyberstock.tcdop.server.service.tasks;

import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Droplet;
import com.myjeeva.digitalocean.pojo.Droplets;

import java.util.List;

/**
 * Created by beolnix on 24/05/15.
 */
public class DOUtils {

    public static Droplet findDropletByName(DigitalOceanClient doClient, String dropletName) throws Exception {
        int pageNumber = 0;
        while (true) {
            Droplets droplets = doClient.getAvailableDroplets(pageNumber);
            List<Droplet> dropletList = droplets.getDroplets();
            if (dropletList.isEmpty()) {
                break;
            } else {
                for (Droplet droplet : dropletList) {
                    if (droplet.getName().equals(dropletName)) {
                        return droplet;
                    }
                }
            }

            ++pageNumber;
        }

        return null;
    }
}
