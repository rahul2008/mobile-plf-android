/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.pins.shinelib;

/**
 * Class that represents the timeout error during storage of BlueLib settings and user data.
 */
public class TimeoutException extends RuntimeException {
    public TimeoutException(String msg) {
        super(msg);
    }
}
