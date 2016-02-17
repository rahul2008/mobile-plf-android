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
import com.philips.cdp.di.iap.response.cart.AddToCartData;
import com.philips.cdp.di.iap.response.cart.Entries;
import com.philips.cdp.di.iap.response.cart.GetCartData;
import com.philips.cdp.di.iap.utils.IAPLog;

import java.util.HashMap;
import java.util.List;

public class IAPHandler {
    private Context mContext;

    public void initApp(Context context, String userName, String janRainID) {
        //We register with app context to avoid any memory leaks
        mContext = context.getApplicationContext();
        HybrisDelegate.getInstance(mContext).initStore(mContext, userName, janRainID);
        IAPLog.i(IAPLog.IAPHANDLER, "IAPHandler == initApp");
    }

    public void launchIAP(String pStoreID, String pLanguage, String pCountry, int pThemeIndex) {
        //launching ShowppingCarFragment
        IAPLog.i(IAPLog.IAPHANDLER, "IAPHandler == launchIAP");
    }

    public void addItemtoCart(String productCTN, final IAPHandlerListner iapHandlerListner) {

        //addToCart
        IAPLog.i(IAPLog.IAPHANDLER, "IAPHandler == addItemtoCart");
        HashMap<String, String> params = new HashMap<>();
        params.put(CartModel.PRODUCT_CODE, productCTN);
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        CartAddProductRequest model = new CartAddProductRequest(delegate.getStore(), params, null);
        delegate.sendRequest(RequestCode.ADD_TO_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                AddToCartData addToCartData = (AddToCartData) msg.obj;
                iapHandlerListner.onAddItemToCart(addToCartData.getStatusCode());
            }

            @Override
            public void onError(final Message msg) {
                IAPLog.i(IAPLog.IAPHANDLER, "IAPHandler == addItemtoCart = onError");
                VolleyError error = (VolleyError) msg.obj;
                iapHandlerListner.onAddItemToCart(error.getLocalizedMessage());
            }
        });
    }

    public void getCartQuantity(final IAPHandlerListner iapHandlerListner) {
        //get cart from the Hybris Server
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(delegate.getStore(), null, null);
        model.setContext(mContext);
        delegate.sendRequest(RequestCode.GET_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                GetCartData getCartData = (GetCartData) msg.obj;
                if (null != getCartData) {
                    int quantity = 0;
                    int totalItems = getCartData.getCarts().get(0).getTotalItems();
                    List<Entries> entries = getCartData.getCarts().get(0).getEntries();
                    if (totalItems != 0 && null != entries) {
                        for (int i = 0; i < entries.size(); i++) {
                            quantity = quantity + entries.get(i).getQuantity();
                        }
                    }
                    iapHandlerListner.onGetCartQuantity(quantity);
                    IAPLog.i(IAPLog.IAPHANDLER, "IAPHandler == getCartQuantity = " + quantity);
                }
            }

            @Override
            public void onError(final Message msg) {
                iapHandlerListner.onGetCartQuantity(-1);
            }
        });
    }

    public void createCart() {
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        CartCreateRequest model = new CartCreateRequest(delegate.getStore(), null, null);
        delegate.sendRequest(RequestCode.CREATE_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                IAPLog.i(IAPLog.IAPHANDLER, "IAPHandler == createCart = onSuccess ");
            }

            @Override
            public void onError(final Message msg) {
                IAPLog.i(IAPLog.IAPHANDLER, "IAPHandler == createCart = onError ");
            }
        });
    }
}