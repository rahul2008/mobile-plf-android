package com.philips.cdp.digitalcare.productdetails;

import android.content.Context;

import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.digitalcare.productdetails.model.listener.PrxCallback;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.ProductAssetBuilder;
import com.philips.cdp.prxclient.prxdatabuilder.ProductSummaryBuilder;
import com.philips.cdp.prxclient.prxdatamodels.assets.Asset;
import com.philips.cdp.prxclient.prxdatamodels.assets.AssetModel;
import com.philips.cdp.prxclient.prxdatamodels.assets.Assets;
import com.philips.cdp.prxclient.prxdatamodels.summary.Data;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 * Project : PRX Common Component.
 * Created by naveen@philips.com on 03-Nov-15.
 */
public class PrxProductData {

    private static final String TAG = PrxProductData.class.getSimpleName();


    private Context mContext = null;
    private String mCtn = "RQ1250/17";
    private String mSectorCode = "B2C";
    private String mLocale = "en_GB";
    private String mCatalogCode = "CONSUMER";

    private SummaryModel mSummaryModel = null;
    private AssetModel mAssetModel = null;
    private ViewProductDetailsModel mObject = null;
    private PrxCallback mCallback = null;


    public PrxProductData(Context context, PrxCallback callback) {
        this.mContext = context;
        mObject = new ViewProductDetailsModel();
        mCallback = callback;
        executeSummaryRequest();
        executeAssetRequest();
    }


    public ProductSummaryBuilder getPrxSummaryData() {
        ProductSummaryBuilder mProductSummaryBuilder = new ProductSummaryBuilder(mCtn, null);
        mProductSummaryBuilder.setmSectorCode(mSectorCode);
        mProductSummaryBuilder.setmLocale(mLocale);
        mProductSummaryBuilder.setmCatalogCode(mCatalogCode);
        mProductSummaryBuilder.setmCtnCode(mCtn);

        return mProductSummaryBuilder;
    }

    public ProductAssetBuilder getPrxAssetData() {
        ProductAssetBuilder mProductAssetBuilder = new ProductAssetBuilder(mCtn, null);
        mProductAssetBuilder.setmSectorCode(mSectorCode);
        mProductAssetBuilder.setmLocale(mLocale);
        mProductAssetBuilder.setmCatalogCode(mCatalogCode);
        mProductAssetBuilder.setmCtnCode(mCtn);

        return mProductAssetBuilder;
    }


    public void executeSummaryRequest() {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(mContext);
        mRequestManager.executeRequest(getPrxSummaryData(), new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                if (responseData != null) {
                    mSummaryModel = (SummaryModel) responseData;
                    DigiCareLogger.d(TAG, "Summary Data Received ? " + mSummaryModel.isSuccess());
                    Data data = mSummaryModel.getData();
                    mObject.setmProductName(data.getProductTitle());
                    mObject.setmCtnName(data.getCtn());
                    mObject.setmProductImage(data.getImageURL());
                    mCallback.onSummaryDataReceived(mObject);
                }
            }

            @Override
            public void onResponseError(String error) {
                DigiCareLogger.d(TAG, "Summary Error Response : " + error);
            }
        });
    }


    public void executeAssetRequest() {

        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(mContext);
        mRequestManager.executeRequest(getPrxAssetData(), new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {

                if (responseData != null) {
                    mAssetModel = (AssetModel) responseData;
                    com.philips.cdp.prxclient.prxdatamodels.assets.Data data = mAssetModel.getData();
                    Assets assets = data.getAssets();
                    List<Asset> asset = assets.getAsset();
                    List<String> mVideoList = new ArrayList<String>();
                    for (Asset assetObject : asset) {
                        String assetDescription = assetObject.getDescription();
                        String assetResource = assetObject.getAsset();
                        String assetExtension = assetObject.getExtension();
                        if (assetDescription.equalsIgnoreCase(DigitalCareConstants.VIEWPRODUCTDETAILS_PRX_ASSETS_USERMANUAL_PDF))
                            if (assetResource != null)
                                mObject.setmManualLink(assetResource);
                        if (assetExtension.equalsIgnoreCase(DigitalCareConstants.VIEWPRODUCTDETAILS_PRX_ASSETS_VIDEO_URL))
                            if (assetResource != null)
                                mVideoList.add(assetResource);
                    }
                    mObject.setmVideoLinks(mVideoList);
                    mCallback.onAssetDataReceived(mObject);
                }
            }

            @Override
            public void onResponseError(String error) {
                DigiCareLogger.d(TAG, "Asset Error Response : " + error);
            }
        });
    }
}
