package io.cyberstock.tcdop.model;

/**
 * Created by beolnix on 16/05/15.
 */
public enum DOIntegrationMode {
    PREPARED_IMAGE(DOConfigConstants.PREPARED_IMAGE_MODE_CODE),
    DOCKER_BASED_AGENT(DOConfigConstants.DOCKER_BASED_MODE_CODE);

    private String code;

    private DOIntegrationMode(String code) {
        this.code = code;
    }

    public static DOIntegrationMode getByCode(String code) {
        if (code == null) {
            return null;
        }

        for (DOIntegrationMode mode : DOIntegrationMode.values()) {
            if (mode.getCode().equals(code)) {
                return mode;
            }
        }

        return null;
    }

    public String getCode() {
        return code;
    }
}
