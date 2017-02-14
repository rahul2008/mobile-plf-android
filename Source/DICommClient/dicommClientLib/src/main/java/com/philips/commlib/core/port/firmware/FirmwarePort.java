/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */

package com.philips.commlib.core.port.firmware;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.commlib.core.communication.CommunicationStrategy;
import com.philips.commlib.core.port.firmware.operation.FirmwareUpdateOperation;
import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePullRemote;
import com.philips.commlib.core.port.firmware.operation.FirmwareUpdatePushLocal;
import com.philips.commlib.core.port.firmware.state.IncompatibleStateException;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class FirmwarePort extends DICommPort<FirmwarePortProperties> {

    private static final String FIRMWAREPORT_NAME = "firmware";
    private static final int FIRMWAREPORT_PRODUCTID = 0;

    private FirmwareUpdateOperation operation;
    private FirmwarePortProperties previousFirmwarePortProperties = new FirmwarePortProperties();
    private final Set<FirmwarePortListener> firmwarePortListeners = new CopyOnWriteArraySet<>();

    private Handler callbackHandler;
    private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

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
        callbackHandler = new Handler(Looper.getMainLooper());
    }

    public void pushLocalFirmware(final byte[] firmwareData) {
        if (operation == null) {
            operation = new FirmwareUpdatePushLocal(executor, this, this.mCommunicationStrategy, this.listener, firmwareData);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    operation.start();
                }
            });
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
        try {
            operation.cancel();
        } catch (IncompatibleStateException e) {
            DICommLog.e(DICommLog.FIRMWAREPORT, "Error while canceling operation: " + e.getMessage());
        }
    }

    public void checkForNewerFirmware() {
        throw new UnsupportedOperationException();
    }

    public void deployFirmware() {
        if (operation == null) {
            return;
        }
        try {
            operation.deploy();
        } catch (IncompatibleStateException e) {
            DICommLog.e(DICommLog.FIRMWAREPORT, "Error while canceling operation: " + e.getMessage());
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
                    listener.onDownloadProgress(progress);
                    break;
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
