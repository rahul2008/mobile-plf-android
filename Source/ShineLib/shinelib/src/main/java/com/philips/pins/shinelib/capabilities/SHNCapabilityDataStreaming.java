/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNSetResultListener;
import com.philips.pins.shinelib.datatypes.SHNData;
import com.philips.pins.shinelib.datatypes.SHNDataType;

/**
 * Created by 310188215 on 03/03/15.
 */
public interface SHNCapabilityDataStreaming extends SHNCapability {

    interface SHNCapabilityDataStreamingListener {
        void onReceiveData(SHNData data);
    }

    void getSupportedDataTypes(SHNSetResultListener listener);

    void setDataListener(SHNCapabilityDataStreamingListener listener);

    void setStreamingEnabled(boolean enabled, SHNDataType dataType, SHNResultListener resultListener);
}
