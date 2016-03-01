/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface SHNAssociationProcedurePlugin extends SHNAssociationProcedure{
    SHNResult start();
    void stop();
    boolean getShouldScan();
    void deviceDiscovered(SHNDevice shnDevice, SHNDeviceFoundInfo shnDeviceFoundInfo);
    void scannerTimeout();
    void setShnAssociationProcedureListener(SHNAssociationProcedureListener shnAssociationProcedureListener);

    interface SHNAssociationProcedureListener {
        void onStopScanRequest();
        void onAssociationSuccess(SHNDevice shnDevice);
        void onAssociationFailed(SHNDevice shnDevice, SHNResult error);
    }
}
