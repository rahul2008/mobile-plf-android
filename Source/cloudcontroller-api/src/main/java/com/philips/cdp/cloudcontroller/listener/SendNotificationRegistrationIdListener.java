/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.cloudcontroller.listener;

/**
 * The interface SendNotificationRegistrationIdListener.
 * <p>
 * Provides notifications on the sending of registration ids.
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
