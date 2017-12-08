/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.capabilities;

import com.philips.pins.shinelib.SHNCapability;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.SHNSetResultListener;
import com.philips.pins.shinelib.datatypes.SHNData;
import com.philips.pins.shinelib.datatypes.SHNDataType;

/**
 * Interface to receive a stream of {@link SHNData} objects from a peripheral. Each peripheral
 * has its own set of data types for which it supports streaming.
 *
 * @publicApi
 */
public interface SHNCapabilityDataStreaming extends SHNCapability {

    /**
     * Interface containing the callback through which received streaming events are reported.
     */
    interface SHNCapabilityDataStreamingListener {
        /**
         * Callback carrying a data element for which streaming is enabled.
         *
         * @param data the streamed event data.
         */
        void onReceiveData(SHNData data);
    }

    /**
     * Returns a set of {@link SHNDataType} for which the peripheral supports streaming.
     *
     * @param listener a callback to receive the list
     */
    void getSupportedDataTypes(SHNSetResultListener<SHNDataType> listener);

    /**
     * Set callback to receive a stream of {@link SHNData}.
     * <p/>
     * Requires subscription to be enabled.
     *
     * @param listener to receive a stream of {@link SHNData} objects
     */
    void setDataListener(SHNCapabilityDataStreamingListener listener);

    /**
     * Enable or disable the {@link SHNData} streaming. Note that notifications are received via
     * {@code SHNCapabilityDataStreamingListener} which can be set via {@link #setDataListener(SHNCapabilityDataStreamingListener)}.
     *
     * @param enabled        flag to enable or disable notifications
     * @param resultListener callback to provide feedback about subscription result
     */
    void setStreamingEnabled(boolean enabled, SHNDataType dataType, SHNResultListener resultListener);
}
