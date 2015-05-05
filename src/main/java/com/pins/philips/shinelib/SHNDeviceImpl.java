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
import com.pins.philips.shinelib.framework.Timer;
import com.pins.philips.shinelib.wrappers.SHNCapabilityWrapperFactory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 02/03/15.
 */
public class SHNDeviceImpl implements SHNService.SHNServiceListener, SHNDevice {
    private static final String TAG = SHNDeviceImpl.class.getSimpleName();
    private static final boolean LOGGING = false;
    public static final long CONNECT_TIMEOUT = 10000l;
    private final BTDevice btDevice;
    private final Context applicationContext;
    private final SHNCentral shnCentral;
    private BTGatt btGatt;
    private Timer connectTimer;
    private SHNDeviceListener shnDeviceListener;
    private State state = State.Disconnected;
//    private String name;
//    private String type;
//    private UUID identifier;
//    private int rssiWhenDiscovered; // How is that usefull?

    public SHNDeviceImpl(BTDevice btDevice, SHNCentral shnCentral) {
        this.state = State.Disconnected;
        this.btDevice = btDevice;
        this.shnCentral = shnCentral;
        this.applicationContext = shnCentral.getApplicationContext();
        this.connectTimer = new Timer(shnCentral.getInternalHandler(), new Runnable() {
            @Override
            public void run() {
                if (LOGGING) Log.e(TAG, "connect timeout");
                disconnect();
            }
        }, CONNECT_TIMEOUT);
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String getAddress() {
        return btDevice.getAddress();
    }
    @Override
    public String getName() {
        return btDevice.getName();
    }

    @Override
    public void connect()  {
        if (LOGGING) Log.i(TAG, "connect");
        if (state == State.Disconnected) {
            updateShnDeviceState(State.Connecting);
            btGatt = btDevice.connectGatt(applicationContext, false, btGattCallback);
            connectTimer.restart();
        }
    }

    @Override
    public void disconnect() {
        if (LOGGING) Log.e(TAG, "disconnect");
        state = State.Disconnecting;
        if (btGatt != null) {
            btGatt.disconnect();
        }
    }

    private void updateShnDeviceState(State newState) {
        if (state != newState) {
            if (state == State.Connecting) {
                connectTimer.stop();
            }
            state = newState;
            informListeners();
        }
    }

    private void informListeners() {
        if (shnDeviceListener != null) {
            shnDeviceListener.onStateUpdated(this);
        }
    }

    @Override
    public void setShnDeviceListener(SHNDeviceListener shnDeviceListener) {
        this.shnDeviceListener = shnDeviceListener;
    }

    private Map<SHNCapabilityType, SHNCapability> registeredCapabilities = new HashMap<>();
    private Set<SHNCapabilityType> registeredCapabilityTypes = new HashSet<>();
    @Override
    public Set<SHNCapabilityType> getSupportedCapabilityTypes() {
        return registeredCapabilityTypes;
    }
    @Override
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
        State newState = State.Connected;
        for (SHNService service: registeredServices.values()) {
            if (service.getState() != SHNService.State.Ready) {
                newState = State.Connecting;
                break;
            }
        }
        updateShnDeviceState(newState);
    }

    @Override
    public String toString() {
        return "SHNDevice - " + btDevice.getName() + " [" + btDevice.getAddress() + "]";
    }

    private BTGatt.BTGattCallback btGattCallback = new BTGatt.BTGattCallback() {
        @Override
        public void onConnectionStateChange(BTGatt gatt, int status, int newState) {
            if (LOGGING) Log.i(TAG, "handleOnConnectionStateChange");
            State state = getState();
            if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                if (btGatt != null) {
                    btGatt.close();
                    btGatt = null;
                }
                for (SHNService shnService: registeredServices.values()) {
                    shnService.disconnectFromBLELayer();
                }
                state = State.Disconnected;
            } else if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices();
            }
            updateShnDeviceState(state);
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
