/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
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
import android.support.annotation.VisibleForTesting;

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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * Central class for handling BlueLib initialisation, associating to peripherals and retrieving currently associated peripherals.
 * <p/>
 * Use a {@link com.philips.pins.shinelib.SHNCentral.Builder} to construct a {@code SHNCentral}.
 * After that add {@link SHNDeviceDefinitionInfo} for device types using
 * {@link #registerDeviceDefinition(SHNDeviceDefinitionInfo)} and then associate with a device type or start scanning for it.
 * <p/>
 * Applications should create only one instance of {@code SHNCentral}.
 */
public class SHNCentral {

    private int bluetoothAdapterState;

    /**
     * State that the {@link SHNCentral} currently is in.
     */
    public enum State {
        /**
         * {@code SHNCentral} is in an error state
         */
        SHNCentralStateError,
        /**
         * {@code SHNCentral} is not yet ready to communicate with peripherals (for instance when bluetooth is disabled)
         */
        SHNCentralStateNotReady,
        /**
         * {@code SHNCentral} is ready to communicate with peripherals
         */
        SHNCentralStateReady
    }

    /**
     * A listener for changes in the state of {@code SHNCentral}.
     */
    public interface SHNCentralListener {

        /**
         * Called when the state of the {@code SHNCentral} was changed.
         *
         * @param shnCentral the {@code SHNCentral} object that had its state changed.
         */
        void onStateUpdated(@NonNull SHNCentral shnCentral);
    }

    private static final String TAG = "SHNCentral";
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
                bluetoothAdapterState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (bluetoothAdapterState) {
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
    private Map<String, WeakReference<SHNCentralListener>> shnCentralStatusListeners = new HashMap<>();

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

    /**
     * Old constructor of {@code SHNCentral}. Do not use.
     *
     * @param handler
     * @param context
     * @throws SHNBluetoothHardwareUnavailableException
     * @deprecated Use the {@link SHNCentral.Builder} instead.
     */
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

    /* package */ void registerSHNCentralStatusListenerForAddress(SHNCentralListener shnCentralListener, String address) {
        shnCentralStatusListeners.put(address, new WeakReference<>(shnCentralListener));
    }

    /* package */ void unregisterSHNCentralStatusListenerForAddress(SHNCentralListener shnCentralListener, String address) {
        shnCentralStatusListeners.remove(address);
    }

    /* package */ int getBluetoothAdapterState() {
        return bluetoothAdapterState;
    }

    private void setState(final State state) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                SHNCentral.this.shnCentralState = state;
                onSHNCentralStateChanged();
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

    private void onSHNCentralStateChanged() {
        Set<String> keys = new HashSet<>(shnCentralStatusListeners.keySet());
        for (String key : keys) {
            WeakReference<SHNCentralListener> shnCentralListenerWeakReference = shnCentralStatusListeners.get(key);
            SHNCentralListener shnCentralListener = shnCentralListenerWeakReference.get();
            if (shnCentralListener != null) {
                shnCentralListener.onStateUpdated(this);
            } else {
                shnCentralStatusListeners.remove(key);
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

    /* package */ interface SHNBondStatusListener {
        void onBondStatusChanged(BluetoothDevice device, int bondState, int previousBondState);
    }

    /**
     * Get a {@code Handler} that is used to run internal tasks on. To prevent threading issues,
     * Bluelib code runs on a single thread. This function gives access to a handler that executes
     * the tasks on this "internal" thread. This handler should not be used to execute application
     * level code. This function will be removed from the interface in a future version of Bluelib.
     *
     * @return the {@code Handler} that is used for internal tasks
     */
    public Handler getInternalHandler() {
        return internalHandler;
    }

    /**
     * Get the {@code Handler} that is used to run application level callbacks on.
     *
     * @return the {@code Handler} that is used for callbacks
     * @see com.philips.pins.shinelib.SHNCentral.Builder#setHandler(Handler)
     */
    public Handler getUserHandler() {
        return userHandler;
    }

    /**
     * Shutdown {@code SHNCentral}.
     * <p/>
     * This should be called before the object is destroyed by the garbage collector.
     */
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

    /**
     * Get the {@code Context} set during construction of the {@code SHNCentral}.
     *
     * @return the {@code SHNCentral Context}
     * @see com.philips.pins.shinelib.SHNCentral.Builder
     */
    public Context getApplicationContext() {
        return applicationContext;
    }

    /**
     * Get the {@code PersistentStorageFactory} for this {@code SHNCentral}.
     *
     * @return the {@code PersistentStorageFactory}
     */
    public PersistentStorageFactory getPersistentStorageFactory() {
        return persistentStorageFactory;
    }

    /**
     * Convenience method to run a {@code Runnable} on the {@code Handler} set with
     * {@link com.philips.pins.shinelib.SHNCentral.Builder#setHandler(Handler)}. See also
     * {@link #getUserHandler()}.
     *
     * @param runnable to run on the {@code Handler}
     * @see com.philips.pins.shinelib.SHNCentral.Builder#setHandler(Handler)
     */
    public void runOnUserHandlerThread(Runnable runnable) {
        userHandler.post(runnable);
    }

    /**
     * Get the bluetooth adapter's state.
     *
     * @return true if bluetooth adapter is enabled
     */
    public boolean isBluetoothAdapterEnabled() {
        return bluetoothAdapterEnabled;
    }

    /**
     * Get the current BlueLib version. For release version returns the version. For any other version returns short SHA1 hash and SNAPSHOT tag.
     *
     * @return string representation of the BlueLib version
     */
    public String getVersion() {
        String version = BuildConfig.VERSION_NAME;

        if (version.contains("SNAPSHOT")) {
            version = BuildConfig.HASH + "_" + version;
        }

        return version;
    }

    /**
     * Add a Device Definition to the {@link SHNDeviceDefinitions} managed by {@code SHNCentral}.
     * Typically a device definition has a one to one relation to a plugin. By registering the device
     * definition through this function, Bluelib gains support for devices managed by the plugin that
     * contains it.
     *
     * @param shnDeviceDefinitionInfo
     * @return
     */
    public boolean registerDeviceDefinition(SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
        return shnDeviceDefinitions.add(shnDeviceDefinitionInfo);
    }

    /**
     * Get the collection of currently registered device definitions.
     *
     * @return the collection of device definitions
     */
    public SHNDeviceDefinitions getSHNDeviceDefinitions() {
        return shnDeviceDefinitions;
    }

    /**
     * Register a {@Link SHNCentralListener}.
     *
     * @param shnCentralListener listener to register
     */
    public void registerShnCentralListener(SHNCentralListener shnCentralListener) {
        if (registeredShnCentralListeners == null) {
            registeredShnCentralListeners = new ArrayList<>();
        }
        if (!registeredShnCentralListeners.contains(shnCentralListener)) {
            registeredShnCentralListeners.add(shnCentralListener);
        }
    }

    /**
     * Unregister a {@Link SHNCentralListener}.
     *
     * @param shnCentralListener listener to unregister
     */
    public void unregisterShnCentralListener(SHNCentralListener shnCentralListener) {
        if (registeredShnCentralListeners != null) {
            registeredShnCentralListeners.remove(shnCentralListener);
        }
    }

    /**
     * Get the {@code SHNUserConfiguration} for this {@code SHNCentral}.
     *
     * @return the {@code SHNUserConfiguration} for this {@code SHNCentral}
     */
    @NonNull
    public SHNUserConfiguration getSHNUserConfiguration() {
        return shnUserConfiguration;
    }

    /**
     * Get the {@link SHNDeviceScanner} for this {@code SHNCentral}.
     *
     * @return the {@code SHNDeviceScanner} for this {@code SHNCentral}
     */
    public SHNDeviceScanner getShnDeviceScanner() {
        return shnDeviceScanner;
    }

    /**
     * Get the {@link SHNDeviceAssociation} for this {@code SHNCentral}.
     *
     * @return the {@code SHNDeviceAssociation} for this {@code SHNCentral}
     */
    @NonNull
    public SHNDeviceAssociation getShnDeviceAssociation() {
        if (shnDeviceAssociation == null) {
            shnDeviceAssociation = new SHNDeviceAssociation(this, shnDeviceScannerInternal, persistentStorageFactory);
            shnDeviceAssociation.initAssociatedDevicesListOnInternalThread();
        }
        return shnDeviceAssociation;
    }

    /**
     * Get the state of this {@code SHNCentral}.
     *
     * @return state of this {@code SHNCentral}
     */
    public State getShnCentralState() {
        return shnCentralState;
    }

    /**
     * Get the {@code BTDevice} with the specified address. This function is not intended for
     * application level code and will be removed from the interface in a future version.
     *
     * @param address to retrieve the device for
     * @return the BTDevice
     */
    public BTDevice getBTDevice(String address) {
        return btAdapter.getRemoteDevice(address);
    }

    private Map<String, SHNDevice> createdDevices = new HashMap<>();

    // TEMPORARY HACK TO ENABLE VERIFICATION TESTS WITH BLE SECURITY ENABLED
    // TODO: Remove this once the ShineVerificationApp uses DeviceAssociation.
    @Deprecated
    @VisibleForTesting
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

    /* package */ void removeDeviceFromDeviceCache(SHNDevice shnDeviceToRemove) {
        String key = shnDeviceToRemove.getAddress() + shnDeviceToRemove.getDeviceTypeName();
        createdDevices.remove(key);
    }

    /**
     * The {@code SHNCentral.Builder} is used to build a {@code SHNCentral} object.
     */
    public static class Builder {
        private Handler handler;
        private final Context context;
        private Boolean showPopupIfBLEIsTurnedOff = false;
        private Boolean migrateFromDefaultProviderToCustom = false;
        private SharedPreferencesProvider sharedPreferencesProvider;

        /**
         * Create a {@code SHNCentral.Builder}.
         *
         * @param context the {@code Context} in which the {@link SHNCentral} will be used
         */
        public Builder(@NonNull final Context context) {
            this.context = context;
        }

        /**
         * Add a handler to the {@link SHNCentral} you are currently building.
         * <p/>
         * This handler will be used to post callbacks from Bluelib on.
         *
         * @param handler a {@code Handler} to use for callbacks
         * @return {@code Builder} to chain more calls
         */
        public Builder setHandler(Handler handler) {
            this.handler = handler;
            return this;
        }

        /**
         * Shows a popup if for some reason Bluetooth was not enabled.
         *
         * @param showPopupIfBLEIsTurnedOff set to true if you want the popup to show
         * @return {@code Builder} to chain more calls
         */
        public Builder showPopupIfBLEIsTurnedOff(Boolean showPopupIfBLEIsTurnedOff) {
            this.showPopupIfBLEIsTurnedOff = showPopupIfBLEIsTurnedOff;
            return this;
        }

        /**
         * Set a custom SharedPreferencesProvider for {@link SHNCentral} to use.
         *
         * @param sharedPreferencesProvider custom SharedPreferencesProvider for {@link SHNCentral} to use
         * @return {@code Builder} to chain more calls
         */
        public Builder setSharedPreferencesProvider(SharedPreferencesProvider sharedPreferencesProvider) {
            this.sharedPreferencesProvider = sharedPreferencesProvider;
            return this;
        }

        /**
         * Set to true to indicate that data needs to be migrated.
         * <p/>
         * Data that was previously stored in SharedPreferences will be
         * migrated to the new custom {@link SharedPreferencesProvider} that was set using
         * {@link #setSharedPreferencesProvider(SharedPreferencesProvider)}.
         * If migration was previously completed to the custom SharedPreferencesProvider
         * it will not be done again.
         *
         * @param migrateFromDefaultProviderToCustom set to true to indicate migration is wanted
         * @return {@code Builder} to chain more calls
         */
        public Builder migrateFromDefaultProviderToCustom(Boolean migrateFromDefaultProviderToCustom) {
            this.migrateFromDefaultProviderToCustom = migrateFromDefaultProviderToCustom;
            return this;
        }

        /**
         * Finish this {@code Builder} by creating the actual {@code SHNCentral} using previously specified parameters.
         *
         * @return the created {@code SHNCentral}
         * @throws SHNBluetoothHardwareUnavailableException if no BlueTooth hardware was available on the device
         */
        public SHNCentral create() throws SHNBluetoothHardwareUnavailableException {
            return new SHNCentral(handler, context, showPopupIfBLEIsTurnedOff, sharedPreferencesProvider, migrateFromDefaultProviderToCustom);
        }
    }
}
