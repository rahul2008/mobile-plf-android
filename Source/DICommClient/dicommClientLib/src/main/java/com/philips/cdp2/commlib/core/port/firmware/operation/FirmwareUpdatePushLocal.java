/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights reserved.
 */
package com.philips.cdp2.commlib.core.port.firmware.operation;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePort;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortListener;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortListener.FirmwarePortException;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties;
import com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState;
import com.philips.cdp2.commlib.core.port.firmware.FirmwareUpdate;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateState;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateStateCanceling;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateStateChecking;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateStateDownloading;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateStateError;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateStateIdle;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateStatePreparing;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateStateProgramming;
import com.philips.cdp2.commlib.core.port.firmware.state.FirmwareUpdateStateReady;
import com.philips.cdp2.commlib.core.port.firmware.util.FirmwarePortStateWaiter;
import com.philips.cdp2.commlib.core.port.firmware.util.FirmwareUploader;
import com.philips.cdp2.commlib.core.port.firmware.util.FirmwareUploader.UploadListener;

import java.util.HashMap;
import java.util.Map;

import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey.SIZE;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortKey.STATE;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.CANCELING;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.CHECKING;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.DOWNLOADING;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.ERROR;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.IDLE;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.PREPARING;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.PROGRAMMING;
import static com.philips.cdp2.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.READY;

public class FirmwareUpdatePushLocal implements FirmwareUpdate {

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

    @NonNull
    private final StateMap stateMap;

    @NonNull
    private final FirmwarePort firmwarePort;

    @NonNull
    private final CommunicationStrategy communicationStrategy;

    @NonNull
    private final FirmwarePortListener firmwarePortListener;

    @NonNull
    private final byte[] firmwareData;

    private final int timeoutMillis;

    private FirmwarePortStateWaiter firmwarePortStateWaiter;

    private FirmwareUpdateState currentState;

    public FirmwareUpdatePushLocal(@NonNull final FirmwarePort firmwarePort,
                                   @NonNull final CommunicationStrategy communicationStrategy,
                                   @NonNull final FirmwarePortListener firmwarePortListener,
                                   @NonNull byte[] firmwareData, int timeoutMillis) {
        this.firmwarePort = firmwarePort;
        this.communicationStrategy = communicationStrategy;
        this.firmwarePortListener = firmwarePortListener;

        if (timeoutMillis <= 0) {
            throw new IllegalArgumentException("Timeout value is invalid, must be a non-zero positive integer.");
        }
        this.timeoutMillis = timeoutMillis;

        if (firmwareData.length == 0) {
            throw new IllegalArgumentException("Firmware data has zero length.");
        }
        this.firmwareData = firmwareData;
        this.stateMap = new StateMap();
        initStateMap();
        initDeviceState();
    }

    private void initDeviceState() {
        // TODO check for actual device currentState
        currentState = stateMap.get(IDLE);
    }

    private void initStateMap() {
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
    public void start() {
        currentState.start(null);
    }

    @Override
    public void deploy() throws FirmwareUpdateException {
        currentState.deploy();
    }

    @Override
    public void cancel() throws FirmwareUpdateException {
        currentState.cancel();
    }

    @Override
    public void finish() {
        this.firmwarePort.finishFirmwareUpdate();
    }

    @Override
    public void onError(final String message) {
        currentState.onError(message);
    }

    public void uploadFirmware(UploadListener firmwareUploadListener) {
        new FirmwareUploader(firmwarePort, communicationStrategy, firmwareData, firmwareUploadListener).upload();
    }

    public void onDeployFinished() {
        this.firmwarePortListener.onDeployFinished();
    }

    public void onDownloadFailed() {
        onDownloadFailed(getStatusMessage());
    }

    public void onDownloadFailed(final String reason) {
        firmwarePortListener.onDownloadFailed(new FirmwarePortException(reason));
    }

    public void onDeployFailed() {
        onDeployFailed(getStatusMessage());
    }

    public void onDeployFailed(final String reason) {
        firmwarePortListener.onDeployFailed(new FirmwarePortException(reason));
    }

    public void onDownloadFinished() {
        firmwarePortListener.onDownloadFinished();
    }

    public void onCheckingProgress(final int progressPercentage) {
        firmwarePortListener.onCheckingProgress(progressPercentage);
    }

    public void onDownloadProgress(final int progressPercentage) {
        firmwarePortListener.onDownloadProgress(progressPercentage);
    }

    public void requestState(@NonNull final FirmwarePortState requestedState) {
        this.firmwarePort.putProperties(STATE.toString(), requestedState.toString());
    }

    public void requestStateDownloading() {
        final Map<String, Object> properties = new HashMap<>();
        properties.put(STATE.toString(), DOWNLOADING.toString());
        properties.put(SIZE.toString(), firmwareData.length);
        this.firmwarePort.putProperties(properties);
    }

    public void waitForNextState() {
        FirmwarePortState currentPortState = stateMap.findByState(currentState);

        this.firmwarePortStateWaiter = createFirmwarePortStateWaiter(currentPortState);
        this.firmwarePortStateWaiter.waitForNextState();
    }

    private String getStatusMessage() {
        final FirmwarePortProperties portProperties = this.firmwarePort.getPortProperties();

        return (portProperties == null) ? "Unknown failure." : portProperties.getStatusMessage();
    }

    @VisibleForTesting
    FirmwarePortStateWaiter createFirmwarePortStateWaiter(FirmwarePortState portState) {
        return new FirmwarePortStateWaiter(this.firmwarePort, this.communicationStrategy, portState, this.timeoutMillis, new FirmwarePortStateWaiter.WaiterListener() {

            @Override
            public void onNewState(final FirmwarePortState newState) {
                FirmwareUpdateState previousState = FirmwareUpdatePushLocal.this.currentState;
                FirmwareUpdatePushLocal.this.currentState.finish();
                FirmwareUpdatePushLocal.this.currentState = stateMap.get(newState);
                FirmwareUpdatePushLocal.this.currentState.start(previousState);
            }

            @Override
            public void onError(String message) {
                currentState.onError(message);
            }
        });
    }
}
