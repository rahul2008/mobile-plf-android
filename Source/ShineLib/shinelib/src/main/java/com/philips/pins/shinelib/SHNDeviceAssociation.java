/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.utility.PersistentStorage;
import com.philips.pins.shinelib.utility.PersistentStorageFactory;
import com.philips.pins.shinelib.utility.QuickTestConnection;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

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
        public void deviceFound(SHNDeviceScanner shnDeviceScanner, @NonNull SHNDeviceFoundInfo shnDeviceFoundInfo) {
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
    private SHNInternalScanRequest shnInternalScanRequest;

    public SHNDeviceAssociation(final @NonNull SHNCentral shnCentral, final @NonNull SHNDeviceScannerInternal shnDeviceScannerInternal, final @NonNull PersistentStorageFactory persistentStorageFactory) {
        this.shnCentral = shnCentral;
        this.shnDeviceScannerInternal = shnDeviceScannerInternal;
        this.persistentStorageFactory = persistentStorageFactory;
    }

    void initAssociatedDevicesList() {
        Callable<Boolean> initCallable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                SHNDeviceAssociationHelper associationHelper = getShnDeviceAssociationHelper();
                List<SHNDeviceAssociationHelper.AssociatedDeviceInfo> associatedDeviceInfos = associationHelper.readAssociatedDeviceInfos();
                associatedDevices = new ArrayList<>();
                for (SHNDeviceAssociationHelper.AssociatedDeviceInfo associatedDeviceInfo : associatedDeviceInfos) {
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
                return true;
            }
        };

        FutureTask<Boolean> initFuture = new FutureTask<>(initCallable);

        if (shnCentral.getInternalHandler().post(initFuture)) {

            try {
                initFuture.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else {
            throw new InternalError("The internal thread is not running");
        }
    }

    @NonNull
    SHNDeviceAssociationHelper getShnDeviceAssociationHelper() {
        PersistentStorage persistentStorage = persistentStorageFactory.getPersistentStorage();
        return new SHNDeviceAssociationHelper(persistentStorage);
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
        removeAllAssociatedDevices(new NullDeviceRemovedListener());
    }

    public void removeAllAssociatedDevices(@NonNull final DeviceRemovedListener deviceRemovedListener) {
        shnCentral.getInternalHandler().post(new Runnable() {
            @Override
            public void run() {
                while (!associatedDevices.isEmpty()) {
                    removeAssociatedDeviceInternal(associatedDevices.get(0), deviceRemovedListener);
                }
            }
        });
    }

    public void removeAssociatedDevice(@NonNull final SHNDevice shnDeviceToRemove) {
        removeAssociatedDevice(shnDeviceToRemove, new NullDeviceRemovedListener());
    }

    public void removeAssociatedDevice(@NonNull final SHNDevice shnDeviceToRemove, @NonNull final DeviceRemovedListener deviceRemovedListener) {
        shnCentral.getInternalHandler().post(new Runnable() {
            @Override
            public void run() {
                removeAssociatedDeviceInternal(shnDeviceToRemove, deviceRemovedListener);
            }
        });
    }

    private void removeAssociatedDeviceInternal(@NonNull final SHNDevice shnDeviceToRemove, @NonNull final DeviceRemovedListener deviceRemovedListener) {
        boolean removed = removeAssociatedDeviceFromList(shnDeviceToRemove);
        if (removed) {
            persistAssociatedDeviceList();
            shnDeviceToRemove.registerSHNDeviceListener(createClearStorageOnDisconnectListener(shnDeviceToRemove));
            shnDeviceToRemove.disconnect();

            final ArrayList<DeviceRemovedListener> copyOfDeviceRemovedListeners = new ArrayList<>(deviceRemovedListeners);
            shnCentral.getUserHandler().post(new Runnable() {
                @Override
                public void run() {
                    deviceRemovedListener.onAssociatedDeviceRemoved(shnDeviceToRemove);
                    for (final DeviceRemovedListener listener : copyOfDeviceRemovedListeners) {
                        listener.onAssociatedDeviceRemoved(shnDeviceToRemove);
                    }
                }
            });
        }
    }

    @NonNull
    private SHNDevice.SHNDeviceListener createClearStorageOnDisconnectListener(final SHNDevice shnDeviceToRemove) {
        return new SHNDevice.SHNDeviceListener() {
            @Override
            public void onStateUpdated(final SHNDevice shnDevice) {
                SHNDevice.State state = shnDevice.getState();
                if (state.equals(SHNDevice.State.Disconnected) || state.equals(SHNDevice.State.Disconnecting)) {
                    shnDevice.unregisterSHNDeviceListener(this);
                    shnCentral.getInternalHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            persistentStorageFactory.getPersistentStorageCleaner().clearDeviceData(shnDeviceToRemove);
                        }
                    });
                }
            }

            @Override
            public void onFailedToConnect(final SHNDevice shnDevice, final SHNResult result) {
            }
        };
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
        List<SHNDeviceAssociationHelper.AssociatedDeviceInfo> associatedDeviceInfos = new ArrayList<>();
        for (SHNDevice shnDevice : associatedDevices) {
            associatedDeviceInfos.add(new SHNDeviceAssociationHelper.AssociatedDeviceInfo(shnDevice.getAddress(), shnDevice.getDeviceTypeName()));
        }
        SHNDeviceAssociationHelper associationHelper = getShnDeviceAssociationHelper();
        associationHelper.storeAssociatedDeviceInfos(associatedDeviceInfos);
    }

    private void handleStopAssociation() {
        stopScanning();
        shnAssociationProcedure.setShnAssociationProcedureListener(null);
        shnAssociationProcedure = null;
    }

    private void stopScanning() {
        scanStoppedIndicatesScanTimeout = false;
        if (shnInternalScanRequest != null) {
            shnDeviceScannerInternal.stopScanning(shnInternalScanRequest);
            shnInternalScanRequest = null;
        }
    }

    private void startScanning() {
        stopScanning();
        scanStoppedIndicatesScanTimeout = true;
        shnInternalScanRequest = new SHNInternalScanRequest(null, null, true, 90000l, shnDeviceScannerListener);
        if (!shnDeviceScannerInternal.startScanning(shnInternalScanRequest)) {
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

    private static class NullDeviceRemovedListener implements DeviceRemovedListener {
        @Override
        public void onAssociatedDeviceRemoved(@NonNull final SHNDevice device) {
        }
    }
}
