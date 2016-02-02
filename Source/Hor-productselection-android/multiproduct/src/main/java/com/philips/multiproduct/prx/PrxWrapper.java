package com.philips.multiproduct.prx;

import android.content.Context;

import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.ProductSummaryBuilder;
import com.philips.cdp.prxclient.prxdatamodels.summary.Data;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.multiproduct.utils.MLogger;

/**
 * Created by 310190678 on 01-Feb-16.
 */
public class PrxWrapper {

    private String TAG = PrxWrapper.class.getSimpleName();

    private String mCtn = "RQ1250/17";
    private String mSectorCode = "B2C";
    private String mLocale = "en_GB";
    private String mCatalogCode = "CONSUMER";
    private Callback mListener = null;
    private Context mContext = null;


    public PrxWrapper(String ctn, String sectorCode, String locale, String catalog) {

        this.mCtn = ctn;
        this.mSectorCode = sectorCode;
        this.mLocale = locale;
        this.mCatalogCode = catalog;
    }

    public void requestPrxSummaryData(final Callback listener, String requestTag) {
        if (listener != null)
            mListener = listener;
        else
            throw new IllegalStateException("Callback listener is null");

        final ProductSummaryBuilder summaryBuilder = new ProductSummaryBuilder(mCtn, requestTag);
        summaryBuilder.setmSectorCode(mSectorCode);
        summaryBuilder.setCatalogCode(mCatalogCode);
        summaryBuilder.setLocale(mLocale);

        RequestManager requestManager = new RequestManager();
        requestManager.init(mContext);
        requestManager.executeRequest(summaryBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {

                MLogger.d(TAG, "Response Success for the CTN : " + mCtn);
                SummaryModel summaryModel = (SummaryModel) responseData;
                if (summaryModel.isSuccess()) {
                    ProductData productData = new ProductData();
                    Data summaryData = summaryModel.getData();
                    if (summaryData.getProductTitle() != null)
                        productData.setProductName(summaryData.getProductTitle());

                    if (summaryData.getCtn() != null)
                        productData.setProduuctVariant(summaryData.getCtn());

                    if (summaryData.getImageURL() != null)
                        productData.setImage(summaryData.getImageURL());


                    listener.onSuccess(productData);

                } else
                    MLogger.e(TAG, "Response Failed  for the CTN as \"isSuccess\" false: " + mCtn);

            }

            @Override
            public void onResponseError(String s, int i) {
                MLogger.e(TAG, "Response Failed  for the CTN : " + mCtn);
                listener.onFail(s);
            }
        });


    }


}
