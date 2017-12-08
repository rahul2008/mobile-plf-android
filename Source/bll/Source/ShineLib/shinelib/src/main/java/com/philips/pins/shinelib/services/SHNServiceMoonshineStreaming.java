/*
 * Copyright (c) Koninklijke Philips N.V., 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services;

import com.philips.pins.shinelib.datatypes.SHNCharacteristicInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SHNServiceMoonshineStreaming extends SHNServiceByteStreaming {
    public static final UUID SERVICE_UUID = UUID.fromString("A651FFF1-4074-4131-BCE9-56D4261BC7B1");
    public static final UUID RX_CHARACTERISTIC_UUID = UUID.fromString("A6510001-4074-4131-BCE9-56D4261BC7B1");
    public static final UUID RX_ACK_CHARACTERISTIC_UUID = UUID.fromString("A6510002-4074-4131-BCE9-56D4261BC7B1");
    public static final UUID TX_CHARACTERISTIC_UUID = UUID.fromString("A6510003-4074-4131-BCE9-56D4261BC7B1");
    public static final UUID TX_ACK_CHARACTERISTIC_UUID = UUID.fromString("A6510004-4074-4131-BCE9-56D4261BC7B1");
    public static final UUID PROT_CFG_CHARACTERISTIC_UUID = UUID.fromString("A6510005-4074-4131-BCE9-56D4261BC7B1");

    public SHNServiceMoonshineStreaming() {
        this(getRequiredCharacteristics(), getOptionalCharacteristics());
    }

    protected SHNServiceMoonshineStreaming(Set<SHNCharacteristicInfo> requiredCharacteristics, Set<SHNCharacteristicInfo> optionalCharacteristics) {
        super(SERVICE_UUID, requiredCharacteristics, optionalCharacteristics);
    }

    private static Set<SHNCharacteristicInfo> getRequiredCharacteristics() {
        Set<SHNCharacteristicInfo> requiredCharacteristicUUIDs = new HashSet<>();
        requiredCharacteristicUUIDs.add(new SHNCharacteristicInfo(RX_CHARACTERISTIC_UUID, true));
        requiredCharacteristicUUIDs.add(new SHNCharacteristicInfo(RX_ACK_CHARACTERISTIC_UUID, true));
        requiredCharacteristicUUIDs.add(new SHNCharacteristicInfo(TX_CHARACTERISTIC_UUID, true));
        requiredCharacteristicUUIDs.add(new SHNCharacteristicInfo(TX_ACK_CHARACTERISTIC_UUID, true));
        requiredCharacteristicUUIDs.add(new SHNCharacteristicInfo(PROT_CFG_CHARACTERISTIC_UUID, true));
        return requiredCharacteristicUUIDs;
    }

    private static Set<SHNCharacteristicInfo> getOptionalCharacteristics() {
        return new HashSet<>();
    }

    @Override
    protected UUID getServiceUuid() {
        return SERVICE_UUID;
    }

    @Override
    protected UUID getRxCharacteristicUuid() {
        return RX_CHARACTERISTIC_UUID;
    }

    @Override
    protected UUID getRxAckCharacteristicUuid() {
        return RX_ACK_CHARACTERISTIC_UUID;
    }

    @Override
    protected UUID getTxCharacteristicUuid() {
        return TX_CHARACTERISTIC_UUID;
    }

    @Override
    protected UUID getTxAckCharacteristicUuid() {
        return TX_ACK_CHARACTERISTIC_UUID;
    }

    @Override
    protected UUID getProtCfgCharacteristicUuid() {
        return PROT_CFG_CHARACTERISTIC_UUID;
    }
}
