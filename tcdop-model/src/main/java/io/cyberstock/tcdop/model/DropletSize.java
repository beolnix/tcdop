/* The MIT License
 *
 * Copyright (c) 2010-2015 Danila A. (atmakin.dv@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */
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
