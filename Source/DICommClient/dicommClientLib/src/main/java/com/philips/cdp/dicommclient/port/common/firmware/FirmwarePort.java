/*
 * © Koninklijke Philips N.V., 2015, 2016, 2017.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common.firmware;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

public class FirmwarePort extends DICommPort<FirmwarePortProperties> {

    private final String FIRMWAREPORT_NAME = "firmware";
    private final int FIRMWAREPORT_PRODUCTID = 0;

    private FirmwareUpdateOperation operation;

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
    }

    public void removeFirmwarePortListener(FirmwarePortListener listener) {
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
}
