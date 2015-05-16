package io.cyberstock.tcdop.util.error;

/**
 * Created by beolnix on 16/05/15.
 */
public class KeyPairGenerationException extends RuntimeException {
    private final static String errMsg = "Something wrong with your java runtime, man. Everything looks good but.. " +
            "there is no support of RSA algorithm in it!!! I can't generate keyPair because of it, you must provide it.";

    public KeyPairGenerationException(Exception e) {
        super(errMsg, e);
    }
}
