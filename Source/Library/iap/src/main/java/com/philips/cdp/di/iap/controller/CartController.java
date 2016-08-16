/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.controller;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.DeleteCartRequest;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestCode;

public class CartController implements AbstractModel.DataLoadListener {

    private Context mContext;
    private CartListener mCartListener;
    private HybrisDelegate mDelegate;
    private StoreSpec mStore;

    public interface CartListener {
        void onCreateCart(Message msg);

        void onAddToCart(Message msg);

        void onGetCarts(Message msg);

        void onBuyProduct(Message msg);

        void onDeleteCart(Message msg);

        void onGetCurrentCart(Message msg);
    }

    public CartController(Context context, CartListener listener) {
        mContext = context;
        mCartListener = listener;
    }

    public void deleteCart() {
        DeleteCartRequest model = new DeleteCartRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.DELETE_CART, model, model);
    }

    public void getCurrentCart(){

    }

    @Override
    public void onModelDataLoadFinished(Message msg) {
        sendListener(msg);
    }

    @Override
    public void onModelDataError(Message msg) {
        sendListener(msg);
    }

    private void sendListener(Message msg) {
        int requestCode = msg.what;

        if (null == mCartListener) return;

        switch (requestCode) {
            case RequestCode.CREATE_CART:
                mCartListener.onCreateCart(msg);
                break;
            case RequestCode.ADD_TO_CART:
                mCartListener.onAddToCart(msg);
                break;
            case RequestCode.GET_CART:
                mCartListener.onGetCarts(msg);
                break;
            case RequestCode.DELETE_CART:
                mCartListener.onDeleteCart(msg);
                break;
        }
    }


    public void setHybrisDelegate(HybrisDelegate delegate) {
        mDelegate = delegate;
    }

    HybrisDelegate getHybrisDelegate() {
        if (mDelegate == null) {
            mDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mDelegate;
    }

    public void setStore(StoreSpec store) {
        mStore = store;
    }

    StoreSpec getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }
}
