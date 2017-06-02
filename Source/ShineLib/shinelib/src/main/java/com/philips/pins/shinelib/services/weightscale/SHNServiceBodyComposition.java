/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services.weightscale;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNObjectResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNCharacteristicInfo;
import com.philips.pins.shinelib.framework.BleUUIDCreator;
import com.philips.pins.shinelib.framework.SHNFactory;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @publicPluginApi
 */
public class SHNServiceBodyComposition implements SHNService.SHNServiceListener, SHNCharacteristic.SHNCharacteristicChangedListener {

    private static final String TAG = SHNServiceBodyComposition.class.getSimpleName();

    public static final UUID BODY_COMPOSITION_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x181B));

    public static final UUID BODY_COMPOSITION_FEATURES_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A9B));
    public static final UUID BODY_COMPOSITION_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A9C));

    private SHNServiceBodyCompositionListener shnServiceBodyCompositionListener;

    public interface SHNServiceBodyCompositionListener {
        void onServiceStateChanged(SHNServiceBodyComposition shnServiceBodyComposition, SHNService.State state);

        void onBodyCompositionMeasurementReceived(SHNServiceBodyComposition shnServiceBodyComposition, SHNBodyCompositionMeasurement shnBodyCompositionMeasurement);
    }

    public SHNServiceBodyComposition(SHNFactory shnFactory) {
        shnService = shnFactory.createNewSHNService(BODY_COMPOSITION_UUID, getRequiredCharacteristics(), getOptionalCharacteristics());
        shnService.registerSHNServiceListener(this);
    }

    public SHNService getShnService() {
        return shnService;
    }

    private SHNService shnService;

    private static Set<SHNCharacteristicInfo> getRequiredCharacteristics() {
        Set<SHNCharacteristicInfo> requiredCharacteristicUUIDs = new HashSet<>();
        requiredCharacteristicUUIDs.add(new SHNCharacteristicInfo(BODY_COMPOSITION_FEATURES_CHARACTERISTIC_UUID, true));
        requiredCharacteristicUUIDs.add(new SHNCharacteristicInfo(BODY_COMPOSITION_MEASUREMENT_CHARACTERISTIC_UUID, true));
        return requiredCharacteristicUUIDs;
    }

    private static Set<SHNCharacteristicInfo> getOptionalCharacteristics() {
        return new HashSet<>();
    }

    public void setSHNServiceBodyCompositionListener(SHNServiceBodyCompositionListener shnServiceBodyCompositionListener) {
        this.shnServiceBodyCompositionListener = shnServiceBodyCompositionListener;
    }

    // implements SHNService.SHNServiceListener
    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        if (shnServiceBodyCompositionListener != null) {
            shnServiceBodyCompositionListener.onServiceStateChanged(this, state);
        }
        if (state == SHNService.State.Available) {
            shnService.transitionToReady();
        }
    }

    public void getFeatures(final SHNObjectResultListener listener) {
        if (SHNService.State.Available == shnService.getState()) {
            readFeatures(listener);
        } else {
            listener.onActionCompleted(null, SHNResult.SHNErrorServiceUnavailable);
        }
    }

    public void setNotificationsEnabled(boolean enabled, final SHNResultListener shnResultListener) {
        SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(BODY_COMPOSITION_MEASUREMENT_CHARACTERISTIC_UUID);
        SHNCommandResultReporter shnCommandResultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(@NonNull SHNResult shnResult, byte[] data) {
                shnResultListener.onActionCompleted(shnResult);
            }
        };
        shnCharacteristic.setIndication(enabled, shnCommandResultReporter);
    }

    //implements SHNCharacteristic.SHNCharacteristicChangedListener
    @Override
    public void onCharacteristicChanged(SHNCharacteristic shnCharacteristic, byte[] data) {
        if (shnCharacteristic.getUuid().equals(BODY_COMPOSITION_MEASUREMENT_CHARACTERISTIC_UUID)) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
            try {
                SHNBodyCompositionMeasurement shnBodyCompositionMeasurement = new SHNBodyCompositionMeasurement(byteBuffer);
                shnServiceBodyCompositionListener.onBodyCompositionMeasurementReceived(this, shnBodyCompositionMeasurement);
            } catch (IllegalArgumentException e) {
                SHNLogger.w(TAG, "Received incorrect body composition measurement data");
            } catch (BufferUnderflowException e) {
                SHNLogger.w(TAG, "The supplied data has the wrong length.");
            }
        }
    }

    private void readFeatures(final SHNObjectResultListener listener) {
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(BODY_COMPOSITION_FEATURES_CHARACTERISTIC_UUID);

        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(@NonNull SHNResult shnResult, byte[] data) {
                extractFeatures(shnResult, data, listener);
            }
        };

        shnCharacteristic.read(resultReporter);
    }

    private void extractFeatures(SHNResult shnResult, byte[] data, SHNObjectResultListener listener) {
        SHNBodyCompositionFeatures features = null;
        if (shnResult == SHNResult.SHNOk) {
            ByteBuffer byteBuffer = ByteBuffer.wrap(data);
            byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
            features = new SHNBodyCompositionFeatures(byteBuffer);
        }
        if (listener != null) {
            listener.onActionCompleted(features, shnResult);
        }
    }
}
