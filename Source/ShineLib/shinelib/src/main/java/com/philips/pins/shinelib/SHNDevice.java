/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import java.util.Set;

/**
 * SHNDevice is a representation of a peripheral inside the BlueLib. The interface provides an opportunity to connect and disconnect to the peripheral.
 * The state updates are provided via {@code SHNDeviceListener}. To receive updates an instance of the listener has to be registered for the SHNDevice instance.
 */
public interface SHNDevice {
    /**
     * Returns the current state of the peripheral.
     *
     * @return current state of the peripheral {@code State}
     */
    State getState();

    /**
     * Returns the string representation of MAC address for the bluetooth peripheral.
     *
     * @return string representation of the peripheral MAC address {@link android.bluetooth.BluetoothDevice#getAddress()}
     */
    String getAddress();

    /**
     * Returns the name of the bluetooth peripheral.
     *
     * @return name of the peripheral {@link android.bluetooth.BluetoothDevice#getName()}
     */
    String getName();

    /**
     * Returns the device type name as specifies in the device definition info.
     *
     * @return device type name as specifies in {@link com.philips.pins.shinelib.SHNDeviceDefinitionInfo}
     */
    String getDeviceTypeName();

    /**
     * Provides an opportunity to connect to the peripheral. Callbacks about state changes are provided via registered SHNDeviceListener instance.
     */
    void connect();

    /**
     * Provides an opportunity to disconnect from the peripheral. Callbacks about states change are provided via registered SHNDeviceListener instance.
     */
    void disconnect();

    /**
     * Gives an opportunity to register SHNDeviceListener instance to receive update about the peripheral state.
     */
    void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener);

    /**
     * Gives an opportunity to unregister SHNDeviceListener instance.
     */
    void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener);

    /**
     * Specifies a set of capabilities supported by the peripheral and exposed by BlueLib.
     *
     * @return a set of peripheral's capabilities {@code SHNCapabilityType}
     */
    Set<SHNCapabilityType> getSupportedCapabilityTypes();

    /**
     * Returns a SHNCapability instance for the type. Returned instance is not type strong. The API user needs to caste it to the proper class type.
     *
     * @param type A requested capability type {@code SHNCapabilityType}
     * @return a {@link com.philips.pins.shinelib.SHNCapability} instance for the type. If type is not supported then null is returned.
     */
    SHNCapability getCapabilityForType(SHNCapabilityType type);

    /**
     * Possible states of the peripheral.
     */
    enum State {
        Disconnected, Disconnecting, Connecting, Connected
    }

    /**
     * Interface that provides updates for connection state changes of the SHNDevice.
     */
    interface SHNDeviceListener {
        /**
         * Gives updates for the peripheral state.
         *
         * @param shnDevice SHNDevice instance that has changed the state
         */
        void onStateUpdated(SHNDevice shnDevice);

        /**
         * Indicates that the initiated connection was not successful.
         *
         * @param shnDevice SHNDevice instance that has changed the state
         * @param result    Reason for the connection to fail
         */
        void onFailedToConnect(SHNDevice shnDevice, SHNResult result);
    }
}
