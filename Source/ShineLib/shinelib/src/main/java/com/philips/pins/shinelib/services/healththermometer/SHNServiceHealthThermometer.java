/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services.healththermometer;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.SHNTemperatureMeasurementIntervalResultListener;
import com.philips.pins.shinelib.SHNTemperatureMeasurementResultListener;
import com.philips.pins.shinelib.datatypes.SHNCharacteristicInfo;
import com.philips.pins.shinelib.framework.BleUUIDCreator;
import com.philips.pins.shinelib.framework.SHNFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @publicPluginApi
 */
public class SHNServiceHealthThermometer implements SHNService.SHNServiceListener, SHNCharacteristic.SHNCharacteristicChangedListener {
    private final static String TAG = SHNServiceHealthThermometer.class.getSimpleName();
    final static UUID SERVICE_HEALTH_THERMOMETER_UUID                = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x1809));
    final static UUID CHARACTERISTIC_TEMPERATURE_MEASUREMENT_UUID    = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A1C));
    final static UUID CHARACTERISTIC_TEMPERATURE_TYPE_UUID           = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A1D));
    final static UUID CHARACTERISTIC_INTERMEDIATE_TEMPERATURE_UUID   = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A1E));
    final static UUID CHARACTERISTIC_MEASUREMENT_INTERVAL_UUID       = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A21));

    private SHNService shnService;
    private SHNServiceHealthThermometerListener shnServiceHealthThermometerListener;

    public interface SHNServiceHealthThermometerListener {
        void onTemperatureMeasurementReceived(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNTemperatureMeasurement shnTemperatureMeasurement);
        void onServiceStateChanged(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNService.State state);
        void onIntermediateTemperatureReceived(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNTemperatureMeasurement shnTemperatureMeasurement);
        void onMeasurementIntervalChanged(SHNServiceHealthThermometer shnServiceHealthThermometer, SHNTemperatureMeasurementInterval shnTemperatureMeasurementInterval);
    }

    public SHNService getShnService(){
        return shnService;
    }

    public SHNServiceHealthThermometer(SHNFactory shnFactory) {
        shnService = shnFactory.createNewSHNService(SERVICE_HEALTH_THERMOMETER_UUID, getRequiredCharacteristics(), getOptionalCharacteristics());
        SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(CHARACTERISTIC_TEMPERATURE_MEASUREMENT_UUID);
        if (shnCharacteristic != null) {
            shnCharacteristic.setShnCharacteristicChangedListener(this);
        }
        shnService.registerSHNServiceListener(this);
    }

    private static Set<SHNCharacteristicInfo> getRequiredCharacteristics() {
        Set<SHNCharacteristicInfo> requiredCharacteristicUUIDs = new HashSet<>();

        requiredCharacteristicUUIDs.add(new SHNCharacteristicInfo(CHARACTERISTIC_TEMPERATURE_MEASUREMENT_UUID, true));

        return requiredCharacteristicUUIDs;
    }

    private static Set<SHNCharacteristicInfo> getOptionalCharacteristics() {
        Set<SHNCharacteristicInfo> optionalCharacteristicUUIDs = new HashSet<>();

        optionalCharacteristicUUIDs.add(new SHNCharacteristicInfo(CHARACTERISTIC_TEMPERATURE_TYPE_UUID, true));
        optionalCharacteristicUUIDs.add(new SHNCharacteristicInfo(CHARACTERISTIC_INTERMEDIATE_TEMPERATURE_UUID, true));
        optionalCharacteristicUUIDs.add(new SHNCharacteristicInfo(CHARACTERISTIC_MEASUREMENT_INTERVAL_UUID, true));

        return optionalCharacteristicUUIDs;
    }

    public void setSHNServiceHealthThermometerListener(SHNServiceHealthThermometerListener shnServiceHealthThermometerListener) {
        this.shnServiceHealthThermometerListener = shnServiceHealthThermometerListener;
    }

    private void setNotificationOnCharacteristic(UUID uuid, boolean enabled, final SHNResultListener shnResultListener) {
        SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(uuid);
        SHNCommandResultReporter shnCommandResultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(@NonNull SHNResult shnResult, byte[] data) {
                if(shnResultListener!=null) {
                    shnResultListener.onActionCompleted(shnResult);
                }
            }
        };
        shnCharacteristic.setIndication(enabled, shnCommandResultReporter);
    }

    public void setReceiveTemperatureMeasurements(boolean enabled, final SHNResultListener shnResultListener) {
        setNotificationOnCharacteristic(CHARACTERISTIC_TEMPERATURE_MEASUREMENT_UUID, enabled, shnResultListener);
    }

    public void setReceiveIntermediateTemperatures(boolean enabled, SHNResultListener shnResultListener) {
        setNotificationOnCharacteristic(CHARACTERISTIC_INTERMEDIATE_TEMPERATURE_UUID, enabled, shnResultListener);
    }

    public void setReceiveMeasurementIntervalChanges(boolean enabled, SHNResultListener shnResultListener) {
        setNotificationOnCharacteristic(CHARACTERISTIC_MEASUREMENT_INTERVAL_UUID, enabled, shnResultListener);
    }

    public void readTemperatureType(SHNTemperatureMeasurementResultListener shnTemperatureMeasurementResultListener) {
        throw new UnsupportedOperationException();
    }

    public void readMeasurementInterval(SHNTemperatureMeasurementIntervalResultListener shnTemperatureMeasurementIntervalResultListener) {
        throw new UnsupportedOperationException();
    }

    public void writeMeasurementInterval(SHNTemperatureMeasurementInterval shnTemperatureMeasurementInterval, SHNResultListener shnResultListener) {
        throw new UnsupportedOperationException();
    }

    // implements SHNService.SHNServiceListener
    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        if (shnServiceHealthThermometerListener != null) {
            shnServiceHealthThermometerListener.onServiceStateChanged(this, state);
        }

        if (state == SHNService.State.Available) {
            shnService.transitionToReady();
        }
    }

    // implements SHNCharacteristic.SHNCharacteristicChangedListener
    @Override
    public void onCharacteristicChanged(SHNCharacteristic shnCharacteristic, byte[] data) {
        if (shnCharacteristic.getUuid().equals(CHARACTERISTIC_TEMPERATURE_MEASUREMENT_UUID)) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
            try {
                SHNTemperatureMeasurement shnTemperatureMeasurement = new SHNTemperatureMeasurement(byteBuffer);
                shnServiceHealthThermometerListener.onTemperatureMeasurementReceived(this, shnTemperatureMeasurement);
            } catch(IllegalArgumentException e) {}
        }
        if (shnCharacteristic.getUuid().equals(CHARACTERISTIC_INTERMEDIATE_TEMPERATURE_UUID)) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
            try {
                SHNTemperatureMeasurement shnTemperatureMeasurement = new SHNTemperatureMeasurement(byteBuffer);
                shnServiceHealthThermometerListener.onIntermediateTemperatureReceived(this, shnTemperatureMeasurement);
            } catch(IllegalArgumentException e) {}
        }
    }
}
