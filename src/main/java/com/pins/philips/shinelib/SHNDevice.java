package com.pins.philips.shinelib;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.util.Log;

import com.pins.philips.shinelib.bluetoothwrapper.BTDevice;
import com.pins.philips.shinelib.bluetoothwrapper.BTGatt;
import com.pins.philips.shinelib.wrappers.SHNCapabilityWrapperFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDevice implements SHNService.SHNServiceListener {
    private static final String TAG = SHNDevice.class.getSimpleName();
    private static final boolean LOGGING = false;
    public static final long CONNECT_TIMEOUT = 10000l;
    private final BTDevice btDevice;
    private final Context applicationContext;
    private final SHNCentral shnCentral;
    private BTGatt btGatt;
    private Runnable connectTimer;
    private SHNDeviceListener shnDeviceListener;
    private SHNDeviceState shnDeviceState = SHNDeviceState.SHNDeviceStateDisconnected;
//    private String name;
//    private String type;
//    private UUID identifier;
//    private int rssiWhenDiscovered; // How is that usefull?


    public enum SHNDeviceState {
        SHNDeviceStateDisconnected, SHNDeviceStateDisconnecting, SHNDeviceStateConnecting, SHNDeviceStateConnected
    }
    public interface SHNDeviceListener {
        void onStateUpdated(SHNDevice shnDevice);
    }

    public SHNDevice(BTDevice btDevice, SHNCentral shnCentral) {
        this.shnDeviceState = SHNDeviceState.SHNDeviceStateDisconnected;
        this.btDevice = btDevice;
        this.shnCentral = shnCentral;
        this.applicationContext = shnCentral.getApplicationContext();
    }

    public SHNDeviceState getState() {
        return shnDeviceState;
    }

    public String getAddress() {
        return btDevice.getAddress();
    }
    public String getName() {
        return btDevice.getName();
    }

    public void connect()  {
        if (LOGGING) Log.i(TAG, "connect");
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                handleConnect();
            }
        };
        shnCentral.getInternalHandler().post(runnable);
        cancelConnectTimer();
    }

    private void cancelConnectTimer() {
        if (connectTimer != null) {
            shnCentral.getInternalHandler().removeCallbacks(connectTimer);
            connectTimer = null;
        }
    }

    private void handleConnect()  {
        if (LOGGING) Log.i(TAG, "handleConnect");
        if (shnDeviceState == SHNDeviceState.SHNDeviceStateDisconnected) {
            updateShnDeviceState(SHNDeviceState.SHNDeviceStateConnecting);
            btGatt = btDevice.connectGatt(applicationContext, false, btGattCallback);

            // Start a timer (refactor?)
            connectTimer = new Runnable() {
                @Override
                public void run() {
                    handleConnectTimeout();
                }
            };
            shnCentral.getInternalHandler().postDelayed(connectTimer, CONNECT_TIMEOUT);
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
        shnCentral.getInternalHandler().post(runnable);
    }
    private void handleDisconnect() {
        if (LOGGING) Log.e(TAG, "handleDisconnect");
        if (btGatt != null) {
            btGatt.disconnect();
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
        shnCapabilityWrapper = SHNCapabilityWrapperFactory.createCapabilityWrapper(shnCapability, shnCapabilityType, shnCentral.getInternalHandler(), shnCentral.getUserHandler());

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

    // SHNServiceListener callback
    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        if (LOGGING) Log.e(TAG, "onServiceStateChanged: " + shnService.getState() + " [" + shnService.getUuid() + "]");
        SHNDeviceState newState = SHNDeviceState.SHNDeviceStateConnected;
        for (SHNService service: registeredServices.values()) {
            if (service.getState() != SHNService.State.Ready) {
                newState = SHNDeviceState.SHNDeviceStateConnecting;
                break;
            }
        }
        updateShnDeviceState(newState);
    }

    @Override
    public String toString() {
        return btDevice.getName() + " [" + btDevice.getAddress() + "]";
    }

    private BTGatt.BTGattCallback btGattCallback = new BTGatt.BTGattCallback() {
        @Override
        public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
            if (LOGGING) Log.i(TAG, "handleOnConnectionStateChange");
            SHNDeviceState shnDeviceState = getState();
            if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                if (btGatt != null) {
                    btGatt.close();
                    btGatt = null;
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

        @Override
        public void onServicesDiscovered(BTGatt gatt, int status) {
            if (LOGGING) Log.i(TAG, "handleOnServicesDiscovered");
            if (status == BluetoothGatt.GATT_SUCCESS) {
                for (BluetoothGattService bluetoothGattService: btGatt.getServices()) {
                    SHNService shnService = getSHNService(bluetoothGattService.getUuid());
                    if (LOGGING) Log.i(TAG, "handleOnServicesDiscovered service: " + bluetoothGattService.getUuid() + ((shnService == null) ? " not found" : " connecting"));
                    if (shnService != null) {
                        shnService.connectToBLELayer(gatt, bluetoothGattService);
                    }
                }
            }
            else
            {
                // TODO
            }
        }

        @Override
        public void onCharacteristicReadWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status, byte[] data) {
            SHNService shnService = getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicReadWithData(gatt, characteristic, status, data);
        }

        @Override
        public void onCharacteristicWrite(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
            SHNService shnService = getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicWrite(gatt, characteristic, status);
        }

        @Override
        public void onCharacteristicChangedWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data) {
            SHNService shnService = getSHNService(characteristic.getService().getUuid());
            shnService.onCharacteristicChangedWithData(gatt, characteristic, data);
        }

        @Override
        public void onDescriptorReadWithData(BTGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
            SHNService shnService = getSHNService(descriptor.getCharacteristic().getService().getUuid());
            shnService.onDescriptorReadWithData(gatt, descriptor, status, data);
        }

        @Override
        public void onDescriptorWrite(BTGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            SHNService shnService = getSHNService(descriptor.getCharacteristic().getService().getUuid());
            shnService.onDescriptorWrite(gatt, descriptor, status);
        }

        @Override
        public void onReliableWriteCompleted(BTGatt gatt, int status) {
            throw new UnsupportedOperationException("onReliableWriteCompleted");
        }

        @Override
        public void onReadRemoteRssi(BTGatt gatt, int rssi, int status) {
            throw new UnsupportedOperationException("onReadRemoteRssi");
        }

        @Override
        public void onMtuChanged(BTGatt gatt, int mtu, int status) {
        }
    };
}
