/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.bluetoothwrapper.BTAdapter;
import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BleUtilities;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.utility.DataMigrater;
import com.philips.pins.shinelib.utility.LoggingExceptionHandler;
import com.philips.pins.shinelib.utility.PersistentStorageFactory;
import com.philips.pins.shinelib.utility.SharedPreferencesMigrator;
import com.philips.pins.shinelib.wrappers.SHNDeviceWrapper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class SHNCentral {

    public enum State {
        SHNCentralStateError, SHNCentralStateNotReady, SHNCentralStateReady
    }

    public interface SHNCentralListener {
        void onStateUpdated(SHNCentral shnCentral);
    }

    private static final String TAG = SHNCentral.class.getSimpleName();
    private SHNUserConfiguration shnUserConfiguration;
    private SHNDeviceScanner shnDeviceScanner;
    private final Handler userHandler;
    private final Context applicationContext;
    private boolean bluetoothAdapterEnabled;
    private BroadcastReceiver bondStateChangedReceiver;

    private final BroadcastReceiver bluetoothBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                    case BluetoothAdapter.STATE_TURNING_OFF:
                    case BluetoothAdapter.STATE_TURNING_ON:
                        bluetoothAdapterEnabled = false;
                        setState(State.SHNCentralStateNotReady);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        bluetoothAdapterEnabled = true;
                        setState(State.SHNCentralStateReady);
                        break;
                }
            }
        }
    };
    private List<SHNCentralListener> registeredShnCentralListeners;
    private SHNDeviceScannerInternal shnDeviceScannerInternal;
    private SHNDeviceAssociation shnDeviceAssociation;
    private State shnCentralState = State.SHNCentralStateNotReady;
    private BTAdapter btAdapter;
    private Handler internalHandler;
    private SHNDeviceDefinitions shnDeviceDefinitions;
    private PersistentStorageFactory persistentStorageFactory;
    private Map<String, WeakReference<SHNBondStatusListener>> shnBondStatusListeners = new HashMap<>();

    private SharedPreferencesProvider defaultSharedpreferencesProvider = new SharedPreferencesProvider() {
        @NonNull
        @Override
        public SharedPreferences getSharedPreferences(String key, int mode) {
            return applicationContext.getSharedPreferences(key, Context.MODE_PRIVATE);
        }

        @NonNull
        @Override
        public String getSharedPreferencesPrefix() {
            return "";
        }
    };

    @Deprecated
    public SHNCentral(Handler handler, final Context context) throws SHNBluetoothHardwareUnavailableException {
        this(handler, context, false, null, false);
    }

    SHNCentral(Handler handler, final Context context, final boolean showPopupIfBLEIsTurnedOff, final SharedPreferencesProvider customSharedPreferencesProvider, final boolean migrateDataToCustomSharedPreferencesProvider) throws SHNBluetoothHardwareUnavailableException {
        applicationContext = context.getApplicationContext();
        BleUtilities.init(applicationContext);
        if (!BleUtilities.deviceHasBle()) {
            throw new SHNBluetoothHardwareUnavailableException();
        }

        internalHandler = createInternalHandler();
        if (internalHandler != null) {
            Timer.setHandler(internalHandler);
        }

        // The handler is used for callbacks to the usercode. When no handler is provided, the MainLoop a.k.a. UI Thread is used.
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        this.userHandler = handler;

        Callable<Boolean> initCallable = new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                initializeSHNCentral(showPopupIfBLEIsTurnedOff, customSharedPreferencesProvider, migrateDataToCustomSharedPreferencesProvider);
                return true;
            }
        };

        FutureTask<Boolean> initFuture = new FutureTask<>(initCallable);

        if (internalHandler.post(initFuture)) {

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

    private void initializeSHNCentral(boolean showPopupIfBLEIsTurnedOff, SharedPreferencesProvider customSharedPreferencesProvider, boolean migrateDataToCustomSharedPreferencesProvider) {
        persistentStorageFactory = setUpPersistentStorageFactory(applicationContext, customSharedPreferencesProvider, migrateDataToCustomSharedPreferencesProvider);

        // Check that the adapter is enabled.
        bluetoothAdapterEnabled = BleUtilities.isBluetoothAdapterEnabled();
        if (bluetoothAdapterEnabled) {
            shnCentralState = State.SHNCentralStateReady;
        } else if (showPopupIfBLEIsTurnedOff) {
            BleUtilities.startEnableBluetoothActivity();
        }

        // Register a broadcast receiver listening for BluetoothAdapter state changes
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        applicationContext.registerReceiver(bluetoothBroadcastReceiver, filter);

        setupBondStatusListener();

        shnDeviceDefinitions = new SHNDeviceDefinitions();

        shnDeviceScannerInternal = new SHNDeviceScannerInternal(this, shnDeviceDefinitions.getRegisteredDeviceDefinitions());
        shnDeviceScanner = new SHNDeviceScanner(shnDeviceScannerInternal, internalHandler, userHandler);

        SHNDeviceWrapper.setHandlers(internalHandler, userHandler);

        btAdapter = new BTAdapter(applicationContext, internalHandler);

        shnUserConfiguration = createUserConfiguration();
    }

    /* package */ SHNUserConfiguration createUserConfiguration() {
        SHNUserConfigurationImpl shnUserConfigurationImpl = new SHNUserConfigurationImpl(persistentStorageFactory, getInternalHandler(), new SHNUserConfigurationCalculations());
        return shnUserConfigurationImpl;
    }

    /* package */ SharedPreferencesMigrator createSharedPreferencesMigrator(PersistentStorageFactory source, PersistentStorageFactory destination) {
        return new SharedPreferencesMigrator(source, destination);
    }

    /* package */ PersistentStorageFactory createPersistentStorageFactory(SharedPreferencesProvider sharedPreferencesProvider) {
        return new PersistentStorageFactory(sharedPreferencesProvider);
    }

    /* package */ long getHandlerThreadId(Handler handler) {
        return handler.getLooper().getThread().getId();
    }

    /* package */ Handler createInternalHandler() {
        HandlerThread thread = new HandlerThread("InternalShineLibraryThread");
        thread.setUncaughtExceptionHandler(new LoggingExceptionHandler());
        thread.start();
        return new Handler(thread.getLooper());
    }

    /* package */ DataMigrater createDataMigrater() {
        return new DataMigrater();
    }

    /* package */ void registerBondStatusListenerForAddress(SHNBondStatusListener shnBondStatusListener, String address) {
        shnBondStatusListeners.put(address, new WeakReference<>(shnBondStatusListener));
    }

    /* package */ void unregisterBondStatusListenerForAddress(SHNBondStatusListener shnBondStatusListener, String address) {
        shnBondStatusListeners.remove(address);
    }

    private void setState(final State state) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                SHNCentral.this.shnCentralState = state;
                if (registeredShnCentralListeners != null) {
                    // copy the array to prevent ConcurrentModificationException
                    ArrayList<SHNCentralListener> copyOfRegisteredShnCentralListeners = new ArrayList<>(registeredShnCentralListeners);
                    for (final SHNCentralListener shnCentralListener : copyOfRegisteredShnCentralListeners) {
                        if (shnCentralListener != null) {
                            userHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    shnCentralListener.onStateUpdated(SHNCentral.this);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    private void setupBondStatusListener() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        bondStateChangedReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, final Intent intent) {
                internalHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        onBondStateChanged(intent);
                    }
                });
            }
        };
        getApplicationContext().registerReceiver(bondStateChangedReceiver, intentFilter);
    }

    private void onBondStateChanged(Intent intent) {
        Bundle bundle = intent.getExtras();
        BluetoothDevice device = bundle.getParcelable(BluetoothDevice.EXTRA_DEVICE);
        int bondState = bundle.getInt(BluetoothDevice.EXTRA_BOND_STATE);
        int previousBondState = bundle.getInt(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE);
        WeakReference<SHNBondStatusListener> shnBondStatusListener = shnBondStatusListeners.get(device.getAddress());
        if (shnBondStatusListener != null) {
            SHNBondStatusListener listener = shnBondStatusListener.get();
            if (listener != null) {
                listener.onBondStatusChanged(device, bondState, previousBondState);
            } else {
                shnBondStatusListeners.remove(device.getAddress());
            }
        }
    }

    private PersistentStorageFactory setUpPersistentStorageFactory(Context context, SharedPreferencesProvider customSharedPreferencesProvider, boolean migrateDataToCustomSharedPreferencesProvider) {
        PersistentStorageFactory defaultPersistentStorageFactory = createPersistentStorageFactory(defaultSharedpreferencesProvider);

        if (customSharedPreferencesProvider == null) {
            migrateDataFromOldKeysToNewKeys(context, defaultSharedpreferencesProvider);
            return defaultPersistentStorageFactory;
        } else {
            TimeGuardedSharedPreferencesProviderWrapper timeGuardedSharedPreferencesProviderWrapper = new TimeGuardedSharedPreferencesProviderWrapper(customSharedPreferencesProvider, getHandlerThreadId(internalHandler));
            PersistentStorageFactory customPersistentStorageFactory = createPersistentStorageFactory(timeGuardedSharedPreferencesProviderWrapper);

            SharedPreferencesMigrator sharedPreferencesMigrator = createSharedPreferencesMigrator(defaultPersistentStorageFactory, customPersistentStorageFactory);
            if (!sharedPreferencesMigrator.destinationPersistentStorageContainsData() && migrateDataToCustomSharedPreferencesProvider) {
                migrateDataFromOldKeysToNewKeys(context, defaultSharedpreferencesProvider);
                sharedPreferencesMigrator.execute();
            } else {
                migrateDataFromOldKeysToNewKeys(context, timeGuardedSharedPreferencesProviderWrapper); // This call is needed to make the destinationPersistentStorageContainsData method return true after the first time creation with the same custom provider.
            }
            return customPersistentStorageFactory;
        }
    }

    private void migrateDataFromOldKeysToNewKeys(Context context, SharedPreferencesProvider sharedPreferencesProvider) {
        DataMigrater dataMigrater = createDataMigrater();
        dataMigrater.execute(context, createPersistentStorageFactory(sharedPreferencesProvider));
    }

    public interface SHNBondStatusListener {
        void onBondStatusChanged(BluetoothDevice device, int bondState, int previousBondState);
    }

    public Handler getInternalHandler() {
        return internalHandler;
    }

    public Handler getUserHandler() {
        return userHandler;
    }

    public void shutdown() {
        internalHandler.getLooper().quitSafely();
        applicationContext.unregisterReceiver(bluetoothBroadcastReceiver);
        if (bondStateChangedReceiver != null) {
            applicationContext.unregisterReceiver(bondStateChangedReceiver);
            bondStateChangedReceiver = null;
        }
        shnDeviceScannerInternal.stopScanning();
        shnDeviceScannerInternal = null;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public PersistentStorageFactory getPersistentStorageFactory() {
        return persistentStorageFactory;
    }

    public void runOnUserHandlerThread(Runnable runnable) {
        userHandler.post(runnable);
    }

    public boolean isBluetoothAdapterEnabled() {
        return bluetoothAdapterEnabled;
    }

    public String getVersion() {
        throw new UnsupportedOperationException();
    }

    public boolean registerDeviceDefinition(SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
        return shnDeviceDefinitions.add(shnDeviceDefinitionInfo);
    }

    public SHNDeviceDefinitions getSHNDeviceDefinitions() {
        return shnDeviceDefinitions;
    }

    public void registerShnCentralListener(SHNCentralListener shnCentralListener) {
        if (registeredShnCentralListeners == null) {
            registeredShnCentralListeners = new ArrayList<>();
        }
        if (!registeredShnCentralListeners.contains(shnCentralListener)) {
            registeredShnCentralListeners.add(shnCentralListener);
        }
    }

    public void unregisterShnCentralListener(SHNCentralListener shnCentralListener) {
        if (registeredShnCentralListeners != null) {
            registeredShnCentralListeners.remove(shnCentralListener);
        }
    }

    public SHNUserConfiguration getSHNUserConfiguration() {
        return shnUserConfiguration;
    }

    public SHNDeviceScanner getShnDeviceScanner() {
        return shnDeviceScanner;
    }

    public SHNDeviceAssociation getShnDeviceAssociation() {
        if (shnDeviceAssociation == null) {
            shnDeviceAssociation = new SHNDeviceAssociation(this, shnDeviceScannerInternal, persistentStorageFactory);
            shnDeviceAssociation.initAssociatedDevicesListOnInternalThread();
        }
        return shnDeviceAssociation;
    }

    public State getShnCentralState() {
        return shnCentralState;
    }

    public BTDevice getBTDevice(String address) {
        return btAdapter.getRemoteDevice(address);
    }

    private Map<String, SHNDevice> createdDevices = new HashMap<>();

    // TEMPORARY HACK TO ENABLE VERIFICATION TESTS WITH BLE SECURITY ENABLED
    // TODO: Remove this once the ShineVerificationApp uses DeviceAssociation.
    @Deprecated
    public SHNDevice createSHNDeviceForAddressAndDefinition(@NonNull String deviceAddress, @NonNull SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
        String key = deviceAddress + shnDeviceDefinitionInfo.getDeviceTypeName();
        SHNDevice shnDevice = createdDevices.get(key);
        if (shnDevice == null) {
            shnDevice = shnDeviceDefinitionInfo.getSHNDeviceDefinition().createDeviceFromDeviceAddress(deviceAddress, shnDeviceDefinitionInfo, this);
            if (shnDevice != null) {
                createdDevices.put(key, shnDevice);
            }
        }
        return shnDevice;
    }

    public static class Builder {
        private Handler handler;
        private final Context context;
        private Boolean showPopupIfBLEIsTurnedOff = false;
        private Boolean migrateFromDefaultProviderToCustom = false;
        private SharedPreferencesProvider sharedPreferencesProvider;

        public Builder(@NonNull final Context context) {
            this.context = context;
        }

        public Builder setHandler(Handler handler) {
            this.handler = handler;
            return this;
        }

        public Builder showPopupIfBLEIsTurnedOff(Boolean showPopupIfBLEIsTurnedOff) {
            this.showPopupIfBLEIsTurnedOff = showPopupIfBLEIsTurnedOff;
            return this;
        }

        public Builder setSharedPreferencesProvider(SharedPreferencesProvider sharedPreferencesProvider) {
            this.sharedPreferencesProvider = sharedPreferencesProvider;
            return this;
        }

        public Builder migrateFromDefaultProviderToCustom(Boolean migrateFromDefaultProviderToCustom) {
            this.migrateFromDefaultProviderToCustom = migrateFromDefaultProviderToCustom;
            return this;
        }

        public SHNCentral create() throws SHNBluetoothHardwareUnavailableException {
            return new SHNCentral(handler, context, showPopupIfBLEIsTurnedOff, sharedPreferencesProvider, migrateFromDefaultProviderToCustom);
        }
    }
}
