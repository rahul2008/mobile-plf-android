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

    public void requestPrxSummaryData(final Callback listener, final String requestTag) {
        if (listener == null)
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
