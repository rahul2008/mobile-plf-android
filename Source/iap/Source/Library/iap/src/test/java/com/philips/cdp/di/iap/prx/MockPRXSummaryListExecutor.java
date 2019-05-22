package com.philips.cdp.di.iap.prx;

import android.content.Context;

import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.prxclient.datamodels.summary.PRXSummaryListResponse;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductSummaryRequest;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONException;

import java.util.ArrayList;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MockPRXSummaryListExecutor extends PRXSummaryListExecutor {

    public MockPRXSummaryListExecutor(final Context context, final ArrayList<String> ctns, final AbstractModel.DataLoadListener listener) {
        super(context, ctns, listener);
    }



    public void sendSuccess(ResponseData responseData) throws JSONException {
        notifySuccess((PRXSummaryListResponse) responseData);
    }

    public void sendFailure(final PrxError prxError) {
        notifyError(prxError);
    }

}
