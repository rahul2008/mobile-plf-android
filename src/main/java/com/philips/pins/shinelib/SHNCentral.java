package com.philips.pins.shinelib;

/*
 * @startuml
 * title Scanning for registered devices
 * App -> SomeDeviceShinePlugin : getDeviceInfo
 * App -> SHNCentral : registerDeviceInfo
 * App -> SHNCentral : getShnDeviceScanner
 * App -> SHNDeviceScanner : startScanning
 * SHNDeviceScanner -> SHNCentral : getRegisteredDeviceDefinitions
 * SHNDeviceScanner -> BluetoothAdapter : startLeScan
 * BluetoothAdapter -> SHNDeviceScanner : onLeScan
 * SHNDeviceScanner -> EventDispatcher : queueDeviceFoundEvent
 * EventDispatcher -> SHNDeviceScanner : handleDeviceFoundEvent
 * SHNDeviceScanner -> SHNCentral :  reportDeviceFound
 * @enduml
@startuml
class SHNDICapabilityImpl
interface SHNDeviceListener {
    + void onDeviceStateChanged()
}
interface SHNServiceListener {
    + void onServiceStateChanged()
}
interface SHNCharacteristicListener {
    + void onCharacteristicNotificationStateChanged()
    + void OnValueUpdated()
}
interface SHNCapability
class BLEDevice
class BLEService
class BLECharacteristic
class SHNDevice
interface SHNDICapability {
    + read(SHNDIType type, SHNDIReadListener completion)
}
class SHNDIService {
    + boolean read(SHNDIType type, SHNDIReadListener compl)
}

class SHNCharacteristic {
    + void read(CompletionListener completion)
    + void onCharacteristicRead(BLECharacteristic char, int status, CompletionListener compl)
}

class SHNService {
    + void read(SHNCharacteristic char, CompletionListener compl)
    + void onCharacteristicRead(BLECharacteristic char, int status, CompletionListener compl)
}

class SHNDevice {
    + void read(SHNCharacteristic char, CompletionListener compl)
    + void onCharacteristicRead(BLECharacteristic char, int status)
}

SHNCapability <|-- SHNDICapability
SHNCharacteristic "1" o-- "1" BLECharacteristic
SHNCharacteristic -right-> SHNCharacteristicListener : uses
SHNDevice -left-> SHNDeviceListener : uses
SHNDevice "1" o-- "1" BLEDevice
SHNDevice *-- SHNService
SHNDevice "1" *-- "1" SHNDIDeviceDefinition
SHNDeviceDefinition <|-- SHNDIDeviceDefinition
SHNDICapability <|-- SHNDICapabilityImpl
SHNDICapabilityImpl "1" o-- "1" SHNDIService
SHNDIDeviceDefinition *-- SHNDICapabilityImpl
SHNDIDeviceDefinition *-- SHNDIService
SHNDIService --|> SHNServiceListener
SHNDIService "1" *-- "1" SHNService
SHNDIService "1" *-- "*" SHNCharacteristic
SHNDIService --|> SHNCharacteristicListener
SHNService *-right- SHNCharacteristic
SHNService "1" o-- "1" BLEService
SHNServiceListener <-left- SHNService
@enduml
 */

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.philips.pins.shinelib.bluetoothwrapper.BTAdapter;
import com.philips.pins.shinelib.bluetoothwrapper.BTDevice;
import com.philips.pins.shinelib.bluetoothwrapper.BleUtilities;
import com.philips.pins.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.philips.pins.shinelib.utility.ShinePreferenceWrapper;
import com.philips.pins.shinelib.framework.Timer;
import com.philips.pins.shinelib.wrappers.SHNDeviceWrapper;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNCentral {
    private static final String TAG = SHNCentral.class.getSimpleName();

    public enum SHNCentralState {
        SHNCentralStateError, SHNCentralStateNotReady, SHNCentralStateReady
    }
    public interface SHNCentralListener {
        void onStateUpdated(SHNCentral shnCentral);
    }

    private final Handler userHandler;
    private final Context applicationContext;
    private boolean bluetoothAdapterEnabled;
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
                        break;
                    case BluetoothAdapter.STATE_ON:
                        bluetoothAdapterEnabled = true;
                        break;
                }
            }
        }
    };
    private SHNCentralListener shnCentralListener;
    private SHNDeviceScanner shnDeviceScanner;
    private SHNDeviceAssociation shnDeviceAssociation;
    private SHNCentralState shnCentralState = SHNCentralState.SHNCentralStateError;
    private BTAdapter btAdapter;
    private Handler internalHandler;
    private SHNDeviceDefinitions shnDeviceDefinitions;
    private ShinePreferenceWrapper shinePreferenceWrapper;

    public SHNCentral(Handler handler, Context context) throws SHNBluetoothHardwareUnavailableException {
        applicationContext = context.getApplicationContext();
        BleUtilities.init(applicationContext);

        // The handler is used for callbacks to the usercode. When no handler is provided, the MainLoop a.k.a. UI Thread is used.
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        this.userHandler = handler;

        // Check that the device supports BLE.
        if (!BleUtilities.deviceHasBle()) {
            throw new SHNBluetoothHardwareUnavailableException();
        }

        // Check that the adapter is enabled.
        if (!(bluetoothAdapterEnabled = BleUtilities.isBluetoothAdapterEnabled())) {
            BleUtilities.startEnableBluetoothActivity();
        }

        // Register a broadcast receiver listening for BluetoothAdapter state changes
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        applicationContext.registerReceiver(bluetoothBroadcastReceiver, filter);

        setupBondStatusListener();

        shnDeviceDefinitions = new SHNDeviceDefinitions();
        shnDeviceScanner = new SHNDeviceScanner(this, shnDeviceDefinitions.getRegisteredDeviceDefinitions());

        HandlerThread thread = new HandlerThread("InternalShineLibraryThread");
        thread.start();
        try {
            internalHandler = new Handler(thread.getLooper());
            Timer.setHandler(internalHandler);
        } catch (RuntimeException e) {
            // Added for testing support. The HandlerThread is not mocked in the mockedAndroidJar :-(
        }
        SHNDeviceWrapper.setHandlers(internalHandler, userHandler);

        btAdapter = new BTAdapter(applicationContext, internalHandler);

        shinePreferenceWrapper = new ShinePreferenceWrapper(applicationContext);
    }


    private Map<String, WeakReference<SHNBondStatusListener>> shnBondStatusListeners = new HashMap<>();
    public interface SHNBondStatusListener {
        void onBondStatusChanged(BluetoothDevice device, int bondState, int previousBondState);
    }

    /* package */ void registerBondStatusListenerForAddress(SHNBondStatusListener shnBondStatusListener, String address) {
        shnBondStatusListeners.put(address, new WeakReference<SHNBondStatusListener>(shnBondStatusListener));
    }

    /* package */ void unregisterBondStatusListenerForAddress(SHNBondStatusListener shnBondStatusListener, String address) {
        shnBondStatusListeners.remove(address);
    }

    private void setupBondStatusListener() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        BroadcastReceiver searchDevices = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle  = intent.getExtras();
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
        };
        getApplicationContext().registerReceiver(searchDevices, intentFilter);
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
        shnDeviceScanner.shutdown();
        shnDeviceScanner = null;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public void runOnUserHandlerThread(Runnable runnable) {
        userHandler.post(runnable);
    }

    public boolean isBluetoothAdapterEnabled() {
        return bluetoothAdapterEnabled;
    }

    public String getVersion() { throw new UnsupportedOperationException(); }

    public boolean registerDeviceDefinition(SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
        return shnDeviceDefinitions.add(shnDeviceDefinitionInfo);
    }

    public SHNDeviceDefinitions getSHNDeviceDefinitions() {
        return shnDeviceDefinitions;
    }

    // Getters and setters
    public SHNCentralListener getShnCentralListener() {
        return shnCentralListener;
    }

    public void setShnCentralListener(SHNCentralListener shnCentralListener) {
        this.shnCentralListener = shnCentralListener;
    }

    public SHNDeviceScanner getShnDeviceScanner() {
        return shnDeviceScanner;
    }

    public SHNDeviceAssociation getShnDeviceAssociation() {
        if (shnDeviceAssociation == null) {
            shnDeviceAssociation = new SHNDeviceAssociation(this);
        }
        return shnDeviceAssociation;
    }

    public SHNCentralState getShnCentralState() {
        return shnCentralState;
    }

    public BTDevice getBTDevice(String address) {
        return btAdapter.getRemoteDevice(address);
    }

    public ShinePreferenceWrapper getShinePreferenceWrapper() {
        return shinePreferenceWrapper;
    }

    public boolean startScanningForDevices(Collection<UUID> serviceUUIDs, SHNDeviceScanner.ScannerSettingDuplicates scannerSettingDuplicates, SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener) {
        return shnDeviceScanner.startScanning(shnDeviceScannerListener, scannerSettingDuplicates, 90000l);
    }

    public void stopScanning() {
        shnDeviceScanner.stopScanning();
    }

    public SHNDevice creatSHNDeviceForAddress(String deviceAddress, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
        SHNDevice shnDevice = shnDeviceDefinitionInfo.getSHNDeviceDefinition().createDeviceFromDeviceAddress(deviceAddress, shnDeviceDefinitionInfo, this);
        return shnDevice;
    }
}
