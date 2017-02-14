/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */

package com.philips.commlib.core.port.firmware;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.commlib.core.communication.CommunicationStrategy;
import com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState;
import com.philips.commlib.core.port.firmware.operation.FirmwareUpdateOperation;
import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePullRemote;
import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.CHECKING;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.DOWNLOADING;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.IDLE;
import static com.philips.commlib.core.port.firmware.FirmwarePortProperties.FirmwarePortState.READY;

public class FirmwarePort extends DICommPort<FirmwarePortProperties> {

    private static final String FIRMWAREPORT_NAME = "firmware";
    private static final int FIRMWAREPORT_PRODUCTID = 0;

    private FirmwareUpdateOperation operation;
    private FirmwarePortProperties previousFirmwarePortProperties = new FirmwarePortProperties();
    private final Set<FirmwarePortListener> firmwarePortListeners = new CopyOnWriteArraySet<>();

    private final FirmwarePortListener listener = new FirmwarePortListener() {
        @Override
        public void onProgressUpdated(final FirmwarePortState state, final int progress) {
            for (FirmwarePortListener listener : firmwarePortListeners) {
                listener.onProgressUpdated(state, progress);
            }
        }

        @Override
        public void onDownloadFailed(final FirmwarePortException exception) {
            for (FirmwarePortListener listener : firmwarePortListeners) {
                listener.onDownloadFailed(exception);
            }
        }

        @Override
        public void onDownloadFinished() {
            for (FirmwarePortListener listener : firmwarePortListeners) {
                listener.onDownloadFinished();
            }
        }

        @Override
        public void onFirmwareAvailable(final String version) {
            for (FirmwarePortListener listener : firmwarePortListeners) {
                listener.onFirmwareAvailable(version);
            }
        }

        @Override
        public void onDeployFailed(final FirmwarePortException exception) {
            for (FirmwarePortListener listener : firmwarePortListeners) {
                listener.onDeployFailed(exception);
            }
        }

        @Override
        public void onDeployFinished() {
            for (FirmwarePortListener listener : firmwarePortListeners) {
                listener.onDeployFinished();
            }
        }
    };

    public FirmwarePort(CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
    }

    public void pushLocalFirmware(final byte[] firmwareData) {
        if (operation == null) {
            operation = new FirmwareUpdatePushLocal(new ScheduledThreadPoolExecutor(1), this, this.mCommunicationStrategy, this.listener, firmwareData);
            operation.execute();
        } else {
            throw new IllegalStateException("Operation already in progress.");
        }
    }

    public void pullRemoteFirmware(String version) {
        if (operation == null) {
            operation = new FirmwareUpdatePullRemote();
        } else {
            throw new IllegalStateException("Operation already in progress.");
        }
    }

    public void cancel() {
        if (operation == null) {
            return;
        }
        operation.cancel();
    }

    public void checkForNewerFirmware() {
        throw new UnsupportedOperationException();
    }

    public void deployFirmware() {
        if (operation == null) {
            return;
        }
        operation.deploy();
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
            DICommLog.e(DICommLog.FIRMWAREPORT, "FirmwarePort properties is null.");
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

    public void finishFirmwareUpdateOperation() {
        this.operation = null;
    }

    private void notifyListenersWithPortProperties(@NonNull FirmwarePortProperties firmwarePortProperties) {
        if (firmwarePortProperties.getProgress() != previousFirmwarePortProperties.getProgress() || firmwarePortProperties.getState() != previousFirmwarePortProperties.getState()) {
            int progress = getProgressPercentage(firmwarePortProperties);

            switch (firmwarePortProperties.getState()) {
                case DOWNLOADING:
                    listener.onProgressUpdated(DOWNLOADING, progress);
                    break;
                case CHECKING:
                    listener.onProgressUpdated(CHECKING, progress);
                    break;
                default:
                    DICommLog.d(DICommLog.FIRMWAREPORT, "There is no progress for the " + firmwarePortProperties.getState() + " state.");
            }
        }

        if ((previousFirmwarePortProperties.getState() == DOWNLOADING || previousFirmwarePortProperties.getState() == CHECKING) && (firmwarePortProperties.getState() == READY || firmwarePortProperties.getState() == IDLE)) {
            listener.onProgressUpdated(CHECKING, 100);
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
