package com.philips.cdp2.bluelib.plugindefinition;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.datatypes.SHNStreamData;
import com.philips.pins.shinelib.datatypes.StreamIdentifier;
import com.philips.pins.shinelib.protocols.moonshinestreaming.MoonshineStreamIdentifier;
import com.philips.pins.shinelib.protocols.moonshinestreaming.SHNProtocolMoonshineStreaming;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class StreamingCapability implements CapabilityDiComm {

    private static String TAG = "StreamingCapability";

    private SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming;
    private Set<ResultListener<SHNStreamData>> mDataRawResultListeners = new CopyOnWriteArraySet<>();

    private SHNProtocolMoonshineStreaming.SHNProtocolMoonshineStreamingListener listener = new SHNProtocolMoonshineStreaming.SHNProtocolMoonshineStreamingListener() {
        @Override
        public void onDataReceived(byte[] data, MoonshineStreamIdentifier streamIdentifier) {
            for (ResultListener<SHNStreamData> listener : mDataRawResultListeners) {
                listener.onActionCompleted(new SHNStreamData(data, StreamIdentifier.fromValue(streamIdentifier.getValue())), SHNResult.SHNOk);
            }
        }

        @Override
        public void onProtocolAvailable() {
            shnProtocolMoonshineStreaming.transitionToReady();
        }

        @Override
        public void onProtocolUnavailable() {
            shnProtocolMoonshineStreaming.transitionToError(SHNResult.SHNErrorServiceUnavailable);
        }
    };

    public StreamingCapability(SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming) {
        this.shnProtocolMoonshineStreaming = shnProtocolMoonshineStreaming;
        shnProtocolMoonshineStreaming.setShnProtocolMoonshineStreamingListener(listener);
    }

    @Override
    public void writeData(byte[] data, StreamIdentifier streamIdentifier) {
        shnProtocolMoonshineStreaming.sendData(data, MoonshineStreamIdentifier.fromValue(streamIdentifier.getValue()));
    }

    @Override
    public void addDataListener(ResultListener<SHNStreamData> dataListener) {
        mDataRawResultListeners.add(dataListener);
    }

    @Override
    public void removeDataListener(@NonNull ResultListener<SHNStreamData> dataListener) {
        mDataRawResultListeners.remove(dataListener);
    }
}
