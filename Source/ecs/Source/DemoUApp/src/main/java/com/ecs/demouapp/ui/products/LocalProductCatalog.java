/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.products;

import android.content.Context;
import android.os.Message;


import com.ecs.demouapp.R;
import com.ecs.demouapp.ui.analytics.ECSAnalytics;
import com.ecs.demouapp.ui.analytics.ECSAnalyticsConstant;
import com.ecs.demouapp.ui.integration.ECSListener;
import com.ecs.demouapp.ui.model.AbstractModel;
import com.ecs.demouapp.ui.session.IAPNetworkError;
import com.ecs.demouapp.ui.utils.NetworkUtility;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalProductCatalog implements ProductCatalogAPI, AbstractModel.DataLoadListener {
    private Context mContext;
    private ProductCatalogHelper mProductCatalogHelper;
    private ProductCatalogPresenter.ProductCatalogListener mProductCatalogListener;
    private Products mProductCatalog;

    public LocalProductCatalog(final Context context, final ProductCatalogPresenter.ProductCatalogListener productCatalogListener) {
        mContext = context;
        mProductCatalogListener = productCatalogListener;

        mProductCatalogHelper = new ProductCatalogHelper(context, productCatalogListener, this);
    }

    @Override
    public boolean getProductCatalog(int currentPage, int pageSize, ECSListener listener) {
        return true;
    }

    @Override
    public void getCategorizedProductList(final ArrayList<String> productList) {
        mProductCatalog = new Products();
        List<Product> productsEntityList = new ArrayList<>();

        if (productList != null) {
            for (String productCode : productList) {
                Product productsEntity = new Product();
                productsEntity.setCode(productCode);
                productsEntityList.add(productsEntity);
            }
        }
        mProductCatalog.setProducts(productsEntityList);
        mProductCatalogHelper.sendPRXRequest(mProductCatalog);
    }

    @Override
    public void getCompleteProductList(ECSListener iapListener) {
    }

    @Override
    public void getCatalogCount(ECSListener listener) {
    }


    @Override
    public void onModelDataLoadFinished(final Message msg) {
        if (msg.obj instanceof HashMap) {
           // mProductCatalogHelper.processPRXResponse(msg, mProductCatalog, null);
        }
    }

    @Override
    public void onModelDataError(final Message msg) {
        if (msg.obj instanceof IAPNetworkError)
            mProductCatalogListener.onLoadError((IAPNetworkError) msg.obj);
        else {
            ECSAnalytics.trackAction(ECSAnalyticsConstant.SEND_DATA,
                    ECSAnalyticsConstant.ERROR, ECSAnalyticsConstant.PRX + ECSAnalyticsConstant.NO_PRODUCT_FOUND);

            mProductCatalogListener.onLoadError(NetworkUtility.getInstance()
                    .createIAPErrorMessage(ECSAnalyticsConstant.PRX, mContext.getString(R.string.iap_no_product_available)));
        }
    }
}
