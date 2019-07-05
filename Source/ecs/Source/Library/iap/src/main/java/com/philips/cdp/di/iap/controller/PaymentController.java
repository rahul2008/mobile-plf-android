/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.controller;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.GetPaymentDetailRequest;
import com.philips.cdp.di.iap.model.PaymentRequest;
import com.philips.cdp.di.iap.model.PlaceOrderRequest;
import com.philips.cdp.di.iap.model.SetPaymentDetailsRequest;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.ModelConstants;

import java.util.HashMap;

public class PaymentController implements AbstractModel.DataLoadListener {

    private Context mContext;
    private PaymentListener mPaymentListener;
    private MakePaymentListener mMakePaymentListener;
    private HybrisDelegate mDelegate;
    private StoreListener mStore;

    public interface PaymentListener {
        void onGetPaymentDetails(Message msg);

        void onSetPaymentDetails(Message msg);
    }

    public interface MakePaymentListener {
        void onMakePayment(Message msg);

        void onPlaceOrder(Message msg);
    }

    public PaymentController(Context context, PaymentListener listener) {
        mContext = context;
        mPaymentListener = listener;
    }

    public PaymentController(Context context, MakePaymentListener listener) {
        mContext = context;
        mMakePaymentListener = listener;
    }

    public void getPaymentDetails() {
        GetPaymentDetailRequest model = new GetPaymentDetailRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_PAYMENT_DETAILS, model, model);
    }

    public void setPaymentDetails(String paymentId) {
        HashMap<String, String> params = new HashMap<>();
        params.put(ModelConstants.PAYMENT_DETAILS_ID, paymentId);

        SetPaymentDetailsRequest model = new SetPaymentDetailsRequest(getStore(), params, this);
        getHybrisDelegate().sendRequest(RequestCode.SET_PAYMENT_DETAILS, model, model);
    }

    public void placeOrder(String pSecurityCode) {
        final HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        HashMap<String, String> query = new HashMap<>();
        if (pSecurityCode != null)
            query.put(ModelConstants.SECURITY_CODE, pSecurityCode);

        PlaceOrderRequest request = new PlaceOrderRequest(delegate.getStore(), query, this);
        delegate.sendRequest(RequestCode.PLACE_ORDER, request, request);
    }

    public void makPayment(String orderID) {
        final HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);

        HashMap<String, String> query = new HashMap<>();
        query.put(ModelConstants.ORDER_NUMBER, orderID);

        PaymentRequest request = new PaymentRequest(delegate.getStore(), query, this);
        delegate.sendRequest(RequestCode.MAKE_PAYMENT, request, request);
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
        switch (requestCode) {
            case RequestCode.GET_PAYMENT_DETAILS:
                mPaymentListener.onGetPaymentDetails(msg);
                break;
            case RequestCode.SET_PAYMENT_DETAILS:
                mPaymentListener.onSetPaymentDetails(msg);
                break;
            case RequestCode.PLACE_ORDER:
                mMakePaymentListener.onPlaceOrder(msg);
                break;
            case RequestCode.MAKE_PAYMENT:
                mMakePaymentListener.onMakePayment(msg);
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

    public void setStore(StoreListener store) {
        mStore = store;
    }

    StoreListener getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }
}
