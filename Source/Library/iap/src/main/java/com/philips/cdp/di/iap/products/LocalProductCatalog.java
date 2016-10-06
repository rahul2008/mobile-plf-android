/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.products;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class LocalProductCatalog implements ProductCatalogAPI, AbstractModel.DataLoadListener {
    private Context mContext;
    private ProductCatalogHelper mProductCatalogHelper;
    ProductCatalogPresenter.ProductCatalogListener mProductCatalogListener;
    ArrayList<String> mProductList;

    public LocalProductCatalog(final Context context, final ProductCatalogPresenter.ProductCatalogListener productCatalogListener) {
        mContext = context;
        mProductCatalogListener = productCatalogListener;
        mProductCatalogHelper = new ProductCatalogHelper(context, productCatalogListener, this);
        mProductList = new ArrayList<>();
    }

    @Override
    public boolean getProductCatalog(int currentPage, int pageSize, IAPListener listener) {
        return false;
    }

    @Override
    public void getCategorizedProductList(final ArrayList<String> productList) {
        if (productList != null) {
            mProductList = productList;
            mProductCatalogHelper.sendPRXRequest(productList, null);
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
        if (msg.obj instanceof IAPNetworkError)
            mProductCatalogListener.onLoadError((IAPNetworkError) msg.obj);
        else {
            mProductCatalogListener.onLoadError(NetworkUtility.getInstance().createIAPErrorMessage(mContext.getString(R.string.iap_no_product_available)));
        }
    }

}
