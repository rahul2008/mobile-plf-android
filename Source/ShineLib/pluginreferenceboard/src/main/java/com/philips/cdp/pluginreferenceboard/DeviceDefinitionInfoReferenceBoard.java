package com.philips.cdp.pluginreferenceboard;

import android.bluetooth.BluetoothDevice;

import com.philips.pins.shinelib.SHNAssociationProcedurePlugin;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.associationprocedures.SHNAssociationProcedureNearestDevice;
import com.philips.pins.shinelib.framework.BleUUIDCreator;
import com.philips.pins.shinelib.utility.BleScanRecord;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DeviceDefinitionInfoReferenceBoard implements SHNDeviceDefinitionInfo {

//    public static final String PHILIPS_CUSTOM_SERVICE_UUID = "477EA600-A260-11E4-AE37-0002A5D50010";
    public static final String DEVICE_INFORMATION_SERVICE_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x180A);
    public static final String DEVICE_TYPE_NAME = "ReferenceBoard";

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
            primaryServiceUUIDs.add(UUID.fromString(DEVICE_INFORMATION_SERVICE_UUID));
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
            DeviceDefinitionReferenceBoardFactory factory = new DeviceDefinitionReferenceBoardFactory();
            shnDeviceDefinition = new DeviceDefinitionReferenceBoard(factory);
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
}
