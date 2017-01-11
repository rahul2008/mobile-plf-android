package com.philips.cdp2.bluelib.plugindefinition;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.capabilities.CapabilityDiComm;
import com.philips.pins.shinelib.datatypes.SHNDataRaw;
import com.philips.pins.shinelib.protocols.moonshinestreaming.SHNProtocolMoonshineStreaming;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class StreamingCapability implements CapabilityDiComm {

    private static String TAG = "StreamingCapability";

    private SHNProtocolMoonshineStreaming shnProtocolMoonshineStreaming;
    private Set<ResultListener<SHNDataRaw>> mDataRawResultListeners = new CopyOnWriteArraySet<>();

    private SHNProtocolMoonshineStreaming.SHNProtocolMoonshineStreamingListener listener = new SHNProtocolMoonshineStreaming.SHNProtocolMoonshineStreamingListener() {
        @Override
        public void onDataReceived(byte[] data) {
            for (ResultListener<SHNDataRaw> listener : mDataRawResultListeners) {
                listener.onActionCompleted(new SHNDataRaw(data), SHNResult.SHNOk);
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
    public void writeData(byte[] data) {
        shnProtocolMoonshineStreaming.sendData(data);
    }

    @Override
    public void addDataListener(ResultListener<SHNDataRaw> dataListener) {
        mDataRawResultListeners.add(dataListener);
    }

    @Override
    public void removeDataListener(@NonNull ResultListener<SHNDataRaw> dataListener) {
        mDataRawResultListeners.remove(dataListener);
    }
}
