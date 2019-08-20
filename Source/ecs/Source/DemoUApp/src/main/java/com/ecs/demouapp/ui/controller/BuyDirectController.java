/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.controller;

import android.content.Context;
import android.os.Message;


import com.ecs.demouapp.ui.session.RequestCode;
import com.ecs.demouapp.ui.store.StoreListener;

public class BuyDirectController {
    private BuyDirectListener mBuyDirectListener;
    private Context mContext;
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
