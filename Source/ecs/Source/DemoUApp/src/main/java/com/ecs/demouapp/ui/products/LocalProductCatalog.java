/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.products;

import com.ecs.demouapp.ui.integration.ECSListener;
import com.ecs.demouapp.ui.utils.ECSUtility;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;

import java.util.ArrayList;
import java.util.List;

public class LocalProductCatalog implements ProductCatalogAPI {


    @Override
    public void getProductCatalog(int currentPage, int pageSize, ECSCallback<Products, Exception> ecsCallback) {

    }

    @Override
    public void getCategorizedProductList(final ArrayList<String> productList , ECSCallback<Products, Exception> ecsCallback) {


        ECSUtility.getInstance().getEcsServices().getProductSummary(productList, new ECSCallback<List<Product>, Exception>() {
            @Override
            public void onResponse(List<Product> result) {

                Products products = new Products();
                products.setProducts(result);
                ecsCallback.onResponse(products);
            }

            @Override
            public void onFailure(Exception error, ECSError ecsError) {
                ecsCallback.onFailure(error, ecsError);
            }
        });
    }

    @Override
    public void getCompleteProductList(ECSListener iapListener) {

    }

    @Override
    public void getCatalogCount(ECSListener listener) {
    }

}
