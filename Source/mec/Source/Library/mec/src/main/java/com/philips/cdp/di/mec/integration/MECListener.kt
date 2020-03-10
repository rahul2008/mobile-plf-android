/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration


interface MECListener {

    /**
     * Gets the product count in cart
     * @param count - count is an integer through which basis user can update the count visibility
     * @since 1.0.0
     */
    fun onGetCartCount(count: Int)

    /**
     * Notifies when product count in cart is updated
     * @since 1.0.0
     */
     fun onUpdateCartCount( count: Int)

    /**
     * Notifies true for cart icon visibility or false for hide
     * @param shouldShow  boolean will help to update hte cart icon visibility
     * @since 1.0.0
     */
     fun updateCartIconVisibility(shouldShow: Boolean)



    fun onFailure(exception: Exception)

}
