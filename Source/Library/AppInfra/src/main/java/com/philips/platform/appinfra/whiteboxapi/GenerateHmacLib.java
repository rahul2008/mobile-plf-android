package com.philips.platform.appinfra.whiteboxapi;

/**
 * Created by 310238655 on 11/23/2016.
 */

public class GenerateHmacLib {

    static {
        System.loadLibrary("pshmac");
    }

    public static native byte[] pshmac(byte[] key, byte[] message);

    public static byte[] createHmac(final byte[] key, final byte[] data) {
        return pshmac(key, data);
    }
}
