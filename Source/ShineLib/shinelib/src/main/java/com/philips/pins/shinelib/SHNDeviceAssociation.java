/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.pins.shinelib.utility.PersistentStorage;
import com.philips.pins.shinelib.utility.PersistentStorageFactory;
import com.philips.pins.shinelib.utility.QuickTestConnection;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Class that allows the API users to add new associate peripherals and remove them.
 * <p/>
 * Note that only peripherals for which a {@link SHNDeviceDefinitionInfo} is registered with {@link SHNCentral} are exposed by {@code SHNDeviceScanner}.
 * The matching behaviour of the scanner is under control of {@link SHNDeviceDefinitionInfo#useAdvertisedDataMatcher()} method in the corresponding
 * peripheral's device definition info. {@code SHNDeviceScanner} can be obtained via {@link SHNCentral#getShnDeviceScanner()}.
 */
public class SHNDeviceAssociation {

    /**
     * Possible state of {@code SHNDeviceAssociation}
     */
    public enum State {
        Idle,
        Associating
    }

    /**
     * Interface to receive updates for the current association.
     */
    public interface SHNDeviceAssociationListener {
        /**
         * Indicates that the association was started with the {@code SHNAssociationProcedure}.
         *
         * @param shnDeviceAssociationProcedure used to associate with the peripheral
         */
        void onAssociationStarted(SHNAssociationProcedure shnDeviceAssociationProcedure);

        /**
         * Indicates that the association has been stopped.
         */
        void onAssociationStopped();

        /**
         * Indicates that the association has finished successfully. The associated device is stored automatically. It can be obtained via {@link #getAssociatedDevices()}.
         *
         * @param shnDevice created for the associated peripheral
         */
        void onAssociationSucceeded(SHNDevice shnDevice);

        /**
         * Indicates that the association has failed for the peripheral.
         *
         * @param shnError that occurred during the association
         */
        void onAssociationFailed(SHNResult shnError);

        /**
         * Indicates that the association has been updated. Will be called if the peripheral information was injected successfully via {@link #injectAssociatedDevice(String, String, SHNResultListener)}.
         */
        void onAssociatedDevicesUpdated();
    }

    /**
     * Interface to receive updates for an associated device getting removed.
     */
    public interface DeviceRemovedListener {
        /**
         * Indicates that device was removed.
         *
         * @param device that has been removed
         */
        void onAssociatedDeviceRemoved(@NonNull final SHNDevice device);
    }

    private static final String TAG = SHNDeviceAssociation.class.getSimpleName();

    private final SHNDeviceScannerInternal shnDeviceScannerInternal;
    private List<SHNDevice> associatedDevices;
    private SHNAssociationProcedurePlugin shnAssociationProcedure;
    private String associatingWithDeviceTypeName;

    @NonNull
    private final PersistentStorageFactory persistentStorageFactory;
    private List<DeviceRemovedListener> deviceRemovedListeners = new ArrayList<>();

    @Nullable
    private SHNDeviceAssociationListener shnDeviceAssociationListener;
    private final SHNCentral shnCentral;
    private SHNInternalScanRequest shnInternalScanRequest;

    private boolean scanStoppedIndicatesScanTimeout;
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

    public SHNDeviceAssociation(final @NonNull SHNCentral shnCentral, final @NonNull SHNDeviceScannerInternal shnDeviceScannerInternal, final @NonNull PersistentStorageFactory persistentStorageFactory) {
        this.shnCentral = shnCentral;
        this.shnDeviceScannerInternal = shnDeviceScannerInternal;
        this.persistentStorageFactory = persistentStorageFactory;
    }

    /*package*/ void initAssociatedDevicesListOnInternalThread() {
        if (isRunningOnTheInternalThread()) {
            initAssociatedDevicesList();
        } else {
            Callable<Boolean> initCallable = new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    initAssociatedDevicesList();
                    return true;
                }
            };

            FutureTask<Boolean> initFuture = new FutureTask<>(initCallable);

            if (shnCentral.getInternalHandler().post(initFuture)) {

                try {
                    initFuture.get();
                } catch (InterruptedException e) {
                    throw new InternalError("Caught unexpected InterruptedException");
                } catch (ExecutionException e) {
                    throw new InternalError("Caught unexpected ExecutionException");
                }
            } else {
                throw new InternalError("The internal thread is not running");
            }
        }
    }

    @VisibleForTesting
    /*package*/ boolean isRunningOnTheInternalThread() {
        return shnCentral.getInternalHandler().getLooper().getThread().equals(Thread.currentThread());
    }

    private void initAssociatedDevicesList() {
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
    }

    @NonNull
     /*package*/ SHNDeviceAssociationHelper getShnDeviceAssociationHelper() {
        PersistentStorage persistentStorage = persistentStorageFactory.getPersistentStorage();
        return new SHNDeviceAssociationHelper(persistentStorage);
    }

    /**
     * Returns current state of the device assocaition.
     * <p/>
     * Note new association can only be started in state {@code Idle}.
     *
     * @return current state of the device assocaition
     */
    public State getState() {
        return (shnAssociationProcedure != null) ? State.Associating : State.Idle;
    }

    /**
     * Set callback to receive updates about the ongoing association.
     *
     * @param shnDeviceAssociationListener to receive updates.
     */
    public void setShnDeviceAssociationListener(@Nullable SHNDeviceAssociationListener shnDeviceAssociationListener) {
        this.shnDeviceAssociationListener = shnDeviceAssociationListener;
    }

    /**
     * Start association procedure for the device type. Please use device type name as specified in {@link SHNDeviceDefinitionInfo#getDeviceTypeName()}.
     * <p/>
     * Callbacks about association are provided via {@code SHNDeviceAssociationListener}
     *
     * @param deviceTypeName to start association for
     */
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

    /**
     * Returns a list of all associated device.
     *
     * @return list of associated device
     */
    public List<SHNDevice> getAssociatedDevices() {
        return Collections.unmodifiableList(associatedDevices);
    }

    /**
     * Remove all associated devices.
     * <p/>
     * Devices' persistent storage will be removed. Do not issue calls to the {@code SHNDevice} instance after it has been removed. Note the function does not remove bonds on OS level.
     */
    public void removeAllAssociatedDevices() {
        removeAllAssociatedDevices(new NullDeviceRemovedListener());
    }

    /**
     * Remove all associated devices.
     * <p/>
     * Devices' persistent storage will be removed. Do not issue calls to the {@code SHNDevice} instance after it has been removed. Note the function does not remove bonds on OS level.
     *
     * @param deviceRemovedListener to receive result of the deletion
     */
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

    /**
     * Remove this associated device.
     * <p/>
     * Device persistent storage will be removed. Do not issue calls to the {@code SHNDevice} instance after it has been removed. Note the function does not remove bonds on OS level.
     *
     * @param shnDeviceToRemove device to be removed
     */
    public void removeAssociatedDevice(@NonNull final SHNDevice shnDeviceToRemove) {
        removeAssociatedDevice(shnDeviceToRemove, new NullDeviceRemovedListener());
    }

    /**
     * Remove this associated device.
     * <p/>
     * Device persistent storage will be removed. Do not issue calls to the {@code SHNDevice} instance after it has been removed. Note the function does not remove bonds on OS level.
     *
     * @param shnDeviceToRemove     device to be removed
     * @param deviceRemovedListener to receive result of the deletion
     */
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
            shnCentral.removeDeviceFromDeviceCache(shnDeviceToRemove);
            SHNDevice.State state = shnDeviceToRemove.getState();
            if (state.equals(SHNDevice.State.Disconnected) || state.equals(SHNDevice.State.Disconnecting)) {
                persistentStorageFactory.getPersistentStorageCleaner().clearDeviceData(shnDeviceToRemove);
            } else {
                shnDeviceToRemove.registerSHNDeviceListener(createClearStorageOnDisconnectListener(shnDeviceToRemove));
                shnDeviceToRemove.disconnect();
            }

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
                if (shnDeviceAssociationListener != null) {
                    shnDeviceAssociationListener.onAssociationStarted(shnAssociationProcedure);
                }
            }
        });
    }

    /**
     * Stops current running association and sets the state of the device association to {@code Idle}
     * <p/>
     * Callbacks are provided via {@code SHNDeviceAssociationListener}
     */
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
                            if (shnDeviceAssociationListener != null) {
                                shnDeviceAssociationListener.onAssociationStopped();
                            }
                        }
                    });
                } else {
                    SHNLogger.w(TAG, "stopAssociation: association not stopped: it is already stopped!");
                }
            }
        });
    }

    /**
     * With this function a device can be added to the associated devices list.
     * <p/>
     * Note that the data provided by the device information capability
     * is not available from cache until the first time a connection has been made. More importantly this function will not restore a previously
     * existing bond on the OS level, nor will it check that such a bond exists or is needed by the device to function properly.
     * For a successful completion, there must be a plugin registered that handles the device indicated by the device type name. A result of SHNOk
     * indicates that the injection was successful. A result of SHNErrorInvalidParameter indicates that
     * either the MAC address is invalid or that there is no plugin registered for the deviceTypeName. A result
     * of SHNErrorOperationFailed indicates that an association already exists for a device with the given address.
     * A result of SHNErrorInvalidResponse indicates that there was an error creating the device.
     *
     * @param deviceMACAddress  The MAC address of the device to inject. Casing does not matter, only colon separated addresses are allowed.
     * @param deviceTypeName    The device type name as defined by the device plugin.
     * @param shnResultListener The result listener where the success or failure of the injection must be reported.
     */
    public void injectAssociatedDevice(@NonNull final String deviceMACAddress, @NonNull final String deviceTypeName, @NonNull final SHNResultListener shnResultListener) {
        shnCentral.getInternalHandler().post(new Runnable() {
            @Override
            public void run() {
                internalInjectAssociatedDevice(deviceMACAddress, deviceTypeName, new SHNResultListener() {
                    @Override
                    public void onActionCompleted(final SHNResult result) {
                        shnCentral.getUserHandler().post(new Runnable() {
                            @Override
                            public void run() {
                                shnResultListener.onActionCompleted(result);
                            }
                        });
                    }
                });
            }
        });
    }

    private void internalInjectAssociatedDevice(@NonNull String deviceMACAddress, @NonNull String deviceTypeName, @NonNull SHNResultListener shnResultListener) {
        deviceMACAddress = deviceMACAddress.toUpperCase(Locale.US);
        if (!isAValidMACAddress(deviceMACAddress)) {
            SHNLogger.e(TAG, "Failed injecting associated device (MAC address: " + deviceMACAddress + " deviceType: " + deviceTypeName + "); invalid MAC address");
            shnResultListener.onActionCompleted(SHNResult.SHNErrorInvalidParameter);
        } else {
            if (hasAssociatedDeviceForTheMACAddress(deviceMACAddress)) {
                SHNLogger.e(TAG, "Failed injecting associated device (MAC address: " + deviceMACAddress + " deviceType: " + deviceTypeName + "); associated device already registered");
                shnResultListener.onActionCompleted(SHNResult.SHNErrorOperationFailed);
            } else {
                SHNDeviceDefinitionInfo shnDeviceDefinitionInfo = shnCentral.getSHNDeviceDefinitions().getSHNDeviceDefinitionInfoForDeviceTypeName(deviceTypeName);
                if (shnDeviceDefinitionInfo == null) {
                    SHNLogger.e(TAG, "Failed injecting associated device (MAC address: " + deviceMACAddress + " deviceType: " + deviceTypeName + "); no device definition found");
                    shnResultListener.onActionCompleted(SHNResult.SHNErrorInvalidParameter);
                } else {
                    SHNDevice shnDevice = shnCentral.createSHNDeviceForAddressAndDefinition(deviceMACAddress, shnDeviceDefinitionInfo);
                    if (shnDevice == null) {
                        SHNLogger.e(TAG, "Failed injecting associated device (MAC address: " + deviceMACAddress + " deviceType: " + deviceTypeName + "); error creating a device");
                        shnResultListener.onActionCompleted(SHNResult.SHNErrorInvalidResponse);
                    } else {
                        addAssociatedDevice(shnDevice);
                        shnResultListener.onActionCompleted(SHNResult.SHNOk);
                        if (shnDeviceAssociationListener != null) {
                            shnDeviceAssociationListener.onAssociatedDevicesUpdated();
                        }
                    }
                }
            }
        }
    }

    private boolean isAValidMACAddress(@NonNull String deviceMACAddress) {
        return deviceMACAddress.matches("([0-9A-F]{2}:){5}([0-9A-F]{2})");
    }

    private boolean hasAssociatedDeviceForTheMACAddress(@NonNull String deviceMACAddress) {
        for (SHNDevice associatedDevice : associatedDevices) {
            if (associatedDevice.getAddress().equalsIgnoreCase(deviceMACAddress)) {
                return true;
            }
        }
        return false;
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
    /*package*/ QuickTestConnection createQuickTestConnection() {
        return new QuickTestConnection(shnCentral.getInternalHandler());
    }

    /**
     * Use this function to receive notification if a {@link SHNDevice} is removed from the list of associated device.
     *
     * @param listener to be added
     */
    public void addDeviceRemovedListeners(@NonNull final DeviceRemovedListener listener) {
        deviceRemovedListeners.add(listener);
    }

    /**
     * Use this function to stop receive notification if a {@link SHNDevice} is removed from the list of associated device.
     *
     * @param listener to be removed
     */
    public void removeDeviceRemovedListeners(@NonNull final DeviceRemovedListener listener) {
        deviceRemovedListeners.remove(listener);
    }

    private static class NullDeviceRemovedListener implements DeviceRemovedListener {
        @Override
        public void onAssociatedDeviceRemoved(@NonNull final SHNDevice device) {
        }
    }
}
