/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

public class InAppPurchase {

    public static int getCartItemCount(String janRainID, String userID) {
        return HybrisDelegate.getCartItemCount(janRainID, userID);
    }
}