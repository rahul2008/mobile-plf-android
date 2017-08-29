/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
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
