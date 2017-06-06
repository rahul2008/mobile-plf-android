/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.request;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.dicommsupport.DiCommMessage;
import com.philips.pins.shinelib.dicommsupport.DiCommRequest;

import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class BlePutRequest extends BleRequest {

    @NonNull
    private final Map<String, Object> dataMap;

    /**
     * Instantiates a new BleRequest.
     *
     * @param deviceCache            the device cache
     * @param cppId                  the cppId of the BleDevice
     * @param portName               the port name
     * @param productId              the product id
     * @param dataMap                the Map of data to put
     * @param responseHandler        the response handler
     * @param disconnectAfterRequest indicates if the request should disconnect from the device after communicating
     */
    public BlePutRequest(@NonNull final BleDeviceCache deviceCache,
                         @NonNull final String cppId,
                         @NonNull final String portName,
                         final int productId,
                         @NonNull final Map<String, Object> dataMap,
                         @NonNull final ResponseHandler responseHandler,
                         @NonNull final Handler handlerToPostResponseOnto,
                         @NonNull AtomicBoolean disconnectAfterRequest) {
        super(deviceCache, cppId, portName, productId, responseHandler, handlerToPostResponseOnto, disconnectAfterRequest);
        this.dataMap = dataMap;
    }

    @Override
    protected void execute(@NonNull final CapabilityDiComm capability) {
        if (dataMap == null) {
            responseHandler.onError(Error.INVALID_PARAMETER, "Request data is null.");
            return;
        }

        if (dataMap.isEmpty()) {
            responseHandler.onError(Error.NO_REQUEST_DATA, "Request data is empty.");
            return;
        }

        final DiCommMessage putPropsMessage = new DiCommRequest().putPropsRequestDataWithProduct(productId, portName, dataMap);

        if (putPropsMessage == null) {
            responseHandler.onError(Error.INVALID_PARAMETER, "Payload contains null key.");
            return;
        }

        if (putPropsMessage.getPayload() != null && putPropsMessage.getPayload().length > MAX_PAYLOAD_LENGTH) {
            responseHandler.onError(Error.INVALID_PARAMETER, "Payload too big.");
            return;
        }

        capability.writeData(putPropsMessage.toData());
    }
}
