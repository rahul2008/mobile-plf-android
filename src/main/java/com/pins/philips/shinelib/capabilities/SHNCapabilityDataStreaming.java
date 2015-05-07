package com.pins.philips.shinelib.capabilities;

import com.pins.philips.shinelib.SHNCapability;
import com.pins.philips.shinelib.SHNResultListener;
import com.pins.philips.shinelib.datatypes.SHNDataType;

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
