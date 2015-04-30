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

import com.pins.philips.shinelib.capabilities.SHNCapabilityNotifications;
import com.pins.philips.shinelib.framework.BluetoothGattCallbackOnExecutor;
import com.pins.philips.shinelib.utility.Utilities;
import com.pins.philips.shinelib.wrappers.SHNCapabilityNotificationsWrapper;
import com.pins.philips.shinelib.wrappers.SHNCapabilityWrapperFactory;

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
    private static final boolean LOGGING = false;
    public static final long CONNECT_TIMEOUT = 10000l;
    public static final String CLIENT_CHARACTERISTIC_CONFIG_UUID = "00002902-0000-1000-8000-00805f9b34fb";
    private final BluetoothDevice bluetoothDevice;
    private final List<Runnable> bluetoothGattCommands;
    private final Context applicationContext;
    private final SHNCentral shnCentral;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCallback bluetoothGattCallback;
    private SHNGattCommandResultReporter currentResultListener;
    private ScheduledFuture<?> connectTimer;
    private boolean waitingForGattCallback;

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
        void reportResult(SHNResult shnResult);
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
        return new BluetoothGattCallbackOnExecutor(shnCentral.getScheduledThreadPoolExecutor(), new SHNBluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if (LOGGING) Log.i(TAG, "onConnectionStateChange");
                try {
                    handleOnConnectionStateChange(gatt, status, newState);
                } catch (Exception e) {
                    shnCentral.reportExceptionOnAppMainThread(e);
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                if (LOGGING) Log.i(TAG, "onServicesDiscovered");
                try {
                    handleOnServicesDiscovered(gatt, status);
                } catch (Exception e) {
                    shnCentral.reportExceptionOnAppMainThread(e);
                }
            }

            @Override
            public void onCharacteristicReadWithData(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status, byte[] data) {
                if (LOGGING) Log.i(TAG, "onCharacteristicRead");
                try {
                    handleOnCharacteristicRead(gatt, characteristic, status, data);
                } catch (Exception e) {
                    shnCentral.reportExceptionOnAppMainThread(e);
                }
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                if (LOGGING) Log.i(TAG, "onCharacteristicWrite: " + status);
                try {
                    handleOnCharacteristicWrite(gatt, characteristic, status);
                } catch (Exception e) {
                    shnCentral.reportExceptionOnAppMainThread(e);
                }
            }

            @Override
            public void onCharacteristicChangedWithData(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data) {
                if (LOGGING) Log.i(TAG, "onCharacteristicChanged");
                try {
                    handleOnCharacteristicChanged(gatt, characteristic, data);
                } catch (Exception e) {
                    shnCentral.reportExceptionOnAppMainThread(e);
                }
            }

            @Override
            public void onDescriptorReadWithData(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
                if (LOGGING) Log.i(TAG, "onDescriptorRead");
                try {
                    handleOnDescriptorRead(gatt, descriptor, status, data);
                } catch (Exception e) {
                    shnCentral.reportExceptionOnAppMainThread(e);
                }
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                if (LOGGING) Log.i(TAG, "onDescriptorWrite");
                try {
                    handleOnDescriptorWrite(gatt, descriptor, status);
                } catch (Exception e) {
                    shnCentral.reportExceptionOnAppMainThread(e);
                }
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                if (LOGGING) Log.i(TAG, "onReliableWriteCompleted");
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                if (LOGGING) Log.i(TAG, "onReadRemoteRssi");
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                if (LOGGING) Log.i(TAG, "onMtuChanged");
            }
        });
    }

    private void handleOnConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (LOGGING) Log.i(TAG, "handleOnConnectionStateChange");
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
        if (LOGGING) Log.i(TAG, "handleOnServicesDiscovered");
        if (status == BluetoothGatt.GATT_SUCCESS) {
            for (BluetoothGattService bluetoothGattService: bluetoothGatt.getServices()) {
                SHNService shnService = getSHNService(bluetoothGattService.getUuid());
                if (LOGGING) Log.i(TAG, "handleOnServicesDiscovered service: " + bluetoothGattService.getUuid() + ((shnService == null) ? " not found" : " connecting"));
                if (shnService != null) {
                    shnService.connectToBLELayer(bluetoothGattService);
                }
            }
        }
    }

    private void handleOnCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status, byte[] data) {
        if (LOGGING) Log.i(TAG, "handleOnCharacteristicRead");
        if (currentResultListener != null) {
            currentResultListener.reportResult(SHNResult.SHNOk); // TODO use the status and put the data in the result...
            currentResultListener = null;
        }
        waitingForGattCallback = false;
        executeNextBluetoothGattCommand();
    }

    private void handleOnCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        if (LOGGING) Log.i(TAG, "handleOnCharacteristicWrite");
        if (currentResultListener != null) {
            currentResultListener.reportResult(SHNResult.SHNOk); // TODO use the status
            currentResultListener = null;
        }
        waitingForGattCallback = false;
        executeNextBluetoothGattCommand();
    }

    private void handleOnCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data) {
        if (LOGGING) Log.i(TAG, "handleOnCharacteristicChanged");
        SHNService shnService = getSHNService(characteristic.getService().getUuid());
        SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(characteristic.getUuid());
        shnCharacteristic.onCharacteristicChanged(data);
    }

    private void handleOnDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        if (LOGGING) Log.i(TAG, "handleOnDescriptorWrite");
        if (currentResultListener != null) {
            currentResultListener.reportResult(SHNResult.SHNOk); // TODO use the status
            currentResultListener = null;
        }
        waitingForGattCallback = false;
        executeNextBluetoothGattCommand();
    }

    private void handleOnDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
        if (LOGGING) Log.i(TAG, "handleOnDescriptorRead");
        if (currentResultListener != null) {
            currentResultListener.reportResult(SHNResult.SHNOk); // TODO use the status
            currentResultListener = null;
        }
        waitingForGattCallback = false;
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
        if (LOGGING) Log.i(TAG, "connect");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handleConnect();
            }
        };
        shnCentral.getScheduledThreadPoolExecutor().execute(runnable);
        cancelConnectTimer();
    }

    private void cancelConnectTimer() {
        if (connectTimer != null) {
            connectTimer.cancel(false);
            connectTimer = null;
        }
    }

    private void handleConnect()  {
        if (LOGGING) Log.i(TAG, "handleConnect");
        if (shnDeviceState == SHNDeviceState.SHNDeviceStateDisconnected) {
            updateShnDeviceState(SHNDeviceState.SHNDeviceStateConnecting);
            bluetoothGatt = bluetoothDevice.connectGatt(applicationContext, false, bluetoothGattCallback);

            // Start a timer (refactor?)
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    handleConnectTimeout();
                }
            };
            connectTimer = shnCentral.getScheduledThreadPoolExecutor().schedule(runnable, CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
        }
    }

    private void handleConnectTimeout() {
        if (LOGGING) Log.e(TAG, "handleConnectTimeout");
        handleDisconnect();
    }

    public void disconnect() {
        if (LOGGING) Log.e(TAG, "disconnect");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handleDisconnect();
            }
        };
        shnCentral.getScheduledThreadPoolExecutor().execute(runnable);
    }
    private void handleDisconnect() {
        if (LOGGING) Log.e(TAG, "handleDisconnect");
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    private void updateShnDeviceState(SHNDeviceState newShnDeviceState) {
        if (shnDeviceState != newShnDeviceState) {
            if (shnDeviceState == SHNDeviceState.SHNDeviceStateConnecting) {
                cancelConnectTimer();
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
        if (registeredCapabilities.containsKey(shnCapabilityType)) {
            throw new IllegalStateException("Capability already registered");
        }

        SHNCapability shnCapabilityWrapper = null;
        shnCapabilityWrapper = SHNCapabilityWrapperFactory.createCapabilityWrapper(shnCapability, shnCapabilityType, shnCentral.getScheduledThreadPoolExecutor(), shnCentral.getUserHandler());

        registeredCapabilityTypes.add(shnCapabilityType);
        registeredCapabilities.put(shnCapabilityType, shnCapabilityWrapper);
    }


    private Map<UUID, SHNService> registeredServices = new HashMap<>();
    public void registerService(SHNService shnService) {
        registeredServices.put(shnService.getUuid(), shnService);
        shnService.registerSHNServiceListener(this);
    }
    private SHNService getSHNService(UUID serviceUUID) {
        return registeredServices.get(serviceUUID);
    }

    public boolean readCharacteristic(final BluetoothGattCharacteristic bluetoothGattCharacteristic, final SHNGattCommandResultReporter shnGattCommandResultReporter) {
        if (LOGGING) Log.i(TAG, "readCharacteristic");
        final Runnable readCharacteristicCommand = new Runnable() {
            @Override
            public void run() {
                if (LOGGING) Log.i(TAG, "Execute readCharacteristic");
                if (bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic)) {
                    currentResultListener = shnGattCommandResultReporter;
                    waitingForGattCallback = true;
                } else {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            shnGattCommandResultReporter.reportResult(SHNResult.SHNInvalidStateError);
                        }
                    };
                    shnCentral.runOnHandlerThread(runnable);
                    executeNextBluetoothGattCommand();
                }
            }
        };
        Runnable queueCommand = new Runnable() {
            @Override
            public void run() {
                bluetoothGattCommands.add(readCharacteristicCommand);
                executeNextBluetoothGattCommand();
            }
        };
        shnCentral.getScheduledThreadPoolExecutor().execute(queueCommand);
        return true;
    }

    public boolean writeCharacteristic(final byte[] data, final BluetoothGattCharacteristic bluetoothGattCharacteristic, final SHNGattCommandResultReporter shnGattCommandResultReporter) {
        if (LOGGING) Log.i(TAG, "writeCharacteristic");
        final Runnable writeCharacteristicCommand = new Runnable() {
            @Override
            public void run() {
                if (LOGGING) Log.i(TAG, "Execute writting characteristic value: " + Utilities.byteToString(data));
                bluetoothGattCharacteristic.setValue(data);
                if (bluetoothGatt.writeCharacteristic(bluetoothGattCharacteristic)) {
                    currentResultListener = shnGattCommandResultReporter;
                    waitingForGattCallback = true;
                } else {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            shnGattCommandResultReporter.reportResult(SHNResult.SHNInvalidStateError);
                        }
                    };
                    shnCentral.runOnHandlerThread(runnable);
                    executeNextBluetoothGattCommand();
                }
            }
        };
        Runnable queueCommand = new Runnable() {
            @Override
            public void run() {
                bluetoothGattCommands.add(writeCharacteristicCommand);
                executeNextBluetoothGattCommand();
            }
        };
        shnCentral.getScheduledThreadPoolExecutor().execute(queueCommand);
        return true;
    }

    public boolean setCharacteristicNotification(final BluetoothGattCharacteristic bluetoothGattCharacteristic, final boolean enable, final SHNGattCommandResultReporter shnGattCommandResultReporter) {
        if (LOGGING) Log.i(TAG, "setCharacteristicNotification enable: " + enable);
        final Runnable setCharacteristicNotificationCommand = new Runnable() {
            @Override
            public void run() {
                boolean result = bluetoothGatt.setCharacteristicNotification(bluetoothGattCharacteristic, enable);
                if (LOGGING) Log.i(TAG, "Execute setCharacteristicNotification enable: " + enable + " result: " + result + " charUUID: " + bluetoothGattCharacteristic.getUuid());
                if (result) {
                    BluetoothGattDescriptor descriptor = bluetoothGattCharacteristic.getDescriptor(UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG_UUID));
                    descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                    bluetoothGatt.writeDescriptor(descriptor);
                    waitingForGattCallback = true;
                } else {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            shnGattCommandResultReporter.reportResult(SHNResult.SHNInvalidStateError);
                        }
                    };
                    shnCentral.runOnHandlerThread(runnable);
                    executeNextBluetoothGattCommand();
                }
            }
        };
        final Runnable queueCommand = new Runnable() {
            @Override
            public void run() {
                bluetoothGattCommands.add(setCharacteristicNotificationCommand);
                executeNextBluetoothGattCommand();
            }
        };
        shnCentral.getScheduledThreadPoolExecutor().execute(queueCommand);
        return true;
    }

    private void executeNextBluetoothGattCommand() {
        if (!waitingForGattCallback && !bluetoothGattCommands.isEmpty()) {
            Runnable command = bluetoothGattCommands.remove(0);
            command.run();
        }
    }

    // SHNServiceListener callback
    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        if (LOGGING) Log.e(TAG, "onServiceStateChanged: " + shnService.getState() + " [" + shnService.getUuid() + "]");
        SHNDeviceState newState = SHNDeviceState.SHNDeviceStateConnected;
        for (SHNService service: registeredServices.values()) {
            if (service.getState() != SHNService.State.Active) {
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
