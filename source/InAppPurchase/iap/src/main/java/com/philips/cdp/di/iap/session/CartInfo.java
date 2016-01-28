package com.philips.cdp.di.iap.session;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CartInfo {
    protected static String totalItems;
    protected static String totalCost;
    protected static String currency;

    private static CartInfo cartInfo = new CartInfo( );

    /* Static 'instance' method */
    public static CartInfo getInstance( ) {
        return cartInfo;
    }

    private CartInfo(){

    }

    public static String getTotalItems(){
        return totalItems;
    }

    public static String getTotalCost(){
        return totalCost;
    }

    public static String getCurrency(){
        return currency;
    }
}
