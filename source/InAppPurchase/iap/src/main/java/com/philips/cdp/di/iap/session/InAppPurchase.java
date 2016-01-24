/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;

public class InAppPurchase {

    public static int getCartItemCount(Context context,String janRainID, String userID) {
        return HybrisDelegate.getCartItemCount(context, janRainID, userID);
    }
}