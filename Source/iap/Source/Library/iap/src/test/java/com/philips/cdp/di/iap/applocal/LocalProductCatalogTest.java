/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.products.LocalProductCatalog;
import com.philips.cdp.di.iap.products.ProductCatalogData;
import com.philips.cdp.di.iap.products.ProductCatalogPresenter;
import com.philips.cdp.di.iap.response.products.PaginationEntity;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.utils.NetworkUtility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant.PRX;
import static junit.framework.Assert.assertFalse;

@RunWith(RobolectricTestRunner.class)
public class LocalProductCatalogTest {
    @Mock
    private Context mContext;

    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;
    LocalProductCatalog mLocalProductCatalog;

    ProductCatalogPresenter.ProductCatalogListener listener =
            new ProductCatalogPresenter.ProductCatalogListener() {
                @Override
                public void onLoadFinished(ArrayList<ProductCatalogData> data, PaginationEntity paginationEntity) {

                }

                @Override
                public void onLoadError(IAPNetworkError error) {

                }
            };

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(mContext);
        mLocalProductCatalog = new LocalProductCatalog(mContext, listener);
    }

    @Test
    public void testGetCompleteProductList() throws Exception {
        mLocalProductCatalog.getCompleteProductList(null);
    }

    @Test
    public void testGetCatalogCount() throws Exception {
        mLocalProductCatalog.getCatalogCount(null);
    }

    @Test
    public void testGetProductCategorizedProduct() throws Exception {
        mLocalProductCatalog.getCategorizedProductList(new ArrayList<String>());
    }

    public void testGetCategorizedProduct() throws Exception {
        ArrayList<String> ctnList = new ArrayList<>();
        ctnList.add("HX8071/10");
        ctnList.add("HX9042/64");
        mLocalProductCatalog.getCategorizedProductList(ctnList);
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

    @Test
    public void testCreateIAPErrorMessage() throws Exception {
        NetworkUtility.getInstance().createIAPErrorMessage(PRX, "No product found in your Store.");
    }

    @Test
    public void getProductCatalog() throws Exception {
        assertFalse(mLocalProductCatalog.getProductCatalog(0, 1, null));
    }

}