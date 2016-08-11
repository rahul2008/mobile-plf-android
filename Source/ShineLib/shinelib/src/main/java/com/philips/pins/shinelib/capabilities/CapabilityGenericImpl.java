/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNDataRawResultListener;
import com.philips.pins.shinelib.SHNService;

public class CapabilityGenericImpl implements CapabilityGeneric {

    CapabilityGenericImpl(SHNService.SHNServiceListener listener){
        // TODO: => Maybe pass in DragonFireService
    }

    @Override
    public void readCharacteristic(SHNDataRawResultListener listener) {
        // TODO:
    }

    @Override
    public void setCapabilityGenericListener(CapabilityGenericListener genericCapabilityListener) {
        // TODO:
    }
}
