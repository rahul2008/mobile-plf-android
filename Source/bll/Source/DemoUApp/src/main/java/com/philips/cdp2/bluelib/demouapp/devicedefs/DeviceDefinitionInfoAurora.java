/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.cdp2.bluelib.demouapp.devicedefs;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;

import com.philips.pins.shinelib.SHNAssociationProcedurePlugin;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.associationprocedures.SHNAssociationProcedureNearestDevice;
import com.philips.pins.shinelib.utility.BleScanRecord;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DeviceDefinitionInfoAurora implements SHNDeviceDefinitionInfo {

    public static final UUID AURORA_PRIMARY_SERVICE_UUID = UUID.fromString("477EA600-A260-11E4-AE37-0002A5D50001");
    public static final String DEVICE_TYPE_NAME = "Aurora handle";

    private SHNDeviceDefinition shnDeviceDefinition;
    private Set<UUID> primaryServiceUUIDs;

    @Override
    public String getDeviceTypeName() {
        return DEVICE_TYPE_NAME;
    }

    @Override
    public Set<UUID> getPrimaryServiceUUIDs() {
        if (primaryServiceUUIDs == null) {
            primaryServiceUUIDs = new HashSet<>();
            primaryServiceUUIDs.add(AURORA_PRIMARY_SERVICE_UUID);
        }
        return primaryServiceUUIDs;
    }

    @Override
    public SHNAssociationProcedurePlugin createSHNAssociationProcedure(SHNCentral central, SHNAssociationProcedurePlugin.SHNAssociationProcedureListener shnAssociationProcedureListener) {
        // using existing building block from BlueLib. It is also possible to use Association procedure specified in plugin.
        return new SHNAssociationProcedureNearestDevice(shnAssociationProcedureListener);
    }

    @Override
    public SHNDeviceDefinition getSHNDeviceDefinition() {
        if (shnDeviceDefinition == null) {
            shnDeviceDefinition = new DeviceDefinitionAurora();
        }
        return shnDeviceDefinition;
    }

    @Override
    public boolean useAdvertisedDataMatcher() {
        return false;
    }

    @Override
    public boolean matchesOnAdvertisedData(BluetoothDevice bluetoothDevice, BleScanRecord bleScanRecord, int rssi) {
        return false;
    }

    @Override
    public int getConnectionPriority() {
        return BluetoothGatt.CONNECTION_PRIORITY_HIGH;
    }
}
