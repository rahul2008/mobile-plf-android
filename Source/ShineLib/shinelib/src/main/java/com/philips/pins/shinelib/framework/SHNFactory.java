/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.framework;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNCharacteristicInfo;

import java.util.Set;
import java.util.UUID;

public class SHNFactory {
    private SHNDeviceImpl shnDeviceImpl;

    public SHNFactory(SHNDeviceImpl shnDeviceImpl) {
        this.shnDeviceImpl = shnDeviceImpl;
    }

    public SHNCharacteristic createCharacteristicForUUID(SHNCharacteristicInfo characteristicInfo) {
        return new SHNCharacteristic(characteristicInfo);
    }

    public SHNService createNewSHNService(UUID serviceUUID, Set<SHNCharacteristicInfo> requiredCharacteristics, Set<SHNCharacteristicInfo> optionalCharacteristics) {
        return new SHNService(serviceUUID, requiredCharacteristics, optionalCharacteristics);
    }
}
