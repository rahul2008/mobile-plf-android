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
 */

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;

import com.pins.philips.shinelib.bletestsupport.BleUtilities;
import com.pins.philips.shinelib.exceptions.SHNBluetoothHardwareUnavailableException;
import com.pins.philips.shinelib.framework.SingleThreadEventDispatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNCentral {
    private final Handler handler;
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
    private SingleThreadEventDispatcher singleThreadEventDispatcher;
    public SingleThreadEventDispatcher getEventDispatcher() {
        return singleThreadEventDispatcher;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public enum SHNCentralState {
        SHNCentralStateError, SHNCentralStateNotReady, SHNCentralStateReady
    }
    public interface SHNCentralListener {
        public void onStateUpdated(SHNCentral shnCentral);
    }

    public SHNCentral(Handler handler, Context context) throws SHNBluetoothHardwareUnavailableException {
        applicationContext = context.getApplicationContext();
        BleUtilities.init(applicationContext);

        // The handler is used for callbacks to the usercode. When no handler is provided, the MainLoop a.k.a. UI Thread is used.
        if (handler == null) {
            handler = new Handler(Looper.getMainLooper());
        }
        this.handler = handler;

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
        singleThreadEventDispatcher = new SingleThreadEventDispatcher();
        singleThreadEventDispatcher.start();
        shnDeviceScanner = new SHNDeviceScanner(this);
    }

    public void shutdown() {
        singleThreadEventDispatcher.stop();
        applicationContext.unregisterReceiver(bluetoothBroadcastReceiver);
        shnDeviceScanner.shutdown();
        shnDeviceScanner = null;
    }

    void reportDeviceFound(final SHNDeviceScanner.SHNDeviceScannerListener shnDeviceScannerListener, final SHNDeviceScanner shnDeviceScanner, final SHNDevice shnDevice) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                shnDeviceScannerListener.deviceFound(shnDeviceScanner, shnDevice);
            }
        };
        handler.post(runnable);
    }

    void reportSHNDeviceUpdated(final SHNDevice.SHNDeviceListener shnDeviceListener, final SHNDevice shnDevice) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                shnDeviceListener.onStateUpdated(shnDevice);
            }
        };
        handler.post(runnable);
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

}
