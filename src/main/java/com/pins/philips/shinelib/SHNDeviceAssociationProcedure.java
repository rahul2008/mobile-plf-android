package com.pins.philips.shinelib;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceAssociationProcedure {
    public interface SHNDeviceAssociationProcedureListener {
        public void onStopScanRequest();
        public void onAssociationSuccess(SHNDevice shnDevice);
        public void onAssociationFailed(SHNDevice shnDevice);
    }

    public void deviceDiscovered(SHNDevice shnDevice, SHNBLEAdvertisementData shnbleAdvertisementData, int rssi) { throw new UnsupportedOperationException(); }
}
