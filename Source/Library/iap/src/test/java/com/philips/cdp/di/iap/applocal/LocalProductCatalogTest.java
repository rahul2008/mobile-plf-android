package com.philips.cdp.di.iap.applocal;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.productCatalog.ProductCatalogData;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;
import com.philips.cdp.di.iap.response.products.PaginationEntity;
import com.philips.cdp.di.iap.session.IAPNetworkError;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

/**
 * Created by 310228564 on 9/19/2016.
 */
@RunWith(RobolectricTestRunner.class)
public class LocalProductCatalogTest {
    LocalProductCatalog mLocalProductCatalog;

    @Mock
    Context mContext;
    ProductCatalogPresenter.LoadListener listener = new ProductCatalogPresenter.LoadListener() {
        @Override
        public void onLoadFinished(ArrayList<ProductCatalogData> data, PaginationEntity paginationEntity) {

        }

        @Override
        public void onLoadError(IAPNetworkError error) {

        }
    };

    @Before
    public void setUp() {
        mLocalProductCatalog = new LocalProductCatalog(mContext, listener);
    }

    @Test
    public void testgetCompleteProductList() throws Exception {
        mLocalProductCatalog.getCompleteProductList(null);
    }

    @Test
    public void getCatalogCount() throws Exception {
        mLocalProductCatalog.getCatalogCount(null);
    }

    @Test
    public void testGetProductCategorizedProduct() throws Exception {
        mLocalProductCatalog.getCategorizedProductList(new ArrayList<String>());
    }

    @Test
    public void testOnModelDataLoadFinished() throws Exception {
        mLocalProductCatalog.onModelDataLoadFinished(new Message());
    }

    @Test
    public void testOnModelDataError() throws Exception {
        Message msg = new Message();
        msg.obj = new IAPNetworkError(null, 1, null);
        mLocalProductCatalog.onModelDataError(msg);
    }

    @Test(expected = NullPointerException.class)
    public void testOnModelDataErrorForNoProduct() throws Exception {
        Message msg = new Message();
        mLocalProductCatalog.onModelDataError(msg);
    }

    @Test
    public void testCreateIAPErrorMessage() throws Exception {
        mLocalProductCatalog.createIAPErrorMessage("No product found in your Store.");
    }

    @Test
    public void getProductCatalog() throws Exception {
        Assert.assertFalse(mLocalProductCatalog.getProductCatalog(0, 1, null));
    }

}