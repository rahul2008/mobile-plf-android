package com.philips.pins.shinelib.capabilities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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

    public CapabilityFirmwareUpdateDiComm(@NonNull DiCommFirmwarePort diCommPort, @NonNull DiCommFirmwarePortStateWaiter diCommFirmwarePortStateWaiter) {
        this.firmwareDiCommPort = diCommPort;
        this.diCommFirmwarePortStateWaiter = diCommFirmwarePortStateWaiter;

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
                            if (firmwareDiCommPort.getState() != DiCommFirmwarePort.State.Idle) {
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

    private void resetFirmwarePortToIdle(DiCommFirmwarePort.State state) {
        if (state == DiCommFirmwarePort.State.Programming) {
            failWithResult(SHNResult.SHNErrorProcedureAlreadyInProgress);
        } else if (state == DiCommFirmwarePort.State.Error) {
            sendIdleCommand();
        } else {
            diCommFirmwarePortStateWaiter.waitUntilStateIsReached(DiCommFirmwarePort.State.Error, new DiCommFirmwarePortStateWaiter.Listener() {
                @Override
                public void onRequestReceived(DiCommFirmwarePort.State state, SHNResult shnResult) {
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
        properties.put(DiCommFirmwarePort.Key.STATE, DiCommFirmwarePort.Command.Idle.getName());
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
        properties.put(DiCommFirmwarePort.Key.STATE, DiCommFirmwarePort.Command.Cancel.getName());
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
    public void deployFirmware() {
        if (firmwareDiCommPort.getState() == DiCommFirmwarePort.State.Ready && firmwareDiCommPort.getCanUpgrade()) {
            setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateDeploying);

            diCommFirmwarePortStateWaiter.waitUntilStateIsReached(DiCommFirmwarePort.State.Idle, new DiCommFirmwarePortStateWaiter.Listener() {
                @Override
                public void onRequestReceived(DiCommFirmwarePort.State state, SHNResult shnResult) {
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
        properties.put(DiCommFirmwarePort.Key.STATE, DiCommFirmwarePort.Command.DeployGo.getName());
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
    private SHNFirmwareUpdateState toShnFirmwareUpdateState(DiCommFirmwarePort.State remoteState) {
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

    private void updateState() {
        DiCommFirmwarePort.State remoteState = firmwareDiCommPort.getState();
        setState(toShnFirmwareUpdateState(remoteState));
    }

    private SHNFirmwareInfo.SHNFirmwareState toFirmwareState(DiCommFirmwarePort.State state) {
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

                    diCommFirmwarePortStateWaiter.waitUntilStateIsReached(DiCommFirmwarePort.State.Downloading, new DiCommFirmwarePortStateWaiter.Listener() {
                        @Override
                        public void onRequestReceived(DiCommFirmwarePort.State state, SHNResult shnResult) {
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

            diCommFirmwarePortStateWaiter.waitUntilStateIsReached(DiCommFirmwarePort.State.Ready, new DiCommFirmwarePortStateWaiter.Listener() {
                @Override
                public void onRequestReceived(DiCommFirmwarePort.State state, SHNResult shnResult) {
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
        properties.put(DiCommFirmwarePort.Key.STATE, DiCommFirmwarePort.Command.Downloading.getName());
        properties.put(DiCommFirmwarePort.Key.SIZE, firmwareData.length);
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
        properties.put(DiCommFirmwarePort.Key.DATA, Arrays.copyOfRange(firmwareData, offset, offset + nextChunkSize));

        firmwareDiCommPort.putProperties(properties, new SHNMapResultListener<String, Object>() {
            @Override
            public void onActionCompleted(Map<String, Object> properties, @NonNull SHNResult result) {
                if (result == SHNResult.SHNOk) {
                    if (firmwareDiCommPort.getState() == DiCommFirmwarePort.State.Downloading) {
                        if (properties.containsKey(DiCommFirmwarePort.Key.PROGRESS)) {
                            int progress = (int) properties.get(DiCommFirmwarePort.Key.PROGRESS);
                            uploadNextChunk(progress, maxChunkSize);
                        } else {
                            failWithResult(SHNResult.SHNErrorInvalidParameter);
                        }
                    } else if (firmwareDiCommPort.getState() == DiCommFirmwarePort.State.Checking) {
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
