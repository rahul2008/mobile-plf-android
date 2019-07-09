/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.ecs.demouapp.ui.products;



import com.ecs.demouapp.ui.integration.ECSListener;

import java.util.ArrayList;

public interface ProductCatalogAPI {
    boolean getProductCatalog(int currentPage, int pageSize, ECSListener listener);

    void getCategorizedProductList(ArrayList<String> productList);

    void getCompleteProductList(ECSListener iapListener);

    void getCatalogCount(ECSListener listener);
}
