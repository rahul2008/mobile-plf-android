package com.pins.philips.shinelib;

import android.util.Log;

import java.util.List;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceAssociation {
    private static final String TAG = SHNDeviceAssociation.class.getSimpleName();
    private static final boolean LOGGING  = false;
    private SHNAssociationProcedure shnAssociationProcedure;

    public enum SHNDeviceAssociationState {
        SHNDeviceAssociationStateIdle, SHNDeviceAssociationStateAssociating
    }
    public interface SHNDeviceAssociationListener {
        void onAssociationStarted(SHNAssociationProcedure shnDeviceAssociationProcedure);
        void onAssociationStopped();
        void onAssociationSucceeded(SHNDevice shnDevice);
        void onAssociationFailed(SHNResult shnError); // The iOS version uses NSError
    }

    private SHNDeviceAssociationListener shnDeviceAssociationListener;
    private SHNDeviceAssociationState shnDeviceAssociationState = SHNDeviceAssociationState.SHNDeviceAssociationStateIdle;
    private List<SHNDevice> associatedDevices;
    private final SHNCentral shnCentral;
    private SHNAssociationProcedure.SHNAssociationProcedureListener shnAssociationProcedureListener = new SHNAssociationProcedure.SHNAssociationProcedureListener() {
        @Override
        public void onStopScanRequest() {
            shnCentral.stopScanning();
        }

        @Override
        public void onAssociationSuccess(SHNDevice shnDevice) {
            handleStopAssociation();
            addAssociatedDevice(shnDevice);
            shnDeviceAssociationListener.onAssociationSucceeded(shnDevice);
        }

        @Override
        public void onAssociationFailed(SHNDevice shnDevice) {
            handleStopAssociation();
            shnDeviceAssociationListener.onAssociationFailed(SHNResult.SHNAssociationError);
        }
    };

    private SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener = new SHNDeviceScanner.SHNDeviceScannerListener() {
        @Override
        public void deviceFound(SHNDeviceScanner shnDeviceScanner, SHNDeviceFoundInfo shnDeviceFoundInfo) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
            throw new UnsupportedOperationException();
        }
    };

    private void addAssociatedDevice(SHNDevice shnDevice) {
    }

    public SHNDeviceAssociation(SHNCentral shnCentral) {
        this.shnCentral = shnCentral;
    }

    public void setShnDeviceAssociationListener(SHNDeviceAssociationListener shnDeviceAssociationListener) {
        this.shnDeviceAssociationListener = shnDeviceAssociationListener;
    }

    public SHNDeviceAssociationState getShnDeviceAssociationState() {
        return shnDeviceAssociationState;
    }

    public void startAssociationForDeviceType(String deviceTypeName) {
        if (shnAssociationProcedure == null) {
            SHNDeviceDefinitionInfo shnDeviceDefinitionInfo = shnCentral.getSHNDeviceDefinitions().getSHNDeviceDefinitionInfoForDeviceTypeName(deviceTypeName);
            if (shnDeviceDefinitionInfo != null) {
                shnAssociationProcedure = shnDeviceDefinitionInfo.createSHNAssociationProcedure(shnCentral, shnAssociationProcedureListener);
                if (shnAssociationProcedure.getShouldScan()) {
                    shnCentral.startScanningForDevices(shnDeviceDefinitionInfo.getPrimaryServiceUUIDs(), SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, shnDeviceScannerListener);
                }
                shnDeviceAssociationListener.onAssociationStarted(shnAssociationProcedure);
            } else {
                shnDeviceAssociationListener.onAssociationFailed(SHNResult.SHNUnknownDeviceTypeError);
            }
        } else {
            Log.w(TAG, "startAssociationForDeviceType: association not started: it is already running!");
        }
    }

    public void stopAssociation() {
        if (shnAssociationProcedure != null) {
            handleStopAssociation();
            shnDeviceAssociationListener.onAssociationStopped();
        } else {
            Log.w(TAG, "stopAssociation: association not stopped: it is already stopped!");
        }
    }

    private void handleStopAssociation() {
        shnCentral.stopScanning();
        shnAssociationProcedure = null;
    }
}
