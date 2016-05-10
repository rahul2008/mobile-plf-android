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

public class CapabilityFirmwareUpdateDiComm implements SHNCapabilityFirmwareUpdate, DiCommPort.Listener, DiCommPort.UpdateListener {

    private static final String TAG = "FirmwareUpdateDiComm";
    private DiCommFirmwarePort firmwareDiCommPort;
    private DiCommFirmwarePortStateWaiter diCommFirmwarePortStateWaiter;
    private SHNFirmwareUpdateState state = SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle;

    @Nullable
    private SHNCapabilityFirmwareUpdateListener shnCapabilityFirmwareUpdateListener;
    private byte[] firmwareData;

    public CapabilityFirmwareUpdateDiComm(@NonNull DiCommFirmwarePort diCommPort, @NonNull DiCommFirmwarePortStateWaiter diCommFirmwarePortStateWaiter) {
        this.firmwareDiCommPort = diCommPort;
        this.diCommFirmwarePortStateWaiter = diCommFirmwarePortStateWaiter;

        this.firmwareDiCommPort.setListener(this);

        //TODO refresh state if available
    }

    @Override
    public boolean supportsUploadWithoutDeploy() {
        return true;
    }

    @Override
    public void uploadFirmware(final byte[] firmwareData) {
        if (firmwareData == null || firmwareData.length == 0) {
            notifyUploadFailed(SHNResult.SHNErrorInvalidParameter);
        } else {
            setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateUploading);
            firmwareDiCommPort.reloadProperties(new SHNMapResultListener<String, Object>() {
                @Override
                public void onActionCompleted(Map<String, Object> value, @NonNull SHNResult result) {
                    if (result != SHNResult.SHNOk) {
                        setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
                        notifyUploadFailed(result);
                    } else {
                        if (firmwareDiCommPort.getState() != DiCommFirmwarePort.State.Idle) {
                            setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
                            notifyUploadFailed(SHNResult.SHNErrorInvalidState);
                        } else {
                            CapabilityFirmwareUpdateDiComm.this.firmwareData = firmwareData;
                            prepareForUpload();
                        }
                    }
                }
            });
        }
    }

    private void setState(SHNFirmwareUpdateState state) {
        this.state = state;

        if (shnCapabilityFirmwareUpdateListener != null) {
            shnCapabilityFirmwareUpdateListener.onStateChanged(this);
        }
    }

    @Override
    public void abortFirmwareUpload() {

    }

    @Override
    public void deployFirmware() {

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

    // implements DiCommPort.Listener
    @Override
    public void onPortAvailable(DiCommPort diCommPort) {

    }

    @Override
    public void onPortUnavailable(DiCommPort diCommPort) {

    }

    // implements DiCommPort.UpdateListener
    @Override
    public void onPropertiesChanged(@NonNull Map<String, Object> properties) {

    }

    @Override
    public void onSubscriptionFailed(SHNResult shnResult) {

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

    private void notifyUploadFailed(@NonNull SHNResult result) {
        if (shnCapabilityFirmwareUpdateListener != null) {
            shnCapabilityFirmwareUpdateListener.onUploadFailed(CapabilityFirmwareUpdateDiComm.this, result);
        }
    }

    private void prepareForUpload() {
        firmwareDiCommPort.subscribe(this, new SHNResultListener() {
            @Override
            public void onActionCompleted(SHNResult result) {
                if (result != SHNResult.SHNOk) {
                    setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
                    notifyUploadFailed(result);
                } else {
                    sendDownloading();

                    diCommFirmwarePortStateWaiter.waitUntilStateIsReached(DiCommFirmwarePort.State.Downloading, new DiCommFirmwarePortStateWaiter.Listener() {
                        @Override
                        public void onRequestReceived(DiCommFirmwarePort.State state, SHNResult shnResult) {
                            if (shnResult == SHNResult.SHNOk) {
                                startUpload();
                            } else {
                                setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
                                notifyUploadFailed(shnResult);
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
                            shnCapabilityFirmwareUpdateListener.onUploadFinished(CapabilityFirmwareUpdateDiComm.this);
                        }
                        firmwareDiCommPort.unsubscribe(CapabilityFirmwareUpdateDiComm.this, null);
                    } else {
                        setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
                        notifyUploadFailed(shnResult);
                    }
                }
            });

            uploadNextChunk(0, maxChunkSize);
        } else {
            SHNLogger.e(TAG, "The firmware-port did not expose a valid chunk size");
            setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
            notifyUploadFailed(SHNResult.SHNErrorInvalidParameter);
        }
    }

    private void sendDownloading() {
        Map<String, Object> properties = new HashMap<>();
        properties.put(DiCommFirmwarePort.Key.STATE, DiCommFirmwarePort.State.Downloading.getName());
        properties.put(DiCommFirmwarePort.Key.SIZE, firmwareData.length);
        firmwareDiCommPort.putProperties(properties, new SHNMapResultListener<String, Object>() {
            @Override
            public void onActionCompleted(Map<String, Object> value, @NonNull SHNResult result) {
                if (result != SHNResult.SHNOk) {
                    setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
                    notifyUploadFailed(result);
                    diCommFirmwarePortStateWaiter.cancel();
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
                            setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
                            notifyUploadFailed(SHNResult.SHNErrorInvalidParameter);
                        }
                    } else if (firmwareDiCommPort.getState() == DiCommFirmwarePort.State.Checking) {
                        SHNLogger.d(TAG, "Upload finished, waiting for the device to finish validating the firmware image");
                    }
                } else {
                    setState(SHNFirmwareUpdateState.SHNFirmwareUpdateStateIdle);
                    notifyUploadFailed(result);
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
}
