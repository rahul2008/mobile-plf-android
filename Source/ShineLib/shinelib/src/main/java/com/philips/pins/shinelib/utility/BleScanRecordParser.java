package com.philips.pins.shinelib.utility;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Created by 310188215 on 17/09/15.
 */
public class BleScanRecordParser {
    private List<UUID> uuids;
    private byte[] manufacturerSpecificData;

    public static BleScanRecordParser createNewInstance(byte[] scanRecord) {
        return new BleScanRecordParser(scanRecord);
    }

    private BleScanRecordParser(byte[] scanRecord) {
        uuids = new ArrayList<>();
        parseScanRecord(scanRecord);
    }

    public byte[] getManufacturerSpecificData() {
        if (manufacturerSpecificData != null)
            return manufacturerSpecificData.clone();
        return null;
    }

    public List<UUID> getUuids() {
        if (uuids != null)
            return Collections.unmodifiableList(uuids);
        return null;
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
                        int uuid16 = advertisedData[offset++];
                        uuid16 += (advertisedData[offset++] << 8);
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
