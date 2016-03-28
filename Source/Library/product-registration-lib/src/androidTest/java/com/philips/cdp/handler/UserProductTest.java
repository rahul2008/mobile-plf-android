package com.philips.cdp.handler;

import android.content.Context;

import com.philips.cdp.MockitoTestCase;
import com.philips.cdp.backend.ProdRegConstants;
import com.philips.cdp.backend.ProdRegRequestInfo;
import com.philips.cdp.backend.UserProduct;
import com.philips.cdp.error.ErrorType;
import com.philips.cdp.prxclient.response.ResponseData;

import static org.mockito.Mockito.mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UserProductTest extends MockitoTestCase {

    UserProduct userProduct;
    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        userProduct = new UserProduct();
        context = getInstrumentation().getContext();
    }

    public void testHandleErrorCases() {
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        final ProdRegListener listener = new ProdRegListener() {
            @Override
            public void onProdRegSuccess(final ResponseData responseData) {
            }

            @Override
            public void onProdRegFailed(final ErrorType errorType) {
                assertEquals(ErrorType.INVALID_CTN, errorType);
            }
        };
        userProduct.handleError(ErrorType.INVALID_CTN.getCode(), prodRegRequestInfo, listener);
    }

    public void testReturnCorrectRequestType() {
        ProdRegRequestInfo prodRegRequestInfo = mock(ProdRegRequestInfo.class);
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        userProduct.registerProduct(context, prodRegRequestInfo, prodRegListener);
        assertTrue(userProduct.getRequestType().equals(ProdRegConstants.PRODUCT_REGISTRATION));

        userProduct.getRegisteredProducts(context, prodRegRequestInfo, prodRegListener);
        assertTrue(userProduct.getRequestType().equals(ProdRegConstants.FETCH_REGISTERED_PRODUCTS));
    }
}