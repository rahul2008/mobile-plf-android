package com.philips.cdp.di.iap.ProductCatalog;

import android.content.Context;
import android.os.Message;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.philips.cdp.di.iap.prx.PRXProductAssetBuilder;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductAssetRequest;
import com.philips.cdp.prxclient.response.ResponseData;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MockProductAssetBuilder extends PRXProductAssetBuilder{

    public MockProductAssetBuilder(final Context context, final String CTN, final AssetListener listener) {
        super(context, CTN, listener);
    }

    @Override
    public void executeRequest(final ProductAssetRequest productAssetBuilder) {

    }

    public void sendSucces(ResponseData responseData){
        notifySuccess(responseData);
    }

    public void sendFailure(final PrxError errorCode){
        notifyError(errorCode);
    }
}
