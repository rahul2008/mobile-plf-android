/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.prx;

import android.content.Context;
import android.os.Message;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.datamodels.assets.Asset;
import com.philips.cdp.prxclient.datamodels.assets.AssetModel;
import com.philips.cdp.prxclient.datamodels.assets.Assets;
import com.philips.cdp.prxclient.datamodels.assets.Data;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductAssetRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class PRXProductAssetBuilder {
    private AssetListener mAssetListener;
    private Context mContext;
    String mCTN;
    private static final int RTP = 1;
    private static final int APP = 2;
    private static final int DPP = 3;
    private static final int MI1 = 4;
    private static final int PID = 5;

    public interface AssetListener {
        void onFetchAssetSuccess(Message msg);

        void onFetchAssetFailure(Message msg);
    }

    public PRXProductAssetBuilder(Context context, String CTN,
                                  AssetListener listener) {

        mContext = context;
        mAssetListener = listener;
        mCTN = CTN;
    }

    public void build() {
        PrxLogger.enablePrxLogger(true);
        executeRequest(prepareAssetBuilder(mCTN));
    }

    public void executeRequest(final ProductAssetRequest productAssetBuilder) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(mContext);
        mRequestManager.executeRequest(productAssetBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                notifySuccess(responseData);
            }

            @Override
            public void onResponseError(final PrxError prxError) {
                notifyError(prxError);
            }
        });
    }

    protected void notifySuccess(ResponseData responseData) {
        AssetModel assetModel = (AssetModel) responseData;
        Data data = assetModel.getData();
        Assets assets = data.getAssets();
        List<Asset> asset = assets.getAsset();

        ArrayList<String> assetArray = fetchImageUrlsFromPRXAssets(asset);

        Message result = Message.obtain();
        result.obj = assetArray;
        if (mAssetListener != null) {
            mAssetListener.onFetchAssetSuccess(result);
        }
    }

    private ArrayList<String> fetchImageUrlsFromPRXAssets(List<Asset> assets) {
        ArrayList<String> mAssetsFromPRX = new ArrayList<>();
        TreeMap<Integer, String> sortedAssetsFromPRX = new TreeMap<>();
        GetHeightAndWidth getHeightAndWidth = new GetHeightAndWidth().invoke();
        int width = getHeightAndWidth.getWidth();
        int height = getHeightAndWidth.getHeight();

        for (Asset asset : assets) {
            int assetType = getAssetType(asset);
            if (assetType != -1) {
                String imagepath = asset.getAsset() + "?wid=" + width +
                        "&hei=" + height + "&$pnglarge$" + "&fit=fit,1";
                sortedAssetsFromPRX.put(assetType, imagepath);
            }
            mAssetsFromPRX = new ArrayList<>(sortedAssetsFromPRX.values());
        }

        return mAssetsFromPRX;
    }

    private int getAssetType(final Asset asset) {
        switch (asset.getType()) {
            case "RTP":
                return RTP;
            case "APP":
                return APP;
            case "DPP":
                return DPP;
            case "MI1":
                return MI1;
            case "PID":
                return PID;
            default:
                return -1;
        }
    }

    protected void notifyError(final PrxError errorCode) {
        Message result = Message.obtain();
        if (PrxError.PrxErrorType.NO_INTERNET_CONNECTION.getId() == errorCode.getStatusCode()) {
            result.obj = new IAPNetworkError(new NoConnectionError(), 0, null);
        } else if (PrxError.PrxErrorType.TIME_OUT.getId() == errorCode.getStatusCode()) {
            result.obj = new IAPNetworkError(new TimeoutError(), 0, null);
        } else {
            result.obj = errorCode.getStatusCode();
        }
        if (mAssetListener != null) {
            mAssetListener.onFetchAssetFailure(result);
        }
    }

    private ProductAssetRequest prepareAssetBuilder(final String code) {
        String locale = HybrisDelegate.getInstance(mContext).getStore().getLocale();

        ProductAssetRequest productAssetBuilder = new ProductAssetRequest(code, null);
        productAssetBuilder.setSector(Sector.B2C);
        productAssetBuilder.setLocaleMatchResult(locale);
        productAssetBuilder.setCatalog(Catalog.CONSUMER);
        return productAssetBuilder;
    }

    private class GetHeightAndWidth {
        private int width;
        private int height;

        public int getWidth() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public GetHeightAndWidth invoke() {
            width = 0;
            height = 0;
            //Adding try Catch for Test case - In jUnit Test case since context is Mocked, it returns Null Pointer Exception
            try {
                 width = mContext.getResources().getDisplayMetrics().widthPixels;
                 height = (int) mContext.getResources().getDimension(R.dimen.iap_product_detail_image_height);
            }catch (NullPointerException e){

            }
            return this;
        }
    }
}