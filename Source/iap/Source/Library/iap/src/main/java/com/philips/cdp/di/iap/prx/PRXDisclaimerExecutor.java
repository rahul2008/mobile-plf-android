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
import com.philips.cdp.prxclient.datamodels.Disclaimer.DisclaimerModel;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductDisclaimerRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import java.util.ArrayList;
import java.util.HashMap;

public class PRXDisclaimerExecutor {

    Context mContext;
    ArrayList<String> mCtns;
    AbstractModel.DataLoadListener mDataLoadListener;
    private HashMap<String, DisclaimerModel> mDisclaimerData;


    //Handling error cases where Product is in Hybris but not in PRX store.
    protected volatile int mProductUpdateCount;
    protected int mProductPresentInPRX;

    public PRXDisclaimerExecutor(Context mContext, ArrayList<String> mCtns, AbstractModel.DataLoadListener mDataLoadListener) {
        this.mContext = mContext;
        this.mCtns = mCtns;
        this.mDataLoadListener = mDataLoadListener;
        this.mDisclaimerData = new HashMap<>();
    }

    public void preparePRXDataRequest() {
        for (String ctn : mCtns) {
            executeRequest(ctn, prepareDisclaimerRequest(ctn));
        }

        if (mDataLoadListener != null && mProductUpdateCount == mCtns.size()) {
            Message result = Message.obtain();
            result.obj = mDisclaimerData;
            mDataLoadListener.onModelDataLoadFinished(result);
        }
    }

    protected void executeRequest(final String ctn, final ProductDisclaimerRequest productDisclaimerRequestBuilder) {
        RequestManager mRequestManager = new RequestManager();
        PRXDependencies prxDependencies = new PRXDependencies(mContext, CartModelContainer.getInstance().getAppInfraInstance(), IAPAnalyticsConstant.COMPONENT_NAME);
        mRequestManager.init(prxDependencies);
        mRequestManager.executeRequest(productDisclaimerRequestBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                mProductUpdateCount++;
                mProductPresentInPRX++;
                DisclaimerModel disclaimerModel = (DisclaimerModel) responseData;
                if (disclaimerModel.isSuccess()) {
                    CartModelContainer.getInstance().addProductDisclaimer(ctn, disclaimerModel);
                }
                notifySuccess((DisclaimerModel) responseData);
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
                result.obj = mDisclaimerData;
                mDataLoadListener.onModelDataLoadFinished(result);
            } else {
                mDataLoadListener.onModelDataError(result);
            }
        }
    }

    protected void notifySuccess(DisclaimerModel model) {
        if (model.getData() != null) {
           // mDisclaimerData.put(model.getData().getCtn(), model);
        }
        if (mDataLoadListener != null && mProductUpdateCount == mCtns.size()) {
            Message result = Message.obtain();
            result.obj = mDisclaimerData;
            mDataLoadListener.onModelDataLoadFinished(result);
        }
    }

    private ProductDisclaimerRequest prepareDisclaimerRequest(final String code) {
//        String locale = HybrisDelegate.getInstance(mContext).getStore().getLocale();

        ProductDisclaimerRequest productDisclaimerRequest = new ProductDisclaimerRequest(code, null);
        productDisclaimerRequest.setSector(PrxConstants.Sector.B2C);
        // productDisclaimerRequest.setLocaleMatchResult(locale);
        productDisclaimerRequest.setCatalog(PrxConstants.Catalog.CONSUMER);
        return productDisclaimerRequest;
    }
}
