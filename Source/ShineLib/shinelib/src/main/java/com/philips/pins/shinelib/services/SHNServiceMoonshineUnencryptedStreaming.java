/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services;

import com.philips.pins.shinelib.datatypes.SHNCharacteristicInfo;

import java.util.HashSet;
import java.util.Set;

public class SHNServiceMoonshineUnencryptedStreaming extends SHNServiceMoonshineStreaming {

    public SHNServiceMoonshineUnencryptedStreaming() {
        super(getRequiredCharacteristics(), getOptionalCharacteristics());
    }

    private static Set<SHNCharacteristicInfo> getRequiredCharacteristics() {
        Set<SHNCharacteristicInfo> requiredCharacteristicUUIDs = new HashSet<>();
        requiredCharacteristicUUIDs.add(new SHNCharacteristicInfo(RX_CHARACTERISTIC_UUID, false));
        requiredCharacteristicUUIDs.add(new SHNCharacteristicInfo(RX_ACK_CHARACTERISTIC_UUID, false));
        requiredCharacteristicUUIDs.add(new SHNCharacteristicInfo(TX_CHARACTERISTIC_UUID, false));
        requiredCharacteristicUUIDs.add(new SHNCharacteristicInfo(TX_ACK_CHARACTERISTIC_UUID, false));
        requiredCharacteristicUUIDs.add(new SHNCharacteristicInfo(PROT_CFG_CHARACTERISTIC_UUID, false));
        return requiredCharacteristicUUIDs;
    }

    private static Set<SHNCharacteristicInfo> getOptionalCharacteristics() {
        return new HashSet<>();
    }
}
