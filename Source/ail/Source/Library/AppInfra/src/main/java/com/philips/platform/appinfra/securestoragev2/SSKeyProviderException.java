package com.philips.platform.appinfra.securestoragev2;

import java.io.Serializable;

/**
 * Created by abhishek on 2/7/18.
 */

public class SSKeyProviderException extends Exception implements Serializable {
    private static final long serialVersionUID = 5809903828681057142L;

    public SSKeyProviderException(String message) {
        super(message);
    }
}
