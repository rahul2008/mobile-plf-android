package com.pins.philips.shinelib;

import java.util.List;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceAssociation {
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
            throw new UnsupportedOperationException();
        }

        @Override
        public void onAssociationSuccess(SHNDevice shnDevice) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void onAssociationFailed(SHNDevice shnDevice) {
            throw new UnsupportedOperationException();
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
        SHNDeviceDefinitionInfo shnDeviceDefinitionInfo = getSHNDeviceDefinitionInfoForDeviceTypeName(deviceTypeName);
        if (shnDeviceDefinitionInfo != null) {
            shnAssociationProcedure = shnDeviceDefinitionInfo.createSHNAssociationProcedure(shnCentral, shnAssociationProcedureListener);
            if (shnAssociationProcedure.getShouldScan()) {
                shnCentral.startScanningForDevices(shnDeviceDefinitionInfo.getPrimaryServiceUUIDs(), SHNCentral.ScannerSettingDuplicates.DuplicatesAllowed, shnDeviceScannerListener);
            }
            shnDeviceAssociationListener.onAssociationStarted(shnAssociationProcedure);
        } else {
            shnDeviceAssociationListener.onAssociationFailed(SHNResult.SHNUnknownDeviceTypeError);
        }

    }

    public void stopAssociation() {
        throw new UnsupportedOperationException();
    }

    private SHNDeviceDefinitionInfo getSHNDeviceDefinitionInfoForDeviceTypeName(String deviceTypeName) {
        List<SHNDeviceDefinitionInfo> associations = shnCentral.getRegisteredDeviceDefinitions();
        for (SHNDeviceDefinitionInfo shnDeviceDefinitionInfo: associations) {
            if (shnDeviceDefinitionInfo.getDeviceTypeName().equals(deviceTypeName)) {
                return shnDeviceDefinitionInfo;
            }
        }
        return null;
    }
}
