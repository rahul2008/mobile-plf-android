/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.request;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.dicommsupport.DiCommMessage;
import com.philips.pins.shinelib.dicommsupport.DiCommRequest;

import java.util.concurrent.atomic.AtomicBoolean;

public class BleGetRequest extends BleRequest {

    /**
     * Instantiates a new BleRequest.
     *
     * @param deviceCache            the device cache
     * @param cppId                  the cppId of the BleDevice
     * @param portName               the port name
     * @param productId              the product id
     * @param responseHandler        the response handler
     * @param disconnectAfterRequest indicates if the request should disconnect from the device after communicating
     */
    public BleGetRequest(@NonNull final BleDeviceCache deviceCache,
                         @NonNull final String cppId,
                         @NonNull final String portName,
                         final int productId,
                         @NonNull final ResponseHandler responseHandler,
                         @NonNull final Handler handlerToPostResponseOnto,
                         @NonNull AtomicBoolean disconnectAfterRequest) {
        super(deviceCache, cppId, portName, productId, responseHandler, handlerToPostResponseOnto, disconnectAfterRequest);
    }

    @Override
    protected void execute(@NonNull final CapabilityDiComm capability) {
        DiCommMessage getPropsMessage = new DiCommRequest().getPropsRequestDataWithProduct(productId, portName);
        capability.writeData(getPropsMessage.toData());
    }
}
