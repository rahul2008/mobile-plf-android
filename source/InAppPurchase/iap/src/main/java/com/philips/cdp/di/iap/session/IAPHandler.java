/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.Message;

import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.model.CartAddProductRequest;
import com.philips.cdp.di.iap.model.CartCreateRequest;
import com.philips.cdp.di.iap.model.CartCurrentInfoRequest;
import com.philips.cdp.di.iap.model.CartModel;
import com.philips.cdp.di.iap.model.IAPResponseError;
import com.philips.cdp.di.iap.response.cart.AddToCartData;
import com.philips.cdp.di.iap.utils.IAPLog;

import java.util.HashMap;

public class IAPHandler {
    private Context mContext;

    public void initApp(Context context, String userName, String janRainID) {
        //We register with app context to avoid any memory leaks
        mContext = context.getApplicationContext();
        HybrisDelegate.getInstance(mContext).initStore(mContext, userName, janRainID);
    }

    public void launchIAP(String pStoreID, String pLanguage, String pCountry, int pThemeIndex) {
        //launching ShowppingCarFragment
    }

    public void addItemtoCart(String productCTN) {
        //addToCart
        HashMap<String, String> params = new HashMap<>();
        params.put(CartModel.PRODUCT_CODE, productCTN);
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        CartAddProductRequest model = new CartAddProductRequest(delegate.getStore(), params, null);
        delegate.sendRequest(RequestCode.ADD_TO_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                AddToCartData addToCartData = (AddToCartData) msg.obj;
                if (addToCartData.getStatusCode().equalsIgnoreCase("success")) {
                    int mCount = addToCartData.getEntry().getQuantity();
                    IAPLog.i(IAPLog.LOG, "IAPHandler == getCartQuantity = " + mCount);
                }
            }

            @Override
            public void onError(final Message msg) {
                IAPResponseError iapResponseError = new IAPResponseError();
                VolleyError error = (VolleyError) msg.obj;
                iapResponseError.errorMessage = error.getLocalizedMessage();
            }
        });
    }

    public void getCartQuantity() {
        //get cart from the Hybris Server
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(delegate.getStore(), null, null);
        model.setContext(mContext);
        delegate.sendRequest(RequestCode.GET_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                AddToCartData addToCartData = (AddToCartData) msg.obj;
                if (addToCartData.getStatusCode().equalsIgnoreCase("success")) {
                    int mCount = addToCartData.getEntry().getQuantity();
                    IAPLog.i(IAPLog.LOG, "IAPHandler == getCartQuantity = " + mCount);
                }
            }

            @Override
            public void onError(final Message msg) {
                if (RequestCode.GET_CART == msg.obj) {
                    createCart();
                }
                IAPResponseError iapResponseError = new IAPResponseError();
                VolleyError error = (VolleyError) msg.obj;
                iapResponseError.errorMessage = error.getLocalizedMessage();
            }
        });
    }

    public void createCart() {
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        CartCreateRequest model = new CartCreateRequest(delegate.getStore(), null, null);
        delegate.sendRequest(RequestCode.CREATE_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                IAPLog.i(IAPLog.LOG, "IAPHandler == createCart = onSuccess ");
            }

            @Override
            public void onError(final Message msg) {
                IAPLog.i(IAPLog.LOG, "IAPHandler == createCart = onError ");
            }
        });
    }
}