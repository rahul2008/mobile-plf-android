/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.ShoppingCart;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.ProductAssetBuilder;
import com.philips.cdp.prxclient.prxdatamodels.assets.Asset;
import com.philips.cdp.prxclient.prxdatamodels.assets.AssetModel;
import com.philips.cdp.prxclient.prxdatamodels.assets.Assets;
import com.philips.cdp.prxclient.prxdatamodels.assets.Data;
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

    private void executeRequest(final ProductAssetBuilder productAssetBuilder) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(mContext);
        mRequestManager.executeRequest(productAssetBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                notifySuccess(responseData);
            }

            @Override
            public void onResponseError(String error, int code) {
                notifyError(error);
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
        result.obj = error;
        if (mAssetListener != null) {
            mAssetListener.onFetchAssetFailure(result);
        }
    }

    private ProductAssetBuilder prepareAssetBuilder(final String code) {
        String sectorCode = NetworkConstants.PRX_SECTOR_CODE;
        String locale = NetworkConstants.PRX_LOCALE;
        String catalogCode = NetworkConstants.PRX_CATALOG_CODE;

        ProductAssetBuilder productAssetBuilder = new ProductAssetBuilder(code, null);
        productAssetBuilder.setmSectorCode(sectorCode);
        productAssetBuilder.setmLocale(locale);
        productAssetBuilder.setmCatalogCode(catalogCode);
        return productAssetBuilder;
    }
}