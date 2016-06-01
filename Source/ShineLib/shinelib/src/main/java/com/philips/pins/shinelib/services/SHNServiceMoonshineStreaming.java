/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class SHNServiceMoonshineStreaming extends SHNService implements SHNService.SHNServiceListener {
    private static final String TAG = SHNServiceMoonshineStreaming.class.getSimpleName();

    public static final UUID SERVICE_UUID = UUID.fromString("A651FFF1-4074-4131-BCE9-56D4261BC7B1");
    public static final UUID RX_CHARACTERISTIC_UUID = UUID.fromString("A6510001-4074-4131-BCE9-56D4261BC7B1");
    public static final UUID RX_ACK_CHARACTERISTIC_UUID = UUID.fromString("A6510002-4074-4131-BCE9-56D4261BC7B1");
    public static final UUID TX_CHARACTERISTIC_UUID = UUID.fromString("A6510003-4074-4131-BCE9-56D4261BC7B1");
    public static final UUID TX_ACK_CHARACTERISTIC_UUID = UUID.fromString("A6510004-4074-4131-BCE9-56D4261BC7B1");
    public static final UUID PROT_CFG_CHARACTERISTIC_UUID = UUID.fromString("A6510005-4074-4131-BCE9-56D4261BC7B1");

    public interface SHNServiceMoonshineStreamingListener {
        void onReadProtocolInformation(byte[] configurationData);

        void onReceiveData(byte[] data);

        void onReceiveAck(byte[] data);

        void onServiceAvailable();

        void onServiceUnavailable();
    }

    private SHNServiceMoonshineStreamingListener shnServiceMoonshineStreamingListener;
    private SHNCharacteristic.SHNCharacteristicChangedListener rxAckChangedListener = new SHNCharacteristic.SHNCharacteristicChangedListener() {
        @Override
        public void onCharacteristicChanged(SHNCharacteristic shnCharacteristic, byte[] data) {
            if (shnServiceMoonshineStreamingListener != null) {
                shnServiceMoonshineStreamingListener.onReceiveAck(data);
            }
        }
    };
    private SHNCharacteristic.SHNCharacteristicChangedListener txChangedListener = new SHNCharacteristic.SHNCharacteristicChangedListener() {
        @Override
        public void onCharacteristicChanged(SHNCharacteristic shnCharacteristic, byte[] data) {
            if (shnServiceMoonshineStreamingListener != null) {
                shnServiceMoonshineStreamingListener.onReceiveData(data);
            }
        }
    };

    public SHNServiceMoonshineStreaming() {
        super(SERVICE_UUID, getRequiredCharacteristics(), getOptionalCharacteristics());
        registerSHNServiceListener(this);
    }

    private static Set<UUID> getRequiredCharacteristics() {
        Set<UUID> requiredCharacteristicUUIDs = new HashSet<>();
        requiredCharacteristicUUIDs.add(RX_CHARACTERISTIC_UUID);
        requiredCharacteristicUUIDs.add(RX_ACK_CHARACTERISTIC_UUID);
        requiredCharacteristicUUIDs.add(TX_CHARACTERISTIC_UUID);
        requiredCharacteristicUUIDs.add(TX_ACK_CHARACTERISTIC_UUID);
        requiredCharacteristicUUIDs.add(PROT_CFG_CHARACTERISTIC_UUID);
        return requiredCharacteristicUUIDs;
    }

    private static Set<UUID> getOptionalCharacteristics() {
        return new HashSet<>();
    }

    public void setShnServiceMoonshineStreamingListener(SHNServiceMoonshineStreamingListener shnServiceMoonshineStreamingListener) {
        this.shnServiceMoonshineStreamingListener = shnServiceMoonshineStreamingListener;
    }

    public void readProtocolConfiguration() {
        final SHNCharacteristic shnCharacteristic = getSHNCharacteristic(PROT_CFG_CHARACTERISTIC_UUID);
        if(shnCharacteristic!=null) {
            shnCharacteristic.read(new SHNCommandResultReporter() {
                @Override
                public void reportResult(@NonNull SHNResult shnResult, byte[] data) {
                    if (SHNServiceMoonshineStreaming.this.shnServiceMoonshineStreamingListener != null) {
                        byte[] protocolConfigData = null;
                        if (shnResult == SHNResult.SHNOk) {
                            protocolConfigData = shnCharacteristic.getValue();
                        } else {
                            SHNLogger.d(TAG, "Read of ProtocolConfiguration failed. Using defaults!");
                        }
                        SHNServiceMoonshineStreaming.this.shnServiceMoonshineStreamingListener.onReadProtocolInformation(protocolConfigData);
                    }
                }
            });
        }
    }

    public void sendData(byte[] data) {
        final SHNCharacteristic shnCharacteristic = getSHNCharacteristic(RX_CHARACTERISTIC_UUID);
        shnCharacteristic.write(data, null);
    }

    public void sendAck(byte[] data) {
        final SHNCharacteristic shnCharacteristic = getSHNCharacteristic(TX_ACK_CHARACTERISTIC_UUID);
        shnCharacteristic.write(data, null);
    }

    // implements SHNService.SHNServiceListener
    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        SHNLogger.e(TAG, "onServiceStateChanged state: " + state);
        switch (state) {
            case Unavailable:
                if (shnServiceMoonshineStreamingListener != null) {
                    shnServiceMoonshineStreamingListener.onServiceUnavailable();
                }
                break;
            case Available:
                if (shnServiceMoonshineStreamingListener != null) {
                    shnServiceMoonshineStreamingListener.onServiceAvailable();
                }
                SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(RX_ACK_CHARACTERISTIC_UUID);
                if(shnCharacteristic!=null) {
                    shnCharacteristic.setNotification(true, null);
                    shnCharacteristic.setShnCharacteristicChangedListener(rxAckChangedListener);
                    shnCharacteristic = shnService.getSHNCharacteristic(TX_CHARACTERISTIC_UUID);
                    shnCharacteristic.setNotification(true, null);
                    shnCharacteristic.setShnCharacteristicChangedListener(txChangedListener);
                }
                break;
            case Ready:
                break;
        }
    }
}
