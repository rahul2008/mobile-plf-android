/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services;

import com.philips.pins.shinelib.datatypes.SHNCharacteristicInfo;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SHNServiceDiCommStreaming extends SHNServiceByteStreaming {
    public static final UUID SERVICE_UUID = UUID.fromString("e50ba3c0-af04-4564-92ad-fef019489de6");
    public static final UUID RX_CHARACTERISTIC_UUID = UUID.fromString("e50b0001-af04-4564-92ad-fef019489de6");
    public static final UUID RX_ACK_CHARACTERISTIC_UUID = UUID.fromString("e50b0002-af04-4564-92ad-fef019489de6");
    public static final UUID TX_CHARACTERISTIC_UUID = UUID.fromString("e50b0003-af04-4564-92ad-fef019489de6");
    public static final UUID TX_ACK_CHARACTERISTIC_UUID = UUID.fromString("e50b0004-af04-4564-92ad-fef019489de6");
    public static final UUID PROT_CFG_CHARACTERISTIC_UUID = UUID.fromString("e50b0005-af04-4564-92ad-fef019489de6");

    public SHNServiceDiCommStreaming() {
        super(SERVICE_UUID, getRequiredCharacteristics(), getOptionalCharacteristics());
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