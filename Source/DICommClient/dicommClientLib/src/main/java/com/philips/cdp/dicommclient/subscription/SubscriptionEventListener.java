/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

/**
 * Interface through which {@link SubscriptionHandler}s communicate arrival of port property updates.
 */
public interface SubscriptionEventListener {
     void onSubscriptionEventReceived(String data);
}
