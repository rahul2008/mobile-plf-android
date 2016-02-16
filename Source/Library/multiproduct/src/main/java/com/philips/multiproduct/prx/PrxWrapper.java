package com.philips.multiproduct.prx;

import android.content.Context;

import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.ProductSummaryBuilder;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.multiproduct.utils.ProductSelectionLogger;

/**
 * This is the wrapper Class , which holds responsibility to hit the PRX component by getting the relevant input's from the
 * COniguration file of the MultiProduct Module.
 * <p/>
 * Created by naveen@philips.com on 01-Feb-16.
 */
public class PrxWrapper {

    private String TAG = PrxWrapper.class.getSimpleName();

    private String mCtn = "RQ1250/17";
    private String mSectorCode = "B2C";
    private String mLocale = "en_GB";
    private String mCatalogCode = "CONSUMER";
    private Context mContext = null;


    public PrxWrapper(Context context, String ctn, String sectorCode, String locale, String catalog) {
        this.mContext = context;
        this.mCtn = ctn;
        this.mSectorCode = sectorCode;
        this.mLocale = locale;
        this.mCatalogCode = catalog;
    }

    public void requestPrxSummaryData(final PrxSummaryDataListener listener, final String requestTag) {
        if (listener == null)
            throw new IllegalStateException("PrxSummaryDataListener listener is null");

        final ProductSummaryBuilder summaryBuilder = new ProductSummaryBuilder(mCtn, requestTag);
        summaryBuilder.setmSectorCode(mSectorCode);
        summaryBuilder.setmCatalogCode(mCatalogCode);
        summaryBuilder.setmLocale(mLocale);

        RequestManager requestManager = new RequestManager();
        requestManager.init(mContext);
        requestManager.executeRequest(summaryBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {

                ProductSelectionLogger.d(TAG, "Response Success for the CTN : " + mCtn);
                SummaryModel summaryModel = (SummaryModel) responseData;
                if (summaryModel.isSuccess()) {

                    listener.onSuccess(summaryModel);

                } else
                    ProductSelectionLogger.e(TAG, "Response Failed  for the CTN as \"isSuccess\" false: " + mCtn);

            }

            @Override
            public void onResponseError(String s, int i) {
                ProductSelectionLogger.e(TAG, "Response Failed  for the CTN : " + mCtn);
                listener.onFail(s);
            }
        });


    }


}
