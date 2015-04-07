package com.pins.philips.shinelib;

/**
 * Created by 310188215 on 02/03/15.
 */
class SHNDeviceAssociationProcedure {
    public interface SHNDeviceAssociationProcedureListener {
        void onStopScanRequest();
        void onAssociationSuccess(SHNDevice shnDevice);
        void onAssociationFailed(SHNDevice shnDevice);
    }

    public void deviceDiscovered(SHNDevice shnDevice, SHNBLEAdvertisementData shnbleAdvertisementData, int rssi) { throw new UnsupportedOperationException(); }
}
