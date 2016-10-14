/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.cloudcontroller.listener;

import android.support.annotation.NonNull;

/**
 * The interface DcsResponseListener.
 * <p>
 * Provides notifications on DCS responses.
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
