/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNIntegerResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNCharacteristicInfo;
import com.philips.pins.shinelib.framework.BleUUIDCreator;
import com.philips.pins.shinelib.framework.SHNFactory;
import com.philips.pins.shinelib.utility.SHNLogger;
import com.philips.pins.shinelib.utility.ScalarConverters;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 */
public class SHNServiceBattery implements SHNService.SHNServiceListener {

    public static final UUID SERVICE_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x180F));
    public static final UUID SYSTEM_BATTERY_LEVEL_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A19));

    private static final String TAG = SHNServiceBattery.class.getSimpleName();

    private SHNServiceBatteryListener shnServiceBatteryListener;

    public interface SHNServiceBatteryListener {
        void onBatteryLevelUpdated(int level);
    }

    public SHNService getShnService() {
        return shnService;
    }

    private SHNService shnService;

    protected SHNCharacteristic.SHNCharacteristicChangedListener shnCharacteristicChangedListener = new SHNCharacteristic.SHNCharacteristicChangedListener() {
        @Override
        public void onCharacteristicChanged(SHNCharacteristic shnCharacteristic, byte[] data) {
            if (shnCharacteristic.getUuid() == SYSTEM_BATTERY_LEVEL_CHARACTERISTIC_UUID) {
                ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
                int level = ScalarConverters.ubyteToInt(buffer.get());
                if (level < 100 && shnServiceBatteryListener != null) {
                    shnServiceBatteryListener.onBatteryLevelUpdated(level);
                }
            }
        }
    };

    public void setShnServiceBatteryListener(SHNServiceBatteryListener shnServiceBatteryListener) {
        this.shnServiceBatteryListener = shnServiceBatteryListener;
    }

    public SHNServiceBattery(SHNFactory shnFactory) {
        shnService = shnFactory.createNewSHNService(SERVICE_UUID, getRequiredCharacteristics(), getOptionalCharacteristics());
        shnService.registerSHNServiceListener(this);
    }

    private static Set<SHNCharacteristicInfo> getRequiredCharacteristics() {
        Set<SHNCharacteristicInfo> requiredCharacteristicUUIDs = new HashSet<>();
        requiredCharacteristicUUIDs.add(new SHNCharacteristicInfo(SYSTEM_BATTERY_LEVEL_CHARACTERISTIC_UUID, false));
        return requiredCharacteristicUUIDs;
    }

    private static Set<SHNCharacteristicInfo> getOptionalCharacteristics() {
        return new HashSet<>();
    }

    public void getBatteryLevel(final SHNIntegerResultListener listener) {
        SHNLogger.i(TAG, "getBatteryLevel");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(SYSTEM_BATTERY_LEVEL_CHARACTERISTIC_UUID);
        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(@NonNull SHNResult shnResult, byte[] data) {
                SHNLogger.i(TAG, "getBatteryLevel reportResult");
                int value = -1;
                if (shnResult == SHNResult.SHNOk) {
                    ByteBuffer buffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
                    value = ScalarConverters.ubyteToInt(buffer.get());
                    if (value > 100) {
                        shnResult = SHNResult.SHNErrorWhileParsing;
                        value = -1;
                    }
                }

                if (listener != null) {
                    listener.onActionCompleted(value, shnResult);
                }
            }
        };

        shnCharacteristic.read(resultReporter);
    }

    public void setBatteryLevelNotifications(boolean enabled, final SHNResultListener listener) {
        SHNLogger.i(TAG, "setBatteryLevelNotifications");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(SYSTEM_BATTERY_LEVEL_CHARACTERISTIC_UUID);

        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(@NonNull SHNResult shnResult, byte[] data) {
                SHNLogger.i(TAG, "setBatteryLevelNotifications reportResult");
                if (listener != null) {
                    listener.onActionCompleted(shnResult);
                }
            }
        };

        shnCharacteristic.setNotification(enabled, resultReporter);

        if (enabled) {
            shnCharacteristic.setShnCharacteristicChangedListener(shnCharacteristicChangedListener);
        } else {
            shnCharacteristic.setShnCharacteristicChangedListener(null);
        }
    }

    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        if (state == SHNService.State.Available) {
            shnService.transitionToReady();
        }
    }
}
