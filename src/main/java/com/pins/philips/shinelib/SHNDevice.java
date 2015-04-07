package com.pins.philips.shinelib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import com.pins.philips.shinelib.framework.BluetoothGattCallbackOnExecutor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDevice implements SHNService.SHNServiceListener {
    private static final String TAG = SHNDevice.class.getSimpleName();
    private static final boolean LOGING = true;
    private final BluetoothDevice bluetoothDevice;
    private final List<Runnable> bluetoothGattCommands;
    private final Context applicationContext;
    private final SHNCentral shnCentral;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCallback bluetoothGattCallback;
    private SHNGattCommandResultReporter currentResultListener;
    private ScheduledFuture<?> connectTimer;

    public SHNDeviceState getState() {
        return shnDeviceState;
    }

    public enum SHNDeviceState {
        SHNDeviceStateDisconnected, SHNDeviceStateDisconnecting, SHNDeviceStateConnecting, SHNDeviceStateConnected
    }
    public interface SHNDeviceListener {
        void onStateUpdated(SHNDevice shnDevice);
    }
    public interface SHNGattCommandResultReporter {
        void reportResult();
    }

    public SHNDevice(BluetoothDevice bluetoothDevice, SHNCentral shnCentral) {
        this.shnDeviceState = SHNDeviceState.SHNDeviceStateDisconnected;
        this.bluetoothDevice = bluetoothDevice;
        this.shnCentral = shnCentral;
        this.applicationContext = shnCentral.getApplicationContext();
        this.bluetoothGattCallback = createBluetoothGattCallbackOnExecutor(shnCentral);
        bluetoothGattCommands = new LinkedList<>();
    }

    private BluetoothGattCallbackOnExecutor createBluetoothGattCallbackOnExecutor(final SHNCentral shnCentral) {
        return new BluetoothGattCallbackOnExecutor(shnCentral.getScheduledThreadPoolExecutor(), new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (LOGING) Log.i(TAG, "onConnectionStateChange");
                try {
                    handleOnConnectionStateChange(gatt, status, newState);
                } catch (Exception e) {
                    shnCentral.reportExceptionOnAppMainThread(e);
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (LOGING) Log.i(TAG, "onServicesDiscovered");
                try {
                    handleOnServicesDiscovered(gatt, status);
                } catch (Exception e) {
                    shnCentral.reportExceptionOnAppMainThread(e);
                }
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if (LOGING) Log.i(TAG, "onCharacteristicRead");
                try {
                    handleOnCharacteristicRead(gatt, characteristic, status);
                } catch (Exception e) {
                    shnCentral.reportExceptionOnAppMainThread(e);
                }
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if (LOGING) Log.i(TAG, "onCharacteristicWrite");
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                if (LOGING) Log.i(TAG, "onCharacteristicChanged");
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                if (LOGING) Log.i(TAG, "onDescriptorRead");
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                if (LOGING) Log.i(TAG, "onDescriptorWrite");
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                if (LOGING) Log.i(TAG, "onReliableWriteCompleted");
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                if (LOGING) Log.i(TAG, "onReadRemoteRssi");
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                if (LOGING) Log.i(TAG, "onMtuChanged");
            }
        });
    }

    private void handleOnConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (LOGING) Log.i(TAG, "handleOnConnectionStateChange");
        SHNDeviceState shnDeviceState = getState();
        if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            if (bluetoothGatt != null) {
                bluetoothGatt.close();
                bluetoothGatt = null;
            }
            for (SHNService shnService: registeredServices.values()) {
                shnService.disconnectFromBLELayer();
            }
            shnDeviceState = SHNDeviceState.SHNDeviceStateDisconnected;
        } else if (newState == BluetoothProfile.STATE_CONNECTED) {
            gatt.discoverServices();
        }
        updateShnDeviceState(shnDeviceState);
    }

    private void handleOnServicesDiscovered(BluetoothGatt gatt, int status) {
        if (LOGING) Log.i(TAG, "handleOnServicesDiscovered");
        if (status == BluetoothGatt.GATT_SUCCESS) {
            for (BluetoothGattService bluetoothGattService: bluetoothGatt.getServices()) {
                SHNService shnService = getSHNService(bluetoothGattService.getUuid());
                if (shnService != null) {
                    shnService.connectToBLELayer(bluetoothGattService);
                }
            }
        }
    }

    private void handleOnCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (LOGING) Log.i(TAG, "handleOnCharacteristicRead");
        currentResultListener.reportResult();
        currentResultListener = null;
        executeNextBluetoothGattCommand();
    }

    private SHNDeviceListener shnDeviceListener;
    private SHNDeviceState shnDeviceState = SHNDeviceState.SHNDeviceStateDisconnected;
    private String name;
    private String type;
    private UUID identifier;
    private int rssiWhenDiscovered; // How is that usefull?

    public String getAddress() {
        return bluetoothDevice.getAddress();
    }
    public String getName() {
        return bluetoothDevice.getName();
    }

    public void connect()  {
        if (LOGING) Log.i(TAG, "connect");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handleConnect();
            }
        };
        shnCentral.getScheduledThreadPoolExecutor().execute(runnable);
    }
    private void handleConnect()  {
        if (LOGING) Log.i(TAG, "handleConnect");
        if (shnDeviceState == SHNDeviceState.SHNDeviceStateDisconnected) {
            updateShnDeviceState(SHNDeviceState.SHNDeviceStateConnecting);
            bluetoothGatt = bluetoothDevice.connectGatt(applicationContext, false, bluetoothGattCallback);

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    handleConnectTimeout();
                }
            };
            connectTimer = shnCentral.getScheduledThreadPoolExecutor().schedule(runnable, 10000l, TimeUnit.MILLISECONDS);
        }
    }

    private void handleConnectTimeout() {
        if (LOGING) Log.e(TAG, "handleConnectTimeout");
    }

    public void disconnect() {
        if (LOGING) Log.e(TAG, "disconnect");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handleDisconnect();
            }
        };
        shnCentral.getScheduledThreadPoolExecutor().execute(runnable);
    }
    private void handleDisconnect() {
        if (LOGING) Log.e(TAG, "handleDisconnect");
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    private void updateShnDeviceState(SHNDeviceState newShnDeviceState) {
        if (shnDeviceState != newShnDeviceState) {
            if (shnDeviceState == SHNDeviceState.SHNDeviceStateConnecting) {
                connectTimer.cancel(false);
            }
            shnDeviceState = newShnDeviceState;
            informListeners();
        }
    }

    private void informListeners() {
        if (shnDeviceListener != null) {
            shnCentral.reportSHNDeviceUpdated(shnDeviceListener, this);
        }
    }

    public void setShnDeviceListener(SHNDeviceListener shnDeviceListener) {
        this.shnDeviceListener = shnDeviceListener;
        informListeners();
    }

    private Map<SHNCapabilityType, SHNCapability> registeredCapabilities = new HashMap<>();
    private Set<SHNCapabilityType> registeredCapabilityTypes = new HashSet<>();
    public Set<SHNCapabilityType> getSupportedCapabilityTypes() {
        return registeredCapabilityTypes;
    }
    public SHNCapability getCapabilityForType(SHNCapabilityType type) {
        return registeredCapabilities.get(type);
    }

    public void registerCapability(SHNCapability shnCapability, SHNCapabilityType shnCapabilityType) {
        registeredCapabilityTypes.add(shnCapabilityType);
        registeredCapabilities.put(shnCapabilityType, shnCapability);
    }

    private Map<UUID, SHNService> registeredServices = new HashMap<>();
    public void registerService(SHNService shnService) {
        registeredServices.put(shnService.getUuid(), shnService);
    }
    private SHNService getSHNService(UUID serviceUUID) {
        return registeredServices.get(serviceUUID);
    }

    public boolean readCharacteristic(final BluetoothGattCharacteristic bluetoothGattCharacteristic, final SHNGattCommandResultReporter shnGattCommandResultReporter) {
        if (LOGING) Log.i(TAG, "readCharacteristic");
        Runnable command = new Runnable() {
            @Override
            public void run() {
                bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
                currentResultListener = shnGattCommandResultReporter;
            }
        };
        bluetoothGattCommands.add(command);
        executeNextBluetoothGattCommand();
        return true;
    }

    public boolean setCharacteristicNotification(final BluetoothGattCharacteristic bluetoothGattCharacteristic, final boolean enable, final SHNGattCommandResultReporter shnGattCommandResultReporter) {
        if (LOGING) Log.i(TAG, "setCharacteristicNotification enable: " + enable);
        Runnable command = new Runnable() {
            @Override
            public void run() {
                boolean result = bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, enable);
                if (LOGING) Log.i(TAG, "setCharacteristicNotification enable: " + enable + " result: " + result + " charUUID: " + bluetoothGattCharacteristic.getUuid());
                BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
                descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                bluetoothGatt.writeDescriptor(descriptor);

//                currentResultListener = shnGattCommandResultReporter;
            }
        };
        bluetoothGattCommands.add(command);
        executeNextBluetoothGattCommand();
        return true;
    }

    private void executeNextBluetoothGattCommand() {
        if (currentResultListener == null && !bluetoothGattCommands.isEmpty()) {
            Runnable command = bluetoothGattCommands.remove(0);
            command.run();
        }
    }

    // SHNServiceListener callback
    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        if (LOGING) Log.e(TAG, "onServiceStateChanged");
        SHNDeviceState newState = SHNDeviceState.SHNDeviceStateConnected;
        for (SHNService service: registeredServices.values()) {
            if (service.getState() == SHNService.State.Inactive) {
                newState = SHNDeviceState.SHNDeviceStateConnecting;
                break;
            }
        }
        updateShnDeviceState(newState);
    }

    @Override
    public String toString() {
        return bluetoothDevice.getName() + " [" + bluetoothDevice.getAddress() + "]";
    }
}
