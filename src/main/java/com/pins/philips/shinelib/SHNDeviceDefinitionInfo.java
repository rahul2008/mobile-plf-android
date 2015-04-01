package com.pins.philips.shinelib;

import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 02/03/15.
 */
public interface SHNDeviceDefinitionInfo {
    public interface SHNDeviceDefinition {
        public Set<SHNCapability> getExposedCapabilities();
        public SHNDevice createDeviceFromDeviceAddress(String deviceAddress, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo, SHNCentral shnCentral);
        public void associateWithCompletion(Runnable completion);
        public Set<UUID> getRequiredServiceUUIDs();
        public Set<UUID> getRequiredCharacteristicUUIDs(UUID serviceUUID);
        public Set<UUID> getOptionalCharacteristicUUIDs(UUID serviceUUID);
    }

    public String getDeviceTypeName();
    public Set<UUID> getPrimaryServiceUUIDs();
    public SHNDeviceAssociation getShnDeviceAssociation();

    public SHNDeviceDefinition getSHNDeviceDefinition(); // no other way to enforce a no arguments constructor
}
