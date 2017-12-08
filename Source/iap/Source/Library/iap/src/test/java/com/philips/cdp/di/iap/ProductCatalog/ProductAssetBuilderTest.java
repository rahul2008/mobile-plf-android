/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.ProductCatalog;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.prx.PRXAssetExecutor;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductAssetRequest;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


@RunWith(RobolectricTestRunner.class)
public class ProductAssetBuilderTest implements PRXAssetExecutor.AssetListener {
    @Mock
    Context mContext;
    private MockPRXAssetExecutor builder;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        TestUtils.getStubbedHybrisDelegate();
    }

    @Test
    public void testAssetSuccessResponse() throws JSONException {
        PrxRequest productAssetRequest = new ProductAssetRequest("125", null);
        builder = new MockPRXAssetExecutor(mContext, "HX9033/64", this);
        builder.build();

        JSONObject obj = new JSONObject(TestUtils.readFile(ProductAssetBuilderTest
                .class, "asset_success_response.txt"));
        ResponseData responseData = productAssetRequest.getResponseData(obj);
        builder.sendSuccess(responseData);
    }

    @Test
    public void testAssetErrorResponse() throws JSONException {
        builder = new MockPRXAssetExecutor(mContext, "HX9033/64", this);
        builder.build();
        builder.sendFailure(new PrxError("fail", 500));
    }

    @Test
    public void testAssetResponseWhenNoInternet() throws JSONException {
        builder = new MockPRXAssetExecutor(mContext, "HX9033/64", this);
        builder.build();
        PrxError fail = new PrxError("fail", PrxError.PrxErrorType.NO_INTERNET_CONNECTION.getId());
        builder.sendFailure(fail);
    }

    @Test
    public void testAssetRequestTimeOut() throws JSONException {
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
