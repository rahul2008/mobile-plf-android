/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.ProductCatalog;

import android.content.Context;

import com.android.volley.NetworkResponse;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogData;
import com.philips.cdp.di.iap.productCatalog.ProductCatalogPresenter;
import com.philips.cdp.di.iap.prx.MockPRXDataBuilder;
import com.philips.cdp.di.iap.response.products.PaginationEntity;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.error.PrxError;
import com.philips.cdp.prxclient.request.ProductSummaryRequest;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;

@RunWith(RobolectricTestRunner.class)
public class ProductCatalogPresenterTest implements ProductCatalogPresenter.LoadListener, IAPListener {

    private MockNetworkController mNetworkController;
    private HybrisDelegate mHybrisDelegate;
    @Mock
    Context mContext;
    public ProductCatalogPresenter mProductCatalogPresenter = new ProductCatalogPresenter(mContext, this);
    private MockPRXDataBuilder mMockPRXDataBuilder;
    ArrayList<String> mCTNS = new ArrayList<>();

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mNetworkController = new MockNetworkController(mContext, new MockIAPDependencies());
        mHybrisDelegate = TestUtils.getStubbedHybrisDelegate();
        mNetworkController = (MockNetworkController) mHybrisDelegate.getNetworkController(null);

        mCTNS.add("HX9033/64");
        mCTNS.add("HX9023/64");
        mCTNS.add("HX9003/64");
    }

    @Test
    public void getProductCatalogVerifySuccess() throws JSONException {
        mProductCatalogPresenter = new ProductCatalogPresenter(mContext, this);
        mMockPRXDataBuilder = new MockPRXDataBuilder(mContext, mCTNS, mProductCatalogPresenter);
        mProductCatalogPresenter.setHybrisDelegate(mHybrisDelegate);
        mProductCatalogPresenter.getProductCatalog(0, 20, null);

        JSONObject obj = new JSONObject(TestUtils.readFile(ProductCatalogPresenterTest
                .class, "product_catalog_get_request.txt"));
        mNetworkController.sendSuccess(obj);

        makePRXData();
    }


    @Test
    public void getProductCatalogVerifyPRXFail() throws JSONException {
        mProductCatalogPresenter = new ProductCatalogPresenter(mContext, this);
        mMockPRXDataBuilder = new MockPRXDataBuilder(mContext, mCTNS, mProductCatalogPresenter);
        mProductCatalogPresenter.setHybrisDelegate(mHybrisDelegate);
        mProductCatalogPresenter.getProductCatalog(0, 20, null);

        JSONObject obj = new JSONObject(TestUtils.readFile(ProductCatalogPresenterTest
                .class, "product_catalog_get_request.txt"));
        mNetworkController.sendSuccess(obj);

        mMockPRXDataBuilder.sendFailure(new PrxError("fail", 500));
    }


    @Test
    public void TestcreateIAPErrorMessage() {
        mProductCatalogPresenter = new ProductCatalogPresenter(mContext, this);
        IAPNetworkError error = mProductCatalogPresenter.createIAPErrorMessage("Appologies");
        boolean isIAPNetworkError = error instanceof IAPNetworkError;
        assert (isIAPNetworkError);
    }

    @Test
    public void getCompleteProductListVerifySuccessPageSize20() throws JSONException {
        mProductCatalogPresenter = new ProductCatalogPresenter(mContext, this);
        mMockPRXDataBuilder = new MockPRXDataBuilder(mContext, mCTNS, mProductCatalogPresenter);
        mProductCatalogPresenter.setHybrisDelegate(mHybrisDelegate);
        mProductCatalogPresenter.getCompleteProductList(this);

        JSONObject obj = new JSONObject(TestUtils.readFile(ProductCatalogPresenterTest
                .class, "product_catalog_get_request.txt"));
        mNetworkController.sendSuccess(obj);
    }

    @Test
    public void getCompleteProductListVerifySuccessPageSize1() throws JSONException {
        mProductCatalogPresenter = new ProductCatalogPresenter(mContext, this);
        mMockPRXDataBuilder = new MockPRXDataBuilder(mContext, mCTNS, mProductCatalogPresenter);
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
        ProductSummaryRequest mProductSummaryBuilder = new ProductSummaryRequest("125", null);

        JSONObject obj = new JSONObject(TestUtils.readFile(MockPRXDataBuilder
                .class, "get_prx_success_response_HX9033_64.txt"));
        ResponseData responseData = mProductSummaryBuilder.getResponseData(obj);
        CartModelContainer.getInstance().addProductSummary("HX9033/64", (SummaryModel) responseData);
        mMockPRXDataBuilder.sendSuccess(responseData);

        obj = new JSONObject(TestUtils.readFile(MockPRXDataBuilder
                .class, "get_prx_success_response_HX9023_64.txt"));
        responseData = mProductSummaryBuilder.getResponseData(obj);
        CartModelContainer.getInstance().addProductSummary("HX9023/64", (SummaryModel) responseData);
        mMockPRXDataBuilder.sendSuccess(responseData);

        obj = new JSONObject(TestUtils.readFile(MockPRXDataBuilder
                .class, "get_prx_success_response_HX9003_64.txt"));
        responseData = mProductSummaryBuilder.getResponseData(obj);
        CartModelContainer.getInstance().setCountry("US");
        CartModelContainer.getInstance().addProductSummary("HX9003/64", (SummaryModel) responseData);
        mMockPRXDataBuilder.sendSuccess(responseData);
    }

    @Test
    public void getProductCatalogVerifyHybrisFail() throws JSONException {
        mProductCatalogPresenter = new ProductCatalogPresenter();
        mMockPRXDataBuilder = new MockPRXDataBuilder(mContext, mCTNS, mProductCatalogPresenter);
        mProductCatalogPresenter.setHybrisDelegate(mHybrisDelegate);
        mProductCatalogPresenter.getProductCatalog(0, 20, null);

        JSONObject obj = new JSONObject(TestUtils.readFile(ProductCatalogPresenterTest
                .class, "product_catalog_error.txt"));
        NetworkResponse networkResponse = new NetworkResponse(500, obj.toString().getBytes(), null, true, 1222L);

        VolleyError error = new ServerError(networkResponse);

        mNetworkController.sendFailure(error);
    }

    @Test
    public void getProductCompleteListHybrisFailPageSize20() throws JSONException {
        mProductCatalogPresenter = new ProductCatalogPresenter();
        mMockPRXDataBuilder = new MockPRXDataBuilder(mContext, mCTNS, mProductCatalogPresenter);
        mProductCatalogPresenter.setHybrisDelegate(mHybrisDelegate);
        mProductCatalogPresenter.getCompleteProductList(this);

        JSONObject obj = new JSONObject(TestUtils.readFile(ProductCatalogPresenterTest
                .class, "product_catalog_error.txt"));
        NetworkResponse networkResponse = new NetworkResponse(500, obj.toString().getBytes(), null, true, 1222L);

        VolleyError error = new ServerError(networkResponse);

        mNetworkController.sendFailure(error);
    }

    @Test
    public void getProductCompleteListHybrisFailForPage1() throws JSONException {
        mProductCatalogPresenter = new ProductCatalogPresenter();
        mMockPRXDataBuilder = new MockPRXDataBuilder(mContext, mCTNS, mProductCatalogPresenter);
        mProductCatalogPresenter.setHybrisDelegate(mHybrisDelegate);
        mProductCatalogPresenter.getCompleteProductList(this);

        JSONObject obj = new JSONObject(TestUtils.readFile(ProductCatalogPresenterTest
                .class, "product_catalog_get_request_page_size_1.txt"));
        mNetworkController.sendSuccess(obj);

        obj = new JSONObject(TestUtils.readFile(ProductCatalogPresenterTest
                .class, "product_catalog_error.txt"));
        NetworkResponse networkResponse = new NetworkResponse(500, obj.toString().getBytes(), null, true, 1222L);

        VolleyError error = new ServerError(networkResponse);

        mNetworkController.sendFailure(error);
    }

    @Test
    public void getHybrisDelegateTest() {
        mProductCatalogPresenter = new ProductCatalogPresenter();
        HybrisDelegate delegate = mProductCatalogPresenter.getHybrisDelegate();
        boolean isHybrisDelegateInstance = delegate instanceof HybrisDelegate;
        assert (isHybrisDelegateInstance);
    }

    @Override
    public void onLoadFinished(final ArrayList<ProductCatalogData> data, final PaginationEntity paginationEntity) {
        assert (data != null);
        assert (paginationEntity != null);
        if (data.size() > 0) {
            assert (data.get(0) instanceof ProductCatalogData);
//            assertEquals(mCTNS.size(), data.size());
        } else {
            assertFalse(false);
        }
    }

    @Override
    public void onLoadError(final IAPNetworkError error) {
        boolean isHybrisError = error instanceof IAPNetworkError;
        assert (isHybrisError);
        assertEquals(error.getStatusCode(), error.getIAPErrorCode());
        assertEquals("PRX", error.getServerError().getErrors().get(0).getType());
        assertEquals("PRX might not have data", error.getServerError().getErrors().get(0).getReason());
        assertEquals("PRX Error", error.getServerError().getErrors().get(0).getSubject());
        assertEquals("No product found in your Store.", error.getServerError().getErrors().get(0).getMessage());
    }

    @Override
    public void onSuccess() {

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
        assert (productList != null);

        if (productList.size() > 0) {
            assert (productList.get(0) instanceof String);
        } else {
            assertFalse(false);
        }
    }

    @Override
    public void onFailure(final int errorCode) {
        assertFalse(false);
    }
}
