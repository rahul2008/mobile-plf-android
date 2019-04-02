/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.networkEssential.HybrisNetworkEssentials;
import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.StoreConfiguration;

import org.json.JSONObject;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;

public class MockNetworkController extends NetworkController {
    private IAPJsonRequest mIAPJSONRequest;
    @Mock
    IAPUser mIAPMockedUser;
    @Mock
    StoreConfiguration mStoreConfig;
    @Mock
    IAPDependencies mIAPDependencies;
    Context mContext;

    public MockNetworkController(final Context context, final MockIAPSetting iapSetting, final MockIAPDependencies iapDependencies) {
        super(context);
        mContext = context;
        setIapSettings(iapSetting);
        setmIapDependencies(iapDependencies);
        setNetworkEssentials(new HybrisNetworkEssentials());
    }

    @Override
    void initHurlStack(final Context context) {
        MockIAPHurlStack mockIAPHurlStack = new MockIAPHurlStack(mOAuthListener);
        mIapHurlStack = mockIAPHurlStack.getHurlStack();
    }

    @Override
    public void initStore(Context context, IAPSettings iapSettings,IAPDependencies iapDependencies) {
        mStoreListener = new MockStore(context, mock(IAPUser.class)).getStore(iapSettings,iapDependencies);
    }

    @Override
    public void hybrisVolleyCreateConnection(final Context context) {
        //just simulate
    }

    @Override
    IAPJsonRequest getIapJsonRequest(final AbstractModel model, final Response.ErrorListener error, final Response.Listener<JSONObject> response) {
        IAPJsonRequest iapJsonRequest = super.getIapJsonRequest(model, error, response);
        mIAPJSONRequest = iapJsonRequest;
        return iapJsonRequest;
    }

    @Override
    public void addToVolleyQueue(final IAPJsonRequest jsObjRequest) {
        //just simulate
    }

    public void sendSuccess(JSONObject successResponse) {
        mIAPJSONRequest.deliverResponse(successResponse);
    }

    public void sendFailure(VolleyError volleyError) {
        mIAPJSONRequest.deliverError(volleyError);
    }
}