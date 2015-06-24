package io.cyberstock.tcdop.server.integration.digitalocean;

import com.google.common.base.Optional;
import com.intellij.openapi.diagnostic.Logger;
import com.myjeeva.digitalocean.exception.DigitalOceanException;
import com.myjeeva.digitalocean.exception.RequestUnsuccessfulException;
import com.myjeeva.digitalocean.impl.DigitalOceanClient;
import com.myjeeva.digitalocean.pojo.Droplet;
import io.cyberstock.tcdop.model.error.DOError;
import io.cyberstock.tcdop.server.integration.teamcity.TCCloudInstance;
import org.jetbrains.annotations.NotNull;

/**
 * Created by beolnix on 24/06/15.
 */
public class DOClientService {

    private final DigitalOceanClient doClient;

    // constants
    private static final Logger LOG = Logger.getInstance(DOAsyncClientServiceWrapper.class.getName());

    public DOClientService(DigitalOceanClient doClient) {
        this.doClient = doClient;
    }

    public Droplet initializeInstance(TCCloudInstance cloudInstance) throws DOError {
        String dropletId = cloudInstance.getInstanceId();
        Droplet droplet = null;
        if (dropletId != null) {
            droplet = findOrCreateInstance(dropletId);
        } else {
            droplet = createInstance();
        }

        startInstance(droplet);

        return droplet;
    }

    public Droplet findOrCreateInstance(String dropletId) throws DOError {

        try {
            Optional<Droplet> dropletOpt = DOUtils.findDropletById(doClient, dropletId);
            if (!dropletOpt.isPresent()) {
                return createInstance();
            } else {
                return dropletOpt.get();
            }
        } catch (DigitalOceanException e) {
            throw new DOError(e.getMessage(), e);
        } catch (RequestUnsuccessfulException e) {
            throw new DOError(e.getMessage(), e);
        }
    }

    public void startInstance(Droplet droplet) throws DOError {

    }

    @NotNull
    public Droplet createInstance() throws DOError {
        return null;
    }
}
