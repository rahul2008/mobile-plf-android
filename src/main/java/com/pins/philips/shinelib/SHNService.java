package com.pins.philips.shinelib;

/*
@startuml
participant App
participant SHNServiceUser
participant SHNDevice
participant SHNService
participant SHNCharacteristic
participant BLEDevice
participant BLEGatt
participant BLEGattCallback

group Setup
SHNServiceUser --> SHNService : create(UUID, reqCharUUIDS, optCharUUIDS)
    loop for each char
        SHNService --> SHNCharacteristic : create(UUID)
    end
    App --> SHNDevice : create(remoteAddress)
    SHNDevice --> BLEDevice : create(remoteAddress)
end
group Connect
    App -> SHNDevice : connect
    SHNDevice -> BLEDevice : connect
    BLEDevice --> BLEGatt : createdViaConnect
    BLEGattCallback -> SHNDevice : onConnectionStateChange(Connected)
    SHNDevice -> BLEGatt : discoverServices
    BLEGattCallback -> SHNDevice : onServicesDiscovered
    loop for each service
        SHNDevice -> SHNService : connectToBleService
        loop for each service.characteristic
            SHNDevice -> SHNService : connectToBleCharacteristic
            SHNService -> SHNCharacteristic : connectToBleCharacteristic
        end
        SHNService -> SHNServiceUser : onServiceStateChange(Available)
    end
    SHNServiceUser -> SHNService : upperLayerReady
    SHNService -> SHNServiceUser : onServiceStateChange(Ready)
end
group streamValue
    SHNServiceUser -> SHNCharacteristic : streamValue
    SHNService -> SHNDevice : setCharacteristicNotification(true)
    SHNDevice -> BLEDevice : setCharacteristicNotification(true)
    BLEGattCallback -> SHNDevice : onCharacteristicChanged
    SHNDevice -> SHNCharacteristic : onCharacteristicChanged
    SHNCharacteristic -> SHNServiceUser : onValueRead(value)
    SHNServiceUser -> SHNCharacteristic : unstreamValue
    SHNService -> SHNDevice : setCharacteristicNotification(false)
    SHNDevice -> BLEDevice : setCharacteristicNotification(false)
end
group readValue
    SHNServiceUser -> SHNCharacteristic : readValue
    SHNCharacteristic -> SHNDevice : readCharacteristic
    SHNDevice -> BLEDevice : readCharacteristic
    BLEGattCallback -> SHNDevice : onCharacteristicRead
    SHNDevice -> SHNCharacteristic : onCharacteristicRead
    SHNCharacteristic -> SHNServiceUser : onValueRead(value)
end
group Disconnect
    BLEGattCallback -> SHNDevice : onConnectionStateChange(Disconnected)
    SHNService -> SHNServiceUser : onServiceStateChange(Unavailable)
    loop for each service
        SHNDevice -> SHNService : disconnectService
        loop for each service.characteristic
            SHNService -> SHNCharacteristic : disconnectCharacteristic
        end
    end
end
@enduml
 */

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 26/03/15.
 */
public class SHNService {
    private static final String TAG = SHNService.class.getSimpleName();
    private static final boolean LOGGING = false;

    public enum State {Inactive, WaitingForUpperLayer, Active}

    public interface SHNServiceListener {
        void onServiceStateChanged(SHNService shnService, State state);
    }
    private State state = State.Inactive;
    private final UUID uuid;
    private final SHNDevice shnDevice;
    private WeakReference<BluetoothGattService> bluetoothGattServiceWeakReference;
    private List<SHNCharacteristic> requiredCharacteristics;
    private List<SHNCharacteristic> optionalCharacteristics;
    private Map<UUID, SHNCharacteristic> characteristicMap;
    private Set<SHNServiceListener> shnServiceListeners;

    public SHNService(SHNDevice shnDevice, UUID serviceUuid, Set<UUID> requiredCharacteristics, Set<UUID> optionalCharacteristics) {
        this.uuid = serviceUuid;
        this.requiredCharacteristics = new ArrayList<>();
        this.optionalCharacteristics = new ArrayList<>();
        this.characteristicMap = new HashMap<>();
        this.shnServiceListeners = new HashSet<>();
        this.shnDevice = shnDevice;

        for (UUID characteristicUUID: requiredCharacteristics) {
            SHNCharacteristic shnCharacteristic = new SHNCharacteristic(shnDevice, characteristicUUID);
            addRequiredSHNCharacteristic(shnCharacteristic);
        }
        for (UUID characteristicUUID: optionalCharacteristics) {
            SHNCharacteristic shnCharacteristic = new SHNCharacteristic(shnDevice, characteristicUUID);
            addOptionalSHNCharacteristic(shnCharacteristic);
        }
    }

    public State getState() {
        return state;
    }

    public boolean registerSHNServiceListener(SHNServiceListener shnServiceListener) {
        return shnServiceListeners.add(shnServiceListener);
    }

    public boolean unregisterSHNServiceListener(SHNServiceListener shnServiceListener) {
        return shnServiceListeners.remove(shnServiceListener);
    }

    public UUID getUuid() {
        return uuid;
    }

    public SHNCharacteristic getSHNCharacteristic(UUID characteristicUUID) {
        return characteristicMap.get(characteristicUUID);
    }

    public void addRequiredSHNCharacteristic(SHNCharacteristic shnCharacteristic) {
        characteristicMap.put(shnCharacteristic.getUuid(), shnCharacteristic);
        requiredCharacteristics.add(shnCharacteristic);
    }

    public void addOptionalSHNCharacteristic(SHNCharacteristic shnCharacteristic) {
        characteristicMap.put(shnCharacteristic.getUuid(), shnCharacteristic);
        optionalCharacteristics.add(shnCharacteristic);
    }

    private void updateState(State newState) {
        if (state != newState) {
            if (LOGGING) Log.i(TAG, "updateState for: " + getUuid() + " new state: " + newState);
            state = newState;
            for (SHNServiceListener shnServiceListener: shnServiceListeners) {
                shnServiceListener.onServiceStateChanged(this, state);
            }
        }
    }

    public void connectToBLELayer(BluetoothGattService bluetoothGattService) {
        bluetoothGattServiceWeakReference = new WeakReference<>(bluetoothGattService);
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic: bluetoothGattService.getCharacteristics()) {
            SHNCharacteristic shnCharacteristic = getSHNCharacteristic(bluetoothGattCharacteristic.getUuid());
            if (LOGGING) Log.i(TAG, "connectToBLELayer characteristic: " + bluetoothGattCharacteristic.getUuid() + ((shnCharacteristic == null) ? " not found" : " connecting"));
            if (shnCharacteristic != null) {
                shnCharacteristic.connectToBLELayer(bluetoothGattCharacteristic);
            }
        }

        // Check if the state should be updated
        State newState = State.WaitingForUpperLayer;
        for (SHNCharacteristic shnCharacteristic: requiredCharacteristics) {
            if (shnCharacteristic.getState() != SHNCharacteristic.State.Active) {
                newState = State.Inactive;
                break;
            }
        }
        updateState(newState);
    }

    public void upperLayerReady() {
        updateState(State.Active);
    }

    public void disconnectFromBLELayer() {
        bluetoothGattServiceWeakReference.clear();
        for (SHNCharacteristic shnCharacteristic: characteristicMap.values()) {
            shnCharacteristic.disconnectFromBLELayer();
        }
        updateState(State.Inactive);
    }
}
