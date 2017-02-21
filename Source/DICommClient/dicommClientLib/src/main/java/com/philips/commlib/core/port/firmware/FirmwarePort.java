/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */

package com.philips.commlib.core.port.firmware;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.commlib.core.communication.CommunicationStrategy;
import com.philips.commlib.core.port.firmware.FirmwareUpdate.FirmwareUpdateException;
import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePullRemote;
import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;
import com.philips.commlib.core.util.HandlerProvider;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class FirmwarePort extends DICommPort<FirmwarePortProperties> {

    private static final String FIRMWAREPORT_NAME = "firmware";
    private static final int FIRMWAREPORT_PRODUCTID = 0;

    private FirmwareUpdate firmwareUpdate;
    private FirmwarePortProperties previousFirmwarePortProperties = new FirmwarePortProperties();
    private final Set<FirmwarePortListener> firmwarePortListeners = new CopyOnWriteArraySet<>();

    @NonNull
    private Handler callbackHandler;

    private final FirmwarePortListener listener = new FirmwarePortListener() {

        @Override
        public void onCheckingProgress(final int progress) {
            for (final FirmwarePortListener listener : firmwarePortListeners) {
                callbackHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onCheckingProgress(progress);
                    }
                });
            }
        }

        @Override
        public void onDownloadProgress(final int progress) {
            for (final FirmwarePortListener listener : firmwarePortListeners) {
                callbackHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onDownloadProgress(progress);
                    }
                });
            }
        }

        @Override
        public void onDownloadFailed(final FirmwarePortException exception) {
            for (final FirmwarePortListener listener : firmwarePortListeners) {
                callbackHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onDownloadFailed(exception);
                    }
                });
            }
        }

        @Override
        public void onDownloadFinished() {
            for (final FirmwarePortListener listener : firmwarePortListeners) {
                callbackHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onDownloadFinished();
                    }
                });
            }
        }

        @Override
        public void onFirmwareAvailable(final String version) {
            for (final FirmwarePortListener listener : firmwarePortListeners) {
                callbackHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFirmwareAvailable(version);
                    }
                });
            }
        }

        @Override
        public void onDeployFailed(final FirmwarePortException exception) {
            for (final FirmwarePortListener listener : firmwarePortListeners) {
                callbackHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onDeployFailed(exception);
                    }
                });
            }
        }

        @Override
        public void onDeployFinished() {
            for (final FirmwarePortListener listener : firmwarePortListeners) {
                callbackHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onDeployFinished();
                    }
                });
            }
        }
    };

    public FirmwarePort(CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
        callbackHandler = HandlerProvider.createHandler();
    }

    /**
     * Initiate upload of firmwareData
     *
     * @param firmwareData The data to upload
     * @throws IllegalStateException When firmware upload process is already running
     */
    public void pushLocalFirmware(final byte[] firmwareData) throws IllegalStateException {
        if (firmwareUpdate == null) {
            firmwareUpdate = new FirmwareUpdatePushLocal(this, this.mCommunicationStrategy, this.listener, firmwareData);
            firmwareUpdate.start();
        } else {
            throw new IllegalStateException("Firmware update already in progress.");
        }
    }

    public void pullRemoteFirmware(String version) {
        if (firmwareUpdate == null) {
            firmwareUpdate = new FirmwareUpdatePullRemote();
            firmwareUpdate.start();
        } else {
            throw new IllegalStateException("Firmware update already in progress.");
        }
    }

    public void cancel() {
        if (firmwareUpdate == null) {
            return;
        }
        try {
            firmwareUpdate.cancel();
        } catch (FirmwareUpdateException e) {
            DICommLog.e(DICommLog.FIRMWAREPORT, "Error while canceling firmware update: " + e.getMessage());
        }
    }

    /**
     * Future feature, don't use yet
     */
    public void checkForNewerFirmware() {
        throw new UnsupportedOperationException();
    }

    public void deployFirmware() {
        if (firmwareUpdate == null) {
            final String message = "Firmware update not in progress.";
            listener.onDeployFailed(new FirmwarePortListener.FirmwarePortException(message));
            DICommLog.e(DICommLog.FIRMWAREPORT, message);

            return;
        }
        try {
            firmwareUpdate.deploy();
        } catch (FirmwareUpdateException e) {
            final String message = "Error while canceling firmware update: " + e.getMessage();

            listener.onDeployFailed(new FirmwarePortListener.FirmwarePortException(message));
            DICommLog.e(DICommLog.FIRMWAREPORT, message);
        }
    }

    public void addFirmwarePortListener(FirmwarePortListener listener) {
        firmwarePortListeners.add(listener);
    }

    public void removeFirmwarePortListener(FirmwarePortListener listener) {
        firmwarePortListeners.remove(listener);
    }

    @Override
    public boolean isResponseForThisPort(String jsonResponse) {
        return parseResponse(jsonResponse) != null;
    }

    @Override
    public void processResponse(String jsonResponse) {
        FirmwarePortProperties firmwarePortProperties = parseResponse(jsonResponse);

        if (firmwarePortProperties == null) {
            DICommLog.d(DICommLog.FIRMWAREPORT, "FirmwarePort properties is null.");
        } else {
            setPortProperties(firmwarePortProperties);
            notifyListenersWithPortProperties(firmwarePortProperties);
        }
    }

    private FirmwarePortProperties parseResponse(String response) {
        try {
            FirmwarePortProperties firmwarePortInfo = gson.fromJson(response, FirmwarePortProperties.class);
            if (firmwarePortInfo.isValid()) {
                return firmwarePortInfo;
            }
        } catch (Exception e) {
            DICommLog.e(DICommLog.FIRMWAREPORT, e.getMessage());
        }
        return null;
    }

    @Override
    public String getDICommPortName() {
        return FIRMWAREPORT_NAME;
    }

    @Override
    public int getDICommProductId() {
        return FIRMWAREPORT_PRODUCTID;
    }

    @Override
    public boolean supportsSubscription() {
        return true;
    }

    public void finishFirmwareUpdate() {
        this.firmwareUpdate = null;
    }

    private void notifyListenersWithPortProperties(@NonNull FirmwarePortProperties firmwarePortProperties) {
        if (firmwarePortProperties.getProgress() != previousFirmwarePortProperties.getProgress() || firmwarePortProperties.getState() != previousFirmwarePortProperties.getState()) {
            int progress = getProgressPercentage(firmwarePortProperties);

            switch (firmwarePortProperties.getState()) {
                case CHECKING:
                    listener.onCheckingProgress(progress);
                    break;
                default:
                    DICommLog.d(DICommLog.FIRMWAREPORT, "There is no progress for state [" + firmwarePortProperties.getState() + "]");
            }
        }

        if (!previousFirmwarePortProperties.getUpgrade().equals(firmwarePortProperties.getUpgrade())) {
            listener.onFirmwareAvailable(firmwarePortProperties.getUpgrade());
        }
        previousFirmwarePortProperties = firmwarePortProperties;
    }

    private int getProgressPercentage(final @NonNull FirmwarePortProperties firmwarePortProperties) {
        return firmwarePortProperties.getSize() > 0 ? (int) ((float) firmwarePortProperties.getProgress() / firmwarePortProperties.getSize() * 100) : 0;
    }
}
