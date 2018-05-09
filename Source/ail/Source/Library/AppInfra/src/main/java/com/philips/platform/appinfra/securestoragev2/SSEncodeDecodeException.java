package com.philips.platform.appinfra.securestoragev2;

import java.io.Serializable;

/**
 * Created by abhishek on 2/7/18.
 */

public class SSEncodeDecodeException extends Exception implements Serializable {
    private static final long serialVersionUID = -2092623379705101184L;

    public SSEncodeDecodeException(String message) {
        super(message);
    }
}
