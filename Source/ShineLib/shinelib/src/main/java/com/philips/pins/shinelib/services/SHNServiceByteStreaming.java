/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.services;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.SHNCharacteristic;
import com.philips.pins.shinelib.SHNCommandResultReporter;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNService;
import com.philips.pins.shinelib.datatypes.SHNCharacteristicInfo;
import com.philips.pins.shinelib.utility.SHNLogger;

import java.util.Set;
import java.util.UUID;

public abstract class SHNServiceByteStreaming extends SHNService implements SHNService.SHNServiceListener {
    private static final String TAG = SHNServiceByteStreaming.class.getSimpleName();

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

    public SHNServiceByteStreaming(UUID serviceUuid, Set<SHNCharacteristicInfo> requiredCharacteristics, Set<SHNCharacteristicInfo> optionalCharacteristics) {
        super(serviceUuid, requiredCharacteristics, optionalCharacteristics);
        registerSHNServiceListener(this);
    }



    public void setShnServiceMoonshineStreamingListener(SHNServiceMoonshineStreamingListener shnServiceMoonshineStreamingListener) {
        this.shnServiceMoonshineStreamingListener = shnServiceMoonshineStreamingListener;
    }

    public void readProtocolConfiguration() {
        final SHNCharacteristic shnCharacteristic = getSHNCharacteristic(getProtCfgCharacteristicUuid());
        if(shnCharacteristic!=null) {
            shnCharacteristic.read(new SHNCommandResultReporter() {
                @Override
                public void reportResult(@NonNull SHNResult shnResult, byte[] data) {
                    if (SHNServiceByteStreaming.this.shnServiceMoonshineStreamingListener != null) {
                        byte[] protocolConfigData = null;
                        if (shnResult == SHNResult.SHNOk) {
                            protocolConfigData = shnCharacteristic.getValue();
                        } else {
                            SHNLogger.d(TAG, "Read of ProtocolConfiguration failed. Using defaults!");
                        }
                        SHNServiceByteStreaming.this.shnServiceMoonshineStreamingListener.onReadProtocolInformation(protocolConfigData);
                    }
                }
            });
        }
    }

    public void sendData(byte[] data) {
        final SHNCharacteristic shnCharacteristic = getSHNCharacteristic(getRxCharacteristicUuid());
        shnCharacteristic.write(data, null);
    }

    public void sendAck(byte[] data) {
        final SHNCharacteristic shnCharacteristic = getSHNCharacteristic(getTxAckCharacteristicUuid());
        shnCharacteristic.write(data, null);
    }

    // implements SHNService.SHNServiceListener
    @Override
    public void onServiceStateChanged(SHNService shnService, SHNService.State state) {
        SHNLogger.i(TAG, "onServiceStateChanged state: " + state);
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
                SHNCharacteristic shnCharacteristic = shnService.getSHNCharacteristic(getRxAckCharacteristicUuid());
                if (shnCharacteristic != null) {
                    shnCharacteristic.setNotification(true, null);
                    shnCharacteristic.setShnCharacteristicChangedListener(rxAckChangedListener);
                    shnCharacteristic = shnService.getSHNCharacteristic(getTxCharacteristicUuid());
                    shnCharacteristic.setNotification(true, null);
                    shnCharacteristic.setShnCharacteristicChangedListener(txChangedListener);
                }
                break;
            case Ready:
                break;
        }
    }

    protected abstract UUID getServiceUuid();
    protected abstract UUID getRxCharacteristicUuid();
    protected abstract UUID getRxAckCharacteristicUuid();
    protected abstract UUID getTxCharacteristicUuid();
    protected abstract UUID getTxAckCharacteristicUuid();
    protected abstract UUID getProtCfgCharacteristicUuid();
}
