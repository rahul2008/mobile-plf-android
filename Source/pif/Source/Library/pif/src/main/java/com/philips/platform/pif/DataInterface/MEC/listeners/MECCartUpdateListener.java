package com.philips.platform.pif.DataInterface.MEC.listeners;

/**
 * The interface Mec cart update listener.
 */
public interface MECCartUpdateListener extends  CartListener {

    /**
     * On update cart count.
     *
     * @param count the shopping cart update count
     * @since 2002.1.0
     */
    void onUpdateCartCount( int count);

    /**
     * Should show cart.
     *
     * @param shouldShow the should show shopping cart icon
     * @since 2002.1.0
     */
    void shouldShowCart(Boolean shouldShow );
}
