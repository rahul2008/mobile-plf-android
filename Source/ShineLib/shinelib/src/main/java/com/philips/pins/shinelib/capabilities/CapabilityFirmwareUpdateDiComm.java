/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.pins.shinelib.SHNFirmwareInfo;
import com.philips.pins.shinelib.SHNFirmwareInfoResultListener;
import com.philips.pins.shinelib.SHNMapResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.dicommsupport.DiCommPort;
import com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort.Key;
import static com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort.State;

/**
 * @publicPluginApi
 */
public class CapabilityFirmwareUpdateDiComm implements SHNCapabilityFirmwareUpdate {

    private static final String TAG = "FirmwareUpdateDiComm";
    private DiCommFirmwarePort firmwareDiCommPort;
    private DiCommFirmwarePortStateWaiter diCommFirmwarePortStateWaiter;
    private SHNFirmwareUpdateState state = SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle;

    @Nullable
    private SHNCapabilityFirmwareUpdateListener shnCapabilityFirmwareUpdateListener;
    private byte[] firmwareData;

    private DiCommPort.UpdateListener updateListener = new DiCommPort.UpdateListener() {
        @Override
        public void onPropertiesChanged(@NonNull Map<String, Object> properties) {
            updateState();
        }
    };

    private DiCommPort.Listener listener = new DiCommPort.Listener() {

        @Override
        public void onPortAvailable(DiCommPort diCommPort) {
            updateState();
        }

        @Override
        public void onPortUnavailable(DiCommPort diCommPort) {
            updateState();
        }
    };

    public CapabilityFirmwareUpdateDiComm(@NonNull DiCommFirmwarePort diCommPort, @NonNull Handler internalHandler) {
        this.firmwareDiCommPort = diCommPort;
        this.diCommFirmwarePortStateWaiter = createDiCommFirmwarePortStateWaiter(diCommPort, internalHandler);

        this.firmwareDiCommPort.setListener(listener);

        if (this.firmwareDiCommPort.isAvailable()) {
            updateState();
        }
    }

    @Override
    public boolean supportsUploadWithoutDeploy() {
        return true;
    }

    @Override
    public void uploadFirmware(final byte[] firmwareData) {
        if (state == SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle) {
            if (firmwareData == null || firmwareData.length == 0) {
                notifyUploadFailed(SHNResult.SHNErrorInvalidParameter);
            } else {
                setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateUploading);
                CapabilityFirmwareUpdateDiComm.this.firmwareData = firmwareData;

                firmwareDiCommPort.reloadProperties(new SHNMapResultListener<String, Object>() {
                    @Override
                    public void onActionCompleted(Map<String, Object> value, @NonNull SHNResult result) {
                        if (result != SHNResult.SHNOk) {
                            failWithResult(result);
                        } else {
                            if (firmwareDiCommPort.getState() != State.Idle) {
                                resetFirmwarePortToIdle(firmwareDiCommPort.getState());
                            } else {
                                prepareForUpload();
                            }
                        }
                    }
                });
            }
        } else {
            SHNLogger.d(TAG, "Unable to start firmware upload; State is: " + state);
        }
    }

    @VisibleForTesting
    protected DiCommFirmwarePortStateWaiter createDiCommFirmwarePortStateWaiter(@NonNull DiCommFirmwarePort diCommPort, @NonNull Handler internalHandler) {
        return new DiCommFirmwarePortStateWaiter(diCommPort, internalHandler);
    }

    private void resetFirmwarePortToIdle(State state) {
        if (state == State.Programming) {
            failWithResult(SHNResult.SHNErrorProcedureAlreadyInProgress);
        } else if (state == State.Error) {
            sendIdleCommand();
        } else {
            diCommFirmwarePortStateWaiter.waitUntilStateIsReached(State.Error, new DiCommFirmwarePortStateWaiter.Listener() {
                @Override
                public void onStateUpdated(State state, SHNResult shnResult) {
                    if (shnResult == SHNResult.SHNOk) {
                        sendIdleCommand();
                    } else {
                        failWithResult(shnResult);
                    }
                }
            });

            sendCancelCommand();
        }
    }

    private void sendIdleCommand() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(Key.STATE, DiCommFirmwarePort.Command.Idle.getName());
        firmwareDiCommPort.putProperties(properties, new SHNMapResultListener<String, Object>() {
            @Override
            public void onActionCompleted(Map<String, Object> value, @NonNull SHNResult result) {
                if (result != SHNResult.SHNOk) {
                    failWithResult(result);
                } else {
                    prepareForUpload();
                }
            }
        });
    }

    private void setState(SHNFirmwareUpdateState state) {
        if (this.state != state) {
            this.state = state;

            if (shnCapabilityFirmwareUpdateListener != null) {
                shnCapabilityFirmwareUpdateListener.onStateChanged(this);
            }
        }
    }

    @Override
    public void abortFirmwareUpload() {
        switch (firmwareDiCommPort.getState()) {
            case Preparing:
            case Downloading:
            case Checking:
            case Ready:
                sendCancelCommand();
                break;
        }
    }

    private void sendCancelCommand() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(Key.STATE, DiCommFirmwarePort.Command.Cancel.getName());
        firmwareDiCommPort.putProperties(properties, new SHNMapResultListener<String, Object>() {
            @Override
            public void onActionCompleted(Map<String, Object> value, @NonNull SHNResult result) {
                if (result == SHNResult.SHNOk) {
                    setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
                } else {
                    failWithResult(result);
                }
            }
        });
    }

    @Override
    public void deployFirmware() {
        if (firmwareDiCommPort.getState() == State.Ready && firmwareDiCommPort.getCanUpgrade()) {
            setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateDeploying);

            diCommFirmwarePortStateWaiter.waitUntilStateIsReached(State.Idle, new DiCommFirmwarePortStateWaiter.Listener() {
                @Override
                public void onStateUpdated(State state, SHNResult shnResult) {
                    if (shnResult == SHNResult.SHNOk) {
                        setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
                        if (shnCapabilityFirmwareUpdateListener != null) {
                            shnCapabilityFirmwareUpdateListener.onDeployFinished(CapabilityFirmwareUpdateDiComm.this, shnResult);
                        }
                    } else {
                        failWithResult(shnResult);
                    }
                }
            });

            sendDeployCommand();
        } else {
            SHNLogger.d(TAG, "Unable to start firmware deploy; State is not ready: " + firmwareDiCommPort.getState());
        }
    }

    private void sendDeployCommand() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(Key.STATE, DiCommFirmwarePort.Command.DeployGo.getName());
        firmwareDiCommPort.putProperties(properties, new SHNMapResultListener<String, Object>() {
            @Override
            public void onActionCompleted(Map<String, Object> value, @NonNull SHNResult result) {
                if (result != SHNResult.SHNOk) {
                    failWithResult(result);
                }
            }
        });
    }

    @Override
    public void getUploadedFirmwareInfo(SHNFirmwareInfoResultListener shnFirmwareInfoResultListener) {
        if (firmwareDiCommPort.isAvailable()) {
            String version = firmwareDiCommPort.getUploadedUpgradeVersion();
            SHNFirmwareInfo.SHNFirmwareState shnFirmwareState = toFirmwareState(firmwareDiCommPort.getState());

            SHNFirmwareInfo shnFirmwareInfo = new SHNFirmwareInfo(version, "n/a", "n/a", shnFirmwareState);
            shnFirmwareInfoResultListener.onActionCompleted(shnFirmwareInfo, SHNResult.SHNOk);
        } else {
            shnFirmwareInfoResultListener.onActionCompleted(null, SHNResult.SHNErrorServiceUnavailable);
        }
    }

    @Override
    public void setSHNCapabilityFirmwareUpdateListener(SHNCapabilityFirmwareUpdateListener shnCapabilityFirmwareUpdateListener) {
        this.shnCapabilityFirmwareUpdateListener = shnCapabilityFirmwareUpdateListener;
    }

    @Override
    public SHNFirmwareUpdateState getState() {
        return state;
    }

    @NonNull
    private SHNFirmwareUpdateState toShnFirmwareUpdateState(State remoteState) {
        switch (remoteState) {
            case Error:
                failWithResult(SHNResult.SHNErrorInvalidState);
            case Unknown:
            case Idle:
            case Ready:
                return SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle;
            case Preparing:
                return SHNFirmwareUpdateState.SHNFirmwareUpdateStatePreparing;
            case Canceling:
            case Downloading:
                return SHNFirmwareUpdateState.SHNFirmwareUpdateStateUploading;
            case Checking:
                return SHNFirmwareUpdateState.SHNFirmwareUpdateStateVerifying;
            case Programming:
                return SHNFirmwareUpdateState.SHNFirmwareUpdateStateDeploying;
        }

        return null;
    }

    @VisibleForTesting
    void updateState() {
        State remoteState = firmwareDiCommPort.getState();
        setState(toShnFirmwareUpdateState(remoteState));
    }

    private SHNFirmwareInfo.SHNFirmwareState toFirmwareState(State state) {
        switch (state) {
            case Idle:
            case Canceling:
            case Error:
            case Unknown:
            case Programming:
                return SHNFirmwareInfo.SHNFirmwareState.Idle;
            case Preparing:
            case Downloading:
            case Checking:
                return SHNFirmwareInfo.SHNFirmwareState.Uploading;
            case Ready:
                return SHNFirmwareInfo.SHNFirmwareState.ReadyToDeploy;
        }
        return null;
    }

    private void prepareForUpload() {
        firmwareDiCommPort.subscribe(updateListener, new SHNResultListener() {
            @Override
            public void onActionCompleted(SHNResult result) {
                if (result != SHNResult.SHNOk) {
                    failWithResult(result);
                } else {
                    sendDownloadingCommand();

                    diCommFirmwarePortStateWaiter.waitUntilStateIsReached(State.Downloading, new DiCommFirmwarePortStateWaiter.Listener() {
                        @Override
                        public void onStateUpdated(State state, SHNResult shnResult) {
                            if (shnResult == SHNResult.SHNOk) {
                                startUpload();
                            } else {
                                failWithResult(shnResult);
                            }
                        }
                    });
                }
            }
        });
    }

    private void startUpload() {
        int maxChunkSize = firmwareDiCommPort.getMaxChunkSize();
        if (maxChunkSize != Integer.MAX_VALUE) {

            diCommFirmwarePortStateWaiter.waitUntilStateIsReached(State.Ready, new DiCommFirmwarePortStateWaiter.Listener() {
                @Override
                public void onStateUpdated(State state, SHNResult shnResult) {
                    if (shnResult == SHNResult.SHNOk) {
                        setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
                        if (shnCapabilityFirmwareUpdateListener != null) {
                            updateProgress(firmwareData.length);
                            shnCapabilityFirmwareUpdateListener.onUploadFinished(CapabilityFirmwareUpdateDiComm.this);
                        }
                        firmwareDiCommPort.unsubscribe(updateListener, null);
                    } else {
                        failWithResult(shnResult);
                    }
                }
            });

            uploadNextChunk(0, maxChunkSize);
        } else {
            SHNLogger.e(TAG, "The firmware-port did not expose a valid chunk size");
            failWithResult(SHNResult.SHNErrorInvalidParameter);
        }
    }

    private void sendDownloadingCommand() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(Key.STATE, DiCommFirmwarePort.Command.Downloading.getName());
        properties.put(Key.SIZE, firmwareData.length);
        firmwareDiCommPort.putProperties(properties, new SHNMapResultListener<String, Object>() {
            @Override
            public void onActionCompleted(Map<String, Object> value, @NonNull SHNResult result) {
                if (result != SHNResult.SHNOk) {
                    failWithResult(result);
                }
            }
        });
    }

    private void uploadNextChunk(int offset, final int maxChunkSize) {
        updateProgress(offset);

        Map<String, Object> properties = new HashMap<>();
        int nextChunkSize = Math.min(maxChunkSize, firmwareData.length - offset);
        properties.put(Key.DATA, Arrays.copyOfRange(firmwareData, offset, offset + nextChunkSize));

        firmwareDiCommPort.putProperties(properties, new SHNMapResultListener<String, Object>() {
            @Override
            public void onActionCompleted(Map<String, Object> properties, @NonNull SHNResult result) {
                if (result == SHNResult.SHNOk) {
                    if (firmwareDiCommPort.getState() == State.Downloading) {
                        if (properties.containsKey(Key.PROGRESS)) {
                            Object progress = properties.get(Key.PROGRESS);
                            if (progress instanceof Double) {
                                uploadNextChunk(((Double) progress).intValue(), maxChunkSize);
                            } else if (progress instanceof Integer) {
                                uploadNextChunk((Integer) progress, maxChunkSize);
                            } else {
                                failWithResult(SHNResult.SHNErrorInvalidParameter);
                            }
                        } else {
                            failWithResult(SHNResult.SHNErrorInvalidParameter);
                        }
                    } else if (firmwareDiCommPort.getState() == State.Checking) {
                        SHNLogger.d(TAG, "Upload finished, waiting for the device to finish validating the firmware image");
                    }
                } else {
                    failWithResult(result);
                }
            }
        });
    }

    private void updateProgress(int offset) {
        float progress = (float) offset / firmwareData.length;

        if (shnCapabilityFirmwareUpdateListener != null) {
            shnCapabilityFirmwareUpdateListener.onProgressUpdate(this, progress);
        }
    }

    private void failWithResult(SHNResult shnResult) {
        if (state == SHNFirmwareUpdateState.SHNFirmwareUpdateStateUploading) {
            notifyUploadFailed(shnResult);
        } else if (state == SHNFirmwareUpdateState.SHNFirmwareUpdateStateDeploying) {
            notifyDeployFailed(shnResult);
        }
        firmwareDiCommPort.unsubscribe(updateListener, null);
        diCommFirmwarePortStateWaiter.cancel();
        setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
    }

    private void notifyUploadFailed(@NonNull SHNResult result) {
        if (shnCapabilityFirmwareUpdateListener != null) {
            shnCapabilityFirmwareUpdateListener.onUploadFailed(CapabilityFirmwareUpdateDiComm.this, result);
        }
    }

    private void notifyDeployFailed(SHNResult shnResult) {
        if (shnCapabilityFirmwareUpdateListener != null) {
            shnCapabilityFirmwareUpdateListener.onDeployFailed(CapabilityFirmwareUpdateDiComm.this, shnResult);
        }
    }
}
