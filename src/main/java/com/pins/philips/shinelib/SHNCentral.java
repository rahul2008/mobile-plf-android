package com.pins.philips.shinelib;

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
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;

import com.pins.philips.shinelib.bletestsupport.BTAdapter;
import com.pins.philips.shinelib.bletestsupport.BTDevice;
import com.pins.philips.shinelib.bletestsupport.BleUtilities;
import com.pins.philips.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

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
    private List<SHNDeviceDefinitionInfo> registeredDeviceDefinitions;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;
    private BTAdapter btAdapter;
    private final Handler internalHandler;

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

        registeredDeviceDefinitions = new ArrayList<>();
        shnDeviceScanner = new SHNDeviceScanner(this, getRegisteredDeviceDefinitions());

        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1); // An executor with one thread.
        internalHandler = new Handler(); // TODO: fix, use looper, thread, etc.
        btAdapter = new BTAdapter(applicationContext, internalHandler);
    }

    public Handler getUserHandler() {
        return userHandler;
    }

    public void shutdown() {
        scheduledThreadPoolExecutor.shutdown();
        applicationContext.unregisterReceiver(bluetoothBroadcastReceiver);
        shnDeviceScanner.shutdown();
        shnDeviceScanner = null;
    }

    public ScheduledThreadPoolExecutor getScheduledThreadPoolExecutor() {
        return scheduledThreadPoolExecutor;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public void runOnHandlerThread(Runnable runnable) {
        userHandler.post(runnable);
    }

    void reportSHNDeviceUpdated(final SHNDevice.SHNDeviceListener shnDeviceListener, final SHNDevice shnDevice) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                shnDeviceListener.onStateUpdated(shnDevice);
            }
        };
        userHandler.post(runnable);
    }

    public boolean isBluetoothAdapterEnabled() {
        return bluetoothAdapterEnabled;
    }

    public String getVersion() { throw new UnsupportedOperationException(); }
    public boolean registerDeviceDefinition(SHNDeviceDefinitionInfo shnDeviceDefinitionInfo) {
        return registeredDeviceDefinitions.add(shnDeviceDefinitionInfo);
    }

    public List<SHNDeviceDefinitionInfo> getRegisteredDeviceDefinitions() {
        return Collections.unmodifiableList(registeredDeviceDefinitions);
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
        return shnDeviceAssociation;
    }

    public SHNCentralState getShnCentralState() {
        return shnCentralState;
    }

    public void reportExceptionOnAppMainThread(Exception e) {
        Thread t = getApplicationContext().getMainLooper().getThread();
        t.getUncaughtExceptionHandler().uncaughtException(t, e);
    }

    public BTDevice getBTDevice(String address) {
        return btAdapter.getRemoteDevice(address);
    }
}
