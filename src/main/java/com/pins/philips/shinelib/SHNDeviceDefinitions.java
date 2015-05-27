package com.pins.philips.shinelib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 310188215 on 27/05/15.
 */
public class SHNDeviceDefinitions {
    private List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions;

    public SHNDeviceDefinitions() {
        registeredDeviceDefinitions = new ArrayList<>();
    }

    public boolean add(SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
        for (SHNDeviceDefinitionInfo registeredDeviceDefinition: registeredDeviceDefinitions) {
            if (registeredDeviceDefinition.getDeviceTypeName().equals(shnDeviceDefinitionInfo.getDeviceTypeName())) {
                throw new IllegalStateException("SHNDeviceDefinition for DeviceTypeName: " + shnDeviceDefinitionInfo.getDeviceTypeName() + " already registered");
            }
        }
        return registeredDeviceDefinitions.add(shnDeviceDefinitionInfo);
    }

    public List<SHNDeviceDefinitionInfo> getRegisteredDeviceDefinitions() {
        return Collections.unmodifiableList(registeredDeviceDefinitions);
    }

    public SHNDeviceDefinitionInfo getSHNDeviceDefinitionInfoForDeviceTypeName(String deviceTypeName) {
        for (SHNDeviceDefinitionInfo shnDeviceDefinitionInfo: registeredDeviceDefinitions) {
            if (shnDeviceDefinitionInfo.getDeviceTypeName().equals(deviceTypeName)) {
                return shnDeviceDefinitionInfo;
            }
        }
        return null;
    }
}
