
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.events;

/**
 * Event listener interface
 */
public interface CounterListener {
    /**
     * {@onEventReceived} method to on event received
     * @param event event
     */
    void onCounterEventReceived(String event, long timeLeft);

}
