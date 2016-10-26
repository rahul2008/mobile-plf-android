package com.philips.cdp.di.iap.ProductCatalog;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.prx.PRXAssetExecutor;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductAssetRequest;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(RobolectricTestRunner.class)
public class ProductAssetBuilderTest implements
        PRXAssetExecutor.AssetListener {

    @Mock
    Context mContext;
    MockPRXAssetExecutor builder;
    MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mNetworkController = new MockNetworkController(mContext, new MockIAPSetting(mContext));
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(null);
    }

    @Test
    public void makeAssetRequestSuccess() throws JSONException {
        PrxRequest productAssetRequest = new ProductAssetRequest("125", null);
        builder = new MockPRXAssetExecutor(mContext, "HX9033/64", this);
        builder.build();

        JSONObject obj = new JSONObject(TestUtils.readFile(ProductAssetBuilderTest
                .class, "asset_success_response.txt"));
        ResponseData responseData = productAssetRequest.getResponseData(obj);

        builder.sendSucces(responseData);
    }


    @Test
    public void makeAssetRequestError() throws JSONException {
        builder = new MockPRXAssetExecutor(mContext, "HX9033/64", this);
        builder.build();
        builder.sendFailure(new PrxError("fail", 500));
    }

    @Test
    public void makeAssetRequestNoInternet() throws JSONException {
        builder = new MockPRXAssetExecutor(mContext, "HX9033/64", this);
        builder.build();
        PrxError fail = new PrxError("fail", PrxError.PrxErrorType.NO_INTERNET_CONNECTION.getId());
        builder.sendFailure(fail);
    }

    @Test
    public void makeAssetRequestTimeOut() throws JSONException {
        builder = new MockPRXAssetExecutor(mContext, "HX9033/64", this);
        builder.build();
        PrxError fail = new PrxError("fail", PrxError.PrxErrorType.TIME_OUT.getId());
        builder.sendFailure(fail);
    }


    @Override
    public void onFetchAssetSuccess(final Message msg) {
        assertTrue(true);
    }

    @Override
    public void onFetchAssetFailure(final Message msg) {
        assertFalse(false);
    }
}
