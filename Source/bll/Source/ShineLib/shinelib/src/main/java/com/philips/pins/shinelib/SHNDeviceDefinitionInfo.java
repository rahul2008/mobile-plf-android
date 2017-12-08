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
         * @param deviceAddress the mac address of the peripheral
         * @param shnDeviceDefinitionInfo the corresponding device definition info for this peripheral
         * @param shnCentral an instance of SHNCentral used to provide support
         * @return an instance of SHNDevice implementation used by BlueLib to communicate with the peripheral
         */
        SHNDevice createDeviceFromDeviceAddress(String deviceAddress, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo, SHNCentral shnCentral);
    }

    /**
     * Specifies an unique identifier for this device definition.
     *
     * @return unique string identifier for the device definition
     */
    String getDeviceTypeName();

    /**
     * Specifies a set of UUIDs that are primary for the peripheral.
     *
     * @return a set of UUIDs that are primary for this peripheral
     */
    Set<UUID> getPrimaryServiceUUIDs();

    /**
     * Through this function {@link SHNDeviceAssociation} creates an association procedure to be used to associate with the peripheral.
     * <p/>
     * The procedure will be used by {@link com.philips.pins.shinelib.SHNDeviceAssociation}.
     *
     * @param central an instance of SHNCentral used to provide support
     * @param shnAssociationProcedureListener an internal listener for the association procedure. Use this listener to provide callbacks to BlueLib
     * @return an instance of SHNAssociationProcedurePlugin and SHNAssociationProcedure to be used to associate with the peripheral
     */
    SHNAssociationProcedurePlugin createSHNAssociationProcedure(SHNCentral central, SHNAssociationProcedurePlugin.SHNAssociationProcedureListener shnAssociationProcedureListener);

    /**
     * Through this function a {@link com.philips.pins.shinelib.SHNDeviceDefinitionInfo.SHNDeviceDefinition} for this device definition info can be retrieved.
     *
     * @return {@link com.philips.pins.shinelib.SHNDeviceDefinitionInfo.SHNDeviceDefinition} instance that specifies the corresponding {@link SHNDevice} for this device definition info
     */
    SHNDeviceDefinition getSHNDeviceDefinition();

    /**
     * Indicates to the scanner whether it should use the matchesOnAdvertisedData function or match based on the list of primary UUIDs.
     * <p/>
     * If true then {@link #matchesOnAdvertisedData(BluetoothDevice, BleScanRecord, int)} is called to determine if the scanrecord received from a peripheral identifies a peripheral serviced by the plugin.
     * If false the matching should be done using the UUID's obtained through the {@link #getPrimaryServiceUUIDs()} function.
     *
     * @return true if the {@code matchesOnAdvertisedData} function should be used, false when matching on service UUIDs should be used
     */
    boolean useAdvertisedDataMatcher();

    /**
     * Indicates to the scanner whether the received scanrecord identifies a peripheral serviced by the plugin. See also {@link #useAdvertisedDataMatcher()}.
     * <p/>
     * Do not connect to the provided {@code bluetoothDevice}.
     *
     * @param bluetoothDevice the peripheral from which the advertised data is received
     * @param bleScanRecord an instance of the BleScanRecord containing both the raw and the parsed form of the advertised data
     * @param rssi the Received Signal Strength Indicator from the bluetooth radio
     * @return true when the AdvertisementData matches what is expected for a device handled by the plugin, false otherwise
     */
    boolean matchesOnAdvertisedData(BluetoothDevice bluetoothDevice, BleScanRecord bleScanRecord, int rssi);
}