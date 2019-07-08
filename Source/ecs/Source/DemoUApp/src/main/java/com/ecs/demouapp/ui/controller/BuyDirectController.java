/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.controller;

import android.content.Context;
import android.os.Message;


import com.ecs.demouapp.ui.model.AbstractModel;
import com.ecs.demouapp.ui.model.CartAddProductRequest;
import com.ecs.demouapp.ui.model.CartCreateRequest;
import com.ecs.demouapp.ui.model.DeleteCartRequest;
import com.ecs.demouapp.ui.model.GetDeliveryModesRequest;
import com.ecs.demouapp.ui.model.GetPaymentDetailRequest;
import com.ecs.demouapp.ui.model.GetRegionsRequest;
import com.ecs.demouapp.ui.model.GetUserRequest;
import com.ecs.demouapp.ui.model.SetDeliveryAddressModeRequest;
import com.ecs.demouapp.ui.model.SetDeliveryAddressRequest;
import com.ecs.demouapp.ui.model.SetPaymentDetailsRequest;
import com.ecs.demouapp.ui.session.HybrisDelegate;
import com.ecs.demouapp.ui.session.RequestCode;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ModelConstants;

import java.util.HashMap;

public class BuyDirectController implements AbstractModel.DataLoadListener {
    private BuyDirectListener mBuyDirectListener;
    private Context mContext;
    private HybrisDelegate mDelegate;
    private StoreListener mStore;


    public BuyDirectController(Context pContext, BuyDirectListener pBuyDirectListener) {
        mContext = pContext;
        mBuyDirectListener = pBuyDirectListener;
    }

    public interface BuyDirectListener {
        void onCreateCart(Message msg);

        void onAddToCart(Message msg);

        void onGetRegions(Message msg);

        void onGetUser(Message msg);

        void onSetDeliveryAddress(Message msg);

        void onGetDeliveryMode(Message msg);

        void onSetDeliveryMode(Message msg);

        void onGetPaymentMode(Message msg);

        void onSetPaymentMode(Message msg);

        void onDeleteCart(Message msg);
    }

    public void createCart() {
        CartCreateRequest model = new CartCreateRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.CREATE_CART, model, model);
    }

    public void addToCart(String productCTN) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.PRODUCT_CODE, productCTN);
        CartAddProductRequest model = new CartAddProductRequest(getStore(), params, this);
        getHybrisDelegate().sendRequest(RequestCode.ADD_TO_CART, model, model);
    }

    public void getRegions(){
        GetRegionsRequest model = new GetRegionsRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_REGIONS, model, model);
    }

    public void getUser() {
        GetUserRequest model = new GetUserRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_USER, model, model);
    }

    public void setDeliveryAddress(String pAddressId) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.ADDRESS_ID, pAddressId);
        SetDeliveryAddressRequest model = new SetDeliveryAddressRequest(getStore(), params, this);
        getHybrisDelegate().sendRequest(RequestCode.SET_DELIVERY_ADDRESS, model, model);
    }

    public void getDeliveryModes() {
        GetDeliveryModesRequest model = new GetDeliveryModesRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_DELIVERY_MODE, model, model);
    }

    public void setDeliveryMode(String deliveryMode) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.DELIVERY_MODE_ID, deliveryMode);

        SetDeliveryAddressModeRequest model = new SetDeliveryAddressModeRequest(getStore(), params, this);
        getHybrisDelegate().sendRequest(RequestCode.SET_DELIVERY_MODE, model, model);
    }

    public void getPaymentMode() {
        GetPaymentDetailRequest model = new GetPaymentDetailRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_PAYMENT_DETAILS, model, model);
    }

    public void setPaymentMode(String mPaymentDetailId) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.PAYMENT_DETAILS_ID, mPaymentDetailId);

        SetPaymentDetailsRequest model = new SetPaymentDetailsRequest(getStore(), params, this);
        getHybrisDelegate().sendRequest(RequestCode.SET_PAYMENT_DETAILS, model, model);
    }

    public void deleteCart(){
        DeleteCartRequest model = new DeleteCartRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.DELETE_CART, model, model);
    }

    @Override
    public void onModelDataLoadFinished(Message msg) {
        sendListener(msg);
    }

    @Override
    public void onModelDataError(Message msg) {
        sendListener(msg);
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

    public void setStore(StoreListener store) {
        mStore = store;
    }

    StoreListener getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }

    private void sendListener(Message msg) {
        int requestCode = msg.what;

        if (null == mBuyDirectListener) return;

        switch (requestCode) {
            case RequestCode.CREATE_CART:
                mBuyDirectListener.onCreateCart(msg);
                break;
            case RequestCode.ADD_TO_CART:
                mBuyDirectListener.onAddToCart(msg);
                break;
            case RequestCode.GET_REGIONS:
                mBuyDirectListener.onGetRegions(msg);
                break;
            case RequestCode.GET_USER:
                mBuyDirectListener.onGetUser(msg);
                break;
            case RequestCode.SET_DELIVERY_ADDRESS:
                mBuyDirectListener.onSetDeliveryAddress(msg);
                break;
            case RequestCode.GET_DELIVERY_MODE:
                mBuyDirectListener.onGetDeliveryMode(msg);
                break;
            case RequestCode.SET_DELIVERY_MODE:
                mBuyDirectListener.onSetDeliveryMode(msg);
                break;
            case RequestCode.GET_PAYMENT_DETAILS:
                mBuyDirectListener.onGetPaymentMode(msg);
                break;
            case RequestCode.SET_PAYMENT_DETAILS:
                mBuyDirectListener.onSetPaymentMode(msg);
                break;
            case RequestCode.DELETE_CART:
                mBuyDirectListener.onDeleteCart(msg);
                break;
        }
    }
}
