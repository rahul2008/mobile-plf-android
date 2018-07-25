/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.protocols.moonshinestreaming;

import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.services.SHNServiceByteStreaming;

public interface SHNProtocolMoonshineStreaming extends SHNServiceByteStreaming.SHNServiceMoonshineStreamingListener {
    interface SHNProtocolMoonshineStreamingListener {
        void onDataReceived(byte[] data, MoonshineStreamIdentifier streamIdentifier);

        void onProtocolAvailable();

        void onProtocolUnavailable();
    }

    void setShnProtocolMoonshineStreamingListener(SHNProtocolMoonshineStreamingListener shnProtocolMoonshineStreamingListener);

    void sendData(byte[] bytes, MoonshineStreamIdentifier streamIdentifier);

    void transitionToError(SHNResult shnResult);

    void transitionToReady();

    enum SHNProtocolMoonshineStreamingState {
        Initializing, AcquiringConfiguration, WaitingForHandshakeAck, Ready, Error
    }
}
