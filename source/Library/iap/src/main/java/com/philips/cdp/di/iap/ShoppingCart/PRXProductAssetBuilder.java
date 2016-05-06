/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.shoppingcart;

import android.content.Context;
import android.os.Message;

import com.android.volley.NoConnectionError;
import com.android.volley.TimeoutError;
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

public class PRXProductAssetBuilder {
    private static final String TAG = PRXProductAssetBuilder.class.getSimpleName();
    private AssetListener mAssetListener;
    private Context mContext;
    String mCTN;

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
                notifyError(prxError.getDescription());
            }

        });
    }

    private void notifySuccess(ResponseData responseData) {
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

        for (Asset asset : assets
                ) {
            boolean bool = asset.getExtension().equalsIgnoreCase("tif");

            if (bool) {
                mAssetsFromPRX.add(asset.getAsset());
            }
        }

        return mAssetsFromPRX;
    }

    private void notifyError(final String error) {
        Message result = Message.obtain();
        if(error!=null && error.contains("NoConnectionError")){
            result.obj = new IAPNetworkError(new NoConnectionError(), 0, null);
        }else if(error!=null && error.contains("TimeoutError")){
            result.obj = new IAPNetworkError(new TimeoutError(), 0, null);
        }else {
            result.obj = error;
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
}