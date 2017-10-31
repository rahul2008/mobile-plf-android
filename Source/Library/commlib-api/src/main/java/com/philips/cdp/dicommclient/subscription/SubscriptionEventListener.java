/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

/**
 * Interface through which {@link SubscriptionHandler}s communicate arrival of port property updates.
 */
public interface SubscriptionEventListener {
    void onSubscriptionEventReceived(String portName, String data);

    void onSubscriptionEventDecryptionFailed(String portName);
}
