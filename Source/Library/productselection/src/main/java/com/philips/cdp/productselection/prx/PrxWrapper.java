package com.philips.cdp.productselection.prx;

import android.content.Context;

import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.ProductAssetBuilder;
import com.philips.cdp.prxclient.prxdatabuilder.ProductSummaryBuilder;
import com.philips.cdp.prxclient.prxdatamodels.assets.AssetModel;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the wrapper Class , which holds responsibility to hit the PRX component by getting the relevant input's from the
 * COniguration file of the MultiProduct Module.
 * <p/>
 * Created by naveen@philips.com on 01-Feb-16.
 */
public class PrxWrapper {

    private String TAG = PrxWrapper.class.getSimpleName();


    private String mCtn = null;
    private String mSectorCode = null;
    private String mLocale = null;
    private String mCatalogCode = null;
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
        summaryBuilder.setCatalogCode(mCatalogCode);
        summaryBuilder.setLocale(mLocale);

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

    public void requestPrxAssetData(final PrxAssetDataListener listener, final String requestTag) {
        if (listener == null)
            throw new IllegalStateException("PrxAssetDataListener listener is null");

        final ProductAssetBuilder assetBuilder = new ProductAssetBuilder(mCtn, null);
        assetBuilder.setmSectorCode(mSectorCode);
        assetBuilder.setLocale(mLocale);
        assetBuilder.setCatalogCode(mCatalogCode);

        RequestManager requestManager = new RequestManager();
        requestManager.init(mContext);
        PrxLogger.enablePrxLogger(true);
        requestManager.executeRequest(assetBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {

                ProductSelectionLogger.d(TAG, "Response Success for the CTN : " + mCtn);
                AssetModel assetModel = (AssetModel) responseData;
                if (assetModel.isSuccess()) {

                    listener.onSuccess(assetModel);

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


    public void requestPrxSummaryList(final SummaryDataListener listener, String[] ctnList, final String requestTag) {
        if (listener == null)
            throw new IllegalStateException("PrxSummaryDataListener listener is null");

        final List<SummaryModel> summaryModelList = new ArrayList<SummaryModel>();

        for (int i = 0; i < ctnList.length; i++) {

            final ProductSummaryBuilder summaryBuilder = new ProductSummaryBuilder(ctnList[i], requestTag);
            summaryBuilder.setmSectorCode(mSectorCode);
            summaryBuilder.setCatalogCode(mCatalogCode);
            summaryBuilder.setLocale(mLocale);

            RequestManager requestManager = new RequestManager();
            requestManager.init(mContext);
            final String finalI = ctnList[i];
            final int ctnPosition = i;
            final int ctnListLength = ctnList.length;
            requestManager.executeRequest(summaryBuilder, new ResponseListener() {
                @Override
                public void onResponseSuccess(ResponseData responseData) {

                    ProductSelectionLogger.d(TAG, "Response Success for the CTN : " + finalI);
                    SummaryModel summaryModel = (SummaryModel) responseData;
                    if (summaryModel.isSuccess())
                        summaryModelList.add(summaryModel);
                    if (ctnPosition == (ctnListLength - 1))
                        listener.onSuccess(summaryModelList);

                }

                @Override
                public void onResponseError(String s, int i) {
                    ProductSelectionLogger.e(TAG, "Response Failed  for the CTN : " + finalI);
                    if ((ctnPosition == ctnListLength - 1))
                        listener.onSuccess(summaryModelList);
                }
            });
        }
    }
}
