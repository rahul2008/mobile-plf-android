package com.pins.philips.shinelib;

import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 02/03/15.
 */
public interface SHNDeviceDefinitionInfo {
    interface SHNDeviceDefinition {
        Set<SHNCapability> getExposedCapabilities();
        SHNDevice createDeviceFromDeviceAddress(String deviceAddress, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo, SHNCentral shnCentral);
        void associateWithCompletion(Runnable completion);
//        Set<UUID> getRequiredServiceUUIDs();
//        Set<UUID> getRequiredCharacteristicUUIDs(UUID serviceUUID);
//        Set<UUID> getOptionalCharacteristicUUIDs(UUID serviceUUID);
    }

    String getDeviceTypeName();
    Set<UUID> getPrimaryServiceUUIDs();
    SHNDeviceAssociation createSHNAssociationProcedure(SHNCentral central, SHNAssociationProcedure.SHNAssociationProcedureListener shnAssociationProcedureListener);

    SHNDeviceDefinition getSHNDeviceDefinition(); // no other way to enforce a no arguments constructor
}
