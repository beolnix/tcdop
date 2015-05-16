package io.cyberstock.tcdop.api;

/**
 * Created by beolnix on 08/05/15.
 */
public interface DOConfigConstants {
    public static final String IDENTITY_KEY = "DO_AGENT_TYPE";
    public static final String IDENTITY_VALUE = "DIGITALOCEAN_AGENT_01";
    public static final String CLOUD_CODE = "DO";

    public static final String TOKEN = "api_key";
    public static final String INSTANCE_ID = "instance_id";
    public static final String IMAGE_ID = "image_id";
    public static final String SSH_PRIVATE_KEY = "ssh_private_key";
    public static final String SSH_PUBLIC_KEY = "ssh_public_key";

    public static final String DO_INTEGRATION_MODE = "do_integration_mode";
    public static final String PREPARED_IMAGE_MODE_CODE = "prepared_image_mode";
    public static final String DOCKER_BASED_MODE_CODE = "docer_based";
}