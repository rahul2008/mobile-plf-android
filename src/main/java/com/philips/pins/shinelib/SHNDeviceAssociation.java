package com.philips.pins.shinelib;

import android.util.Log;

import com.philips.pins.shinelib.utility.ShinePreferenceWrapper;

import java.util.List;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceAssociation {
    private static final String TAG = SHNDeviceAssociation.class.getSimpleName();
    private static final boolean LOGGING = false;
    private SHNAssociationProcedure shnAssociationProcedure;
    private final List<ShinePreferenceWrapper.AssociatedDeviceInfo> associatedDeviceInfos;

    public enum State {
        Idle, Associating
    }

    public interface SHNDeviceAssociationListener {
        void onAssociationStarted(SHNAssociationProcedure shnDeviceAssociationProcedure);

        void onAssociationStopped();

        void onAssociationSucceeded(SHNDevice shnDevice);

        void onAssociationFailed(SHNResult shnError); // The iOS version uses NSError
    }

    private SHNDeviceAssociationListener shnDeviceAssociationListener;
    private List<SHNDevice> associatedDevices;
    private final SHNCentral shnCentral;
    private String associationDeviceTypeName;
    private SHNAssociationProcedure.SHNAssociationProcedureListener shnAssociationProcedureListener = new SHNAssociationProcedure.SHNAssociationProcedureListener() {
        @Override
        public void onStopScanRequest() {
            stopScanning();
        }

        @Override
        public void onAssociationSuccess(final SHNDevice shnDevice) {
            handleStopAssociation();
            addAssociatedDevice(shnDevice);
            shnCentral.getUserHandler().post(new Runnable() {
                @Override
                public void run() {
                    if (shnDeviceAssociationListener != null) {
                        shnDeviceAssociationListener.onAssociationSucceeded(shnDevice);
                    }
                }
            });
        }

        @Override
        public void onAssociationFailed(SHNDevice shnDevice, final SHNResult result) {
            handleStopAssociation();
            reportFailure(result);
        }
    };

    private boolean scanStoppedIndicatesScanTimeout;
    private SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener = new SHNDeviceScanner.SHNDeviceScannerListener() {
        @Override
        public void deviceFound(SHNDeviceScanner shnDeviceScanner, SHNDeviceFoundInfo shnDeviceFoundInfo) {
            SHNDevice shnDevice = shnCentral.createSHNDeviceForAddress(shnDeviceFoundInfo.deviceAddress, shnDeviceFoundInfo.shnDeviceDefinitionInfo);
            if (shnAssociationProcedure != null) {
                shnAssociationProcedure.deviceDiscovered(shnDevice, shnDeviceFoundInfo);
            }
        }

        @Override
        public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
            if (shnAssociationProcedure != null && scanStoppedIndicatesScanTimeout) {
                shnAssociationProcedure.scannerTimeout();
            }
        }
    };

    public SHNDeviceAssociation(SHNCentral shnCentral) {
        this.shnCentral = shnCentral;
        associatedDeviceInfos = shnCentral.getShinePreferenceWrapper().readAssociatedDeviceInfos();
    }

    public State getState() {
        return (shnAssociationProcedure != null) ? State.Associating : State.Idle;
    }

    public void setShnDeviceAssociationListener(SHNDeviceAssociationListener shnDeviceAssociationListener) {
        this.shnDeviceAssociationListener = shnDeviceAssociationListener;
    }

    public void startAssociationForDeviceType(final String deviceTypeName) {
        shnCentral.getInternalHandler().post(new Runnable() {
            @Override
            public void run() {
                if (shnAssociationProcedure == null) {
                    SHNDeviceDefinitionInfo shnDeviceDefinitionInfo = shnCentral.getSHNDeviceDefinitions().getSHNDeviceDefinitionInfoForDeviceTypeName(deviceTypeName);
                    associationDeviceTypeName = deviceTypeName;
                    if (shnDeviceDefinitionInfo != null) {
                        shnAssociationProcedure = shnDeviceDefinitionInfo.createSHNAssociationProcedure(shnCentral, shnAssociationProcedureListener);
                        if (shnAssociationProcedure.getShouldScan()) {
                            startScanning(shnDeviceDefinitionInfo);
                        }
                        SHNResult result = shnAssociationProcedure.start();
                        if (result == SHNResult.SHNOk) {
                            shnCentral.getUserHandler().post(new Runnable() {
                                @Override
                                public void run() {
                                    shnDeviceAssociationListener.onAssociationStarted(shnAssociationProcedure);
                                }
                            });
                        } else {
                            shnAssociationProcedure = null;
                            reportFailure(result);
                        }
                    } else {
                        reportFailure(SHNResult.SHNUnknownDeviceTypeError);
                    }
                } else {
                    Log.w(TAG, "startAssociationForDeviceType: association not started: it is already running!");
                }
            }
        });
    }

    private void reportFailure(final SHNResult result) {
        shnCentral.getUserHandler().post(new Runnable() {
            @Override
            public void run() {
                if (shnDeviceAssociationListener != null) {
                    shnDeviceAssociationListener.onAssociationFailed(result);
                }
            }
        });
    }

    public void stopAssociation() {
        shnCentral.getInternalHandler().post(new Runnable() {
            @Override
            public void run() {
                if (shnAssociationProcedure != null) {
                    handleStopAssociation();
                    shnCentral.getUserHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            shnDeviceAssociationListener.onAssociationStopped();
                        }
                    });
                } else {
                    Log.w(TAG, "stopAssociation: association not stopped: it is already stopped!");
                }
            }
        });
    }

    private void addAssociatedDevice(SHNDevice shnDevice) {
        associatedDeviceInfos.add(new ShinePreferenceWrapper.AssociatedDeviceInfo(shnDevice.getAddress(), shnDevice.getDeviceTypeName()));
        shnCentral.getShinePreferenceWrapper().storeAssociatedDeviceInfos(associatedDeviceInfos);
    }

    private void handleStopAssociation() {
        stopScanning();
        shnAssociationProcedure = null;
    }

    private void stopScanning() {
        scanStoppedIndicatesScanTimeout = false;
        shnCentral.stopScanning();
    }

    private void startScanning(SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
        scanStoppedIndicatesScanTimeout = true;
        shnCentral.startScanningForDevices(shnDeviceDefinitionInfo.getPrimaryServiceUUIDs(), SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, shnDeviceScannerListener);
    }
}
