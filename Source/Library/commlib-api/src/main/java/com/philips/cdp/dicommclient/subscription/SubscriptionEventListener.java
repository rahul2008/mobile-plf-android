/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.subscription;

/**
 * Interface through which {@link SubscriptionHandler}s communicate arrival of port property updates.
 */
public interface SubscriptionEventListener {

    /**
     * Called when valid subscription data for a {@link com.philips.cdp.dicommclient.port.DICommPort} comes in
     * @param portName String The name of the port that receives subscription data
     * @param data String The data contained in the subscription event.
     */
    void onSubscriptionEventReceived(String portName, String data);

    /**
     * Called when subscription data for a {@link com.philips.cdp.dicommclient.port.DICommPort} came in, but could not be decrypted.
     * @param portName String The name of the port that receives subscription data
     */
    void onSubscriptionEventDecryptionFailed(String portName);
}
