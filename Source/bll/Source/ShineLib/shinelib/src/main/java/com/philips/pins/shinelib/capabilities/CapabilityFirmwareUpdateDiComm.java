/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
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
import static com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort.Key.PROGRESS;
import static com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort.State;
import static com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort.getMaxChunkSize;
import static com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort.getProgressFromProps;
import static com.philips.pins.shinelib.dicommsupport.ports.DiCommFirmwarePort.getStateFromProps;

/**
 * @publicPluginApi
 */
public class CapabilityFirmwareUpdateDiComm implements SHNCapabilityFirmwareUpdate {

    private static final String TAG = "FirmwareUpdateDiComm";
    private DiCommFirmwarePort firmwareDiCommPort;
    private DiCommFirmwarePortStateWaiter diCommFirmwarePortStateWaiter;
    protected SHNFirmwareUpdateState state = SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle;

    @Nullable
    private SHNCapabilityFirmwareUpdateListener shnCapabilityFirmwareUpdateListener;
    private byte[] firmwareData;

    private DiCommPort.UpdateListener propertiesListener = properties -> updateState();

    private DiCommPort.Listener portAvailabilityListener = new DiCommPort.Listener() {

        @Override
        public void onPortAvailable(DiCommPort diCommPort) {

        }

        @Override
        public void onPortUnavailable(DiCommPort diCommPort) {
            if (state != SHNFirmwareUpdateState.SHNFirmwareUpdateStateDeploying) {
                setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
            }
        }
    };

    public CapabilityFirmwareUpdateDiComm(@NonNull DiCommFirmwarePort diCommPort, @NonNull Handler internalHandler) {
        this.firmwareDiCommPort = diCommPort;
        this.diCommFirmwarePortStateWaiter = createDiCommFirmwarePortStateWaiter(diCommPort, internalHandler);

        this.firmwareDiCommPort.setListener(portAvailabilityListener);
    }

    @Override
    public boolean supportsUploadWithoutDeploy() {
        return true;
    }

    @Override
    public void uploadFirmware(final byte[] firmwareData, final boolean shouldResume) {
        if (state == SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle) {
            if (firmwareData == null || firmwareData.length == 0) {
                notifyUploadFailed(SHNResult.SHNErrorInvalidParameter);
            } else {
                setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateUploading);
                CapabilityFirmwareUpdateDiComm.this.firmwareData = firmwareData;

                firmwareDiCommPort.reloadProperties((properties, result) -> {
                    if (result == SHNResult.SHNOk) {
                        if (getStateFromProps(properties) == State.Idle) {
                            // Pristine start
                            prepareForUpload();
                        } else if (getStateFromProps(properties) == State.Downloading && shouldResume) {
                            // Resume
                            startUploadAt(getProgressFromProps(properties));
                        } else {
                            // Remote state invalid
                            resetFirmwarePortToIdle(getStateFromProps(properties));
                        }
                    } else {
                        notifyUploadFailed(result);
                        cleanupAfterFailure();
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
            cleanupAfterFailure();
        } else if (state == State.Error) {
            sendIdleCommand();
        } else {
            diCommFirmwarePortStateWaiter.waitUntilStateIsReached(State.Error, new DiCommFirmwarePortStateWaiter.Listener() {
                @Override
                public void onStateUpdated(State state, SHNResult shnResult) {
                    if (shnResult == SHNResult.SHNOk) {
                        sendIdleCommand();
                    } else {
                        if (CapabilityFirmwareUpdateDiComm.this.state == SHNFirmwareUpdateState.SHNFirmwareUpdateStateUploading) {
                            notifyUploadFailed(shnResult);
                        }
                        cleanupAfterFailure();
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
                    if (state == SHNFirmwareUpdateState.SHNFirmwareUpdateStateUploading) {
                        notifyUploadFailed(result);
                    }
                    cleanupAfterFailure();
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
                    if (state == SHNFirmwareUpdateState.SHNFirmwareUpdateStateUploading) {
                        notifyUploadFailed(result);
                    }
                    cleanupAfterFailure();
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
                        notifyDeployFailed(shnResult);
                        cleanupAfterFailure();
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
                    notifyDeployFailed(result);
                    cleanupAfterFailure();
                }
            }
        });
    }

    @Override
    public void getUploadedFirmwareInfo(SHNFirmwareInfoResultListener shnFirmwareInfoResultListener) {
        if (firmwareDiCommPort.isAvailable()) {
            String version = firmwareDiCommPort.getUploadedUpgradeVersion();
            SHNFirmwareInfo.SHNFirmwareState shnFirmwareState = toFirmwareState(firmwareDiCommPort.getState());

            SHNFirmwareInfo shnFirmwareInfo = new SHNFirmwareInfo(version, shnFirmwareState);
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
        State cachedRemoteState = firmwareDiCommPort.getState();

        if (cachedRemoteState == State.Error) {
            if (state == SHNFirmwareUpdateState.SHNFirmwareUpdateStateUploading) {
                notifyUploadFailed(SHNResult.SHNErrorInvalidState);
            } else if (state == SHNFirmwareUpdateState.SHNFirmwareUpdateStateDeploying) {
                notifyDeployFailed(SHNResult.SHNErrorInvalidState);
            }
            cleanupAfterFailure();
        }
        setState(toShnFirmwareUpdateState(cachedRemoteState));
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
        firmwareDiCommPort.subscribe(propertiesListener, new SHNResultListener() {
            @Override
            public void onActionCompleted(SHNResult result) {
                if (result == SHNResult.SHNOk) {
                    diCommFirmwarePortStateWaiter.waitUntilStateIsReached(State.Downloading, new DiCommFirmwarePortStateWaiter.Listener() {
                        @Override
                        public void onStateUpdated(State state, SHNResult shnResult) {
                            if (shnResult == SHNResult.SHNOk) {
                                startUpload();
                            } else {
                                notifyUploadFailed(shnResult);
                                cleanupAfterFailure();
                            }
                        }
                    });

                    sendDownloadingCommand();
                } else {
                    notifyUploadFailed(result);
                    cleanupAfterFailure();
                }
            }
        });
    }

    private void startUpload() {
        startUploadAt(0);
    }

    private void startUploadAt(int offset) {
        int maxChunkSize = getMaxChunkSize(firmwareDiCommPort.getProperties());

        if (maxChunkSize == Integer.MAX_VALUE) {
            SHNLogger.e(TAG, "The firmware-port did not expose a valid chunk size");
            notifyUploadFailed(SHNResult.SHNErrorInvalidParameter);
            cleanupAfterFailure();
            return;
        }

        diCommFirmwarePortStateWaiter.waitUntilStateIsReached(State.Ready, new DiCommFirmwarePortStateWaiter.Listener() {
            @Override
            public void onStateUpdated(State state, SHNResult shnResult) {
                if (shnResult == SHNResult.SHNOk) {
                    setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
                    if (shnCapabilityFirmwareUpdateListener != null) {
                        updateProgress(firmwareData.length);
                        shnCapabilityFirmwareUpdateListener.onUploadFinished(CapabilityFirmwareUpdateDiComm.this);
                    }
                    firmwareDiCommPort.unsubscribe(propertiesListener, null);
                } else {
                    notifyUploadFailed(shnResult);
                    cleanupAfterFailure();
                }
            }
        });

        uploadNextChunk(offset, maxChunkSize);
    }

    private void sendDownloadingCommand() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(Key.STATE, DiCommFirmwarePort.Command.Downloading.getName());
        properties.put(Key.SIZE, firmwareData.length);

        firmwareDiCommPort.putProperties(properties, new SHNMapResultListener<String, Object>() {
            @Override
            public void onActionCompleted(Map<String, Object> value, @NonNull SHNResult result) {
                if (result != SHNResult.SHNOk) {
                    notifyUploadFailed(result);
                    cleanupAfterFailure();
                }
            }
        });
    }

    private void uploadNextChunk(int offset, final int maxChunkSize) {
        if (offset >= firmwareData.length || offset < 0) {
            notifyUploadFailed(SHNResult.SHNErrorInvalidState);
            cleanupAfterFailure();
            return;
        }

        updateProgress(offset);
        Map<String, Object> properties = new HashMap<>();
        int nextChunkSize = Math.min(maxChunkSize, firmwareData.length - offset);
        properties.put(Key.DATA, Arrays.copyOfRange(firmwareData, offset, offset + nextChunkSize));

        firmwareDiCommPort.putProperties(properties, new SHNMapResultListener<String, Object>() {
            @Override
            public void onActionCompleted(Map<String, Object> properties, @NonNull SHNResult result) {
                if (result == SHNResult.SHNOk) {
                    if (firmwareDiCommPort.getState() == State.Downloading) {
                        if (properties.containsKey(PROGRESS)) {
                            Object progress = properties.get(PROGRESS);

                            if (progress instanceof Double) {
                                uploadNextChunk(((Double) progress).intValue(), maxChunkSize);
                            } else if (progress instanceof Integer) {
                                uploadNextChunk((Integer) progress, maxChunkSize);
                            } else {
                                notifyUploadFailed(SHNResult.SHNErrorInvalidParameter);
                                cleanupAfterFailure();
                            }
                        } else {
                            notifyUploadFailed(SHNResult.SHNErrorInvalidParameter);
                            cleanupAfterFailure();
                        }
                    } else if (firmwareDiCommPort.getState() == State.Checking) {
                        SHNLogger.d(TAG, "Upload finished, waiting for the device to finish validating the firmware image");
                    }
                } else {
                    notifyUploadFailed(result);
                    cleanupAfterFailure();
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

    private void cleanupAfterFailure() {
        firmwareDiCommPort.unsubscribe(propertiesListener, null);
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
