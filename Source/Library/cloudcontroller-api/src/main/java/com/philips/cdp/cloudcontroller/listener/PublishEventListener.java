/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.cloudcontroller.listener;

import android.support.annotation.NonNull;

/**
 * The interface PublishEventListener.
 * <p>
 * Provides notifications on publish events.
 */
public interface PublishEventListener {
    /**
     * On publish event received.
     *
     * @param status         the status
     * @param messageId      the message id
     * @param conversationId the conversation id
     */
    void onPublishEventReceived(int status, int messageId, @NonNull String conversationId);
}
