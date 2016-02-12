/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.utility.PersistentStorageFactory;
import com.philips.pins.shinelib.utility.QuickTestConnection;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.utility.SHNPersistentStorage;
import com.philips.pins.shinelib.utility.SHNServiceRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceAssociation {

    private static final String TAG = SHNDeviceAssociation.class.getSimpleName();
    private final SHNDeviceScannerInternal shnDeviceScannerInternal;
    private List<SHNDevice> associatedDevices;
    private SHNAssociationProcedurePlugin shnAssociationProcedure;
    private String associatingWithDeviceTypeName;

    @NonNull
    private final PersistentStorageFactory persistentStorageFactory;

    public enum State {
        Idle, Associating
    }

    public interface SHNDeviceAssociationListener {
        void onAssociationStarted(SHNAssociationProcedure shnDeviceAssociationProcedure);

        void onAssociationStopped();

        void onAssociationSucceeded(SHNDevice shnDevice);

        void onAssociationFailed(SHNResult shnError);

        void onAssociatedDevicesUpdated();
    }

    public interface DeviceRemovedListener {
        void onAssociatedDeviceRemoved(@NonNull final SHNDevice device);
    }

    private List<DeviceRemovedListener> deviceRemovedListeners = new ArrayList<>();

    private SHNDeviceAssociationListener shnDeviceAssociationListener;
    private final SHNCentral shnCentral;

    private boolean useQuickTest = false;
    private SHNAssociationProcedurePlugin.SHNAssociationProcedureListener shnAssociationProcedureListener = new SHNAssociationProcedurePlugin.SHNAssociationProcedureListener() {
        @Override
        public void onStopScanRequest() {
            stopScanning();
        }

        @Override
        public void onAssociationSuccess(final SHNDevice shnDevice) {
            handleStopAssociation();
            addAssociatedDevice(shnDevice);
            if (useQuickTest) {
                executeQuickTest(shnDevice);
            } else {
                informAssociationSuccess(shnDevice);
            }
        }

        private void executeQuickTest(final SHNDevice shnDevice) {
            QuickTestConnection quickTestConnection = createQuickTestConnection();
            quickTestConnection.execute(shnDevice, new QuickTestConnection.Listener() {
                @Override
                public void onSuccess() {
                    informAssociationSuccess(shnDevice);
                }

                @Override
                public void onFailure() {
                    informAssociationSuccess(shnDevice);
                }
            });
        }

        private void informAssociationSuccess(final SHNDevice shnDevice) {
            Handler userHandler = shnCentral.getUserHandler();
            userHandler.post(new Runnable() {
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
            if (shnDeviceFoundInfo.getShnDeviceDefinitionInfo().getDeviceTypeName().equals(associatingWithDeviceTypeName)) {
                SHNDevice shnDevice = shnCentral.createSHNDeviceForAddressAndDefinition(shnDeviceFoundInfo.getDeviceAddress(), shnDeviceFoundInfo.getShnDeviceDefinitionInfo());
                if (shnAssociationProcedure != null) {
                    shnAssociationProcedure.deviceDiscovered(shnDevice, shnDeviceFoundInfo);
                }
            }
        }

        @Override
        public void scanStopped(SHNDeviceScanner shnDeviceScanner) {
            if (shnAssociationProcedure != null && scanStoppedIndicatesScanTimeout) {
                shnAssociationProcedure.scannerTimeout();
            }
        }
    };

    public SHNDeviceAssociation(SHNCentral shnCentral, SHNDeviceScannerInternal shnDeviceScannerInternal, final @NonNull PersistentStorageFactory persistentStorageFactory) {
        this.shnCentral = shnCentral;
        this.shnDeviceScannerInternal = shnDeviceScannerInternal;
        this.persistentStorageFactory = persistentStorageFactory;
    }

    void initAssociatedDevicesList() {
        List<SHNPersistentStorage.AssociatedDeviceInfo> associatedDeviceInfos =
                SHNServiceRegistry.getInstance().get(SHNPersistentStorage.class).readAssociatedDeviceInfos();
        associatedDevices = new ArrayList<>();
        for (SHNPersistentStorage.AssociatedDeviceInfo associatedDeviceInfo : associatedDeviceInfos) {
            SHNDeviceDefinitionInfo shnDeviceDefinitionInfo = shnCentral.getSHNDeviceDefinitions().getSHNDeviceDefinitionInfoForDeviceTypeName(associatedDeviceInfo.deviceTypeName);

            SHNDevice shnDevice = null;
            if (shnDeviceDefinitionInfo != null) {
                shnDevice = shnCentral.createSHNDeviceForAddressAndDefinition(associatedDeviceInfo.macAddress,
                        shnDeviceDefinitionInfo);
            }

            if (shnDevice == null) {
                SHNLogger.d(TAG, "Could not create device for mac-address: [" + associatedDeviceInfo.macAddress + "] and type: [" + associatedDeviceInfo.deviceTypeName + "]");
            } else {
                associatedDevices.add(shnDevice);
            }
        }
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
                    startAssociation(deviceTypeName);
                } else {
                    SHNLogger.w(TAG, "startAssociationForDeviceType: association not started: it is already running!");
                }
            }
        });
    }

    public List<SHNDevice> getAssociatedDevices() {
        return Collections.unmodifiableList(associatedDevices);
    }

    public void removeAllAssociatedDevices() {
        while (!associatedDevices.isEmpty()) {
            removeAssociatedDevice(associatedDevices.get(0));
        }
    }

    public void removeAssociatedDevice(SHNDevice shnDeviceToRemove) {
        boolean removed = removeAssociatedDeviceFromList(shnDeviceToRemove);
        if (removed) {
            persistAssociatedDeviceList();
            persistentStorageFactory.clearDeviceData(shnDeviceToRemove);
            shnDeviceToRemove.disconnect();

            ArrayList<DeviceRemovedListener> copyOfDeviceRemovedListeners = new ArrayList<>(this.deviceRemovedListeners);
            for (final DeviceRemovedListener listener : copyOfDeviceRemovedListeners) {
                listener.onAssociatedDeviceRemoved(shnDeviceToRemove);
            }
        }
    }

    private boolean removeAssociatedDeviceFromList(SHNDevice shnDeviceToRemove) {
        SHNDevice matchedSHNDevice = null;
        for (SHNDevice shnDevice : associatedDevices) {
            if (shnDevice.getAddress().equals(shnDeviceToRemove.getAddress())) {
                matchedSHNDevice = shnDevice;
            }
        }
        boolean removed = false;
        if (matchedSHNDevice != null) {
            removed = associatedDevices.remove(matchedSHNDevice);
        }
        return removed;
    }

    private void startAssociation(String deviceTypeName) {
        if (shnCentral.getShnCentralState() == SHNCentral.State.SHNCentralStateReady) {
            associatingWithDeviceTypeName = deviceTypeName;
            SHNDeviceDefinitionInfo shnDeviceDefinitionInfo = shnCentral.getSHNDeviceDefinitions().getSHNDeviceDefinitionInfoForDeviceTypeName(deviceTypeName);
            if (shnDeviceDefinitionInfo != null) {
                SHNAssociationProcedurePlugin shnAssociationProcedure = shnDeviceDefinitionInfo.createSHNAssociationProcedure(shnCentral, shnAssociationProcedureListener);
                if (shnAssociationProcedure.getShouldScan()) {
                    startScanning();
                }
                SHNResult result = shnAssociationProcedure.start();
                if (result == SHNResult.SHNOk) {
                    reportAssociationStarted(shnAssociationProcedure);
                } else {
                    reportFailure(result);
                }
            } else {
                reportFailure(SHNResult.SHNErrorUnknownDeviceType);
            }
        } else {
            reportFailure(SHNResult.SHNErrorBluetoothDisabled);
        }
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

    private void reportAssociationStarted(final SHNAssociationProcedurePlugin shnAssociationProcedure) {
        this.shnAssociationProcedure = shnAssociationProcedure;
        shnCentral.getUserHandler().post(new Runnable() {
            @Override
            public void run() {
                shnDeviceAssociationListener.onAssociationStarted(shnAssociationProcedure);
            }
        });
    }

    public void stopAssociation() {
        shnCentral.getInternalHandler().post(new Runnable() {
            @Override
            public void run() {
                if (shnAssociationProcedure != null) {
                    shnAssociationProcedure.stop();
                    handleStopAssociation();
                    shnCentral.getUserHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            shnDeviceAssociationListener.onAssociationStopped();
                        }
                    });
                } else {
                    SHNLogger.w(TAG, "stopAssociation: association not stopped: it is already stopped!");
                }
            }
        });
    }

    private void addAssociatedDevice(SHNDevice shnDevice) {
        removeAssociatedDeviceFromList(shnDevice);
        associatedDevices.add(shnDevice);
        persistAssociatedDeviceList();
    }

    private void persistAssociatedDeviceList() {
        List<SHNPersistentStorage.AssociatedDeviceInfo> associatedDeviceInfos = new ArrayList<>();
        for (SHNDevice shnDevice : associatedDevices) {
            associatedDeviceInfos.add(new SHNPersistentStorage.AssociatedDeviceInfo(shnDevice.getAddress(), shnDevice.getDeviceTypeName()));
        }
        SHNServiceRegistry.getInstance().get(SHNPersistentStorage.class).storeAssociatedDeviceInfos(associatedDeviceInfos);
    }

    private void handleStopAssociation() {
        stopScanning();
        shnAssociationProcedure.setShnAssociationProcedureListener(null);
        shnAssociationProcedure = null;
    }

    private void stopScanning() {
        scanStoppedIndicatesScanTimeout = false;
        shnDeviceScannerInternal.stopScanning();
    }

    private void startScanning() {
        scanStoppedIndicatesScanTimeout = true;
        if (!shnDeviceScannerInternal.startScanning(shnDeviceScannerListener, SHNDeviceScanner.ScannerSettingDuplicates.DuplicatesAllowed, 90000l)) {
            SHNLogger.e(TAG, "Could not start scanning (already scanning)");
            reportFailure(SHNResult.SHNErrorInvalidState);
        }
    }

    @NonNull
    QuickTestConnection createQuickTestConnection() {
        return new QuickTestConnection(shnCentral.getInternalHandler());
    }

    public void addDeviceRemovedListeners(@NonNull final DeviceRemovedListener listener) {
        deviceRemovedListeners.add(listener);
    }

    public void removeDeviceRemovedListeners(@NonNull final DeviceRemovedListener listener) {
        deviceRemovedListeners.remove(listener);
    }
}
