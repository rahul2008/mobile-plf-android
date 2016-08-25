/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.support.annotation.Nullable;
import java.util.Set;
import java.util.UUID;

/**
 * SHNDevice is a representation of a peripheral inside BlueLib.
 * <p/>
 * This interface provides a means to connect and disconnect to the peripheral.
 * State updates can be received through the {@code SHNDeviceListener} interface.
 * To receive updates an instance of the listener has to be registered with the SHNDevice instance.
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
     * @return string representation of the peripheral MAC address as returned
     * by {@link android.bluetooth.BluetoothDevice#getAddress()}
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
     * <p/>
     * Please note that even in seemingly perfect conditions the Bluetooth stack may be unsuccessful
     * in establishing a connection. Bluelib attempts to resolve this as much as possible, but users
     * of this API need to handle connection failures through a relaxed re-connect mechanism.
     */
    void connect();

    /**
     * Provides a means to disconnect from the peripheral.
     * <p/>
     * Callbacks about state changes are provided via registered SHNDeviceListener instance.
     */
    void disconnect();

    /**
     * Reads the RSSI of a connected peripheral
     */
    void readRSSI();

    /**
     * Register a {@code SHNDeviceListener} instance to receive updates about the peripheral state.
     */
    void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener);

    /**
     * Unregister a {@code SHNDeviceListener}.
     */
    void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener);

    /**
     * Register a {@code DiscoveryListener} instance to receive updates about discovery of services & characteristics
     */
    void registerDiscoveryListener(DiscoveryListener discoveryListener);

    /**
     * Unregister a {@code DiscoveryListener}
     */
    void unregisterDiscoveryListener(DiscoveryListener discoveryListener);

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
         * Indicates that the peripheral state changed.
         * <p/>
         * Please note that when the user receives this callback and retrieves the state via the
         * {@link #getState()} method, the state may already have changed again and a second
         * {@link #onStateUpdated(SHNDevice)} is already queued. This can happen for example when
         * disconnecting and the device state changes rapidly from {@link State#Connected} to
         * {@link State#Disconnecting} to {@link State#Disconnected}. In this case there are two state changes
         * resulting in to calls to {@link #onStateUpdated(SHNDevice)}, but at the time the first
         * callback is processed by the listener, the device state may have already changed state to
         * {@link State#Disconnected}.
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

        /**
         * The rssi from the peripheral
         *
         * @param rssi value as read from the peripheal
         */
        void onReadRSSI(int rssi);
    }

    /**
     * Interface that provides updates on Services and Characteristics discovered on the {@code SHNDevice}.
     */
    interface DiscoveryListener {

        /**
         * @param serviceUuid of the discovered service
         * @param service associated with the UUID, might be null
         */
        void onServiceDiscovered(UUID serviceUuid, SHNService service);

        /**
         * @param characteristicUuid of the discovered characteristic
         * @param data initial value
         * @param associatedCharacteristic might be null
         */
        void onCharacteristicDiscovered(UUID characteristicUuid, byte[] data, @Nullable SHNCharacteristic associatedCharacteristic);
    }
}
