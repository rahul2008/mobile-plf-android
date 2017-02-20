/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware;

public interface FirmwareUpdate {
    class FirmwareUpdateException extends Exception {
        public FirmwareUpdateException(String reason) {
            super(reason);
        }
    }

    void start();

    void deploy() throws FirmwareUpdateException;

    void cancel() throws FirmwareUpdateException;

    void finish();

    void onError(String message);
}
