/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.framework;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNService;

import java.util.Set;
import java.util.UUID;

public class SHNFactory {
    private SHNDeviceImpl shnDeviceImpl;

    public SHNFactory(SHNDeviceImpl shnDeviceImpl) {
        this.shnDeviceImpl = shnDeviceImpl;
    }

    public SHNCharacteristic createCharacteristicForUUID(UUID uuid) {
        return new SHNCharacteristic(uuid);
    }

    public SHNService createNewSHNService(UUID serviceUUID, Set<UUID> requiredCharacteristics, Set<UUID> optionalCharacteristics) {
        return new SHNService(serviceUUID, requiredCharacteristics, optionalCharacteristics);
    }
}
