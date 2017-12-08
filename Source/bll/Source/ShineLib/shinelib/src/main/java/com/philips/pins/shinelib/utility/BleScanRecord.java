/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import android.bluetooth.le.ScanRecord;
import android.os.ParcelUuid;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * {@code BleScanRecord} is a convenience class that converts raw advertisement data.
 * <p/>
 * It extracts a list of UUIDs, local name and remaining manufacturer specific data.
 * <p/>
 * Starting from Android 6 the Bluetooth API includes an interface to the data in the scanrecord.
 * This is a different interface that should not be confused with {@code BleScanRecord}.
 *
 * @publicPluginApi
 */
public class BleScanRecord {
    private ScanRecord scanRecord;

    public static BleScanRecord createNewInstance(ScanRecord scanRecord) {
        return new BleScanRecord(scanRecord);
    }

    private BleScanRecord(ScanRecord scanRecord) {
        this.scanRecord = scanRecord;
    }

    /**
     * Returns the content of the advertisement and scan result received from the remote peripheral.
     *
     * @return raw advertisement and scan data received from the remote peripheral
     */
    public byte[] getScanRecord() {
        return scanRecord.getBytes();
    }

    /**
     * When present, in the raw advertisement and scan data received, returns the content of the manufacturer specific data. No defined format, just an array of bytes.
     *
     * @return raw manufacterer data specified by the peripheral's manufacturer. null if there is no data
     */
    @Nullable
    public byte[] getManufacturerSpecificData(int manufacturerId) {
        if (scanRecord != null) {
            return scanRecord.getManufacturerSpecificData(manufacturerId);
        }
        return null;
    }

    /**
     * Returns the list of {@link java.util.UUID} present in the raw advertisement and scan data received from the peripheral.
     *
     * @return list of UUIDs.
     */
    @NonNull
    public List<UUID> getUuids() {
        List<UUID> uuids = new ArrayList<>();
        if (scanRecord != null && scanRecord.getServiceUuids() != null) {
            for (ParcelUuid uuid : scanRecord.getServiceUuids()) {
                uuids.add(uuid.getUuid());
            }
        }
        return uuids;
    }

    /**
     * Returns the local peripheral's name.
     *
     * @return local name of the peripheral. null if not specified
     */
    @Nullable
    public String getLocalName() {
        if (scanRecord != null) {
            return scanRecord.getDeviceName();
        } else {
            return null;
        }
    }
}
