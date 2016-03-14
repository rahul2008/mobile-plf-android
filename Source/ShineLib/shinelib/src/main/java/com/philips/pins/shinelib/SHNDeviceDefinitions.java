/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SHNDeviceDefinitions {
    private static final String TAG = SHNDeviceDefinitions.class.getSimpleName();
    private List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions;

    public SHNDeviceDefinitions() {
        registeredDeviceDefinitions = new ArrayList<>();
    }

    public boolean add(SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
        for (SHNDeviceDefinitionInfo registeredDeviceDefinition : registeredDeviceDefinitions) {
            if (registeredDeviceDefinition.getDeviceTypeName().equals(shnDeviceDefinitionInfo.getDeviceTypeName())) {
                throw new IllegalStateException("A SHNDeviceDefinition for DeviceTypeName: " + shnDeviceDefinitionInfo.getDeviceTypeName() + " is already registered");
            }
            if (shnDeviceDefinitionInfo.useAdvertisedDataMatcher() && registeredDeviceDefinition.getPrimaryServiceUUIDs().equals(shnDeviceDefinitionInfo.getPrimaryServiceUUIDs())) {
                throw new IllegalStateException("A SHNDeviceDefinition with the same Primary Service UUIDs is already registered");
            }
        }
        return registeredDeviceDefinitions.add(shnDeviceDefinitionInfo);
    }

    public List<SHNDeviceDefinitionInfo> getRegisteredDeviceDefinitions() {
        return Collections.unmodifiableList(registeredDeviceDefinitions);
    }

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
