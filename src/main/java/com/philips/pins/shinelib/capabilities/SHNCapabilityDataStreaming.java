package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.datatypes.SHNDataType;

/**
 * Created by 310188215 on 03/03/15.
 */
public interface SHNCapabilityDataStreaming extends SHNCapability {
    interface SHNCapabilityDataStreamingListener {
        void onDataReceived(byte[] data, SHNDataType shnDataType);
    }

    void setStreamingEnabled(boolean enabled, SHNDataType shnDataType, SHNResultListener shnResultListener);
    void setShnCapabilityDataStreamingListener(SHNCapabilityDataStreamingListener shnCapabilityDataStreamingListener);
}
