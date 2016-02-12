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

    public void launchIAP(String pStoreID, String pLanguage, String pCountry, int pThemeIndex) {
        //launching ShowppingCarFragment
    }

    public void addItemtoCart(String itemCTN) {
        //addToCart
/*        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CartModel.PRODUCT_CODE, ctnNumber);*/
//        HybrisDelegate.getInstance().sendRequest(RequestCode.ADD_TO_CART, this, params);
    }

    public int getCartQuantity() {
        //get cart from the Hybris Server
        final int quantity = 0;
        if (quantity != -1)
            return quantity;
        else
            return -1;
    }
}