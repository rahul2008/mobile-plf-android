package com.philips.cdp2.bluelib.plugindefinition;

import android.bluetooth.BluetoothDevice;

import com.philips.pins.shinelib.SHNAssociationProcedurePlugin;
import com.philips.pins.shinelib.SHNCentral;
import com.philips.pins.shinelib.SHNDeviceDefinitionInfo;
import com.philips.pins.shinelib.associationprocedures.SHNAssociationProcedureNearestDevice;
import com.philips.pins.shinelib.services.SHNServiceDiCommStreaming;
import com.philips.pins.shinelib.utility.BleScanRecord;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ReferenceNodeDeviceDefinitionInfo implements SHNDeviceDefinitionInfo {

    private static final String DEVICE_NAME = "ReferenceNode";
    private Set<UUID> primaryUUID;
    private ReferenceNodeDeviceDefinition referenceNodeDeviceDefinition;

    @Override
    public String getDeviceTypeName() {
        return DEVICE_NAME;
    }

    @Override
    public Set<UUID> getPrimaryServiceUUIDs() {
        if (primaryUUID == null) {
            primaryUUID = new HashSet<>();
            primaryUUID.add(SHNServiceDiCommStreaming.SERVICE_UUID);
        }

        return primaryUUID;
    }

    @Override
    public SHNAssociationProcedurePlugin createSHNAssociationProcedure(SHNCentral central, SHNAssociationProcedurePlugin.SHNAssociationProcedureListener shnAssociationProcedureListener) {
        return new SHNAssociationProcedureNearestDevice(shnAssociationProcedureListener);
    }

    @Override
    public SHNDeviceDefinition getSHNDeviceDefinition() {
        if (referenceNodeDeviceDefinition == null) {
            referenceNodeDeviceDefinition = new ReferenceNodeDeviceDefinition();
        }
        return referenceNodeDeviceDefinition;
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
