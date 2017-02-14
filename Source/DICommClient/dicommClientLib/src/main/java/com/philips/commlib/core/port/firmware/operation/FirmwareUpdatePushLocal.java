/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.commlib.core.port.firmware.operation;

import android.support.annotation.NonNull;

import com.philips.commlib.core.communication.CommunicationStrategy;
import com.philips.commlib.core.port.firmware.FirmwarePort;
import com.philips.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState;
import com.philips.commlib.core.port.firmware.state.FirmwareUpdateState;
import com.philips.commlib.core.port.firmware.state.FirmwareUpdateStateCanceling;
import com.philips.commlib.core.port.firmware.state.FirmwareUpdateStateChecking;
import com.philips.commlib.core.port.firmware.state.FirmwareUpdateStateDownloading;
import com.philips.commlib.core.port.firmware.state.FirmwareUpdateStateError;
import com.philips.commlib.core.port.firmware.state.FirmwareUpdateStateIdle;
import com.philips.commlib.core.port.firmware.state.FirmwareUpdateStatePreparing;
import com.philips.commlib.core.port.firmware.state.FirmwareUpdateStateProgramming;
import com.philips.commlib.core.port.firmware.state.FirmwareUpdateStateReady;
import com.philips.commlib.core.port.firmware.util.FirmwareUploader;
import com.philips.commlib.core.port.firmware.util.FirmwarePortStateWaiter;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;

import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey.STATE;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.CANCELING;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.CHECKING;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.DOWNLOADING;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.ERROR;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.IDLE;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.PREPARING;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.PROGRAMMING;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.READY;

public class FirmwareUpdatePushLocal implements FirmwareUpdateOperation {

    private static final class StateMap extends HashMap<FirmwarePortState, FirmwareUpdateState> {
        FirmwarePortState findByState(@NonNull FirmwareUpdateState state) {
            for (Entry<FirmwarePortState, FirmwareUpdateState> entry : this.entrySet()) {
                if (entry.getValue().equals(state)) {
                    return entry.getKey();
                }
            }
            throw new IllegalStateException("State mapping not implemented.");
        }
    }

    private static final long TIMEOUT_MILLIS = 30000L;

    @NonNull
    private final FirmwarePort firmwarePort;
    @NonNull
    private final byte[] firmwareData;
    @NonNull
    private final StateMap stateMap = new StateMap();
    @NonNull
    private final CommunicationStrategy communicationStrategy;

    private final FirmwarePortStateWaiter firmwarePortStateWaiter;

    private FirmwareUpdateState state;

    public FirmwareUpdatePushLocal(@NonNull final ExecutorService executor, @NonNull final FirmwarePort firmwarePort, @NonNull final CommunicationStrategy communicationStrategy, @NonNull byte[] firmwareData) {
        this.communicationStrategy = communicationStrategy;
        setupStates();

        this.firmwarePort = firmwarePort;

        if (firmwareData.length == 0) {
            throw new IllegalArgumentException("Firmware data has zero length.");
        }
        this.firmwareData = firmwareData;
        this.firmwarePortStateWaiter = new FirmwarePortStateWaiter(executor, this.firmwarePort);

        // TODO check device state first
        this.state = stateMap.get(IDLE);
    }

    private void setupStates() {
        stateMap.put(CANCELING, new FirmwareUpdateStateCanceling(this));
        stateMap.put(CHECKING, new FirmwareUpdateStateChecking(this));
        stateMap.put(DOWNLOADING, new FirmwareUpdateStateDownloading(this));
        stateMap.put(ERROR, new FirmwareUpdateStateError(this));
        stateMap.put(IDLE, new FirmwareUpdateStateIdle(this));
        stateMap.put(PREPARING, new FirmwareUpdateStatePreparing(this));
        stateMap.put(PROGRAMMING, new FirmwareUpdateStateProgramming(this));
        stateMap.put(READY, new FirmwareUpdateStateReady(this));
    }

    @Override
    public void execute() {
        this.state.execute(null);
    }

    @Override
    public void deploy() {
        this.state.deploy();
    }

    @Override
    public void cancel() {
        this.state.cancel();
    }

    public void pushData() throws IOException {
        new FirmwareUploader(firmwarePort, communicationStrategy, firmwareData).upload();
    }

    public void finish() {
        this.firmwarePort.finishFirmwareUpdateOperation();
    }

    public void onError(FirmwareUpdateState previousState) {
        final FirmwarePortProperties portProperties = this.firmwarePort.getPortProperties();
        if (portProperties == null) {
            return;
        }
        String statusMsg = portProperties.getStatusMessage();

        // TODO inform UI
    }

    public void onReadyToDeploy() {
        // TODO inform UI
    }

    public void requestState(@NonNull final FirmwarePortState requestedState) {
        this.firmwarePort.putProperties(STATE.toString(), requestedState.toString());
    }

    public void waitForNextState() {
        FirmwarePortState currentState = stateMap.findByState(this.state);
        final FirmwarePortState newState = firmwarePortStateWaiter.waitForNewState(currentState, TIMEOUT_MILLIS);

        if (newState == null) {
            // TODO cancel ?
            // TODO throw StateWaitException?
            cancel();
        } else {
            FirmwareUpdateState previousState = this.state;
            this.state = stateMap.get(newState);
            this.state.execute(previousState);
        }
    }
}
