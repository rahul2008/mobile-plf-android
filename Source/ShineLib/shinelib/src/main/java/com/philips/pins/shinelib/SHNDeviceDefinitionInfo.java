/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothDevice;

import com.philips.pins.shinelib.utility.BleScanRecord;

import java.util.Set;
import java.util.UUID;

/**
 * This is the interface through which the ShineLib gets information from the plugin about the type of devices it can handle.
 */
public interface SHNDeviceDefinitionInfo {
    interface SHNDeviceDefinition {
        SHNDevice createDeviceFromDeviceAddress(String deviceAddress, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo, SHNCentral shnCentral);
    }

    String getDeviceTypeName();
    Set<UUID> getPrimaryServiceUUIDs();
    SHNAssociationProcedurePlugin createSHNAssociationProcedure(SHNCentral central, SHNAssociationProcedurePlugin.SHNAssociationProcedureListener shnAssociationProcedureListener);

    SHNDeviceDefinition getSHNDeviceDefinition(); // no other way to enforce a no arguments constructor

    /**
     * Indicates to the scanner whether it should use the matchesOnAdvertisedData function to determine of the advertised data is received from
     * a device that is serviced by the plugin, or that the matching should be done on the UUID's obtained through the getPrimaryServiceUUIDS function.
     *
     * @return true if the matchesOnAdvertisedData function should be used, false when matching on service UUIDs should be used.
     */
    boolean useAdvertisedDataMatcher();

    /**
     * Indicates to the scanner whether the received advertisementdata is recognised as belonging to a device that can be handled by the plugin.
     * @param bluetoothDevice   The BluetoothDevice from which the advertised data is received. Use it to query the device but don't connect or...
     * @param bleScanRecord     An instance of the BleScanRecord containing both the raw and the parsed form of the advertised data.
     * @param rssi              The Received Signal Strength Indicator from the bluetooth radio.
     * @return  true when the AdvertisementData matches what is expected for a device handled by the plugin, false otherwise.
     */
    boolean matchesOnAdvertisedData(BluetoothDevice bluetoothDevice, BleScanRecord bleScanRecord, int rssi);
}
