/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Contains a collection of currently registered device definitions.
 *
 * @publicApi
 */

public class SHNDeviceDefinitions {
    private static final String TAG = SHNDeviceDefinitions.class.getSimpleName();
    private List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions;

    public SHNDeviceDefinitions() {
        registeredDeviceDefinitions = new ArrayList<>();
    }

    /**
     * Add a new device definition to the collection
     *
     * @param shnDeviceDefinitionInfo Device definition to add
     * @return Definition as added successfully
     */
    public boolean add(SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
        for (SHNDeviceDefinitionInfo registeredDeviceDefinition : registeredDeviceDefinitions) {
            if (registeredDeviceDefinition.getDeviceTypeName().equals(shnDeviceDefinitionInfo.getDeviceTypeName())) {
                throw new IllegalStateException("A SHNDeviceDefinition for DeviceTypeName: " + shnDeviceDefinitionInfo.getDeviceTypeName() + " is already registered");
            }
            if (!shnDeviceDefinitionInfo.useAdvertisedDataMatcher() && registeredDeviceDefinition.getPrimaryServiceUUIDs().equals(shnDeviceDefinitionInfo.getPrimaryServiceUUIDs())) {
                throw new IllegalStateException("A SHNDeviceDefinition with the same Primary Service UUIDs is already registered");
            }
        }
        return registeredDeviceDefinitions.add(shnDeviceDefinitionInfo);
    }

    /**
     * Get the collection of currently registered device definitions.
     *
     * @return the collection of device definitions
     */
    public List<SHNDeviceDefinitionInfo> getRegisteredDeviceDefinitions() {
        return Collections.unmodifiableList(registeredDeviceDefinitions);
    }

    /**
     * Get the collection of currently registered device definitions for a specific device type
     *
     * @param deviceTypeName Name of the device type
     * @return the collection of device definitions for the device type
     */
    public SHNDeviceDefinitionInfo getSHNDeviceDefinitionInfoForDeviceTypeName(String deviceTypeName) {
        for (SHNDeviceDefinitionInfo shnDeviceDefinitionInfo : registeredDeviceDefinitions) {
            if (shnDeviceDefinitionInfo.getDeviceTypeName().equals(deviceTypeName)) {
                return shnDeviceDefinitionInfo;
            }
        }

        SHNLogger.d(TAG, "Trying to retrieve a SHNDeviceDefinitionInfo for unknown device type name: [" + deviceTypeName + "]");
        return null;
    }
}
