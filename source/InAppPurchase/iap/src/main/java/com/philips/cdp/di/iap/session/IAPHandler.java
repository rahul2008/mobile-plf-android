/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.model.CartAddProductRequest;
import com.philips.cdp.di.iap.model.CartCreateRequest;
import com.philips.cdp.di.iap.model.CartCurrentInfoRequest;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.response.carts.Carts;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;
import com.philips.cdp.di.iap.utils.IAPConstant;

import java.util.HashMap;
import java.util.List;

public class IAPHandler {
    private Context mContext;

    public void initApp(Context context) {
        //We register with app context to avoid any memory leaks
        mContext = context.getApplicationContext();
    }

//    public void launchIAP(String pStoreID, String pLanguage, String pCountry, int pThemeIndex) {
//        //launching ShoppingCart Fragment
//        IAPLog.i(IAPLog.LOG, "IAPHandler == launchIAP");
//    }

    public void getCartQuantity(final IAPHandlerListner iapHandlerListner) {
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(delegate.getStore(), null, null);
        model.setContext(mContext);

        delegate.sendRequest(RequestCode.GET_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
                    createCart(iapHandlerListner);
                } else {
                    Carts getCartData = (Carts) msg.obj;
                    if (null != getCartData) {
                        int quantity = 0;
                        int totalItems = getCartData.getCarts().get(0).getTotalItems();
                        List<EntriesEntity> entries = getCartData.getCarts().get(0).getEntries();
                        if (totalItems != 0 && null != entries) {
                            for (int i = 0; i < entries.size(); i++) {
                                quantity = quantity + entries.get(i).getQuantity();
                            }
                        }
                        iapHandlerListner.onGetCartQuantity(quantity);
                    }
                }
            }

            @Override
            public void onError(final Message msg) {
                iapHandlerListner.onGetCartQuantity(-1);
            }
        });
    }

    public void createCart(final IAPHandlerListner iapHandlerListner) {
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        CartCreateRequest model = new CartCreateRequest(delegate.getStore(), null, null);
        delegate.sendRequest(RequestCode.CREATE_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                iapHandlerListner.onGetCartQuantity(IAPConstant.IAP_SUCCESS);
            }

            @Override
            public void onError(final Message msg) {
                iapHandlerListner.onGetCartQuantity(IAPConstant.IAP_ERROR);
            }
        });
    }

    public void addItemtoCart(String productCTN, final IAPHandlerListner iapHandlerListner, final boolean isFromBuyNow) {
        if (productCTN == null) return;
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.PRODUCT_CODE, productCTN);
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        CartAddProductRequest model = new CartAddProductRequest(delegate.getStore(), params, null);
        delegate.sendRequest(RequestCode.ADD_TO_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if (isFromBuyNow)
                    iapHandlerListner.onBuyNow();
                else
                    iapHandlerListner.onAddItemToCart(msg);
            }

            @Override
            public void onError(final Message msg) {
                iapHandlerListner.onAddItemToCart(msg);
            }
        });
    }

    public void buyNow(final String ctnNumber, final IAPHandlerListner iapHandlerListner) {
        if (ctnNumber == null) return;
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(delegate.getStore(), null, null);
        model.setContext(mContext);

        delegate.sendRequest(RequestCode.GET_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
                    createCart(iapHandlerListner);
                } else if (msg.obj instanceof Carts) {
                    Carts getCartData = (Carts) msg.obj;
                    if (null != getCartData) {
                        int totalItems = getCartData.getCarts().get(0).getTotalItems();
                        List<EntriesEntity> entries = getCartData.getCarts().get(0).getEntries();
                        if (totalItems != 0 && null != entries) {
                            boolean isProductAvailable = false;
                            for (int i = 0; i < entries.size(); i++) {
                                if (entries.get(i).getProduct().getCode().equalsIgnoreCase(ctnNumber)) {
                                    isProductAvailable = true;
                                    iapHandlerListner.onBuyNow();
                                    break;
                                }
                            }
                            if (!isProductAvailable)
                                addItemtoCart(ctnNumber, iapHandlerListner, true);
                        } else {
                            addItemtoCart(ctnNumber, iapHandlerListner, true);
                        }
                    }
                }
            }

            @Override
            public void onError(final Message msg) {
                iapHandlerListner.onBuyNow();
            }
        });
    }
}