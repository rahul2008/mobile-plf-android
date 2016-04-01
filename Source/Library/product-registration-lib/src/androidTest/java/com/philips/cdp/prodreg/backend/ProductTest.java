package com.philips.cdp.prodreg.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.handler.ErrorType;
import com.philips.cdp.prodreg.handler.ProdRegListener;
import com.philips.cdp.prodreg.prxrequest.ProductMetadataRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProductTest extends MockitoTestCase {

    Product product;
    Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        product = new Product("HD8967/01", "1344", "2016-3-22", Sector.B2C, Catalog.CONSUMER);
        context = getInstrumentation().getContext();
    }

    public void testProductMetadataCallInvoked() {
        final RequestManager requestManager = mock(RequestManager.class);
        final ResponseListener responseListener = mock(ResponseListener.class);
        final ProdRegListener prodRegListener = mock(ProdRegListener.class);
        final ProductMetadataRequest productMetadataRequest = mock(ProductMetadataRequest.class);
        Product product = new Product("HD8967/01", "1344", "2016-3-22", Sector.B2C, Catalog.CONSUMER) {
            @NonNull
            @Override
            RequestManager getRequestManager(final Context context) {
                return requestManager;
            }

            @Override
            public ProductMetadataRequest getProductMetadataRequest(final String ctn) {
                return productMetadataRequest;
            }

            @NonNull
            @Override
            ResponseListener getPrxResponseListener(final ProdRegListener metadataListener) {
                return responseListener;
            }
        };
        product.getProductMetadata(context, prodRegListener);
        verify(requestManager).executeRequest(productMetadataRequest, responseListener);
        final ProductMetadataRequest productMetadataRequest1 = product.getProductMetadataRequest("");
        assertTrue(productMetadataRequest1 instanceof ProductMetadataRequest);
    }

    public void testHandleErrorCases() {
        product.handleError(ErrorType.INVALID_CTN.getCode(), new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.INVALID_CTN, errorType);
            }
        });
    }

    public void testGetPrxResponseListener() {
        final Product productMock = mock(Product.class);
        Product product = new Product(null, null, null, Sector.B2C, Catalog.CONSUMER) {
            @Override
            protected Product getProduct() {
                return productMock;
            }
        };
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        ResponseListener responseListener = product.getPrxResponseListener(prodRegListener);
        ResponseData responseDataMock = mock(ResponseData.class);
        responseListener.onResponseSuccess(responseDataMock);
        verify(prodRegListener).onProdRegSuccess(responseDataMock);
        responseListener.onResponseError("test", 10);
        verify(productMock).handleError(10, prodRegListener);
    }
}