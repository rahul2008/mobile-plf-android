/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common.firmware;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class FirmwarePort extends DICommPort<FirmwarePortProperties> {

    private final String FIRMWAREPORT_NAME = "firmware";
    private final int FIRMWAREPORT_PRODUCTID = 0;

    private FirmwareUpdateOperation operation;
    private FirmwarePortProperties previousFirmwarePortProperties = new FirmwarePortProperties();

    private final Set<FirmwarePortListener> firmwarePortListeners = new CopyOnWriteArraySet();

    public FirmwarePort(CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
    }

    public void pushLocalFirmware(final byte[] firmwareData) {
        operation = new FirmwareUpdatePushLocal(firmwareData);
    }

    public void pullRemoteFirmware(String version) {
        operation = new FirmwareUpdatePullRemote();
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
        throw new UnsupportedOperationException();
    }

    public void addFirmwarePortListener(FirmwarePortListener listener) {
        firmwarePortListeners.add(listener);
    }

    public void removeFirmwarePortListener(FirmwarePortListener listener) {
        firmwarePortListeners.remove(listener);
    }

    @Override
    public boolean isResponseForThisPort(String jsonResponse) {
        return (parseResponse(jsonResponse) != null);
    }

    @Override
    public void processResponse(String jsonResponse) {
        FirmwarePortProperties firmwarePortInfo = parseResponse(jsonResponse);
        if (firmwarePortInfo != null) {
            setPortProperties(firmwarePortInfo);
            notifyListenersWithPortProperties(firmwarePortInfo);
            return;
        }
        DICommLog.e(DICommLog.FIRMWAREPORT, "FirmwarePort Info should never be NULL");
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

    private void notifyListenersWithPortProperties(FirmwarePortProperties firmwarePortProperties) {
        if (firmwarePortProperties.getProgress() != previousFirmwarePortProperties.getProgress() || firmwarePortProperties.getState() != previousFirmwarePortProperties.getState()) {
            switch (firmwarePortProperties.getState()) {
                case DOWNLOADING:
                    notifyProgressUpdated(FirmwarePortListener.FirmwarePortProgressType.DOWNLOADING, firmwarePortProperties.getProgress());
                    break;
                case CHECKING:
                    notifyProgressUpdated(FirmwarePortListener.FirmwarePortProgressType.CHECKING, firmwarePortProperties.getProgress());
                    break;
                case PROGRAMMING:
                    notifyProgressUpdated(FirmwarePortListener.FirmwarePortProgressType.DEPLOYING, firmwarePortProperties.getProgress());
                    break;
                default:
                    DICommLog.d(DICommLog.FIRMWAREPORT, "There is no progress for the " + firmwarePortProperties.getState() + " state.");
            }
        }

        previousFirmwarePortProperties = firmwarePortProperties;
    }

    private void notifyProgressUpdated(FirmwarePortListener.FirmwarePortProgressType type, int progress) {
        for (FirmwarePortListener listener : firmwarePortListeners) {
            listener.onProgressUpdated(type, progress);
        }
    }

    private void notifyDownloadFailed(FirmwarePortListener.FirmwarePortException exception) {
        for (FirmwarePortListener listener : firmwarePortListeners) {
            listener.onDownloadFailed(exception);
        }
    }

    private void notifyDownloadFinished() {
        for (FirmwarePortListener listener : firmwarePortListeners) {
            listener.onDownloadFinished();
        }
    }

    private void notifyFirmwareAvailable(String version) {
        for (FirmwarePortListener listener : firmwarePortListeners) {
            listener.onFirmwareAvailable(version);
        }
    }

    private void notifyDeployFailed(FirmwarePortListener.FirmwarePortException exception) {
        for (FirmwarePortListener listener : firmwarePortListeners) {
            listener.onDeployFailed(exception);
        }
    }

    private void notifyDeployFinished() {
        for (FirmwarePortListener listener : firmwarePortListeners) {
            listener.onDeployFinished();
        }
    }

}
