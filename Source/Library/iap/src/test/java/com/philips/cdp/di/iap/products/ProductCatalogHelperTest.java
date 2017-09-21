/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.products;

import android.content.Context;

import com.philips.cdp.di.iap.model.AbstractModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class ProductCatalogHelperTest {
    @Mock
    Context mContext;
    @Mock
    ProductCatalogPresenter.ProductCatalogListener mockProductCatalogListener;
    @Mock
    AbstractModel.DataLoadListener mockDataLoadListener;

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

        assertEquals(2, new ProductCatalogHelper(mContext, mockProductCatalogListener, mockDataLoadListener).getProductCTNs(data).size());
    }
}