package com.philips.cdp.di.iap.model.container;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.ShoppingCart.ShoppingCartData;
import com.philips.cdp.di.iap.response.cart.Carts;
import com.philips.cdp.di.iap.response.cart.Entries;
import com.philips.cdp.di.iap.response.cart.GetCartData;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.prxdatabuilder.ProductSummaryBuilder;
import com.philips.cdp.prxclient.prxdatamodels.summary.Data;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CartContainer implements Serializable, ResponseListener {
    private List<ShoppingCartData> mCartProductList;
    private String code;

    public List<ShoppingCartData> getCartProductList() {
        return mCartProductList;
    }

    public void setCartProductList(List<ShoppingCartData> pCartProductList) {
        mCartProductList = pCartProductList;
    }

    private void initCartCntainer() {
        mCartProductList = new ArrayList<ShoppingCartData>();
    }

    public void updateProductDetails(Context pContext, Message msg) {
        GetCartData data = (GetCartData) msg.obj;
        Carts currentCart = data.getCarts().get(0);

        if (currentCart.getEntries() == null) {
            // Toast.makeText(mContext, "Your Shopping Cart is Currently Empty", Toast.LENGTH_LONG).show();
            IAPLog.d(IAPLog.LOG, "Your Shopping Cart is Currently Empty");
            Utility.dismissProgressDialog();
            return;
        }

        ShoppingCartData item = new ShoppingCartData();
        item.setQuantity(currentCart.getEntries().get(0).getQuantity());
        item.setTotalPrice(currentCart.getTotalPrice().getValue());
        item.setCurrency(currentCart.getTotalPrice().getCurrencyIso());
        item.setTotalItems(currentCart.getTotalItems());

        List<Entries> list = currentCart.getEntries();
        for (int i = 0; i < list.size(); i++) {
            getProductDetails(pContext, list.get(i));
        }
    }

    private void getProductDetails(Context pContext, Entries pEntries) {
        if (NetworkUtility.getInstance().isOnline()) {
            code = pEntries.getProduct().getCode();
            //String mCtn = code.replaceAll("_", "/");
            String mSectorCode = "B2C";
            String mLocale = "en_US";
            String mCatalogCode = "CONSUMER";
            String mRequestTag = null;

            PrxLogger.enablePrxLogger(true);
            ProductSummaryBuilder mProductAssetBuilder = new ProductSummaryBuilder(code, mRequestTag);
            mProductAssetBuilder.setmSectorCode(mSectorCode);
            mProductAssetBuilder.setmLocale(mLocale);
            mProductAssetBuilder.setmCatalogCode(mCatalogCode);

            executeRequestManager(pContext, mProductAssetBuilder);
        } else {
            // Utility.showNetworkError(this, true);
            IAPLog.d(IAPLog.LOG, "Network Not Connected");
        }
    }

    private void executeRequestManager(Context pContext, ProductSummaryBuilder mProductAssetBuilder) {
        RequestManager mRequestManager = new RequestManager();
        mRequestManager.init(pContext);
        mRequestManager.executeRequest(mProductAssetBuilder, this);
    }

    @Override
    public void onResponseSuccess(final ResponseData responseData) {
        SummaryModel mAssetModel = (SummaryModel) responseData;
        IAPLog.d(IAPLog.LOG, "onResponseSuccess");
        Data data = mAssetModel.getData();
        ShoppingCartData productData = new ShoppingCartData();
        productData.setImageUrl(data.getImageURL());
        productData.setProductTitle(data.getProductTitle());
        if (null != code)
            productData.setCtnNumber(code);
        ModelContainer.getInstance().addCartItems(productData);
        //addToCart(productData);
    }

    @Override
    public void onResponseError(String error, int code) {
        IAPLog.d(IAPLog.LOG, "Negative Response Data : " + error + " with error code : " + code);
        IAPLog.d(IAPLog.LOG, "Network Error");
        Utility.dismissProgressDialog();
    }
}
