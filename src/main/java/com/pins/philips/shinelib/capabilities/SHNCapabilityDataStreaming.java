package com.pins.philips.shinelib.capabilities;

import com.pins.philips.shinelib.SHNDataType;
import com.pins.philips.shinelib.SHNResultListener;

/**
 * Created by 310188215 on 03/03/15.
 */
public class SHNCapabilityDataStreaming {
    public interface SHNCapabilityDataStreamingListener {
        public void onDataReceived(byte[] data, SHNDataType shnDataType);
    }

    private SHNCapabilityDataStreamingListener shnCapabilityDataStreamingListener;

    public void setStreamingEnabled(boolean enabled, SHNDataType shnDataType, SHNResultListener shnResultListener) { throw new UnsupportedOperationException(); }

    public SHNCapabilityDataStreamingListener getShnCapabilityDataStreamingListener() {
        return shnCapabilityDataStreamingListener;
    }

    public void setShnCapabilityDataStreamingListener(SHNCapabilityDataStreamingListener shnCapabilityDataStreamingListener) {
        this.shnCapabilityDataStreamingListener = shnCapabilityDataStreamingListener;
    }

}
