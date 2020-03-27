/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.platform.pif.DataInterface.MEC.listeners;

/**
 * The interface Mec hybris availability listener.
 */
public interface MECHybrisAvailabilityListener {
    /**
     * Is hybris available.
     *
     * @param bool is Hybris available
     * @since 2002.1.0
     */
    void isHybrisAvailable( Boolean bool);
}
