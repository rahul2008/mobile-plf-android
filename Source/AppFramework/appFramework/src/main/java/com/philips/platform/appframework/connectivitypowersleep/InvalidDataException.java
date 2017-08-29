/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.platform.appframework.connectivitypowersleep;

public class InvalidDataException extends Exception {

    private static final long serialVersionUID = -1327777649396414973L;

    public InvalidDataException() {
    }

    public InvalidDataException(String message) {
        super(message);
    }
}
