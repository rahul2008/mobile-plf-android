/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.operation;

public class FirmwareUpdatePullRemote implements FirmwareUpdateOperation {

    private final long stateTransitionTimeout;

    public FirmwareUpdatePullRemote(long stateTransitionTimeout) {
        if (stateTransitionTimeout <= 0) {
            throw new IllegalArgumentException("Timeout value is invalid, must be a non-zero positive integer.");
        }
        this.stateTransitionTimeout = stateTransitionTimeout;
    }

    @Override
    public void start(long stateTransitionTimeoutMillis) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void deploy(long stateTransitionTimeoutMillis) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void cancel(long stateTransitionTimeoutMillis) {
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    @Override
    public void finish() {
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
