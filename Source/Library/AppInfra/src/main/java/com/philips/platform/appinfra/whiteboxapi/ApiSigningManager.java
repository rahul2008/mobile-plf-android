package com.philips.platform.appinfra.whiteboxapi;

/**
 * Created by 310238655 on 11/23/2016.
 */

public class APISigningManager implements APISigningInterface {
    @Override
    public byte[] createHmac(final byte[] key, final byte[] data) {
        return pshmac(key, data);
    }

    static {
        System.loadLibrary("pshmac");
    }

    public native byte[] pshmac(byte[] key, byte[] message);
}
