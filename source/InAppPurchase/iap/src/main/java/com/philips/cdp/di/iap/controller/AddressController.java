/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.controller;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.model.GetAddressRequest;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.RequestCode;

public class AddressController implements AbstractModel.DataLoadListener{

    private Context mContext;
    private AddressListener mAddressListener;

    public interface AddressListener {
        void onFinish();
    }

    public AddressController(Context context, AddressListener listener) {
        mContext = context;
        mAddressListener = listener;
    }

    public void getAddress(){
        HybrisDelegate delegate = HybrisDelegate.getInstance(mContext);
        GetAddressRequest model = new GetAddressRequest(delegate.getStore(), null, null);

        delegate.sendRequest(RequestCode.GET_ADDRESS, model, model);
    }

    @Override
    public void onModelDataLoadFinished(Message msg) {

    }

    @Override
    public void onModelDataError(Message msg) {

    }
}
