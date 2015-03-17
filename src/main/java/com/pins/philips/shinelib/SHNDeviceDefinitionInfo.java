package com.pins.philips.shinelib;

import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 02/03/15.
 */
public interface SHNDeviceDefinitionInfo {
    public interface SHNDeviceDefinition {
        public Set<SHNCapability> getExposedCapabilities();
        public SHNDevice createDeviceFromPeripheralIdentifier(UUID peripheralIdentifier, SHNCentral shnCentral);
        public void associateWithCompletion(Runnable completion);
    }

    public String getDeviceTypeName();
    public Set<UUID> getPrimaryServiceUUIDs();
    public SHNDeviceAssociation getShnDeviceAssociation();

    public SHNDeviceDefinition createNewInstance(); // no other way to enforce a no arguments constructor
}
