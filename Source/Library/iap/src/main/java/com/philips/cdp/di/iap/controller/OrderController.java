/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.controller;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.OrderHistoryRequest;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestCode;

public class OrderController implements AbstractModel.DataLoadListener {

    private Context mContext;
    private OrderListener mOrderListener;
    private HybrisDelegate mDelegate;
    private StoreSpec mStore;

    public interface OrderListener {
        void onGetOrderList(Message msg);
    }

    public OrderController(Context context, OrderListener listener) {
        mContext = context;
        mOrderListener = listener;
    }

    public void getOrderList() {
        OrderHistoryRequest model = new OrderHistoryRequest(getStore(), null, this);
        model.setContext(mContext);
        getHybrisDelegate().sendRequest(RequestCode.GET_ORDERS, model, model);
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
        if (null == mOrderListener) return;

        int requestCode = msg.what;

        switch (requestCode) {
            case RequestCode.GET_ORDERS:
                mOrderListener.onGetOrderList(msg);
                break;
        }
    }

    HybrisDelegate getHybrisDelegate() {
        if (mDelegate == null) {
            mDelegate = HybrisDelegate.getInstance(mContext);
        }
        return mDelegate;
    }

    StoreSpec getStore() {
        if (mStore == null) {
            mStore = getHybrisDelegate().getStore();
        }
        return mStore;
    }
}
