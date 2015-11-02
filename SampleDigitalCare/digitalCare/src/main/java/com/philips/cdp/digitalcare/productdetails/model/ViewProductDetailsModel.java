package com.philips.cdp.digitalcare.productdetails.model;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.cdp.prx.assets.AssetModel;
import com.cdp.prx.databuilder.ProductAssetBuilder;
import com.cdp.prx.databuilder.ProductSummaryBuilder;
import com.cdp.prx.summary.SummaryModel;

import java.util.List;

import horizontal.cdp.prxcomponent.RequestManager;
import horizontal.cdp.prxcomponent.ResponseData;
import horizontal.cdp.prxcomponent.listeners.ResponseListener;

/**
 * Description : Class gets the data from the PRX Component & created the object of this class required for the "View Product Details UI Screen".
 *
 * Created by naveen@philips.com on 02-Nov-15.
 */
public class ViewProductDetailsModel {

    private static final String TAG = ViewProductDetailsModel.class.getSimpleName();

    private Context mContext = null;
    private String mProductName = null;
    private String mCtnName = null;
    private ImageView mProductImage = null;
    private String mManualLink = null;
    private String mProductInfoLink = null;
    private List<String> mVideoLinks = null;

    private String mCtn = "RQ1250/17";
    private String mSectorCode = "B2C";
    private String mLocale = "en_GB";
    private String mCatalogCode = "CONSUMER";


    public ViewProductDetailsModel(Context context)
    {
        mContext = context;
    }

    protected void initPrxRequests()
    {
        getSummaryModel();
        getAssetModel();
    }

    protected void getSummaryModel()
    {
        ProductSummaryBuilder mProductSummaryBuilder = new ProductSummaryBuilder(mCtn, null);
        mProductSummaryBuilder.setmSectorCode(mSectorCode);
        mProductSummaryBuilder.setmLocale(mLocale);
        mProductSummaryBuilder.setmCatalogCode(mCatalogCode);
        mProductSummaryBuilder.setmCtnCode(mCtn);

        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(mContext);
        mRequestManager.executeRequest(mProductSummaryBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {

            }

            @Override
            public void onResponseError(String error) {
                Log.d(TAG, "Error Response Data : " + error);
            }
        });
    }


    protected void getAssetModel()
    {
        ProductSummaryBuilder mProductAssetBuilder = new ProductSummaryBuilder(mCtn, null);
        mProductAssetBuilder.setmSectorCode(mSectorCode);
        mProductAssetBuilder.setmLocale(mLocale);
        mProductAssetBuilder.setmCatalogCode(mCatalogCode);
        mProductAssetBuilder.setmCtnCode(mCtn);

        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(mContext);
        mRequestManager.executeRequest(mProductAssetBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {

            }

            @Override
            public void onResponseError(String error) {
                Log.d(TAG, "Error Response Data : " + error);
            }
        });
    }


}
