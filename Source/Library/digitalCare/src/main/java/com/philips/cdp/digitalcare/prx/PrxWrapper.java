package com.philips.cdp.digitalcare.prx;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Looper;

import com.philips.cdp.digitalcare.ConsumerProductInfo;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.listeners.PrxFaqCallback;
import com.philips.cdp.digitalcare.listeners.prxSummaryCallback;
import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.datamodels.assets.Asset;
import com.philips.cdp.prxclient.datamodels.assets.AssetModel;
import com.philips.cdp.prxclient.datamodels.assets.Assets;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.datamodels.support.SupportModel;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductAssetRequest;
import com.philips.cdp.prxclient.request.ProductSummaryRequest;
import com.philips.cdp.prxclient.request.ProductSupportRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 * Project : PRX Common Component.
 *
 * @author naveen@philips.com
 * @Since 03-Nov-15.
 */
public class PrxWrapper {

    public static final String PRX_ASSETS_USERMANUAL_PDF = "User manual";
    public static final String PRX_ASSETS_USERMANUAL_QSG_PDF = "qsg";
    public static final String PRX_ASSETS_VIDEO_URL = "mp4";
    private static final String TAG = PrxWrapper.class.getSimpleName();
    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Thread mUiThread = Looper.getMainLooper().getThread();
    private ConsumerProductInfo mProductInfo = null;
    /*    private String mCtn = "RQ1250/17";
        private String mSectorCode = "B2C";
        private String mLocale = "en_GB";
        private String mCatalogCode = "CONSUMER";*/
    private DigitalCareConfigManager mConfigManager = null;
    private Activity mActivity = null;
    private prxSummaryCallback mSummaryCallback = null;
    private PrxFaqCallback mSupportCallback = null;
    private String mCtn = null;
    private String mSectorCode = null;
    private String mLocale = null;
    private String mCatalogCode = null;
    private SummaryModel mSummaryModel = null;
    private AssetModel mAssetModel = null;
    private ViewProductDetailsModel mProductDetailsObject = null;
    private ProgressDialog mProgressDialog = null;
    private RequestManager mRequestManager = null;


    public PrxWrapper(Activity activity, prxSummaryCallback callback) {
        this.mActivity = activity;
        this.mSummaryCallback = callback;
        mConfigManager = DigitalCareConfigManager.getInstance();
        mProductDetailsObject = new ViewProductDetailsModel();


        initProductCredentials();
    }

    public PrxWrapper(Activity activity, PrxFaqCallback callback) {
        this.mActivity = activity;
        this.mSupportCallback = callback;
        mConfigManager = DigitalCareConfigManager.getInstance();
        initProductCredentials();
    }

    public void executeRequests() {
        updateUi(new Runnable() {
            @Override
            public void run() {
                //   executeAssetRequest();
                executeSummaryRequest();
            }
        });

    }


    public void executePrxAssetRequestWithSummaryData(final SummaryModel summaryModel) {
        updateUi(new Runnable() {
                     @Override
                     public void run() {

                         final Data data = summaryModel.getData();
                         if (data != null) {
                             mProductDetailsObject.setProductName(data.getProductTitle());
                             mProductDetailsObject.setCtnName(data.getCtn());
                             mProductDetailsObject.setProductImage(data.getImageURL());
                             mProductDetailsObject.setProductInfoLink(data.getProductURL());
                             mConfigManager.setViewProductDetailsData(mProductDetailsObject);

                             try {
                                 executeAssetRequest();
                             } catch (Exception ex) {
                                 DigiCareLogger.e(TAG, "File not Found Exception log from " +
                                         "localematch component: " + ex);
                             }


                         }
                     }
                 }


        );
    }

    protected final void updateUi(Runnable runnable) {
        if (isCurrentThreadNotUiThread()) {
            mHandler.post(runnable);
        } else {
            runnable.run();
        }
    }

    private boolean isCurrentThreadNotUiThread() {
        return Thread.currentThread() != mUiThread;
    }

    protected void initProductCredentials() {
        if (mRequestManager == null) {
            mRequestManager = new RequestManager();
        }
        mRequestManager.init(mActivity);
        final DigitalCareConfigManager mConfigManager = DigitalCareConfigManager.getInstance();
        mProductInfo = mConfigManager.getConsumerProductInfo();
        mCtn = mProductInfo.getCtn();
        mSectorCode = mProductInfo.getSector();
        mLocale = mConfigManager.getLocaleMatchResponseWithCountryFallBack().toString();
        mCatalogCode = mProductInfo.getCatalog();

    }

    public ProductSummaryRequest getPrxSummaryData() {
        final ProductSummaryRequest mProductSummaryRequest = new ProductSummaryRequest(mCtn, null);
        mProductSummaryRequest.setSector(getSectorEnum(mSectorCode));
        // mProductSummaryRequest.setLocale(mLocale);
        mProductSummaryRequest.setCatalog(getCatalogEnum(mCatalogCode));

        return mProductSummaryRequest;
    }

    public ProductSupportRequest getPrxSupportData() {
        final ProductSupportRequest productSupportRequest = new ProductSupportRequest(mCtn, null);
        productSupportRequest.setSector(getSectorEnum(mSectorCode));
        // productSupportRequest.setLocale(mLocale);
        productSupportRequest.setCatalog(getCatalogEnum(mCatalogCode));
        return productSupportRequest;
    }

    public ProductAssetRequest getPrxAssetData() {
        final ProductAssetRequest mProductAssetRequest = new ProductAssetRequest(mCtn, null);
        mProductAssetRequest.setSector(getSectorEnum(mSectorCode));
        //mProductAssetRequest.setLocale(mLocale);
        mProductAssetRequest.setCatalog(getCatalogEnum(mCatalogCode));

        return mProductAssetRequest;
    }


    private Sector getSectorEnum(String sectorText) {
        switch (sectorText) {
            case "B2C":
                return Sector.B2C;

            case "B2B_LI":
                return Sector.B2B_LI;

            case "B2B_HC":
                return Sector.B2B_HC;
            default:
                return Sector.DEFAULT;

        }
    }

    private Catalog getCatalogEnum(String catalogText) {
        switch (catalogText) {
            case "CONSUMER":
                return Catalog.CONSUMER;
            case "NONCONSUMER":
                return Catalog.NONCONSUMER;
            case "CARE":
                return Catalog.CARE;
            case "PROFESSIONAL":
                return Catalog.PROFESSIONAL;
            case "LP_OEM_ATG":
                return Catalog.LP_OEM_ATG;
            case "LP_PROF_ATG":
                return Catalog.LP_PROF_ATG;
            case "HC":
                return Catalog.HC;
            case "HHSSHOP":
                return Catalog.HHSSHOP;
            case "MOBILE":
                return Catalog.MOBILE;
            case "EXTENDEDCONSENT":
                return Catalog.EXTENDEDCONSENT;
            default:
                return Catalog.DEFAULT;
        }
    }

    public void executeFaqSupportRequest() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity, R.style.loaderTheme);
        }
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        mProgressDialog.setCancelable(false);
        if (!(mActivity.isFinishing())) {
            mProgressDialog.show();
        }
        mRequestManager.executeRequest(getPrxSupportData(), new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                SupportModel supportModel = null;
                if (responseData != null) {
                    supportModel = (SupportModel) responseData;
                    DigiCareLogger.d(TAG, "Support Data Received ? " + supportModel.isSuccess());
                    if (supportModel.isSuccess()) {
                        final com.philips.cdp.prxclient.datamodels.support.Data data
                                = supportModel.getData();
                        if (data != null) {
                            if (mSupportCallback != null) {
                                mSupportCallback.onResponseReceived(supportModel);
                            }
                        } else {
                            if (mSupportCallback != null) {
                                mSupportCallback.onResponseReceived(null);
                            }
                        }
                    }
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                    }
                }
            }

            @Override
            public void onResponseError(PrxError prxError) {
                if (mSupportCallback != null) {
                    mSupportCallback.onResponseReceived(null);
                }
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
            }
        });
    }


    public void executeSummaryRequest() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity, R.style.loaderTheme);
        }
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        mProgressDialog.setCancelable(false);
        if (!(mActivity.isFinishing())) {
            mProgressDialog.show();
        }
        mRequestManager.executeRequest(getPrxSummaryData(), new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                if (responseData != null) {
                    mSummaryModel = (SummaryModel) responseData;
                    DigiCareLogger.d(TAG, "Summary Data Received ? " + mSummaryModel.isSuccess());
                    if (mSummaryModel.isSuccess()) {
                        final Data data = mSummaryModel.getData();
                        if (data != null) {
                            mProductDetailsObject.setProductName(data.getProductTitle());
                            mProductDetailsObject.setCtnName(data.getCtn());
                            mProductDetailsObject.setProductImage(data.getImageURL());
                            mProductDetailsObject.setProductInfoLink(data.getProductURL());
                            mConfigManager.setViewProductDetailsData(mProductDetailsObject);
                            if (mSummaryCallback != null)
                                mSummaryCallback.onResponseReceived(mSummaryModel);
                        }
                    } else {
                        mConfigManager.setViewProductDetailsData(mProductDetailsObject);
                        if (mSummaryCallback != null) {
                            mSummaryCallback.onResponseReceived(null);
                        }
                    }
                    if (mProgressDialog != null && mProgressDialog.isShowing()) {
                        mProgressDialog.cancel();
                    }
                }
            }

            @Override
            public void onResponseError(PrxError error) {
                DigiCareLogger.e(TAG, "Summary Error Response : " + error);
                mConfigManager.setViewProductDetailsData(mProductDetailsObject);
                if (mSummaryCallback != null) {
                    mSummaryCallback.onResponseReceived(null);
                }
                if (mProgressDialog != null && mProgressDialog.isShowing()) {
                    mProgressDialog.cancel();
                }
            }
        });
    }

    public void executeAssetRequest() {

        mRequestManager.executeRequest(getPrxAssetData(), new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {

                ViewProductDetailsModel viewProductDetailsData = DigitalCareConfigManager.getInstance().getViewProductDetailsData();
                if (viewProductDetailsData == null) {
                    viewProductDetailsData = new ViewProductDetailsModel();
                }

                if (responseData != null) {
                    mAssetModel = (AssetModel) responseData;
                    com.philips.cdp.prxclient.datamodels.assets.Data data = mAssetModel.getData();
                    String qsgManual = null, usermanual = null;
                    if (data != null) {
                        final Assets assets = data.getAssets();
                        final List<Asset> asset = assets.getAsset();
                        final List<String> mVideoList = new ArrayList<String>();
                        for (Asset assetObject : asset) {
                            String assetDescription = assetObject.getDescription();
                            String assetResource = assetObject.getAsset();
                            String assetExtension = assetObject.getExtension();
                            if (assetDescription.equalsIgnoreCase(PRX_ASSETS_USERMANUAL_QSG_PDF))
                                if (assetResource != null) {
                                    qsgManual = assetResource;
                                }
                            if ((mProductDetailsObject.getManualLink() == null) && (assetDescription.equalsIgnoreCase(PRX_ASSETS_USERMANUAL_PDF)))
                                if (assetResource != null) {
                                    usermanual = assetResource;
                                }
                            if (assetExtension.equalsIgnoreCase(PRX_ASSETS_VIDEO_URL))
                                if (assetResource != null) {
                                    mVideoList.add(assetResource);
                                }
                        }
                        if (qsgManual != null) {
                            viewProductDetailsData.setManualLink(qsgManual);
                        } else if (usermanual != null) {
                            viewProductDetailsData.setManualLink(usermanual);
                        }
                        viewProductDetailsData.setmVideoLinks(mVideoList);
                        DigiCareLogger.d(TAG, "Manual PDF : " + qsgManual);
                        DigiCareLogger.d(TAG, "Manual Link : " + usermanual);
                        DigiCareLogger.d(TAG, "Manual videoListSize : " + mVideoList.size());
                        mConfigManager.setViewProductDetailsData(viewProductDetailsData);
                        if (mSummaryCallback != null) {
                            mSummaryCallback.onResponseReceived(null);
                        }

                    }
                }
            }

            @Override
            public void onResponseError(PrxError error) {
                DigiCareLogger.e(TAG, "Asset Error Response : " + error);
                mConfigManager.setViewProductDetailsData(DigitalCareConfigManager.getInstance().getViewProductDetailsData());
            }
        });
    }

    public void executeAssetData() {
        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(mActivity, R.style.loaderTheme);
        mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
        mProgressDialog.setCancelable(false);
        if (!(mActivity.isFinishing()))
            mProgressDialog.show();

        mRequestManager.executeRequest(getPrxAssetData(), new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {

                if (responseData != null) {
                    mAssetModel = (AssetModel) responseData;
                    com.philips.cdp.prxclient.datamodels.assets.Data data = mAssetModel.getData();
                    String qsgManual = null, usermanual = null;
                    if (data != null) {
                        Assets assets = data.getAssets();
                        List<Asset> asset = assets.getAsset();
                        List<String> mVideoList = new ArrayList<String>();
                        for (final Asset assetObject : asset) {
                            final String assetDescription = assetObject.getDescription();
                            final String assetResource = assetObject.getAsset();
                            final String assetExtension = assetObject.getExtension();
                            if (assetDescription.equalsIgnoreCase(PRX_ASSETS_USERMANUAL_QSG_PDF)) {
                                if (assetResource != null) {
                                    qsgManual = assetResource;
                                }
                            }
                            if ((mProductDetailsObject.getManualLink() == null) &&
                                    (assetDescription.equalsIgnoreCase(PRX_ASSETS_USERMANUAL_PDF))) {
                                if (assetResource != null) {
                                    usermanual = assetResource;
                                }
                            }
                            if (assetExtension.equalsIgnoreCase(PRX_ASSETS_VIDEO_URL)) {
                                if (assetResource != null) {
                                    mVideoList.add(assetResource);
                                }
                            }
                        }
                        if (qsgManual != null) {
                            mProductDetailsObject.setManualLink(qsgManual);
                        } else if (usermanual != null) {
                            mProductDetailsObject.setManualLink(usermanual);
                        }
                        mProductDetailsObject.setmVideoLinks(mVideoList);
                        mConfigManager.setViewProductDetailsData(mProductDetailsObject);

                        if (mProgressDialog != null && mProgressDialog.isShowing()) {
                            mProgressDialog.cancel();
                        }

                    }
                }
            }

            @Override
            public void onResponseError(PrxError error) {
                DigiCareLogger.e(TAG, "Asset Error Response : " + error);
                mConfigManager.setViewProductDetailsData(mProductDetailsObject);
                if (mProgressDialog != null && mProgressDialog.isShowing())
                    mProgressDialog.cancel();
            }
        });
    }
}
