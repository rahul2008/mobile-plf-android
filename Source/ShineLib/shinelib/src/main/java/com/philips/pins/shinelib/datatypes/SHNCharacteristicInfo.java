/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

import java.util.UUID;

public class SHNCharacteristicInfo {
    private final UUID uuid;
    private final boolean encrypted;

    public SHNCharacteristicInfo(UUID characteristicUUID, boolean characteristicEncrypted) {
        this.uuid = characteristicUUID;
        this.encrypted = characteristicEncrypted;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isEncrytped() {
        return encrypted;
    }

    @Override
    public String toString() {
        return "DebugMoonshine: " + getUUID();
    }
}
