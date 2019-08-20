/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.ecs.demouapp.ui.products;

import com.ecs.demouapp.ui.integration.ECSListener;
import com.ecs.demouapp.ui.utils.ECSUtility;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Products;

import java.util.ArrayList;

public class ProductCatalogPresenter implements ProductCatalogAPI {


    @Override
    public void getCatalogCount(final ECSListener iapListener) {

    }


    @Override
    public void getProductCatalog(int currentPage, int pageSize, ECSCallback<Products, Exception> ecsCallback) {
        ECSUtility.getInstance().getEcsServices().getProductList(currentPage, pageSize, ecsCallback);
    }

    @Override
    public void getCategorizedProductList(ArrayList<String> ctnList, ECSCallback<Products, Exception> ecsCallback) {

    }

    @Override
    public void getCompleteProductList(ECSListener iapListener) {

    }

}
