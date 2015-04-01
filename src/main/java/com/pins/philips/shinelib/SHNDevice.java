package com.pins.philips.shinelib;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;

import com.pins.philips.shinelib.framework.BluetoothGattCallbackOnExecutor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDevice implements SHNService.SHNServiceListener {
    private static final String TAG = SHNDevice.class.getSimpleName();
    private final BluetoothDevice bluetoothDevice;

    private final Context applicationContext;
    private final SHNCentral shnCentral;
    private BluetoothGatt bluetoothGatt;
    private BluetoothGattCallback bluetoothGattCallback;

    public SHNDeviceState getState() {
        return shnDeviceState;
    }

    public boolean readCharacteristic(BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return bluetoothGatt.readCharacteristic(bluetoothGattCharacteristic);
    }

    public enum SHNDeviceState {
        SHNDeviceStateDisconnected, SHNDeviceStateDisconnecting, SHNDeviceStateConnecting, SHNDeviceStateConnected
    }
    public interface SHNDeviceListener {
        public void onStateUpdated(SHNDevice shnDevice);
    }

    public SHNDevice(BluetoothDevice bluetoothDevice, SHNDeviceDefinitionInfo shnDeviceDefinitionInfo, SHNCentral shnCentral) {
        this.shnDeviceState = SHNDeviceState.SHNDeviceStateDisconnected;
        this.bluetoothDevice = bluetoothDevice;
        this.shnCentral = shnCentral;
        this.applicationContext = shnCentral.getApplicationContext();
        this.bluetoothGattCallback = createBluetoothGattCallbackOnExecutor(shnCentral);
    }

    private BluetoothGattCallbackOnExecutor createBluetoothGattCallbackOnExecutor(final SHNCentral shnCentral) {
        return new BluetoothGattCallbackOnExecutor(shnCentral.getScheduledThreadPoolExecutor(), new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                try {
                    handleOnConnectionStateChange(gatt, status, newState);
                } catch (Exception e) {
                    shnCentral.reportExceptionOnAppMainThread(e, shnCentral);
                }
            }

            @Override
            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                try {
                    handleOnServicesDiscovered(gatt, status);
                } catch (Exception e) {
                    shnCentral.reportExceptionOnAppMainThread(e, shnCentral);
                }
            }

            @Override
            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                try {
                    handleOnCharacteristicRead(gatt, characteristic, status);
                } catch (Exception e) {
                    shnCentral.reportExceptionOnAppMainThread(e, shnCentral);
                }
            }

            @Override
            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            }

            @Override
            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
            }

            @Override
            public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            }

            @Override
            public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            }

            @Override
            public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            }

            @Override
            public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            }

            @Override
            public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            }
        });
    }

    private void handleOnConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
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
        if (status == BluetoothGatt.GATT_SUCCESS) {
            for (BluetoothGattService bluetoothGattService: bluetoothGatt.getServices()) {
                SHNService shnService = getSHNService(bluetoothGattService.getUuid());
                if (shnService != null) {
                    shnService.connectToBLELayer(bluetoothGattService);
                }
            }
        }
    }

    public void handleOnCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
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
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handleConnect();
            }
        };
        shnCentral.getScheduledThreadPoolExecutor().execute(runnable);
    }
    private void handleConnect()  {
        if (shnDeviceState == SHNDeviceState.SHNDeviceStateDisconnected) {
            updateShnDeviceState(SHNDeviceState.SHNDeviceStateConnecting);
            bluetoothGatt = bluetoothDevice.connectGatt(applicationContext, false, bluetoothGattCallback);
        }
    }

    public void disconnect() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handleDisconnect();
            }
        };
        shnCentral.getScheduledThreadPoolExecutor().execute(runnable);
    }
    private void handleDisconnect() {
        if (bluetoothGatt != null) {
            bluetoothGatt.disconnect();
        }
    }

    private void updateShnDeviceState(SHNDeviceState newShnDeviceState) {
        if (shnDeviceState != newShnDeviceState) {
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

    // SHNServiceListener callback
    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
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
