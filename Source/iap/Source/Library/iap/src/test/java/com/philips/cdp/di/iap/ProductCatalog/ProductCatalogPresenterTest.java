/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.ProductCatalog;

import android.content.Context;
import android.os.Message;
import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.products.LocalProductCatalog;
import com.philips.cdp.di.iap.products.ProductCatalogData;
import com.philips.cdp.di.iap.products.ProductCatalogPresenter;
import com.philips.cdp.di.iap.prx.MockPRXSummaryListExecutor;
import com.philips.cdp.di.iap.response.products.PaginationEntity;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.prxclient.datamodels.summary.PRXSummaryListResponse;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductSummaryListRequest;
import com.philips.cdp.prxclient.request.ProductSummaryRequest;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant.PRX;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class ProductCatalogPresenterTest implements ProductCatalogPresenter.ProductCatalogListener, IAPListener {
    private Context mContext;

    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;
    private ProductCatalogPresenter mProductCatalogPresenter;
    private MockPRXSummaryListExecutor mMockPRXDataBuilder;
    private ArrayList<String> mCTNS = new ArrayList<>();

    @Mock
    private AppInfraInterface mockAppInfraInterface;
    @Mock
    private LoggingInterface mockLoggingInterface;

    @Before
    public void setUp() {
        initMocks(this);
        mContext = getInstrumentation().getContext();
        CartModelContainer.getInstance().setAppInfraInstance(mockAppInfraInterface);
        when(mockAppInfraInterface.getLogging()).thenReturn(mockLoggingInterface);
        when(mockLoggingInterface.createInstanceForComponent(anyString(), anyString())).thenReturn(mockLoggingInterface);

        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(mContext);

        mCTNS.add("HX9033/64");
        mCTNS.add("HX9023/64");
        mCTNS.add("HX9003/64");
        mProductCatalogPresenter = new ProductCatalogPresenter(mContext, this);
    }

    @Test
    public void testGetLocalProductCatalogSuccessResponse() {
        LocalProductCatalog localCatalog = new LocalProductCatalog(mContext, this);
        assertFalse(localCatalog.getProductCatalog(0, 20, null));
    }

    @Test
    public void testGetProductCatalogSuccessResponse() throws JSONException {
        mProductCatalogPresenter = new ProductCatalogPresenter(mContext, this);
        mMockPRXDataBuilder = new MockPRXSummaryListExecutor(mContext, mCTNS, mProductCatalogPresenter);
        mProductCatalogPresenter.setHybrisDelegate(mHybrisDelegate);
        mProductCatalogPresenter.getProductCatalog(0, 20, null);

        JSONObject obj = new JSONObject(TestUtils.readFile(ProductCatalogPresenterTest
                .class, "product_catalog_get_request.txt"));
        mNetworkController.sendSuccess(obj);
        makePRXData();
    }

    @Test
    public void testGetProductCatalogErrorResponse() throws JSONException {
        mProductCatalogPresenter = new ProductCatalogPresenter(mContext, this);
        mMockPRXDataBuilder = new MockPRXSummaryListExecutor(mContext, mCTNS, mProductCatalogPresenter);
        mProductCatalogPresenter.setHybrisDelegate(mHybrisDelegate);
        mProductCatalogPresenter.getProductCatalog(0, 20, null);

        JSONObject obj = new JSONObject(TestUtils.readFile(ProductCatalogPresenterTest
                .class, "product_catalog_get_request.txt"));
        mNetworkController.sendSuccess(obj);
        mMockPRXDataBuilder.sendFailure(new PrxError("fail", 500));
    }

    @Test
    public void testCreateIAPErrorMessage() {
        IAPNetworkError error = NetworkUtility.getInstance().createIAPErrorMessage(PRX, "Apologies");
        assertTrue(error != null);
    }

    @Test
    public void testGetProductListWithNoPaginationSuccessResponse() throws JSONException {
        mProductCatalogPresenter = new ProductCatalogPresenter(mContext, this);
        mProductCatalogPresenter.setHybrisDelegate(mHybrisDelegate);
        mProductCatalogPresenter.getCompleteProductList(this);

        JSONObject obj = new JSONObject(TestUtils.readFile(ProductCatalogPresenterTest
                .class, "product_catalog_get_request_page_size_1.txt"));
        mNetworkController.sendSuccess(obj);

        obj = new JSONObject(TestUtils.readFile(ProductCatalogPresenterTest
                .class, "product_catalog_get_request.txt"));
        mNetworkController.sendSuccess(obj);
    }

    private void makePRXData() throws JSONException {

        ArrayList<String> ctns = new ArrayList<>();
        ctns.add("HD5061/01");
        ctns.add("HD7870/10");

        ProductSummaryListRequest mProductSummaryBuilder = new ProductSummaryListRequest(ctns, null,null,null);

        JSONObject obj = new JSONObject(TestUtils.readFile(MockPRXSummaryListExecutor
                .class, "get_prx_success_response_HX9033_64.txt"));
        ResponseData responseData = mProductSummaryBuilder.getResponseData(obj);
        mMockPRXDataBuilder.sendSuccess(responseData);

    }

    @Test
    public void testGetProductListErrorResponse() throws JSONException {
        mProductCatalogPresenter = new ProductCatalogPresenter(mContext, this);
        mProductCatalogPresenter.setHybrisDelegate(mHybrisDelegate);
        mProductCatalogPresenter.getProductCatalog(0, 20, null);

        JSONObject obj = new JSONObject(TestUtils.readFile(ProductCatalogPresenterTest
                .class, "product_catalog_error.txt"));
        NetworkResponse networkResponse = new NetworkResponse
                (500, obj.toString().getBytes(), null, true, 1222L);

        VolleyError error = new ServerError(networkResponse);
        mNetworkController.sendFailure(error);
    }

    @Test
    public void testGetCompleteProductListWithPaginationErrorResponse() throws JSONException {
        mProductCatalogPresenter = new ProductCatalogPresenter(mContext, this);
        mProductCatalogPresenter.setHybrisDelegate(mHybrisDelegate);
        mProductCatalogPresenter.getCompleteProductList(this);

        JSONObject obj = new JSONObject(TestUtils.readFile(ProductCatalogPresenterTest
                .class, "product_catalog_error.txt"));
        NetworkResponse networkResponse = new NetworkResponse(500, obj.toString().getBytes(), null, true, 1222L);

        VolleyError error = new ServerError(networkResponse);
        mNetworkController.sendFailure(error);
    }

    @Test
    public void testNoProductError() throws Exception {
        ProductCatalogPresenter.ProductCatalogListener mProductCatalogListener = Mockito.mock(ProductCatalogPresenter.ProductCatalogListener.class);
        mProductCatalogPresenter.onModelDataError(new Message());
        mProductCatalogListener.onLoadError(NetworkUtility.getInstance().createIAPErrorMessage
                ("", "No product found in your Store."));
    }

    @Test
    public void testNetworkError() throws Exception {
        Message msg = new Message();
        msg.obj = new IAPNetworkError(null, 0, null);
        mProductCatalogPresenter.onModelDataError(msg);
    }

    @Test
    public void testNonIAPNetworkError() throws Exception {
        ProductCatalogPresenter.ProductCatalogListener mProductCatalogListener = Mockito.mock(ProductCatalogPresenter.ProductCatalogListener.class);
        Message msg = new Message();
        msg.obj = new Error();
        mProductCatalogListener.onLoadError(NetworkUtility.getInstance().createIAPErrorMessage
                ("", mContext.getString(R.string.iap_no_product_available)));
        // mProductCatalogPresenter.onModelDataError(msg);
    }

    @Test
    public void testGetCategorisedProductCatalog() throws Exception {
        mProductCatalogPresenter.getCategorizedProductList(mCTNS);
    }

    @Test
    public void testGetCompleteProductListWithStoredProductList() throws Exception {
        CartModelContainer.getInstance().addProduct("HX8033/11", new ProductCatalogData());
        mProductCatalogPresenter.completeProductList(this);
    }

    @Test
    public void testGetCompleteProductListWithSameCountryCode() throws Exception {
        mProductCatalogPresenter.completeProductList(this);
    }

    @Override
    public void onLoadFinished(final ArrayList<ProductCatalogData> data, final PaginationEntity paginationEntity) {
        if (data != null && data.size() > 0) {
            assertEquals(mCTNS.size(), data.size());
        } else {
            assertFalse(false);
        }
    }

    @Override
    public void onLoadError(final IAPNetworkError error) {
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onSuccess(boolean bool) {

    }

    @Override
    public void onGetCartCount(int count) {

    }

    @Override
    public void onUpdateCartCount() {

    }

    @Override
    public void updateCartIconVisibility(boolean shouldShow) {

    }

    @Override
    public void onGetCompleteProductList(ArrayList<String> productList) {
        if (productList != null && productList.size() > 0) {
            assertTrue(true);
        } else {
            assertFalse(false);
        }
    }

    @Override
    public void onFailure(final int errorCode) {
        assertFalse(false);
    }
}
