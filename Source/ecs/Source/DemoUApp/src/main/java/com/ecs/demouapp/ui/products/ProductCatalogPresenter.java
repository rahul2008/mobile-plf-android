/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.ecs.demouapp.ui.products;

import android.content.Context;
import android.os.Message;

import com.android.volley.NetworkError;
import com.android.volley.VolleyError;
import com.ecs.demouapp.R;
import com.ecs.demouapp.ui.analytics.ECSAnalytics;
import com.ecs.demouapp.ui.analytics.ECSAnalyticsConstant;
import com.ecs.demouapp.ui.container.CartModelContainer;
import com.ecs.demouapp.ui.integration.ECSListener;
import com.ecs.demouapp.ui.model.AbstractModel;
import com.ecs.demouapp.ui.model.GetProductCatalogRequest;
import com.ecs.demouapp.ui.session.HybrisDelegate;
import com.ecs.demouapp.ui.session.IAPNetworkError;
import com.ecs.demouapp.ui.session.RequestListener;
import com.ecs.demouapp.ui.store.StoreListener;
import com.ecs.demouapp.ui.utils.ECSUtility;
import com.ecs.demouapp.ui.utils.ModelConstants;
import com.ecs.demouapp.ui.utils.NetworkUtility;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.PaginationEntity;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;
import com.philips.cdp.di.ecs.model.summary.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductCatalogPresenter implements ProductCatalogAPI {

    private Context mContext;

    private HybrisDelegate mHybrisDelegate;
    private StoreListener mStore;

    private Products mProducts;

    ECSListener mIAPListener;

    private final int PAGE_SIZE = 20;
    private final int CURRENT_PAGE = 0;

    public void getCompleteProductList(final ECSListener iapListener) {
        completeProductList(iapListener);
    }

    public void completeProductList(ECSListener iapListener) {
        if (
                CartModelContainer.getInstance().getProductList() != null
                        && CartModelContainer.getInstance().getProductList().size() != 0) {
            iapListener.onGetCompleteProductList(getProductCatalogDataFromStoredData());
        } else {
            CartModelContainer.getInstance().clearCategorisedProductList();
            getCatalogCount(iapListener);
        }
    }

    ArrayList<String> getProductCatalogDataFromStoredData() {
        ArrayList<String> catalogList = new ArrayList<>();
        HashMap<String, ProductCatalogData> productCatalogDataSaved =
                CartModelContainer.getInstance().getProductList();
        for (Map.Entry<String, ProductCatalogData> entry : productCatalogDataSaved.entrySet()) {
            if (entry != null) {
                catalogList.add(entry.getValue().getCtnNumber());
            }
        }
        return catalogList;
    }

    @Override
    public void getCatalogCount(final ECSListener iapListener) {

    }


    @Override
    public void getProductCatalog(int currentPage, int pageSize, ECSCallback<Products,Exception> ecsCallback) {
        ECSUtility.getInstance().getEcsServices().getProductList(currentPage, pageSize, ecsCallback);
    }

    @Override
    public void getCategorizedProductList(ArrayList<String> ctnList,ECSCallback<Products,Exception> ecsCallback) {
        if (CartModelContainer.getInstance().getProductList() != null) {
            ArrayList<ProductCatalogData> productCatalogList = new ArrayList<>();
            CartModelContainer container = CartModelContainer.getInstance();
            for (String ctn : ctnList) {
                if (container.isProductCatalogDataPresent(ctn)) {
                    productCatalogList.add(container.getProduct(ctn));
                }
            }
        }
    }


}
