/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.connectivitypowersleep.error;

public class FailedSyncSessionsException extends Exception {
    public FailedSyncSessionsException() {
        super("Failed to sync sessions from power sleep device");
    }

    public FailedSyncSessionsException(String message) {
        super(message);
    }
}
