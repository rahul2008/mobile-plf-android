/*
 * Copyright (c) 2016-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.pins.shinelib.protocols.moonshinestreaming;

import android.os.Handler;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.services.SHNServiceByteStreaming;
import com.philips.pins.shinelib.utility.SHNLogger;

public class SHNProtocolByteStreamingVersionSwitcher implements SHNProtocolMoonshineStreaming {
    private static final String TAG = SHNProtocolByteStreamingVersionSwitcher.class.getSimpleName();
    private static final boolean ENABLE_DEBUG_LOGGING = false;
    private static final int PROTOCOL_VERSION_V1 = 1;
    private static final int PROTOCOL_VERSION_V2 = 2;
    private static final int PROTOCOL_VERSION_V3 = 3;
    private final SHNServiceByteStreaming shnServiceByteStreaming;
    private final Handler internalHandler;
    private SHNProtocolMoonshineStreamingState state = SHNProtocolMoonshineStreamingState.Initializing;
    private SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming;
    private SHNProtocolMoonshineStreamingListener shnProtocolMoonshineStreamingListener;

    public SHNProtocolByteStreamingVersionSwitcher(SHNServiceByteStreaming shnServiceByteStreaming, Handler internalHandler) {
        DebugLog("create instance");
        this.shnServiceByteStreaming = shnServiceByteStreaming;
        this.internalHandler = internalHandler;
    }

    /* package */ enum SHNProtocolMoonshineStreamingSubstate {
        Idle, WaitForAck, SendWindowFull
    }

    /* package */ enum PacketType {
        DATA, START
    }

    /* package */ static class SHNProtocolInformation {
        public static final int PROTOCOL_VERSION_OFFSET = 0;
        public static final int TX_WINDOW_SIZE_OFFSET = 1;
        public static final int RX_WINDOW_SIZE_OFFSET = 2;
        public final int protocolVersion;
        public final int txWindowSize;
        public final int rxWindowSize;

        private SHNProtocolInformation(int protocolVersion, int txWindowSize, int rxWindowSize) {
            this.protocolVersion = protocolVersion;
            this.txWindowSize = txWindowSize;
            this.rxWindowSize = rxWindowSize;
        }

        public static SHNProtocolInformation createFromData(byte[] data) {
            SHNProtocolInformation shnProtocolInformation = null;
            if (data != null && data.length == 3) {
                shnProtocolInformation = new SHNProtocolInformation(
                        data[PROTOCOL_VERSION_OFFSET], data[TX_WINDOW_SIZE_OFFSET], data[RX_WINDOW_SIZE_OFFSET]);
            }
            return shnProtocolInformation;
        }
    }

    @Override
    public void setShnProtocolMoonshineStreamingListener(SHNProtocolMoonshineStreamingListener shnProtocolMoonshineStreamingListener) {
        DebugLog("setShnProtocolMoonshineStreamingListener");
        this.shnProtocolMoonshineStreamingListener = shnProtocolMoonshineStreamingListener;
        if (shnProtocolMoonshineStreaming != null) {
            shnProtocolMoonshineStreaming.setShnProtocolMoonshineStreamingListener(shnProtocolMoonshineStreamingListener);
        }
    }

    @Override
    public void sendData(byte[] bytes) {
        DebugLog("sendData");
        if (shnProtocolMoonshineStreaming != null) {
            shnProtocolMoonshineStreaming.sendData(bytes);
        }
    }

    @Override
    public void transitionToError(SHNResult shnResult) {
        DebugLog("transitionToError: " + shnResult);
        if (shnProtocolMoonshineStreaming != null) {
            shnProtocolMoonshineStreaming.transitionToError(shnResult);
        }
    }

    @Override
    public void transitionToReady() {
        if (shnProtocolMoonshineStreaming != null) {
            shnProtocolMoonshineStreaming.transitionToReady();
        }
    }

    // implements SHNServiceMoonshineStreaming.SHNServiceMoonshineStreamingListener
    @Override
    public void onReadProtocolInformation(byte[] configurationData) {
        SHNLogger.d(TAG, "onReadProtocolInformation");
        SHNProtocolInformation shnProtocolInformation = SHNProtocolInformation.createFromData(configurationData);

        if (shnProtocolInformation == null || shnProtocolInformation.protocolVersion == PROTOCOL_VERSION_V3) {
            SHNLogger.d(TAG, "onReadProtocolInformation using protocol version: " + PROTOCOL_VERSION_V3);
            shnProtocolMoonshineStreaming = new SHNProtocolMoonshineStreamingV3(shnServiceByteStreaming, internalHandler);
            shnProtocolMoonshineStreaming.setShnProtocolMoonshineStreamingListener(shnProtocolMoonshineStreamingListener);
            shnProtocolMoonshineStreaming.onServiceAvailable();
            shnProtocolMoonshineStreaming.onReadProtocolInformation(configurationData);
        } else if (shnProtocolInformation == null || shnProtocolInformation.protocolVersion == PROTOCOL_VERSION_V2) {
            SHNLogger.d(TAG, "onReadProtocolInformation using protocol version: " + PROTOCOL_VERSION_V2);
            shnProtocolMoonshineStreaming = new SHNProtocolMoonshineStreamingV2(shnServiceByteStreaming, internalHandler);
            shnProtocolMoonshineStreaming.setShnProtocolMoonshineStreamingListener(shnProtocolMoonshineStreamingListener);
            shnProtocolMoonshineStreaming.onServiceAvailable();
            shnProtocolMoonshineStreaming.onReadProtocolInformation(configurationData);
        } else if (shnProtocolInformation.protocolVersion == PROTOCOL_VERSION_V1) {
            SHNLogger.d(TAG, "onReadProtocolInformation using protocol version: " + PROTOCOL_VERSION_V1);
            shnProtocolMoonshineStreaming = new SHNProtocolMoonshineStreamingV1(shnServiceByteStreaming, internalHandler);
            shnProtocolMoonshineStreaming.setShnProtocolMoonshineStreamingListener(shnProtocolMoonshineStreamingListener);
            shnProtocolMoonshineStreaming.onServiceAvailable();
            shnProtocolMoonshineStreaming.onReadProtocolInformation(configurationData);
        } else {
            state = SHNProtocolMoonshineStreamingState.Error;
        }
    }

    @Override
    public void onReceiveData(byte[] data) {
        DebugLog("onReceiveData");
        if (shnProtocolMoonshineStreaming != null) {
            shnProtocolMoonshineStreaming.onReceiveData(data);
        }
    }

    @Override
    public void onReceiveAck(byte[] data) {
        DebugLog("onReceiveAck");
        if (shnProtocolMoonshineStreaming != null) {
            shnProtocolMoonshineStreaming.onReceiveAck(data);
        }
    }

    @Override
    public void onServiceAvailable() {
        DebugLog("onServiceAvailable");
        shnServiceByteStreaming.readProtocolConfiguration();
    }

    @Override
    public void onServiceUnavailable() {
        DebugLog("onServiceUnavailable");
        if (shnProtocolMoonshineStreaming != null) {
            shnProtocolMoonshineStreaming.onServiceUnavailable();
            shnProtocolMoonshineStreaming.setShnProtocolMoonshineStreamingListener(null);
            shnProtocolMoonshineStreaming = null;
        }
    }

    private void DebugLog(String log) {
        if (ENABLE_DEBUG_LOGGING) {
            SHNLogger.i(TAG, log);
        }
    }
}
