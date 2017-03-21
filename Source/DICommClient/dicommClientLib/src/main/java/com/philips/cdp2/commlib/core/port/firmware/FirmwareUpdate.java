/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware;

public interface FirmwareUpdate {
    class FirmwareUpdateException extends Exception {
        public FirmwareUpdateException(String reason) {
            super(reason);
        }
    }

    void start();

    void deploy(int stateTransitionTimeout) throws FirmwareUpdateException;

    void cancel(int stateTransitionTimeout) throws FirmwareUpdateException;

    void finish();

    void onError(String message);
}
