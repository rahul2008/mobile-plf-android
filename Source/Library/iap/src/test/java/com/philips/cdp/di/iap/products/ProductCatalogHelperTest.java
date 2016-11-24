/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.products;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.model.AbstractModel;
import com.philips.cdp.di.iap.response.products.PaginationEntity;
import com.philips.cdp.di.iap.session.IAPNetworkError;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ProductCatalogHelperTest implements ProductCatalogPresenter.ProductCatalogListener, AbstractModel.DataLoadListener {
    @Mock
    Context mContext;

    @Override
    public void onModelDataLoadFinished(Message msg) {

    }

    @Override
    public void onModelDataError(Message msg) {

    }

    @Override
    public void onLoadFinished(ArrayList<ProductCatalogData> data, PaginationEntity paginationEntity) {

    }

    @Override
    public void onLoadError(IAPNetworkError error) {

    }

    @Test
    public void testProductCtn() {
        ArrayList<ProductCatalogData> data = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            ProductCatalogData catalogData = new ProductCatalogData();
            if (i == 0)
                catalogData.setCtnNumber("HX8071/10");
            if (i == 1)
                catalogData.setCtnNumber("HX8331/11");
            data.add(i, catalogData);
        }

        assertEquals(2, new ProductCatalogHelper(mContext, this, this).getProductCTNs(data).size());
    }
}