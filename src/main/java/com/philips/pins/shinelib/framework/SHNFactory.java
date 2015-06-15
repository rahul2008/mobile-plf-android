package com.philips.pins.shinelib.framework;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNDeviceImpl;
import com.philips.pins.shinelib.SHNService;

import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 08/06/15.
 */
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
