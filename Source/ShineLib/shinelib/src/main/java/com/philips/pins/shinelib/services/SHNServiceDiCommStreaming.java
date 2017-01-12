/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services;

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

    private static Set<UUID> getRequiredCharacteristics() {
        Set<UUID> requiredCharacteristicUUIDs = new HashSet<>();
        requiredCharacteristicUUIDs.add(RX_CHARACTERISTIC_UUID);
        requiredCharacteristicUUIDs.add(RX_ACK_CHARACTERISTIC_UUID);
        requiredCharacteristicUUIDs.add(TX_CHARACTERISTIC_UUID);
        requiredCharacteristicUUIDs.add(TX_ACK_CHARACTERISTIC_UUID);
        requiredCharacteristicUUIDs.add(PROT_CFG_CHARACTERISTIC_UUID);
        return requiredCharacteristicUUIDs;
    }

    private static Set<UUID> getOptionalCharacteristics() {
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