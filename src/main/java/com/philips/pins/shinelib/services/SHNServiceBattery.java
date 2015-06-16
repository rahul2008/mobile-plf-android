package com.philips.pins.shinelib.services;

import android.util.Log;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.framework.BleUUIDCreator;
import com.philips.pins.shinelib.framework.SHNFactory;
import com.philips.pins.shinelib.utility.ScalarConverters;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNServiceBattery implements SHNService.SHNServiceListener{

    public static final String SERVICE_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x180F);
    public static final String SYSTEM_BATTERY_LEVEL_CHARACTERISTIC_UUID = BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A19);

    private static final String TAG = SHNServiceBattery.class.getSimpleName();
    private static final boolean LOGGING = false;

    public SHNService getShnService() {
        return shnService;
    }

    private SHNService shnService;

    public SHNServiceBattery(SHNFactory shnFactory) {
        shnService = shnFactory.createNewSHNService(UUID.fromString(SERVICE_UUID), getRequiredCharacteristics(), getOptionalCharacteristics());
        shnService.registerSHNServiceListener(this);
    }

    private static Set<UUID> getRequiredCharacteristics() {
        Set<UUID> requiredCharacteristicUUIDs = new HashSet<>();
        requiredCharacteristicUUIDs.add(UUID.fromString(SYSTEM_BATTERY_LEVEL_CHARACTERISTIC_UUID));
        return requiredCharacteristicUUIDs;
    }

    private static Set<UUID> getOptionalCharacteristics() {
        return new HashSet<>();
    }

    public void getBatteryLevel(final SHNIntegerResultListener listener) {
        if (LOGGING) Log.i(TAG, "getBatteryLevel");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(UUID.fromString(SYSTEM_BATTERY_LEVEL_CHARACTERISTIC_UUID));
        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "getBatteryLevel reportResult");
                int value = 0;
                if (shnResult == SHNResult.SHNOk) {
                    ByteBuffer buffer = ByteBuffer.wrap(data);
                    buffer.order(ByteOrder.LITTLE_ENDIAN);
                    value = ScalarConverters.ubyteToInt(buffer.get());
                }

                if (listener != null) {
                    listener.onActionCompleted(value, shnResult);
                }
            }
        };

        shnCharacteristic.read(resultReporter);
    }

    public void setBatteryLevelNotification(boolean enabled, final SHNResultListener listener) {
        if (LOGGING) Log.i(TAG, "setBatteryLevelNotification");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(UUID.fromString(SYSTEM_BATTERY_LEVEL_CHARACTERISTIC_UUID));

        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "setBatteryLevelNotification reportResult");
                if (listener != null) {
                    listener.onActionCompleted(shnResult);
                }
            }
        };

        shnCharacteristic.setNotification(enabled, resultReporter);

    }

    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        if(state == SHNService.State.Available){
            shnService.transitionToReady();
        }
    }
}