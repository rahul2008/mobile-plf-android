package com.philips.platform.pif.DataInterface.MEC.listeners;

/**
 * The interface Mec fetch cart listener.
 */
public interface MECFetchCartListener  extends  CartListener{
    /**
     * On get cart count.
     *
     * @param count the total number of product added to shopping cart
     *@since 2002.1.0
     */
    void onGetCartCount(int count);
}
