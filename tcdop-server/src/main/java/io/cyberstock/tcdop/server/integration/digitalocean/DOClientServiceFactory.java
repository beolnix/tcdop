package io.cyberstock.tcdop.server.integration.digitalocean;

import io.cyberstock.tcdop.server.integration.digitalocean.impl.DOClientServiceImpl;

/**
 * Created by beolnix on 01/08/15.
 */
public interface DOClientServiceFactory {

    DOClientService createClient(String token);

}
