/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import android.content.Context;
import android.os.Message;

import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.core.ProductCatalogAPI;
import com.philips.cdp.di.iap.core.ProductCatalogHelper;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class LocalProductCatalog implements ProductCatalogAPI, AbstractModel.DataLoadListener {
    private Context mContext;
    private ProductCatalogHelper mProductCatalogHelper;
    ProductCatalogPresenter.LoadListener mListener;
    ArrayList<String> mProductList;

    public LocalProductCatalog(final Context context, final ProductCatalogPresenter.LoadListener listener) {
        mContext = context;
        mListener = listener;
        mProductCatalogHelper = new ProductCatalogHelper(context, listener, this);
        mProductList = new ArrayList<>();
    }

    @Override
    public boolean getProductCatalog(int currentPage, int pageSize, IAPListener listener) {
        return false;
    }

    @Override
    public void getProductCategorizedProduct(final ArrayList<String> productList) {
        if (productList != null) {
            mProductList = productList;
            mProductCatalogHelper.makePrxCall(productList, null);
        }
    }

    @Override
    public void getCompleteProductList(IAPListener iapListener) {

    }

    @Override
    public void getCatalogCount(IAPListener listener) {

    }

    @Override
    public void onModelDataLoadFinished(final Message msg) {
        if (msg.obj instanceof HashMap) {
            mProductCatalogHelper.processPRXResponse(msg, mProductList, null, null);
        }

        if (Utility.isProgressDialogShowing())
            Utility.dismissProgressDialog();
    }

    @Override
    public void onModelDataError(final Message msg) {
        IAPLog.e(IAPConstant.SHOPPING_CART_PRESENTER, "Error:" + msg.obj);
        IAPLog.d(IAPConstant.SHOPPING_CART_PRESENTER, msg.obj.toString());

        if (msg.obj instanceof IAPNetworkError)
            mListener.onLoadError((IAPNetworkError) msg.obj);
        else {
            mListener.onLoadError(createIAPErrorMessage(mContext.getString(R.string.iap_no_product_available)));
        }
    }

    public IAPNetworkError createIAPErrorMessage(String errorMessage) {
        VolleyError volleyError = new ServerError();
        IAPNetworkError error = new IAPNetworkError(volleyError, -1, null);
        error.setCustomErrorMessage(errorMessage);
        return error;
    }
}
