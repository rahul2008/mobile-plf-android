/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ble.request;

import android.support.annotation.NonNull;

import com.philips.cdp.dicommclient.request.ResponseHandler;
import com.philips.cdp2.commlib.ble.BleDeviceCache;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.dicommsupport.DiCommMessage;
import com.philips.pins.shinelib.dicommsupport.DiCommRequest;

public class BleGetRequest extends BleRequest {

    /**
     * Instantiates a new BleRequest.
     *
     * @param deviceCache     the device cache
     * @param cppId           the cppId of the BleDevice
     * @param portName        the port name
     * @param productId       the product id
     * @param responseHandler the response handler
     */
    public BleGetRequest(@NonNull final BleDeviceCache deviceCache,
                         @NonNull final String cppId,
                         @NonNull final String portName,
                         final int productId,
                         @NonNull final ResponseHandler responseHandler) {
        super(deviceCache, cppId, portName, productId, responseHandler);
    }

    @Override
    protected void execute(CapabilityDiComm capability) {
        DiCommMessage getPropsMessage = new DiCommRequest().getPropsRequestDataWithProduct(productId, portName);
        capability.writeData(getPropsMessage.toData());
    }
}
