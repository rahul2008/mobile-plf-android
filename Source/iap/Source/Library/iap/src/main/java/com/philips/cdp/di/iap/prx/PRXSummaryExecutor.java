/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.prx;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.prxclient.PRXDependencies;
import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductSummaryRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PRXSummaryExecutor {

    Context mContext;
    ArrayList<String> mCtns;
    AbstractModel.DataLoadListener mDataLoadListener;
    private HashMap<String, SummaryModel> mPRXSummaryData;

    //Handling error cases where Product is in Hybris but not in PRX store.
    protected volatile int mProductUpdateCount;
    protected int mProductPresentInPRX;

    public PRXSummaryExecutor(Context context, ArrayList<String> ctns, AbstractModel.DataLoadListener listener) {
        mContext = context;
        mCtns = ctns;
        mDataLoadListener = listener;
        mPRXSummaryData = new HashMap<>();
    }

    public void preparePRXDataRequest() {
        for (String ctn : mCtns) {
            executeRequest(ctn, prepareSummaryRequest(ctn));
        }

        if (mDataLoadListener != null && mProductUpdateCount == mCtns.size()) {
            Message result = Message.obtain();
            result.obj = mPRXSummaryData;
            mDataLoadListener.onModelDataLoadFinished(result);
        }
    }

    protected void executeRequest(final String ctn, final ProductSummaryRequest productSummaryBuilder) {
        RequestManager mRequestManager = new RequestManager();
        PRXDependencies prxDependencies = new PRXDependencies(mContext, CartModelContainer.getInstance().getAppInfraInstance(), IAPAnalyticsConstant.COMPONENT_NAME);
        mRequestManager.init(prxDependencies);
        mRequestManager.executeRequest(productSummaryBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                mProductUpdateCount++;
                mProductPresentInPRX++;
                SummaryModel summaryModel = (SummaryModel) responseData;
                if (summaryModel.isSuccess()) {
                    CartModelContainer.getInstance().addProductSummary(ctn, summaryModel);
                }
                notifySuccess((SummaryModel) responseData);
            }

            @Override
            public void onResponseError(final PrxError prxError) {
                mProductUpdateCount++;
                if (prxError.getStatusCode() == 404) {
                    notifyError(ctn, prxError.getStatusCode(), "Product not found in your store");
                } else
                    notifyError(ctn, prxError.getStatusCode(), prxError.getDescription());
            }
        });
    }

    protected void notifyError(final String ctn, final int errorCode, final String error) {
        Message result = Message.obtain();
        result.obj = error;

        IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                IAPAnalyticsConstant.ERROR, IAPAnalyticsConstant.PRX + ctn + "_" + errorCode + error);

        if (mDataLoadListener != null && mProductUpdateCount == mCtns.size()) {
            if (mProductPresentInPRX > 0) {
                result.obj = mPRXSummaryData;
                mDataLoadListener.onModelDataLoadFinished(result);
            } else {
                mDataLoadListener.onModelDataError(result);
            }
        }
    }

    protected void notifySuccess(SummaryModel model) {
        if (model.getData() != null) {
            mPRXSummaryData.put(model.getData().getCtn(), model);
        }
        if (mDataLoadListener != null && mProductUpdateCount == mCtns.size()) {
            Message result = Message.obtain();
            result.obj = mPRXSummaryData;
            mDataLoadListener.onModelDataLoadFinished(result);
        }
    }

    private ProductSummaryRequest prepareSummaryRequest(final String code) {
//        String locale = HybrisDelegate.getInstance(mContext).getStore().getLocale();

        ProductSummaryRequest productSummaryRequest = new ProductSummaryRequest(code, null);
        productSummaryRequest.setSector(PrxConstants.Sector.B2C);
        // productSummaryRequest.setLocaleMatchResult(locale);
        productSummaryRequest.setCatalog(PrxConstants.Catalog.CONSUMER);
        return productSummaryRequest;
    }

}
