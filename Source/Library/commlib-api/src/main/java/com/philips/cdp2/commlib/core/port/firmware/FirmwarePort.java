/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.core.port.firmware;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdateOperation;
import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdatePullRemote;
import com.philips.cdp2.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;
import com.philips.cdp2.commlib.core.port.firmware.util.FirmwareUpdateException;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * The FirmwarePort is a {@link DICommPort} holding information about the firmware in the appliance.
 * It is also used to initiate firmware upgrades.
 * @publicApi
 */
public class FirmwarePort extends DICommPort<FirmwarePortProperties> {

    private static final String FIRMWAREPORT_NAME = "firmware";
    private static final int FIRMWAREPORT_PRODUCTID = 0;

    private FirmwareUpdateOperation firmwareUpdateOperation;
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

    /**
     * Instantiates a FirmwarePort object.
     * @param communicationStrategy CommunicationStrategy
     */
    public FirmwarePort(final @NonNull CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
        callbackHandler = HandlerProvider.createHandler();
    }

    /**
     * Push local firmware.
     *
     * @param firmwareData           The firmware data to upload
     * @param stateTransitionTimeout the timeout in milliseconds to wait for the device to transition to a next state
     * @throws IllegalStateException when firmware upload process is already running
     */
    public void pushLocalFirmware(final byte[] firmwareData, final long stateTransitionTimeout) throws IllegalStateException {
        if (firmwareUpdateOperation == null) {
            firmwareUpdateOperation = new FirmwareUpdatePushLocal(this, this.communicationStrategy, this.listener, firmwareData);
            firmwareUpdateOperation.start(stateTransitionTimeout);
        } else {
            throw new IllegalStateException("Firmware update already in progress.");
        }
    }

    /**
     * Pull remote firmware.
     *
     * @param version                the firmware version string
     * @param stateTransitionTimeout the timeout in milliseconds to wait for the device to transition to a next state
     */
    public void pullRemoteFirmware(String version, final long stateTransitionTimeout) {
        if (firmwareUpdateOperation == null) {
            firmwareUpdateOperation = new FirmwareUpdatePullRemote(stateTransitionTimeout);
            firmwareUpdateOperation.start(stateTransitionTimeout);
        } else {
            throw new IllegalStateException("Firmware update already in progress.");
        }
    }

    /**
     * Cancel firmware update.
     *
     * @param stateTransitionTimeout the state transition timeout
     */
    public void cancel(final long stateTransitionTimeout) {
        if (firmwareUpdateOperation == null) {
            return;
        }
        try {
            firmwareUpdateOperation.cancel(stateTransitionTimeout);
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

    /**
     * Deploy firmware.
     *
     * @param stateTransitionTimeout the state transition timeout
     */
    public void deployFirmware(final long stateTransitionTimeout) {
        if (firmwareUpdateOperation == null) {
            final String message = "Firmware update not in progress.";
            listener.onDeployFailed(new FirmwarePortListener.FirmwarePortException(message));
            DICommLog.e(DICommLog.FIRMWAREPORT, message);

            return;
        }
        try {
            firmwareUpdateOperation.deploy(stateTransitionTimeout);
        } catch (FirmwareUpdateException e) {
            final String message = "Error while canceling firmware update: " + e.getMessage();

            listener.onDeployFailed(new FirmwarePortListener.FirmwarePortException(message));
            DICommLog.e(DICommLog.FIRMWAREPORT, message);
        }
    }

    /**
     * Adds a listener to be notified when FirmwarePort properties are updated
     * @param listener FirmwarePortListener
     */
    public void addFirmwarePortListener(FirmwarePortListener listener) {
        firmwarePortListeners.add(listener);
    }

    /**
     * Removes a listener from being notified when FirmwarePort properties are updated
     * @param listener FirmwarePortListener
     */
    public void removeFirmwarePortListener(FirmwarePortListener listener) {
        firmwarePortListeners.remove(listener);
    }

    /**
     * Indicates the appliance's capability to perform firmware upgrade
     * @return boolean True if the appliance is capable of performing firmware upgrade, false otherwise.
     */
    public boolean canUpgrade() {
        FirmwarePortProperties properties = getPortProperties();
        if (properties == null) {
            return false;
        }
        return properties.canUpgrade();
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

    /**
     * Finished the firmware update operation.
     */
    public void finishFirmwareUpdate() {
        this.firmwareUpdateOperation = null;
    }

    private void notifyListenersWithPortProperties(@NonNull FirmwarePortProperties firmwarePortProperties) {
        if (firmwarePortProperties.getProgress() != previousFirmwarePortProperties.getProgress() || firmwarePortProperties.getState() != previousFirmwarePortProperties.getState()) {

            switch (firmwarePortProperties.getState()) {
                case CHECKING:
                    final int progress = getProgressPercentage(firmwarePortProperties);
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
