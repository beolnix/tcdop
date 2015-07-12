package io.cyberstock.tcdop.model;

/**
 * Created by beolnix on 08/05/15.
 */
public interface DOConfigConstants {

    // cloud id constant
    public static final String CLOUD_CODE = "DO";

    // cloud integration modes
    public static final String PREPARED_IMAGE_MODE_CODE = "prepared_image_mode";
    public static final String DOCKER_BASED_MODE_CODE = "docer_based";

    // agent configuration constants
    public static final String IDENTITY_KEY = "env.AGENT_TYPE";
    public static final String IDENTITY_VALUE = "DIGITALOCEAN_AGENT_TYPE";
    public static final String AGENT_IPV4_PROP_KEY = "agent_ipv4";
    public static final String AGENT_NAME_PROP = "teamcity.agent.name";

}
