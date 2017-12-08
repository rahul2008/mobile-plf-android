/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller.api.listener;

/**
 * The interface SendNotificationRegistrationIdListener.
 * <p>
 * Provides notifications on the sending of registration ids.
 *
 * @publicApi
 */
public interface SendNotificationRegistrationIdListener {
    /**
     * On registration id sent success.
     */
    void onRegistrationIdSentSuccess();

    /**
     * On registration id sent failed.
     */
    void onRegistrationIdSentFailed();
}
