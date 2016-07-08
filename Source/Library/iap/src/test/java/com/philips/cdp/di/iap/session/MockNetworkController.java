/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.hybris.HybrisNetworkEssentials;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;

import org.json.JSONObject;

import static org.mockito.Mockito.mock;

public class MockNetworkController extends NetworkController {
    private Context mMockedContext;
    private IAPJsonRequest mIAPJSONRequest;

    public MockNetworkController(final Context context) {
        super(context, new HybrisNetworkEssentials());
        mMockedContext = mock(Context.class);
    }

    @Override
    void initHurlStack(final Context context) {
        MockIAPHurlStack mockIAPHurlStack = new MockIAPHurlStack(oAuthHandler);
        mockIAPHurlStack.setContext(mMockedContext);
        mIAPHurlStack = mockIAPHurlStack.getHurlStack();
    }

    @Override
    void initStore() {
        store = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore();
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