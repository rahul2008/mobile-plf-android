package com.philips.cdp.di.iap.prx;

import android.content.Context;

import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.prx.PRXDataBuilder;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductSummaryRequest;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MockPRXDataBuilder extends PRXDataBuilder{

    public MockPRXDataBuilder(final Context context, final ArrayList<String> ctns, final AbstractModel.DataLoadListener listener) {
        super(context, ctns, listener);
    }

    @Override
    protected void executeRequest(final String ctn,final ProductSummaryRequest productSummaryBuilder) {
        //simulate
    }

    public void sendSuccess(ResponseData responseData) throws JSONException {
        mProudctUpdateCount++;
        mProductPresentInPRX++;
        notifySucces((SummaryModel) responseData);
    }

    public void sendFailure(final PrxError prxError) {
        mProudctUpdateCount++;
        notifyError(prxError.getDescription());
    }

}
