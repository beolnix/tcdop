package io.cyberstock.tcdop.server;

import java.io.File;

/**
 * Created by beolnix on 11/05/15.
 */
public class PathResolverHelper {

    public PathResolverHelper() {
        File file = new File("classpath:/test");
        System.out.println("full path: " + file.getAbsolutePath());
    }
}
