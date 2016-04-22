/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.protocols.moonshinestreaming;

public interface SHNProtocolMoonshineStreaming extends SHNServiceMoonshineStreaming.SHNServiceMoonshineStreamingListener {
    interface SHNProtocolMoonshineStreamingListener {
        void onDataReceived(byte[] data);

        void onProtocolAvailable();

        void onProtocolUnavailable();
    }

    void setShnProtocolMoonshineStreamingListener(SHNProtocolMoonshineStreamingListener shnProtocolMoonshineStreamingListener);

    void sendData(byte[] bytes);

    enum SHNProtocolMoonshineStreamingState {
        Initializing, AcquiringConfiguration, WaitingForHandshakeAck, Ready, Error
    }
}
