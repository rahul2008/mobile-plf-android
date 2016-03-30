package com.philips.cdp.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.MockitoTestCase;
import com.philips.cdp.handler.ErrorType;
import com.philips.cdp.handler.ProdRegListener;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.response.ResponseData;
import com.philips.cdp.prxclient.response.ResponseListener;
import com.philips.cdp.prxrequest.ProductMetadataRequest;

import org.junit.Test;

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

    @Test
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
            ResponseListener getMetadataResponseListener(final ProdRegListener metadataListener) {
                return responseListener;
            }
        };
        product.getProductMetadata(context, prodRegListener);
        verify(requestManager).executeRequest(productMetadataRequest, responseListener);
        final ProductMetadataRequest productMetadataRequest1 = product.getProductMetadataRequest("");
        assert (productMetadataRequest1 instanceof ProductMetadataRequest);
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
}