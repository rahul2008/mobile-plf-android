/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.api.listener;

import android.support.annotation.NonNull;

/**
 * The interface DcsResponseListener.
 * <p>
 * Provides notifications on DCS responses.
 *
 * @publicApi
 */
public interface DcsResponseListener {
    /**
     * On dcs response received.
     *
     * @param dcsResponse    the dcs response
     * @param conversationId the conversation id
     */
    void onDCSResponseReceived(@NonNull String dcsResponse, @NonNull String conversationId);
}
