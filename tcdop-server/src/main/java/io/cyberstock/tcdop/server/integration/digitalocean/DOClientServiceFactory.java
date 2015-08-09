package io.cyberstock.tcdop.server.integration.digitalocean;

/**
 * Created by beolnix on 10/08/15.
 */
public interface DOClientServiceFactory {

    DOClientService createClient(String token);

}
