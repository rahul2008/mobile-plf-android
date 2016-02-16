/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.ShoppingCart;

import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;

import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.response.cart.Entries;
import com.philips.cdp.di.iap.response.cart.GetCartData;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.ProductSummaryBuilder;
import com.philips.cdp.prxclient.prxdatamodels.summary.Data;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import java.util.ArrayList;
import java.util.List;

public class PRXProductDataBuilder {
    private static final String TAG = PRXProductDataBuilder.class.getSimpleName();
    private AbstractModel.DataLoadListener mDataLoadListener;
    private List<Entries> mEntries;
    private Context mContext;
    private List<ShoppingCartData> mCartItems;
    private GetCartData mCartData;

    public PRXProductDataBuilder(Context context, GetCartData cartData,
                                 AbstractModel.DataLoadListener listener) {
        mCartData = cartData;
        mEntries = cartData.getEntries();
        mContext = context;
        mDataLoadListener = listener;
    }

    public void build() {
        int count = mEntries.size();
        PrxLogger.enablePrxLogger(true);
        for (int index = 0; index < count; index++) {
            Entries entry = mEntries.get(index);
            String code = entry.getProduct().getCode();
            executeRequest(entry, code, prepareSummaryBuilder(code));
        }
    }

    private void executeRequest(final Entries entry, final String code, final ProductSummaryBuilder productSummaryBuilder) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(mContext);
        mRequestManager.executeRequest(productSummaryBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                updateSuccessData((SummaryModel) responseData, code, entry);
            }

            @Override
            public void onResponseError(String error, int code) {
                notifyError(error);
            }
        });
    }

    private void updateSuccessData(final SummaryModel responseData, final String code, final Entries entry) {
        ShoppingCartData cartItem = new ShoppingCartData(entry);
        SummaryModel mAssetModel = responseData;
        Data data = mAssetModel.getData();
        cartItem.setImageUrl(data.getImageURL());
        cartItem.setProductTitle(data.getProductTitle());
        cartItem.setCtnNumber(code);
        cartItem.setQuantity(entry.getQuantity());
        cartItem.setTotalPrice(entry.getTotalPrice().getValue());
        cartItem.setCurrency(entry.getTotalPrice().getCurrencyIso());
//        cartItem.setTotalItems(mCartItems.get);
        addWithNotify(cartItem);
    }

    private void notifyError(final String error) {
        Message result = Message.obtain();
        result.obj = error;
        if (mDataLoadListener != null) {
            mDataLoadListener.onModelDataError(result);
        }
    }

    private void addWithNotify(ShoppingCartData cartItem) {
        if (mCartItems == null) {
            mCartItems = new ArrayList<ShoppingCartData>();
        }
        mCartItems.add(cartItem);
        if (mDataLoadListener != null && mCartItems.size() == mEntries.size()) {
            Message result = Message.obtain();
            result.obj = mCartItems;
            mDataLoadListener.onModelDataLoadFinished(result);
        }
    }

    @NonNull
    private ProductSummaryBuilder prepareSummaryBuilder(final String code) {
        String ctn = code.replaceAll("_", "/");
        String sectorCode = NetworkConstants.PRX_SECTOR_CODE;
        String locale = NetworkConstants.PRX_LOCALE;
        String catalogCode = NetworkConstants.PRX_CATALOG_CODE;

        ProductSummaryBuilder productSummaryBuilder = new ProductSummaryBuilder(ctn, null);
        productSummaryBuilder.setmSectorCode(sectorCode);
        productSummaryBuilder.setmLocale(locale);
        productSummaryBuilder.setmCatalogCode(catalogCode);
        return productSummaryBuilder;
    }
}