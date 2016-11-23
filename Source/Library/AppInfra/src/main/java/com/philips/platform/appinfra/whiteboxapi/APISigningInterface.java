package com.philips.platform.appinfra.whiteboxapi;

import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

/**
 * Created by 310238655 on 11/23/2016.
 */

public interface APISigningInterface {

    /**
     * Gets byte value.
     *
     * @return the byte[] value
     */
    public byte[] createHmac(final byte[] key, final byte[] data);
}
