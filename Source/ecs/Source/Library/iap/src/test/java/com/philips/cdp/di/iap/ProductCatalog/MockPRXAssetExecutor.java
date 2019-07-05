/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.ProductCatalog;

import android.content.Context;

import com.philips.cdp.di.iap.prx.PRXAssetExecutor;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductAssetRequest;
import com.philips.cdp.prxclient.response.ResponseData;

public class MockPRXAssetExecutor extends PRXAssetExecutor {

    public MockPRXAssetExecutor(final Context context, final String CTN, final AssetListener listener) {
        super(context, CTN, listener);
    }

    @Override
    public void executeRequest(final ProductAssetRequest productAssetBuilder) {
    }

    public void sendSuccess(ResponseData responseData) {
        notifySuccess(responseData);
    }

    public void sendFailure(final PrxError errorCode) {
        notifyError(errorCode);
    }
}
