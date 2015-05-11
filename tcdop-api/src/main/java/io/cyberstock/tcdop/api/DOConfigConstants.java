package io.cyberstock.tcdop.api;

/**
 * Created by beolnix on 08/05/15.
 */
public interface DOConfigConstants {
    public static final String IDENTITY_KEY = "DO_AGENT_TYPE";
    public static final String IDENTITY_VALUE = "DIGITALOCEAN_AGENT_01";
    public static final String CLOUD_CODE = "DO";

    public static final String API_KEY = "api_key";
    public static final String INSTANCE_ID = "instance_id";
    public static final String SSH_PRIVATE_KEY = "ssh_private_key";
    public static final String SSH_PUBLIC_KEY = "ssh_public_key";

    public static final String AGENT_CONFIG_TYPE = "agent_config_type";
    public static final String SINGLE_INSTANCE_TYPE = "single_instance";
    public static final String MULTIPLE_DOCKER_BASED_INSTANCE_TYPE = "docer_based";
}
