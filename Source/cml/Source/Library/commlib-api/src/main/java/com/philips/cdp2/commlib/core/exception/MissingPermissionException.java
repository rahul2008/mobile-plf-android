/*
 * Copyright (c) 2016, 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.exception;

/**
 * Exception that indicates that the given permissions are not sufficient.
 * @publicApi
 */
public class MissingPermissionException extends Exception {

    /**
     * @param message String The detailed message for this Exception
     */
    public MissingPermissionException(String message) {
        super(message);
    }
}
