/* Copyright (c) Koninklijke Philips N.V., 2020

 * All rights are reserved. Reproduction or dissemination

 * in whole or in part is prohibited without the prior written

 * consent of the copyright holder.

 */
package com.philips.platform.pif.DataInterface.MEC;

import com.philips.platform.pif.DataInterface.MEC.listeners.MECCartUpdateListener;
import com.philips.platform.pif.DataInterface.MEC.listeners.MECFetchCartListener;
import com.philips.platform.pif.DataInterface.MEC.listeners.MECHybrisAvailabilityListener;

/**
 * The interface Mec data interface.
 */
public interface MECDataInterface {

    /**
     * Fetch cart count.
     *
     * @param mecFetchCartListener the mec fetch cart listener
     *  @since 2002.0
     */
    void fetchCartCount(MECFetchCartListener mecFetchCartListener);

    /**
     * Is hybris available.
     *
     * @param mECHybrisAvailabilityListener the m ec hybris availability listener
     *  @since 2002.0
     */
    void isHybrisAvailable(  MECHybrisAvailabilityListener mECHybrisAvailabilityListener );


    /**
     * This API is called to add a MECCartUpdateListener which will give shopping cart product count update and should show cart
     *
     *
     * @param mecCartUpdateListener the Cart product count update listener
     *  @since 2002.0
     */
    void addCartUpdateListener(MECCartUpdateListener mecCartUpdateListener ) ;


    /**
     *This API is called to remove a previously added MECCartUpdateListener
     *
     * @param mecCartUpdateListener The listener to be removed
     *  @since 2002.0
     */
    void removeCartUpdateListener(MECCartUpdateListener mecCartUpdateListener);

}
