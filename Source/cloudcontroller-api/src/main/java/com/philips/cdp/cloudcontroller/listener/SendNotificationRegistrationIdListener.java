/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.cloudcontroller.listener;

public interface SendNotificationRegistrationIdListener {
    void onRegistrationIdSentSuccess();

    void onRegistrationIdSentFailed();
}
