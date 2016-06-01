/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.utility;

import android.support.annotation.Nullable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * BleScanRecord is a convenience class that converts from raw advertisement data extracting list of UUIDs, local name and remaining manufacturer specific data.
 */
public class BleScanRecord {
    private static final String TAG = "BleScanRecord";
    private byte[] scanRecord;
    private List<UUID> uuids;
    private byte[] manufacturerSpecificData;
    private String localName;

    public static BleScanRecord createNewInstance(byte[] scanRecord) {
        return new BleScanRecord(scanRecord);
    }

    private BleScanRecord(byte[] scanRecord) {
        this.scanRecord = scanRecord.clone();
        uuids = new ArrayList<>();
        parseScanRecord(scanRecord);
    }

    /**
     * Returns the content of the advertisement record offered by the remote peripheral.
     *
     * @return raw advertisement data offered by the remote peripheral
     */
    public byte[] getScanRecord() {
        return scanRecord.clone();
    }

    /**
     * Returns the content of the manufacturer specific data. No defined format, just an array of bytes.
     *
     * @return raw advertisement specified by the peripheral's manufacturer. null if there is no data
     */
    @Nullable
    public byte[] getManufacturerSpecificData() {
        if (manufacturerSpecificData != null)
            return manufacturerSpecificData.clone();
        return null;
    }

    /**
     * Returns the list of {@link java.util.UUID} exposed by the peripheral.
     *
     * @return list of UUIDs exposed by the peripheral.  null if there is no UUIDs exposed
     */
    @Nullable
    public List<UUID> getUuids() {
        if (uuids != null)
            return Collections.unmodifiableList(uuids);
        return null;
    }

    /**
     * Returns the local peripheral's name.
     *
     * @return local name of the peripheral. null if not specified
     */
    @Nullable
    public String getLocalName() {
        return localName;
    }

    private void parseScanRecord(final byte[] advertisedData) {

        int offset = 0;
        while (offset < (advertisedData.length - 2)) {
            int len = advertisedData[offset++];
            if (len == 0)
                break;

            int type = advertisedData[offset++] & 0xFF;
            switch (type) {
                case 0x02: // Partial list of 16-bit UUIDs
                case 0x03: // Complete list of 16-bit UUIDs
                    while (len > 1) {
                        int uuid16 = (advertisedData[offset++] & 0xFF);
                        uuid16 += ((advertisedData[offset++] & 0xFF) << 8);
                        len -= 2;
                        uuids.add(UUID.fromString(String.format("%08x-0000-1000-8000-00805f9b34fb", uuid16)));
                    }
                    break;
                case 0x06:// Partial list of 128-bit UUIDs
                case 0x07:// Complete list of 128-bit UUIDs
                    // Loop through the advertised 128-bit UUID's.
                    while (len >= 16) {
                        try {
                            // Wrap the advertised bits and order them.
                            ByteBuffer buffer = ByteBuffer.wrap(advertisedData, offset++, 16).order(ByteOrder.LITTLE_ENDIAN);
                            long mostSignificantBit = buffer.getLong();
                            long leastSignificantBit = buffer.getLong();
                            uuids.add(new UUID(leastSignificantBit, mostSignificantBit));
                        } catch (IndexOutOfBoundsException e) {
                            // TODO Do we want this -> Defensive programming.
                            continue;
                        } finally {
                            // Move the offset to read the next uuid.
                            offset += 15;
                            len -= 16;
                        }
                    }
                    break;
                case 0x08:
                case 0x09:
                    byte[] buffer = new byte[len - 1];
                    ByteBuffer byteBuffer = ByteBuffer.wrap(advertisedData, offset, len - 1);
                    byteBuffer.get(buffer);
                    localName = new String(buffer, StandardCharsets.US_ASCII);
                    offset += (len - 1);
                    break;
                case 0xff:// Manufacturer specific data. No defined format, just an array of bytes.
                    manufacturerSpecificData = Arrays.copyOfRange(advertisedData, offset, offset + len - 1);
                    offset += (len - 1);
                    break;
                default:
                    offset += (len - 1);
                    break;
            }
        }
    }
}
