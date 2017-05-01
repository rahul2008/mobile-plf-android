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

    @Override
    public boolean equals(Object object) {
        if (object == null)
            return false;
        if (this == object)
            return true;
        if (!(object instanceof SHNCharacteristicInfo))
            return false;

        SHNCharacteristicInfo that = (SHNCharacteristicInfo) object;
        return this.uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }
}
