/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.activity.IAPActivity;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.model.CartAddProductRequest;
import com.philips.cdp.di.iap.model.CartCreateRequest;
import com.philips.cdp.di.iap.model.CartCurrentInfoRequest;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.response.carts.Carts;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.tagging.Tagging;

import java.util.HashMap;
import java.util.List;

public class IAPHandler {

    public void initIAP(Context context, String countryCode, final IAPHandlerListener listener) {
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        delegate.getStore().initStoreConfig(countryCode, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                listener.onSuccess(IAPConstant.IAP_SUCCESS);
            }

            @Override
            public void onError(final Message msg) {
                listener.onFailure(getIAPErrorCode(msg));
            }
        });
    }

    public void launchIAP(Context context, int themeIndex) {
        //Set component version key and value for InAppPurchase
        Tagging.setComponentVersionKey(IAPAnalyticsConstant.COMPONENT_VERSION);
        Tagging.setComponentVersionVersionValue("In app purchase "+ BuildConfig.VERSION_NAME);

        Intent intent = new Intent(context, IAPActivity.class);
        intent.putExtra(IAPConstant.IAP_KEY_ACTIVITY_THEME, themeIndex);
        context.startActivity(intent);
    }

    public void getProductCartCount(final Context context, final IAPHandlerListener
            iapHandlerListener) {
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(delegate.getStore(), null, null);
        model.setContext(context);

        delegate.sendRequest(RequestCode.GET_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
                    createCart(context, iapHandlerListener, null, 0, false);
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
                        if (iapHandlerListener != null) {
                            iapHandlerListener.onSuccess(quantity);
                        }
                    }
                }
            }

            @Override
            public void onError(final Message msg) {
                if (iapHandlerListener != null) {
                    iapHandlerListener.onFailure(getIAPErrorCode(msg));
                }
            }
        });
    }

    private void createCart(final Context context, final IAPHandlerListener iapHandlerListener, final String ctnNumber, final int themeIndex, final boolean isBuy) {
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        CartCreateRequest model = new CartCreateRequest(delegate.getStore(), null, null);
        delegate.sendRequest(RequestCode.CREATE_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if (isBuy) {
                    addProductToCart(context, ctnNumber, iapHandlerListener, true, themeIndex);
                } else {
                    if (iapHandlerListener != null) {
                        iapHandlerListener.onSuccess(0);
                    }
                }
            }

            @Override
            public void onError(final Message msg) {
                if (iapHandlerListener != null) {
                    iapHandlerListener.onFailure(getIAPErrorCode(msg));
                }
            }
        });
    }

    public void addProductToCart(final Context context, String productCTN, final IAPHandlerListener
            iapHandlerListener) {
        addProductToCart(context, productCTN, iapHandlerListener, false, -1);
    }

    private void addProductToCart(final Context context, String productCTN, final IAPHandlerListener iapHandlerListener,
                                  final boolean isFromBuyNow, final int themeIndex) {
        if (productCTN == null) return;
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.PRODUCT_CODE, productCTN);
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        CartAddProductRequest model = new CartAddProductRequest(delegate.getStore(), params, null);
        delegate.sendRequest(RequestCode.ADD_TO_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if (isFromBuyNow) {
                    launchIAP(context, themeIndex);
                    if (iapHandlerListener != null) {
                        iapHandlerListener.onSuccess(0);
                    }
                } else if (iapHandlerListener != null) {
                    iapHandlerListener.onSuccess(0);
                }
            }

            @Override
            public void onError(final Message msg) {
                if (iapHandlerListener != null) {
                    iapHandlerListener.onFailure(getIAPErrorCode(msg));
                }
            }
        });
    }

    public void buyProduct(final Context context, final String ctnNumber, final IAPHandlerListener
            iapHandlerListener, final int themeIndex) {
        if (ctnNumber == null) return;
        HybrisDelegate delegate = HybrisDelegate.getInstance(context);
        CartCurrentInfoRequest model = new CartCurrentInfoRequest(delegate.getStore(), null, null);
        model.setContext(context);

        delegate.sendRequest(RequestCode.GET_CART, model, new RequestListener() {
            @Override
            public void onSuccess(final Message msg) {
                if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
                    createCart(context, iapHandlerListener, ctnNumber, themeIndex, true);
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
                                    launchIAP(context, themeIndex);
                                    break;
                                }
                            }
                            if (!isProductAvailable)
                                addProductToCart(context, ctnNumber, iapHandlerListener, true,
                                        themeIndex);
                            if (iapHandlerListener != null) {
                                iapHandlerListener.onSuccess(0);
                            }
                        } else {
                            addProductToCart(context, ctnNumber, iapHandlerListener, true,
                                    themeIndex);
                        }
                    }
                }
            }

            @Override
            public void onError(final Message msg) {
                if (iapHandlerListener != null) {
                    iapHandlerListener.onFailure(getIAPErrorCode(msg));
                }
            }
        });
    }

    private int getIAPErrorCode(Message msg) {
        if (msg.obj instanceof IAPNetworkError) {
            return ((IAPNetworkError) msg.obj).getIAPErrorCode();
        }
        return IAPConstant.IAP_ERROR_UNKNOWN;
    }
}