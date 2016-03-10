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
    private List<EntriesEntity> mEntries;
    private DeliveryCostEntity mDeliveryCostEntity;
    private Context mContext;
    private List<ShoppingCartData> mCartItems;
    private Carts mCartData;

    public PRXProductDataBuilder(Context context, Carts cartData,
                                 AbstractModel.DataLoadListener listener) {
        mCartData = cartData;
        mEntries = mCartData.getCarts().get(0).getEntries();
        mDeliveryCostEntity = mCartData.getCarts().get(0).getDeliveryCost();
        mContext = context;
        mDataLoadListener = listener;
    }

    public void build() {
        int count = mEntries.size();
        PrxLogger.enablePrxLogger(true);
        for (int index = 0; index < count; index++) {
            EntriesEntity entry = mEntries.get(index);
            String code = entry.getProduct().getCode();
            executeRequest(entry, mDeliveryCostEntity, code, prepareSummaryBuilder(code));
        }
    }

    private void executeRequest(final EntriesEntity entry, final DeliveryCostEntity deliveryCostEntity, final String code, final ProductSummaryBuilder productSummaryBuilder) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(mContext);
        mRequestManager.executeRequest(productSummaryBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                updateSuccessData((SummaryModel) responseData, code, deliveryCostEntity, entry);
            }

            @Override
            public void onResponseError(String error, int code) {
                notifyError(error);
            }
        });
    }

    private void updateSuccessData(final SummaryModel responseData, final String code, final DeliveryCostEntity deliveryCostEntity, final EntriesEntity entry) {
        ShoppingCartData cartItem = new ShoppingCartData(entry, deliveryCostEntity);
        SummaryModel mSummaryModel = responseData;
        Data data = mSummaryModel.getData();
        cartItem.setImageUrl(data.getImageURL());
        cartItem.setProductTitle(data.getProductTitle());
        cartItem.setCtnNumber(code);
        cartItem.setCartNumber(mCartData.getCarts().get(0).getCode());
        cartItem.setQuantity(entry.getQuantity());
        cartItem.setFormatedPrice(entry.getBasePrice().getFormattedValue());
        cartItem.setTotalPriceWithTaxFormatedPrice(mCartData.getCarts().get(0).getTotalPriceWithTax().getFormattedValue());
        cartItem.setTotalPriceFormatedPrice(entry.getTotalPrice().getFormattedValue());
        cartItem.setTotalItems(mCartData.getCarts().get(0).getTotalItems());
        cartItem.setMarketingTextHeader(data.getMarketingTextHeader());
        cartItem.setDeliveryAddressEntity(mCartData.getCarts().get(0).getDeliveryAddress());
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

    private ProductSummaryBuilder prepareSummaryBuilder(final String code) {
        // String ctn = code.replaceAll("_", "/");
        String sectorCode = NetworkConstants.PRX_SECTOR_CODE;
        String locale = NetworkConstants.PRX_LOCALE;
        String catalogCode = NetworkConstants.PRX_CATALOG_CODE;

        ProductSummaryBuilder productSummaryBuilder = new ProductSummaryBuilder(code, null);
        productSummaryBuilder.setmSectorCode(sectorCode);
        productSummaryBuilder.setmLocale(locale);
        productSummaryBuilder.setmCatalogCode(catalogCode);
        return productSummaryBuilder;
    }
}