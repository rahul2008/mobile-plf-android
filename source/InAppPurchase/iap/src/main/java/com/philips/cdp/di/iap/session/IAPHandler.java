/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.philips.cdp.di.iap.model.CartAddProductRequest;
import com.philips.cdp.di.iap.model.CartCreateRequest;
import com.philips.cdp.di.iap.model.CartCurrentInfoRequest;
import com.philips.cdp.di.iap.model.CartModel;

import java.util.HashMap;

public class IAPHandler {

    public static void initApp(Context context, String userName, String janRainID) {
        //We register with app context to avoid any memory leaks
        Context applicationContext = context.getApplicationContext();
        HybrisDelegate.getInstance(applicationContext).initStore(applicationContext, userName, janRainID);
    }

    public void launchIAP(String pStoreID, String pLanguage, String pCountry, int pThemeIndex) {
        //launching ShowppingCarFragment
    }

    public static void addItemtoCart(Context context, int requestCode, String itemCode,
                                     RequestListener listener) {
        //addToCart
        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CartModel.PRODUCT_CODE, itemCode);
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        CartAddProductRequest model = new CartAddProductRequest(delegate.getStore(), params, null);
        delegate.sendRequest(requestCode, model, listener);
    }

    public static void getCartQuantity(Context context, int requestCode, RequestListener listener) {
        //get cart from the Hybris Server
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(delegate.getStore(), null,null);
        model.setContext(context);
        delegate.sendRequest(requestCode, model, listener);
    }

    public static void createCart(Context context, int requestCode, RequestListener listener) {
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        CartCreateRequest model = new CartCreateRequest(delegate.getStore(),null,null);
        delegate.sendRequest(requestCode, model, listener);
    }
 }