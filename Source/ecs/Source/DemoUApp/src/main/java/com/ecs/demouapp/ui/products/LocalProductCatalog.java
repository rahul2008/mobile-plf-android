/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.products;

import com.ecs.demouapp.ui.integration.ECSListener;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.Product;
import com.philips.cdp.di.ecs.model.products.Products;

import java.util.ArrayList;
import java.util.List;

public class LocalProductCatalog implements ProductCatalogAPI {

    private Products mProductCatalog;

    @Override
    public void getProductCatalog(int currentPage, int pageSize, ECSCallback<Products, Exception> ecsCallback) {

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
    }

    @Override
    public void getCompleteProductList(ECSListener iapListener) {
    }

    @Override
    public void getCatalogCount(ECSListener listener) {
    }

}
