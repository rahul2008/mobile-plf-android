package com.philips.cdp.digitalcare.productdetails;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;

import com.philips.cdp.digitalcare.ConsumerProductInfo;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.listeners.IPrxCallback;
import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
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
    public static final String VIEWPRODUCTDETAILS_PRX_ASSETS_USERMANUAL_PDF = "User manual";
    public static final String VIEWPRODUCTDETAILS_PRX_ASSETS_USERMANUAL_QSG_PDF = "qsg";
    public static final String VIEWPRODUCTDETAILS_PRX_ASSETS_VIDEO_URL = "mp4";


    private Activity mActivity = null;
    private IPrxCallback mPrxCallback = null;
/*    private String mCtn = "RQ1250/17";
    private String mSectorCode = "B2C";
    private String mLocale = "en_GB";
    private String mCatalogCode = "CONSUMER";*/

    private String mCtn = null;
    private String mSectorCode = null;
    private String mLocale = null;
    private String mCatalogCode = null;

    private SummaryModel mSummaryModel = null;
    private AssetModel mAssetModel = null;
    private ViewProductDetailsModel mProductDetailsObject = null;

    ConsumerProductInfo mProductInfo = null;
    DigitalCareConfigManager mConfigManager = null;

    private ProgressDialog mSummaryDialog = null;

    private RequestManager mRequestManager = null;
    private Thread mUiThread = Looper.getMainLooper().getThread();
    private final Handler mHandler = new Handler(Looper.getMainLooper());


    public PrxProductData(Activity activity, IPrxCallback callback) {
        this.mActivity = activity;
        this.mPrxCallback = callback;
        mConfigManager = DigitalCareConfigManager.getInstance();
        mProductDetailsObject = new ViewProductDetailsModel();


        initProductCredentials();
    }

    public void executeRequests() {
        updateUI(new Runnable() {
            @Override
            public void run() {
                executeAssetRequest();
                executeSummaryRequest();
            }
        });

    }

    protected final void updateUI(Runnable runnable) {
        if (Thread.currentThread() != mUiThread) {
            mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    protected void initProductCredentials() {
        if (mRequestManager == null)
            mRequestManager = new RequestManager();
        mRequestManager.init(mActivity);
        DigitalCareConfigManager mConfigManager = DigitalCareConfigManager.getInstance();
        mProductInfo = mConfigManager.getConsumerProductInfo();
        mCtn = mProductInfo.getCtn();
        mSectorCode = mProductInfo.getSector();
        mLocale = mConfigManager.getLocaleMatchResponseWithCountryFallBack().toString();
        mCatalogCode = mProductInfo.getCatalog();
        if ((mSectorCode == null) || (mCtn == null) || (mLocale == null) || (mCatalogCode == null))
            DigiCareLogger.e(TAG, "Please make sure to set SectorCode, CatalogCode, ");

    }

    public ProductSummaryBuilder getPrxSummaryData() {
        ProductSummaryBuilder mProductSummaryBuilder = new ProductSummaryBuilder(mCtn, null);
        mProductSummaryBuilder.setmSectorCode(mSectorCode);
        mProductSummaryBuilder.setLocale(mLocale);
        mProductSummaryBuilder.setCatalogCode(mCatalogCode);

        return mProductSummaryBuilder;
    }

    public ProductAssetBuilder getPrxAssetData() {
        ProductAssetBuilder mProductAssetBuilder = new ProductAssetBuilder(mCtn, null);
        mProductAssetBuilder.setmSectorCode(mSectorCode);
        mProductAssetBuilder.setLocale(mLocale);
        mProductAssetBuilder.setCatalogCode(mCatalogCode);

        return mProductAssetBuilder;
    }


    public void executeSummaryRequest() {
        if (mSummaryDialog == null)
            mSummaryDialog = new ProgressDialog(mActivity, R.style.loaderTheme);
        mSummaryDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        mSummaryDialog.setCancelable(false);
        if (!(mActivity.isFinishing()))
            mSummaryDialog.show();
        mRequestManager.executeRequest(getPrxSummaryData(), new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                if (responseData != null) {
                    mSummaryModel = (SummaryModel) responseData;
                    DigiCareLogger.d(TAG, "Summary Data Received ? " + mSummaryModel.isSuccess());
                    Data data = mSummaryModel.getData();
                    if (data != null) {
                        mProductDetailsObject.setProductName(data.getProductTitle());
                        mProductDetailsObject.setCtnName(data.getCtn());
                        mProductDetailsObject.setProductImage(data.getImageURL());
                        mProductDetailsObject.setProductInfoLink(data.getProductURL());
                        mConfigManager.setViewProductDetailsData(mProductDetailsObject);
                        mPrxCallback.onResponseReceived(true);
                    }
                    if (mSummaryDialog != null && mSummaryDialog.isShowing())
                        mSummaryDialog.cancel();
                }
            }

            @Override
            public void onResponseError(String error, int statuscode) {
                DigiCareLogger.e(TAG, "Summary Error Response : " + error);
                mConfigManager.setViewProductDetailsData(mProductDetailsObject);
                mPrxCallback.onResponseReceived(false);
                if (mSummaryDialog != null && mSummaryDialog.isShowing())
                    mSummaryDialog.cancel();
            }
        });
    }


    public void executeAssetRequest() {

        mRequestManager.executeRequest(getPrxAssetData(), new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {

                if (responseData != null) {
                    mAssetModel = (AssetModel) responseData;
                    com.philips.cdp.prxclient.prxdatamodels.assets.Data data = mAssetModel.getData();
                    String qsgManual = null, usermanual = null;
                    if (data != null) {
                        Assets assets = data.getAssets();
                        List<Asset> asset = assets.getAsset();
                        List<String> mVideoList = new ArrayList<String>();
                        for (Asset assetObject : asset) {
                            String assetDescription = assetObject.getDescription();
                            String assetResource = assetObject.getAsset();
                            String assetExtension = assetObject.getExtension();
                            if (assetDescription.equalsIgnoreCase(VIEWPRODUCTDETAILS_PRX_ASSETS_USERMANUAL_QSG_PDF))
                                if (assetResource != null)
                                    qsgManual = assetResource;
                            if ((mProductDetailsObject.getManualLink() == null) && (assetDescription.equalsIgnoreCase(VIEWPRODUCTDETAILS_PRX_ASSETS_USERMANUAL_PDF)))
                                if (assetResource != null)
                                    usermanual = assetResource;
                            if (assetExtension.equalsIgnoreCase(VIEWPRODUCTDETAILS_PRX_ASSETS_VIDEO_URL))
                                if (assetResource != null)
                                    mVideoList.add(assetResource);
                        }
                        if (qsgManual != null)
                            mProductDetailsObject.setManualLink(qsgManual);
                        else if (usermanual != null)
                            mProductDetailsObject.setManualLink(usermanual);
                        mProductDetailsObject.setmVideoLinks(mVideoList);
                        mConfigManager.setViewProductDetailsData(mProductDetailsObject);

                    }
                }
            }

            @Override
            public void onResponseError(String error, int statusCode) {
                DigiCareLogger.e(TAG, "Asset Error Response : " + error);
                mConfigManager.setViewProductDetailsData(mProductDetailsObject);
            }
        });
    }
}
