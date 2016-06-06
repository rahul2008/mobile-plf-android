/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.support.annotation.Nullable;

import java.util.Set;

/**
 * SHNDevice is a representation of a peripheral inside BlueLib.
 * <p/>
 * This interface provides an means to connect and disconnect to the peripheral.
 * The state updates are provided via {@code SHNDeviceListener}.
 * To receive updates an instance of the listener has to be registered for the SHNDevice instance.
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
     * @return string representation of the peripheral MAC address as returned by {@link android.bluetooth.BluetoothDevice#getAddress()}
     */
    String getAddress();

    /**
     * Returns the name of the bluetooth peripheral.
     *
     * @return name of the peripheral as returned by {@link android.bluetooth.BluetoothDevice#getName()}
     */
    String getName();

    /**
     * Returns the device type name as specified in the device definition info.
     *
     * @return device type name as specified in {@link com.philips.pins.shinelib.SHNDeviceDefinitionInfo}
     */
    String getDeviceTypeName();

    /**
     * Provides a means to connect to the peripheral.
     * <p/>
     * Callbacks about state changes are provided via a registered SHNDeviceListener instance.
     */
    void connect();

    /**
     * Provides a means to disconnect from the peripheral.
     * <p/>
     * Callbacks about state changes are provided via registered SHNDeviceListener instance.
     */
    void disconnect();

    /**
     * Register a {@code SHNDeviceListener} instance to receive updates about the peripheral state.
     */
    void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener);

    /**
     * Unregister a {@code SHNDeviceListener}.
     */
    void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener);

    /**
     * Specifies a set of capabilities supported by the peripheral and exposed by BlueLib.
     *
     * @return a set of peripheral capabilities
     */
    Set<SHNCapabilityType> getSupportedCapabilityTypes();

    /**
     * Returns a SHNCapability instance for the type.
     *
     * The returned instance is not type strong. The API user needs to cast it to the proper class type.
     *
     * @param type A requested capability type {@code SHNCapabilityType}
     * @return a {@link com.philips.pins.shinelib.SHNCapability} instance for the type, {@code null} if not supported.
     */
    @Nullable
    SHNCapability getCapabilityForType(SHNCapabilityType type);

    /**
     * Possible states of the peripheral.
     */
    enum State {
        Disconnected, Disconnecting, Connecting, Connected
    }

    /**
     * Interface that provides updates for connection state changes of the {@code SHNDevice}.
     */
    interface SHNDeviceListener {
        /**
         * Gives updates for the peripheral state.
         *
         * @param shnDevice instance that has changed state
         */
        void onStateUpdated(SHNDevice shnDevice);

        /**
         * Indicates that the initiated connection was not successful.
         *
         * @param shnDevice instance that has changed state
         * @param result reason for the connection to fail
         */
        void onFailedToConnect(SHNDevice shnDevice, SHNResult result);
    }
}
