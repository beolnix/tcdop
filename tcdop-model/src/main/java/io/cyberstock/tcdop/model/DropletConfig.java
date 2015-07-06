package io.cyberstock.tcdop.model;


import com.myjeeva.digitalocean.pojo.Kernel;
import com.myjeeva.digitalocean.pojo.Key;
import com.myjeeva.digitalocean.pojo.Region;

import java.util.*;

/**
 * Created by beolnix on 24/06/15.
 */
public class DropletConfig {
    private Integer diskSize = 4;
    private Integer memorySizeInMb = 2048;
    private String sizeSlug = "512mb";
    private Region region = new Region("ams1");
    private String dropletName = "TCDOP-" + UUID.randomUUID();
    private Key key = new Key("TC_AGENT_DROPLET_KEY", "ssh-rsa AAAAB3NzaC1yc2EAAAADAQABAAABAQDRCOa+GC8MWtLM9b4jivIhq0vs/OacC4gSdHQesbBYCybxsWwYuZHnZOP4JgZ9a8OgyrrMnGGdI8yT/FqRnzT8010NYlsy922zGkwyZrt9d73IrbibUQd2NQCRC63SUgEz5VbVfKK/tTv3Dcnnn5zENigHFC2zq2FeuV61dTRTg7743mh9VAJM56fw5EpCfJj0V+FH17SD6HcseRfJTbvtBsT/mkccfGFNNObYPeJYzhe60o7fOpl4ljeY4+iPpu9bKjl8NbeuQwpWQVmuEjkDIISSv5nhYMaOXgCBxj6783MC1yaDkc6khnQcEbDWqm03KBsbbjLjN0bDZhZ0Zc53 beolnix@Danilas-MacBook-Pro.local");
    private final String imageName;

    public DropletConfig(String imageName) {
        this.imageName = imageName;
    }

    public Integer getDiskSize() {
        return diskSize;
    }

    public Integer getMemorySizeInMb() {
        return memorySizeInMb;
    }

    public Region getRegion() {
        return region;
    }

    public List<Key> getKeys() {
        List<Key> keys = new ArrayList<Key>();
        keys.add(key);
        return keys;
    }

    public String getDropletName() {
        return dropletName;
    }

    public String getSizeSlug() {
        return sizeSlug;
    }

    public String getImageName() {
        return imageName;
    }
}
