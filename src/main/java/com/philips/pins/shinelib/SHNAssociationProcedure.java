package com.philips.pins.shinelib;

/**
 * Created by 310188215 on 02/03/15.
 */
public interface SHNAssociationProcedure {

    interface SHNAssociationProcedureListener {
        void onStopScanRequest();
        void onAssociationSuccess(SHNDevice shnDevice);
        void onAssociationFailed(SHNDevice shnDevice, SHNResult error);
    }

    SHNResult start();
    boolean getShouldScan();
    void deviceDiscovered(SHNDevice shnDevice, SHNDeviceFoundInfo shnDeviceFoundInfo);
    void scannerTimeout();
}
