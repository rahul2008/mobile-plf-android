/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.prx;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
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
    private HashMap<String,SummaryModel> mPRXSummaryData;

    //Handling error cases where Product is in Hybris but not in PRX store.
    protected volatile int mProductUpdateCount;
    protected int mProductPresentInPRX;

    public PRXSummaryExecutor(Context context, ArrayList<String> ctns, AbstractModel.DataLoadListener listener) {
        mContext = context;
        mCtns = ctns;
        mDataLoadListener = listener;
        mPRXSummaryData = new HashMap<>();
    }

    public void preparePRXDataRequest(){
        for(String ctn: mCtns){
            if (CartModelContainer.getInstance().isPRXSummaryPresent(ctn)) {
                mProductUpdateCount++;
                mProductPresentInPRX++;
                mPRXSummaryData.put(ctn, CartModelContainer.getInstance().getProductSummary(ctn));
            }else {
                executeRequest(ctn,prepareSummaryRequest(ctn));
            }
        }

        if (mDataLoadListener != null && mProductUpdateCount == mCtns.size()) {
            Message result = Message.obtain();
            result.obj = mPRXSummaryData;
            mDataLoadListener.onModelDataLoadFinished(result);
        }
    }

    protected void executeRequest(final String ctn,final ProductSummaryRequest productSummaryBuilder) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(mContext);
        mRequestManager.executeRequest(productSummaryBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                mProductUpdateCount++;
                mProductPresentInPRX++;
                SummaryModel summaryModel = (SummaryModel)responseData;
                if(summaryModel.isSuccess()) {
                    CartModelContainer.getInstance().addProductSummary(ctn, summaryModel);
                }
                notifySuccess((SummaryModel) responseData);
            }

            @Override
            public void onResponseError(final PrxError prxError) {
                mProductUpdateCount++;
                notifyError(prxError.getDescription());
            }
        });
    }

    protected void notifyError(final String error) {
        Message result = Message.obtain();
        result.obj = error;
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
        mPRXSummaryData.put(model.getData().getCtn(), model);
        if (mDataLoadListener != null && mProductUpdateCount == mCtns.size()) {
            Message result = Message.obtain();
            result.obj = mPRXSummaryData;
            mDataLoadListener.onModelDataLoadFinished(result);
        }
    }


    private ProductSummaryRequest prepareSummaryRequest(final String code) {
        String locale = HybrisDelegate.getInstance(mContext).getStore().getLocale();

        ProductSummaryRequest productSummaryRequest = new ProductSummaryRequest(code, null);
        productSummaryRequest.setSector(Sector.B2C);
        productSummaryRequest.setLocaleMatchResult(locale);
        productSummaryRequest.setCatalog(Catalog.CONSUMER);
        return productSummaryRequest;
    }

}
