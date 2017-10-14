/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep.error;

public class FailedSyncSessionsException extends Exception {
    private static final long serialVersionUID = 8366188850206283421L;

    public FailedSyncSessionsException() {
        super("Failed to sync sessions from power sleep device");
    }

    public FailedSyncSessionsException(String message) {
        super(message);
    }
}
