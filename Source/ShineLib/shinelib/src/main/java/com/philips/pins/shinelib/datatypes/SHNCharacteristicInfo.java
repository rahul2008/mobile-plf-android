/*
 * Copyright (c) Koninklijke Philips N.V., 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.datatypes;

import android.support.annotation.NonNull;

import java.util.Locale;
import java.util.UUID;

public class SHNCharacteristicInfo {
    private final UUID uuid;
    private final boolean encrypted;

    public SHNCharacteristicInfo(@NonNull final UUID characteristicUUID, @NonNull final boolean characteristicEncrypted) {
        this.uuid = characteristicUUID;
        this.encrypted = characteristicEncrypted;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    @Override
    public String toString() {
        return String.format(Locale.US,"SHNCharacteristicInfo: UUID: %s, encrypted: %b", uuid.toString(), encrypted);
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object == null || getClass() != object.getClass())
            return false;

        SHNCharacteristicInfo that = (SHNCharacteristicInfo) object;
        return this.uuid.equals(that.uuid);
    }

    @Override
    public int hashCode() {
        return this.uuid.hashCode();
    }
}
