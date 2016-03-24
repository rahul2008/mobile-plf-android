/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import java.util.Set;

/**
 * SHNDevice is a representation of a peripheral inside the BlueLib.
 */
public interface SHNDevice {
    /**
     * @return current state of the peripheral
     */
    State getState();

    /**
     * @return string representation of the peripheral address
     */
    String getAddress();

    /**
     * @return name of the peripheral
     */
    String getName();

    /**
     * @return device type name as specifies in the device definition info
     */
    String getDeviceTypeName();

    /**
     * Provides an opportunity to connect to the peripheral. Callbacks about state changes are provided via registered SHNDeviceListener instance
     */
    void connect();

    /**
     * Provides an opportunity to disconnect from the peripheral. Callbacks about states change are provided via registered SHNDeviceListener instance
     */
    void disconnect();

    /**
     * Gives an opportunity to register SHNDeviceListener instance to receive update about the peripheral state
     */
    void registerSHNDeviceListener(SHNDeviceListener shnDeviceListener);
    /**
     * Gives an opportunity to unregister SHNDeviceListener instance
     */
    void unregisterSHNDeviceListener(SHNDeviceListener shnDeviceListener);

    /**
     * Specifies a set of capabilities supported by the peripheral and exposed by BlueLib
     * @return a set of peripheral's capabilities
     */
    Set<SHNCapabilityType> getSupportedCapabilityTypes();

    /**
     * Returns a SHNCapability instance for the type. Returned instance is not type strong. The API user needs to caste it to the proper class type.
     * @param type  A requested capability type.
     * @return a SHNCapability instance for the type. If type not supported then null is returned.
     */
    SHNCapability getCapabilityForType(SHNCapabilityType type);

    /**
     * Possible state of the peripheral
     */
    enum State {
        Disconnected, Disconnecting, Connecting, Connected
    }

    /**
     * Interface that provides updates for state and connection changes
     */
    interface SHNDeviceListener {
        /**
         * Gives updates for the peripheral state
         * @param shnDevice SHNDevice instance that has changed the state
         */
        void onStateUpdated(SHNDevice shnDevice);

        /**
         * Indicates that the initiated connection was not successful.
         * @param shnDevice SHNDevice instance that has changed the state
         * @param result    Reason for connection to fail
         */
        void onFailedToConnect(SHNDevice shnDevice, SHNResult result);
    }
}
