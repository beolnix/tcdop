package io.cyberstock.tcdop.util;

import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import org.testng.annotations.Test;

/**
 * Created by beolnix on 16/05/15.
 */
public class DOKeyPairTestCase {

    @Test
    public void test() {
        DOKeyPair keyPair = DOKeyPair.createDOKeyPair();

        assertNotNull(keyPair);
        assertNotNull(keyPair.getPrivatePart());
        assertNotNull(keyPair.getPublicPart());
        assertNotNull(keyPair.getStrPrivatePart());
        assertNotNull(keyPair.getStrPublicPart());
    }

}
