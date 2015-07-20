package com.philips.pins.shinelib.services.weightscale;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNObjectResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.framework.BleUUIDCreator;
import com.philips.pins.shinelib.framework.SHNFactory;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class SHNServiceBodyComposition implements SHNService.SHNServiceListener, SHNCharacteristic.SHNCharacteristicChangedListener {
    public static final UUID BODY_COMPOSITION_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x181B));

    public static final UUID BODY_COMPOSITION_FEATURES_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A9B));
    public static final UUID BODY_COMPOSITION_MEASUREMENT_CHARACTERISTIC_UUID = UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A9C));

    private SHNServiceBodyCompositionListener shnServiceBodyCompositionListener;

    public interface SHNServiceBodyCompositionListener {
        void onServiceStateChanged(SHNServiceBodyComposition shnServiceBodyComposition, SHNService.State state);

        void onBodyCompositionMeasurementReceived(SHNServiceBodyComposition shnServiceBodyComposition, SHNBodyComposition shnBodyComposition);
    }

    public SHNServiceBodyComposition(SHNFactory shnFactory) {
        shnService = shnFactory.createNewSHNService(BODY_COMPOSITION_UUID, getRequiredCharacteristics(), getOptionalCharacteristics());
        shnService.registerSHNServiceListener(this);
    }

    public SHNService getShnService() {
        return shnService;
    }

    private SHNService shnService;

    private static Set<UUID> getRequiredCharacteristics() {
        Set<UUID> requiredCharacteristicUUIDs = new HashSet<>();
        requiredCharacteristicUUIDs.add(BODY_COMPOSITION_FEATURES_CHARACTERISTIC_UUID);
        requiredCharacteristicUUIDs.add(BODY_COMPOSITION_MEASUREMENT_CHARACTERISTIC_UUID);
        return requiredCharacteristicUUIDs;
    }

    private static Set<UUID> getOptionalCharacteristics() {
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
            listener.onActionCompleted(null, SHNResult.SHNServiceUnavailableError);
        }
    }

    public void setNotificationsEnabled(boolean enabled, final SHNResultListener shnResultListener) {
        SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(BODY_COMPOSITION_MEASUREMENT_CHARACTERISTIC_UUID);
        SHNCommandResultReporter shnCommandResultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
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
                SHNBodyComposition shnBodyComposition = new SHNBodyComposition(byteBuffer);
                shnServiceBodyCompositionListener.onBodyCompositionMeasurementReceived(this, shnBodyComposition);
            } catch (IllegalArgumentException e) {
            }
        }
    }

    private void readFeatures(final SHNObjectResultListener listener) {
        final SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(BODY_COMPOSITION_FEATURES_CHARACTERISTIC_UUID);

        SHNCommandResultReporter resultReporter = new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
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
