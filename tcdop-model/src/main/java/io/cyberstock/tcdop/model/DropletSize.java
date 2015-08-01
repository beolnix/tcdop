package io.cyberstock.tcdop.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by beolnix on 12/07/15.
 */
public enum DropletSize {
    M512MB(20, "512MB"),
    M1GB(30, "1GB"),
    M2GB(40, "2GB"),
    M4GB(60, "4GB"),
    M8GB(80, "8GB"),
    M16GB(160, "16GB"),
    M32GB(320, "32GB"),
    M64GB(640, "64GB");

    private Integer diskSize;
    private String slug;

    private DropletSize(Integer diskSize, String slug) {
        this.diskSize = diskSize;
        this.slug = slug;
    }

    public boolean isLessOrEqualThen(DropletSize that) {
        return this.diskSize <= that.diskSize;
    }

    public static DropletSize resolveBySlug(String slug) {
        if (StringUtils.isEmpty(slug)) {
            throw new IllegalArgumentException("slug must not be null");
        }

        for (DropletSize size : DropletSize.values()) {
            if (size.getSlug().equals(slug)) {
                return size;
            }
        }

        throw new IllegalArgumentException("unknown slug: " + slug);
    }

    public static DropletSize resolveByDiskSize(Integer diskSize) {
        if (diskSize == null) {
            throw new IllegalArgumentException("diskSize must not be null");
        }

        for (DropletSize size : DropletSize.values()) {
            if (size.getDiskSize() >= diskSize) {
                return size;
            }
        }
        return null;
    }

    public Integer getDiskSize() {
        return diskSize;
    }

    public String getSlug() {
        return slug;
    }


}
