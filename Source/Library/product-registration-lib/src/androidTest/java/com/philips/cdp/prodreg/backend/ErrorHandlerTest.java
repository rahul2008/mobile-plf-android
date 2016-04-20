package com.philips.cdp.prodreg.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.handler.ProdRegError;
import com.philips.cdp.prodreg.handler.ProdRegListener;
import com.philips.cdp.prodreg.model.RegisteredProduct;

import static org.mockito.Mockito.mock;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ErrorHandlerTest extends MockitoTestCase {

    ErrorHandler errorHandlerMock;
    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        errorHandlerMock = mock(ErrorHandler.class);
    }

    public void testErrorhandle() {
        final UserProduct userProductMock = mock(UserProduct.class);
        UserProduct userProduct = new UserProduct(context) {
            @NonNull
            @Override
            UserProduct getUserProduct() {
                return userProductMock;
            }

            @Override
            protected ErrorHandler getErrorHandler() {
                return errorHandlerMock;
            }
        };

        ProdRegListener prodRegListenerMock = mock(ProdRegListener.class);
        RegisteredProduct product = new RegisteredProduct("ctn", "serial", null, null, null);
        userProduct.getErrorHandler().handleError(product, ProdRegError.INVALID_CTN.getCode(), prodRegListenerMock);

        userProduct.getErrorHandler().handleError(product, ProdRegError.INVALID_SERIALNUMBER.getCode(), prodRegListenerMock);
        userProduct.getErrorHandler().handleError(product, ProdRegError.INVALID_VALIDATION.getCode(), prodRegListenerMock);
        userProduct.getErrorHandler().handleError(product, ProdRegError.NO_INTERNET_AVAILABLE.getCode(), prodRegListenerMock);
        userProduct.getErrorHandler().handleError(product, ProdRegError.INTERNAL_SERVER_ERROR.getCode(), prodRegListenerMock);
        userProduct.getErrorHandler().handleError(product, ProdRegError.METADATA_FAILED.getCode(), prodRegListenerMock);
        userProduct.getErrorHandler().handleError(product, ProdRegError.TIME_OUT.getCode(), prodRegListenerMock);
        userProduct.getErrorHandler().handleError(product, 600, prodRegListenerMock);

        userProduct.getErrorHandler().handleError(product, ProdRegError.ACCESS_TOKEN_INVALID.getCode(), prodRegListenerMock);
    }
}