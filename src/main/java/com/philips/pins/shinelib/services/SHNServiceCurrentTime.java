package com.philips.pins.shinelib.services;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNObjectResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.capabilities.SHNCapabilityLogSynchronization;
import com.philips.pins.shinelib.framework.BleUUIDCreator;
import com.philips.pins.shinelib.framework.SHNFactory;
import com.philips.pins.shinelib.utility.ExactTime256WithAdjustReason;
import com.philips.pins.shinelib.utility.SHNBluetoothDataConverter;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by 310188215 on 22/06/15.
 */
public class SHNServiceCurrentTime {
    public static final UUID SERVICE_UUID =                             UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x1805));
    public static final UUID CURRENT_TIME_CHARACTERISTIC_UUID =         UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A2B));
    public static final UUID LOCAL_TIME_INFO_CHARACTERISTIC_UUID =      UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A0F));
    public static final UUID REFERENCE_TIME_INFO_CHARACTERISTIC_UUID =  UUID.fromString(BleUUIDCreator.create128bitBleUUIDFrom16BitBleUUID(0x2A14));

    private SHNService shnService;
    private SHNService.SHNServiceListener shnServiceListener = new SHNService.SHNServiceListener() {
        @Override
        public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
            if (SHNService.State.Available == state) {
                shnService.transitionToReady();
            }
        }
    };

    public SHNServiceCurrentTime(SHNFactory shnFactory) {
        shnService = shnFactory.createNewSHNService(SERVICE_UUID, getRequiredCharacteristics(), getOptionalCharacteristics());
        shnService.registerSHNServiceListener(shnServiceListener);
    }

    private Set<UUID> getRequiredCharacteristics() {
        Set<UUID> uuids = new HashSet<>();
        uuids.add(CURRENT_TIME_CHARACTERISTIC_UUID);
        return uuids;
    }

    private Set<UUID> getOptionalCharacteristics() {
        Set<UUID> uuids = new HashSet<>();
        uuids.add(LOCAL_TIME_INFO_CHARACTERISTIC_UUID);
        uuids.add(REFERENCE_TIME_INFO_CHARACTERISTIC_UUID);
        return uuids;
    }

    public void getCurrentTime(final SHNObjectResultListener shnObjectResultListener) {
        if (SHNService.State.Available == shnService.getState()) {
            readCurrentTimeFromCharacteristic(shnObjectResultListener);
        } else {
            shnObjectResultListener.onActionCompleted(null, SHNResult.SHNServiceUnavailableError);
        }
    }

    private void readCurrentTimeFromCharacteristic(final SHNObjectResultListener shnObjectResultListener) {
        SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(CURRENT_TIME_CHARACTERISTIC_UUID);
        shnCharacteristic.read(new SHNCommandResultReporter() {
            @Override
            public void reportResult(SHNResult shnResult, byte[] data) {
                extractCurrentTimeFromReport(shnResult, data, shnObjectResultListener);
            }
        });
    }

    private void extractCurrentTimeFromReport(SHNResult shnResult, byte[] data, SHNObjectResultListener shnObjectResultListener) {
        ExactTime256WithAdjustReason exactTime256WithAdjustReason = null;
        if (SHNResult.SHNOk == shnResult) {
            try {
                ByteBuffer byteBuffer = ByteBuffer.wrap(data);
                byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
                exactTime256WithAdjustReason = new ExactTime256WithAdjustReason(byteBuffer);
            } catch (BufferUnderflowException e) {
                shnResult = SHNResult.SHNResponseIncompleteError;
            } catch (IllegalArgumentException e) {
                shnResult = SHNResult.SHNErrorWhileParsing;
            }
        }
        shnObjectResultListener.onActionCompleted(exactTime256WithAdjustReason, shnResult);
    }
}
