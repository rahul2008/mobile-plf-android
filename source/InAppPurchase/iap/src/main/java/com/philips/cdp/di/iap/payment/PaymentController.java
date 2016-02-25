/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.payment;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.GetPaymentDetailRequest;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.store.Store;

public class PaymentController implements AbstractModel.DataLoadListener {

    private Context mContext;
    private PaymentListener mPaymentListener;
    private HybrisDelegate mDelegate;
    private Store mStore;


    public interface PaymentListener {
        void onGetPaymentDetails(Message msg);
    }

    public PaymentController(Context context, PaymentListener listener) {
        mContext = context;
        mPaymentListener = listener;
    }

    public void getPaymentDetails() {
        GetPaymentDetailRequest model = new GetPaymentDetailRequest(getStore(), null, this);
        getHybrisDelegate().sendRequest(RequestCode.GET_PAYMENT_DETAILS, model, model);
    }


    @Override
    public void onModelDataLoadFinished(Message msg) {
        int requestCode = msg.what;
        switch (requestCode) {
            case RequestCode.GET_PAYMENT_DETAILS:
                mPaymentListener.onGetPaymentDetails(msg);
                break;
        }
    }

    @Override
    public void onModelDataError(Message msg) {
        int requestCode = msg.what;
        switch (requestCode) {
            case RequestCode.GET_PAYMENT_DETAILS:
                mPaymentListener.onGetPaymentDetails(msg);
                break;
        }
    }

    void setHybrisDelegate(HybrisDelegate delegate) {
        mDelegate = delegate;
    }

    HybrisDelegate getHybrisDelegate() {
        if (mDelegate == null) {
            mDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mDelegate;
    }

    void setStore(Store store) {
        mStore = store;
    }

    Store getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }
}
