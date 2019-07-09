/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.ecs.demouapp.ui.prx;

import android.content.Context;
import android.os.Message;


import com.ecs.demouapp.ui.analytics.ECSAnalytics;
import com.ecs.demouapp.ui.analytics.ECSAnalyticsConstant;
import com.ecs.demouapp.ui.container.CartModelContainer;
import com.ecs.demouapp.ui.model.AbstractModel;
import com.ecs.demouapp.ui.session.NetworkConstants;
import com.philips.cdp.prxclient.PRXDependencies;
import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.cdp.prxclient.datamodels.summary.PRXSummaryListResponse;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductSummaryListRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PRXSummaryListExecutor {

    Context mContext;
    ArrayList<String> mCtns;
    AbstractModel.DataLoadListener mDataLoadListener;
    private HashMap<String, Data> mPRXSummaryData;

    public PRXSummaryListExecutor(Context context, ArrayList<String> ctns, AbstractModel.DataLoadListener listener) {
        mContext = context;
        mCtns = ctns;
        mDataLoadListener = listener;
        mPRXSummaryData = new HashMap<>();
    }

    public void preparePRXDataRequest() {
        executeRequest(prepareProductSummaryListRequest(mCtns));
    }

    protected void executeRequest(final ProductSummaryListRequest productSummaryListBuilder) {
        RequestManager mRequestManager = new RequestManager();
        PRXDependencies prxDependencies = new PRXDependencies(mContext, CartModelContainer.getInstance().getAppInfraInstance(), ECSAnalyticsConstant.COMPONENT_NAME);
        mRequestManager.init(prxDependencies);
        mRequestManager.executeRequest(productSummaryListBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                notifySuccess(responseData);
            }

            @Override
            public void onResponseError(final PrxError prxError) {
                notifyError(prxError);
            }
        });
    }

    protected void notifyError(PrxError prxError) {
        Message result = Message.obtain();
        result.obj = prxError.getDescription();

        ECSAnalytics.trackAction(ECSAnalyticsConstant.SEND_DATA,
                ECSAnalyticsConstant.ERROR, ECSAnalyticsConstant.PRX + "_" + prxError.getStatusCode() + prxError.getDescription());
        mDataLoadListener.onModelDataError(result);
    }

    protected void notifySuccess(ResponseData model) {

        if (model != null) {

            PRXSummaryListResponse prxSummaryListResponse = (PRXSummaryListResponse) model;
            CartModelContainer.getInstance().setPRXSummaryList(prxSummaryListResponse.getData());

            if (prxSummaryListResponse.getData()!=null && !prxSummaryListResponse.getData().isEmpty()) {

                for (Data data : prxSummaryListResponse.getData())
                    mPRXSummaryData.put(data.getCtn(), data);
            }
        }
        Message result = Message.obtain();
        result.obj = mPRXSummaryData;
        mDataLoadListener.onModelDataLoadFinished(result);
    }

    private ProductSummaryListRequest prepareProductSummaryListRequest(List<String> ctns) {
        ProductSummaryListRequest productSummaryListRequest = new ProductSummaryListRequest(ctns, PrxConstants.Sector.B2C, PrxConstants.Catalog.CONSUMER, null);
        productSummaryListRequest.setRequestTimeOut(NetworkConstants.DEFAULT_TIMEOUT_MS);
        return productSummaryListRequest;
    }
}
