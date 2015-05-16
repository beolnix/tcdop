package io.cyberstock.tcdop.util;

import io.cyberstock.tcdop.util.error.KeyPairGenerationException;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

/**
 * Created by beolnix on 16/05/15.
 */
public class DOKeyPair {

    private byte[] privatePart;
    private byte[] publicPart;

    private String strPrivatePart;
    private String strPublicPart;

    private final static int KEY_LENGTH = 512;

    public static DOKeyPair createDOKeyPair(byte[] privatePart, byte[] publicPart) {
        if (privatePart == null || publicPart == null) {
            throw new IllegalArgumentException("Both private and public keys part must not be null");
        }

        return new DOKeyPair(privatePart, publicPart);
    }

    public static DOKeyPair createDOKeyPair() {
        KeyPairGenerator keyGen;

        try {
            keyGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new KeyPairGenerationException(e);
        }

        keyGen.initialize(KEY_LENGTH);
        KeyPair keyPair = keyGen.genKeyPair();

        return new DOKeyPair(keyPair.getPrivate().getEncoded(),
                keyPair.getPublic().getEncoded());
    }

    private DOKeyPair(byte[] privatePart, byte[] publicPart) {
        this.privatePart = privatePart;
        this.publicPart = publicPart;

        this.strPrivatePart = convertToString(this.privatePart);
        this.strPublicPart = convertToString(this.publicPart);
    }

    public byte[] getPrivatePart() {
        return privatePart;
    }

    public byte[] getPublicPart() {
        return publicPart;
    }

    public String getStrPrivatePart() {
        return strPrivatePart;
    }

    public String getStrPublicPart() {
        return strPublicPart;
    }

    private static String convertToString(byte[] key) {
        StringBuffer retString = new StringBuffer();
        for (int i = 0; i < key.length; ++i) {
            retString.append(Integer.toHexString(0x0100 + (key[i] & 0x00FF)).substring(1));
        }
        return retString.toString();
    }
}
