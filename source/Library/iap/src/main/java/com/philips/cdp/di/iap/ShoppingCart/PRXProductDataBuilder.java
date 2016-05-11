/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.ShoppingCart;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.response.carts.Carts;
import com.philips.cdp.di.iap.response.carts.CartsEntity;
import com.philips.cdp.di.iap.response.carts.DeliveryCostEntity;
import com.philips.cdp.di.iap.response.carts.EntriesEntity;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.datamodels.summary.Data;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductSummaryRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.tagging.Tagging;

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
            executeRequest(entry, mDeliveryCostEntity, code, prepareSummaryRequest(code));
        }
    }

    private void executeRequest(final EntriesEntity entry, final DeliveryCostEntity deliveryCostEntity, final String code, final ProductSummaryRequest productSummaryBuilder) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(mContext);
        mRequestManager.executeRequest(productSummaryBuilder, new ResponseListener() {
            @Override
            public void onResponseSuccess(ResponseData responseData) {
                updateSuccessData((SummaryModel) responseData, code, deliveryCostEntity, entry);
            }

            @Override
            public void onResponseError(final PrxError prxError) {
                notifyError(prxError.getDescription());
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
        cartItem.setValuePrice(String.valueOf(entry.getBasePrice().getValue()));
        cartItem.setTotalPriceWithTaxFormatedPrice(mCartData.getCarts().get(0).getTotalPriceWithTax().getFormattedValue());
        cartItem.setTotalPriceFormatedPrice(entry.getTotalPrice().getFormattedValue());
        cartItem.setTotalItems(mCartData.getCarts().get(0).getTotalItems());
        cartItem.setMarketingTextHeader(data.getMarketingTextHeader());
        cartItem.setDeliveryAddressEntity(mCartData.getCarts().get(0).getDeliveryAddress());
        cartItem.setVatValue(mCartData.getCarts().get(0).getTotalTax().getFormattedValue());
        cartItem.setDeliveryItemsQuantity(mCartData.getCarts().get(0).getDeliveryItemsQuantity());
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
            ArrayList<ShoppingCartData> datas = rearrangeDataSet();
            Message result = Message.obtain();
            result.obj = datas;
            mDataLoadListener.onModelDataLoadFinished(result);
            tagProducts(mCartItems);
        }
    }

    private ArrayList rearrangeDataSet() {
        ArrayList<ShoppingCartData> rearrangedArray = new ArrayList<>();
        for(int i=0;i<mEntries.size();i++){
            ShoppingCartData pShoppingCartData = checkIfEntryPresent(mEntries.get(i).getProduct().getCode());
            rearrangedArray.add(pShoppingCartData);
        }
        return rearrangedArray;
    }

    private ShoppingCartData checkIfEntryPresent(final String code) {
        ShoppingCartData data = new ShoppingCartData();
        for(int i = 0; i< mCartItems.size();i++){
            if(mCartItems.get(i).getCtnNumber().equalsIgnoreCase(code)){
                data = mCartItems.get(i);
            }
        }
        return data;
    }

    private void tagProducts(List<ShoppingCartData> cartData){
        StringBuilder products = new StringBuilder();
        CartsEntity cart = mCartData.getCarts().get(0);
        for (int i = 0; i < cart.getTotalItems(); i++) {
            EntriesEntity entriesEntity = cart.getEntries().get(i);
            if (i > 0) {
                products = products.append(",");
            }
            products = products.append(entriesEntity.getProduct().getCategories().get(0).getCode()).append(";")
                    .append(cartData.get(i).getProductTitle()).append(";").append(String.valueOf(entriesEntity.getQuantity()))
                    .append(";").append(entriesEntity.getTotalPrice().getValue());
        }
        Tagging.trackAction(IAPAnalyticsConstant.SEND_DATA, IAPAnalyticsConstant.PRODUCTS, products);
    }

    private ProductSummaryRequest prepareSummaryRequest(final String code) {
        // String ctn = code.replaceAll("_", "/");
        String locale = HybrisDelegate.getInstance(mContext).getStore().getLocale();

        ProductSummaryRequest productSummaryRequest = new ProductSummaryRequest(code, null);
        productSummaryRequest.setSector(Sector.B2C);
        productSummaryRequest.setLocaleMatchResult(locale);
        productSummaryRequest.setCatalog(Catalog.CONSUMER);
        return productSummaryRequest;
    }
}