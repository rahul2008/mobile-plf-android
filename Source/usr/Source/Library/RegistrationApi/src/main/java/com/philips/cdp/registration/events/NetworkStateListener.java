
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.events;

/**
 * Network state listener interface
 */
public interface NetworkStateListener {
    /**
     * {@code onNetWorkStateReceived} method to on network state received
     * @param isOnline
     */
    void onNetWorkStateReceived(boolean isOnline);

}
