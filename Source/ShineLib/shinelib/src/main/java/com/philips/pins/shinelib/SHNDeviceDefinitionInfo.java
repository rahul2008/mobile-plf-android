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
 * This is the interface through which the BlueLib gets information from the plugin about the type of peripherals it can handle.
 */
public interface SHNDeviceDefinitionInfo {
    interface SHNDeviceDefinition {
        /**
         * Specifies how to instantiate a SHNDevice for a peripheral.
         *
         * @param deviceAddress           The mac address of the peripheral.
         * @param shnDeviceDefinitionInfo The corresponding device definition info for this peripheral.
         * @param shnCentral              An instance of SHNCentral used to provide support.
         * @return an instance of SHNDevice implementation used by BlueLib to communicate with the peripheral.
         */
        SHNDevice createDeviceFromDeviceAddress(String deviceAddress, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo, SHNCentral shnCentral);
    }

    /**
     * Specifies an unique identifier for this device definition.
     *
     * @return unique string identifier for the device definition.
     */
    String getDeviceTypeName();

    /**
     * Specifies a set of UUIDs that are primary for the peripheral.
     *
     * @return a set of UUIDs that are primary for this peripheral
     */
    Set<UUID> getPrimaryServiceUUIDs();

    /**
     * Specifies an association procedure to be used to associate with the peripheral. The procedure will be used by SHNDeviceAssociation.
     *
     * @param central                         An instance of SHNCentral used to provide support.
     * @param shnAssociationProcedureListener An internal listener for the association procedure. Use this listener to provide callbacks to BlueLib.
     * @return an instance of SHNAssociationProcedurePlugin and SHNAssociationProcedure to be used to associate with the peripheral.
     */
    SHNAssociationProcedurePlugin createSHNAssociationProcedure(SHNCentral central, SHNAssociationProcedurePlugin.SHNAssociationProcedureListener shnAssociationProcedureListener);

    /**
     * Specifies a device definition for this device definition info.
     *
     * @return SHNDeviceDefinition instance that specifies the corresponding SHNDevice for this device definition info.
     */
    SHNDeviceDefinition getSHNDeviceDefinition();

    /**
     * Indicates to the scanner whether it should use the matchesOnAdvertisedData function to determine of the advertised data is received from
     * a peripheral that is serviced by the plugin, or that the matching should be done on the UUID's obtained through the getPrimaryServiceUUIDS function.
     *
     * @return true if the matchesOnAdvertisedData function should be used, false when matching on service UUIDs should be used.
     */
    boolean useAdvertisedDataMatcher();

    /**
     * Indicates to the scanner whether the received advertisement data is recognised as belonging to a device that can be handled by the plugin.
     *
     * @param bluetoothDevice The BluetoothDevice from which the advertised data is received. Use it to query the device but don't connect or...
     * @param bleScanRecord   An instance of the BleScanRecord containing both the raw and the parsed form of the advertised data.
     * @param rssi            The Received Signal Strength Indicator from the bluetooth radio.
     * @return true when the AdvertisementData matches what is expected for a device handled by the plugin, false otherwise.
     */
    boolean matchesOnAdvertisedData(BluetoothDevice bluetoothDevice, BleScanRecord bleScanRecord, int rssi);
}