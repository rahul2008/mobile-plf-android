/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;

public class InAppPurchase {

    public static void initApp(Context context, String userName, String janRainID) {
        //We register with app context to avoid any memory leaks
        Context applicationContext = context.getApplicationContext();
        HybrisDelegate.getInstance(applicationContext).initStore(applicationContext, userName, janRainID);
    }

}