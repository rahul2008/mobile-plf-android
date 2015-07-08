package com.philips.pins.shinelib.services.weightscale;

import android.util.Log;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNObjectResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNDataWeightMeasurement;
import com.philips.pins.shinelib.framework.BleUUIDCreator;
import com.philips.pins.shinelib.framework.SHNFactory;
import com.philips.pins.shinelib.services.SHNServiceBattery;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNServiceWeightScale implements SHNService.SHNServiceListener, SHNCharacteristic.SHNCharacteristicChangedListener {
    public static final UUID WEIGHT_SCALE_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x181D));

    public static final UUID WEIGHT_SCALE_FEATURE_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A9E));
    public static final UUID WEIGHT_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A9D));

    private static final String TAG = SHNServiceBattery.class.getSimpleName();
    private static final boolean LOGGING = false;

    private SHNServiceWeightScaleListener shnServiceWeightScaleListener;

    public interface SHNServiceWeightScaleListener {
        void onServiceStateChanged(SHNServiceWeightScale shnServiceWeightScale, SHNService.State state);
        void onWeightMeasurementReceived(SHNServiceWeightScale shnServiceWeightScale, SHNDataWeightMeasurement shnDataWeightMeasurement);
    }

    public SHNServiceWeightScale(SHNFactory shnFactory) {
        shnService = shnFactory.createNewSHNService(WEIGHT_SCALE_UUID, getRequiredCharacteristics(), getOptionalCharacteristics());
        shnService.registerSHNServiceListener(this);
    }

    public SHNService getShnService() {
        return shnService;
    }

    private SHNService shnService;

    private static Set<UUID> getRequiredCharacteristics() {
        Set<UUID> requiredCharacteristicUUIDs = new HashSet<>();
        requiredCharacteristicUUIDs.add(WEIGHT_SCALE_FEATURE_CHARACTERISTIC_UUID);
        requiredCharacteristicUUIDs.add(WEIGHT_MEASUREMENT_CHARACTERISTIC_UUID);
        return requiredCharacteristicUUIDs;
    }

    private static Set<UUID> getOptionalCharacteristics() {
        return new HashSet<>();
    }

    public void setSHNServiceWeightScaleListener(SHNServiceWeightScaleListener shnServiceWeightScaleListener) {
        this.shnServiceWeightScaleListener = shnServiceWeightScaleListener;
    }

    // implements SHNService.SHNServiceListener
    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        if(shnServiceWeightScaleListener !=null){
            shnServiceWeightScaleListener.onServiceStateChanged(this, state);
        }
        if (state == SHNService.State.Available) {
            shnService.transitionToReady();
        }
    }

    public void readFeatures(final SHNObjectResultListener listener){
        if (LOGGING) Log.i(TAG, "readFeatures");
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(WEIGHT_SCALE_FEATURE_CHARACTERISTIC_UUID);

        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                if (LOGGING) Log.i(TAG, "readFeatures reportResult");
                SHNWeightScaleFeatures features = null;
                if (shnResult == SHNResult.SHNOk) {
                    ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                    byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                    features = new SHNWeightScaleFeatures(byteBuffer);
                }
                if (listener != null) {
                    listener.onActionCompleted(features, shnResult);
                }
            }
        };

        shnCharacteristic.read(resultReporter);
    }

    public void setNotificationsEnabled(boolean enabled, final SHNResultListener shnResultListener){
        SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(WEIGHT_MEASUREMENT_CHARACTERISTIC_UUID);
        SHNCommandResultReporter shnCommandResultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                shnResultListener.onActionCompleted(shnResult);
            }
        };
        shnCharacteristic.setNotification(enabled, shnCommandResultReporter);
    }

    //implements SHNCharacteristic.SHNCharacteristicChangedListener
    @Override
    public void onCharacteristicChanged(SHNCharacteristic shnCharacteristic, byte[] data) {
        if (shnCharacteristic.getUuid().equals(WEIGHT_MEASUREMENT_CHARACTERISTIC_UUID)) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
            try {
                SHNDataWeightMeasurement shnDataWeightMeasurement = new SHNDataWeightMeasurement(byteBuffer);
                shnServiceWeightScaleListener.onWeightMeasurementReceived(this, shnDataWeightMeasurement);
            } catch(IllegalArgumentException e) {}
        }
    }
}
