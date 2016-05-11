package com.philips.cdp.prodreg.register;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.listener.MetadataListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prodreg.prxrequest.ProductMetadataRequest;
import com.philips.cdp.prxclient.RequestManager;
import com.philips.cdp.prxclient.error.PrxError;
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
        product = new Product("HD8967/01", Sector.B2C, Catalog.CONSUMER);
        context = getInstrumentation().getContext();
    }

    public void testProductMetadataCallInvoked() {
        final RequestManager requestManager = mock(RequestManager.class);
        final ResponseListener responseListener = mock(ResponseListener.class);
        final MetadataListener metadataListener = mock(MetadataListener.class);
        final ProductMetadataRequest productMetadataRequest = mock(ProductMetadataRequest.class);
        Product product = new Product("HD8967/01", Sector.B2C, Catalog.CONSUMER) {
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
            ResponseListener getPrxResponseListener(final MetadataListener metadataListener) {
                return responseListener;
            }
        };
        product.getProductMetadata(context, metadataListener);
        verify(requestManager).executeRequest(productMetadataRequest, responseListener);
        final ProductMetadataRequest productMetadataRequest1 = product.getProductMetadataRequest("");
        assertTrue(productMetadataRequest1 instanceof ProductMetadataRequest);
    }

    public void testGetPrxResponseListener() {
        final Product productMock = mock(Product.class);
        Product product = new Product(null, Sector.B2C, Catalog.CONSUMER) {
            @Override
            protected Product getProduct() {
                return productMock;
            }
        };
        MetadataListener metadataListener = mock(MetadataListener.class);
        assertTrue(product.getPrxResponseListener(metadataListener) instanceof ResponseListener);
        ResponseListener responseListener = product.getPrxResponseListener(metadataListener);
        ProductMetadataResponse responseDataMock = mock(ProductMetadataResponse.class);
        responseListener.onResponseSuccess(responseDataMock);
        verify(metadataListener).onMetadataResponse(responseDataMock);
        responseListener.onResponseError(new PrxError("test", 8));
        verify(metadataListener).onErrorResponse("test", 8);
    }

    public void testProductGetMethods() {
        assertTrue(product.getProduct() instanceof Product);
        assertTrue(product.getProductMetadataRequest("HD8967/01") instanceof ProductMetadataRequest);
    }
}