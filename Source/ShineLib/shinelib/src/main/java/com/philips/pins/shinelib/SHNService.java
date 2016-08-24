/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.support.annotation.Nullable;
import com.philips.pins.shinelib.bluetoothwrapper.BTGatt;
import com.philips.pins.shinelib.utility.SHNLogger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class SHNService {
    private static final String TAG = SHNService.class.getSimpleName();
    private State state = State.Unavailable;
    private final UUID uuid;
    private BTGatt btGatt;
    private WeakReference<BluetoothGattService> bluetoothGattServiceWeakReference;
    private List<SHNCharacteristic> requiredCharacteristics;
    private Map<UUID, SHNCharacteristic> characteristicMap;
    private Set<SHNServiceListener> shnServiceListeners;
    private Set<CharacteristicDiscoveryListener> characteristicDiscoveryListeners;

    public enum State {Unavailable, Available, Ready, Error}

    public interface SHNServiceListener {
        void onServiceStateChanged(SHNService shnService, State state);
    }

    public interface CharacteristicDiscoveryListener {
        void onCharacteristicDiscovered(UUID characteristicUuid, byte[] data, @Nullable SHNCharacteristic chracteristic);
    }

    public SHNService(UUID serviceUuid, Set<UUID> requiredCharacteristics, Set<UUID> optionalCharacteristics) {
        this.uuid = serviceUuid;
        this.requiredCharacteristics = new ArrayList<>();
        this.characteristicMap = new HashMap<>();
        this.shnServiceListeners = new HashSet<>();
        this.characteristicDiscoveryListeners = new HashSet<>();

        for (UUID characteristicUUID : requiredCharacteristics) {
            SHNCharacteristic shnCharacteristic = new SHNCharacteristic(characteristicUUID);
            addRequiredSHNCharacteristic(shnCharacteristic);
        }
        for (UUID characteristicUUID : optionalCharacteristics) {
            SHNCharacteristic shnCharacteristic = new SHNCharacteristic(characteristicUUID);
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

    public boolean registerCharacteristicDiscoveryListener(CharacteristicDiscoveryListener listener) {
        return characteristicDiscoveryListeners.add(listener);
    }

    public boolean unregisterCharacteristicDiscoverListener(CharacteristicDiscoveryListener listener) {
        return characteristicDiscoveryListeners.remove(listener);
    }

    public UUID getUuid() {
        return uuid;
    }

    @Nullable
    public SHNCharacteristic getSHNCharacteristic(UUID characteristicUUID) {
        return characteristicMap.get(characteristicUUID);
    }

    private void addRequiredSHNCharacteristic(SHNCharacteristic shnCharacteristic) {
        characteristicMap.put(shnCharacteristic.getUuid(), shnCharacteristic);
        requiredCharacteristics.add(shnCharacteristic);
    }

    private void addOptionalSHNCharacteristic(SHNCharacteristic shnCharacteristic) {
        characteristicMap.put(shnCharacteristic.getUuid(), shnCharacteristic);
    }

    private void updateState(State newState) {
        if (state != newState) {
            SHNLogger.i(TAG, "updateState for: " + getUuid() + " new state: " + newState);
            state = newState;
            for (SHNServiceListener shnServiceListener : shnServiceListeners) {
                shnServiceListener.onServiceStateChanged(this, state);
            }
        }
    }

    private void notifyDiscoveryListeners(BluetoothGattCharacteristic characteristic,
            SHNCharacteristic shnCharacteristic) {
        for (CharacteristicDiscoveryListener discoveryListener : characteristicDiscoveryListeners) {
            discoveryListener.onCharacteristicDiscovered(characteristic.getUuid(), characteristic.getValue(),
                    shnCharacteristic);
        }
    }

    /* package */ void connectToBLELayer(BTGatt gatt, BluetoothGattService bluetoothGattService) {
        bluetoothGattServiceWeakReference = new WeakReference<>(bluetoothGattService);
        this.btGatt = gatt;
        for (BluetoothGattCharacteristic bluetoothGattCharacteristic : bluetoothGattService.getCharacteristics()) {
            SHNCharacteristic shnCharacteristic = getSHNCharacteristic(bluetoothGattCharacteristic.getUuid());
            SHNLogger.i(TAG, "connectToBLELayer characteristic: " + bluetoothGattCharacteristic.getUuid() + (
                    (shnCharacteristic == null) ? " not found" : " connecting"));
            notifyDiscoveryListeners(bluetoothGattCharacteristic, shnCharacteristic);
            if (shnCharacteristic != null) {
                shnCharacteristic.connectToBLELayer(btGatt, bluetoothGattCharacteristic);
            }

        }

        // Check if the state should be updated
        State newState = State.Available;
        for (SHNCharacteristic shnCharacteristic : requiredCharacteristics) {
            if (shnCharacteristic.getState() != SHNCharacteristic.State.Active) {
                newState = State.Unavailable;
                break;
            }
        }
        updateState(newState);
    }

    /* package */ void disconnectFromBLELayer() {
        if (bluetoothGattServiceWeakReference != null) {
            bluetoothGattServiceWeakReference.clear();
        }
        btGatt = null;
        for (SHNCharacteristic shnCharacteristic : characteristicMap.values()) {
            shnCharacteristic.disconnectFromBLELayer();
        }
        updateState(State.Unavailable);
    }

    public void transitionToReady() {
        updateState(State.Ready);
    }

    public void transitionToError() {
        updateState(State.Error);
    }

    public void onCharacteristicReadWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status,
            byte[] data) {
        SHNCharacteristic shnCharacteristic = getSHNCharacteristic(characteristic.getUuid());
        shnCharacteristic.onReadWithData(gatt, status, data);
    }

    public void onCharacteristicWrite(BTGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        SHNCharacteristic shnCharacteristic = getSHNCharacteristic(characteristic.getUuid());
        shnCharacteristic.onWrite(gatt, status);
    }

    public void onCharacteristicChangedWithData(BTGatt gatt, BluetoothGattCharacteristic characteristic, byte[] data) {
        SHNCharacteristic shnCharacteristic = getSHNCharacteristic(characteristic.getUuid());
        shnCharacteristic.onChanged(gatt, data);
    }

    public void onDescriptorReadWithData(BTGatt gatt, BluetoothGattDescriptor descriptor, int status, byte[] data) {
        SHNCharacteristic shnCharacteristic = getSHNCharacteristic(descriptor.getCharacteristic().getUuid());
        shnCharacteristic.onDescriptorReadWithData(gatt, descriptor, status, data);
    }

    public void onDescriptorWrite(BTGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        SHNCharacteristic shnCharacteristic = getSHNCharacteristic(descriptor.getCharacteristic().getUuid());
        shnCharacteristic.onDescriptorWrite(gatt, descriptor, status);
    }
}
