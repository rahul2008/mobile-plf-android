/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 02/03/15.
 */
public interface SHNDeviceDefinitionInfo {
    interface SHNDeviceDefinition {
        SHNDevice createDeviceFromDeviceAddress(String deviceAddress, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo, SHNCentral shnCentral);
    }

    String getDeviceTypeName();
    Set<UUID> getPrimaryServiceUUIDs();
    SHNAssociationProcedurePlugin createSHNAssociationProcedure(SHNCentral central, SHNAssociationProcedurePlugin.SHNAssociationProcedureListener shnAssociationProcedureListener);

    SHNDeviceDefinition getSHNDeviceDefinition(); // no other way to enforce a no arguments constructor
}
