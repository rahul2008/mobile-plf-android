/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.mecHandler;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.mec.integration.MECInterface;
import com.philips.cdp.di.mec.integration.MECListener;
import com.philips.cdp.di.mec.session.MECNetworkError;
import com.philips.cdp.di.mec.utils.MECConstant;

public class HybrisHandler extends MECInterface implements MECExposedAPI {
    private Context mContext;

    public HybrisHandler(Context context) {
        mContext = context;
    }

    @Override
    public void getProductCartCount(final MECListener mecListener) {

    }

    private void getProductCount(final MECListener iapListener) {

    }

    @Override
    public void getCompleteProductList(final MECListener iapListener) {

    }

    public int getIAPErrorCode(Message msg) {
        if (msg.obj instanceof MECNetworkError) {
            return ((MECNetworkError) msg.obj).getIAPErrorCode();
        }
        return MECConstant.MEC_ERROR_UNKNOWN;
    }

    /*public boolean isStoreInitialized() {
        return HybrisDelegate.getInstance(mContext).getStore().isStoreInitialized();
    }*/
}
