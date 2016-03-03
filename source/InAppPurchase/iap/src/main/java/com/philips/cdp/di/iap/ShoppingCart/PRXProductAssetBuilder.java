/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.ShoppingCart;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.response.carts.Carts;
import com.philips.cdp.di.iap.response.carts.DeliveryCostEntity;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.ProductAssetBuilder;
import com.philips.cdp.prxclient.prxdatabuilder.ProductSummaryBuilder;
import com.philips.cdp.prxclient.prxdatamodels.summary.Data;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import java.util.ArrayList;
import java.util.List;

public class PRXProductAssetBuilder {
    private static final String TAG = PRXProductAssetBuilder.class.getSimpleName();
    private AbstractModel.DataLoadListener mDataLoadListener;
    private Context mContext;
    String CTN;

    public PRXProductAssetBuilder(Context context, String CTN,
                                  AbstractModel.DataLoadListener listener) {

        mContext = context;
        mDataLoadListener = listener;
    }

    public void build() {
        PrxLogger.enablePrxLogger(true);

            executeRequest(prepareAssetBuilder(CTN));

    }

    private void executeRequest(final ProductAssetBuilder productAssetBuilder) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(mContext);
        mRequestManager.executeRequest(productAssetBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                notifySuccess();
            }

            @Override
            public void onResponseError(String error, int code) {
                notifyError(error);
            }
        });
    }

    private void notifySuccess() {

    }

    private void notifyError(final String error) {
        Message result = Message.obtain();
        result.obj = error;
        if (mDataLoadListener != null) {
            mDataLoadListener.onModelDataError(result);
        }
    }

    private void addWithNotify(ShoppingCartData cartItem) {

        if (mDataLoadListener != null) {
            Message result = Message.obtain();
            //result.obj = mCartItems;
            mDataLoadListener.onModelDataLoadFinished(result);
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