package com.philips.cdp.prodreg.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.handler.ProdRegError;
import com.philips.cdp.prodreg.handler.ProdRegListener;
import com.philips.cdp.prodreg.model.RegisteredProduct;
import com.philips.cdp.prodreg.model.RegistrationState;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ErrorHandlerTest extends MockitoTestCase {

    ErrorHandler errorHandler;
    private Context context;
    private UserProduct userProductMock;


    @Override
    protected void setUp() throws Exception {
        super.setUp();
        userProductMock = mock(UserProduct.class);
        context = getInstrumentation().getContext();
        errorHandler = new ErrorHandler(context) {
            @NonNull
            @Override
            UserProduct getUserProduct(final Context context) {
                return userProductMock;
            }
        };
    }

    public void testErrorHandle() {

        ProdRegListener prodRegListenerMock = mock(ProdRegListener.class);
        RegisteredProduct product = new RegisteredProduct("ctn", "serial", null, null, null);
        errorHandler.handleError(product, ProdRegError.INVALID_CTN.getCode(), prodRegListenerMock);
        verify(prodRegListenerMock).onProdRegFailed(product);
        verify(userProductMock).updateLocaleCacheOnError(product, ProdRegError.INVALID_CTN, RegistrationState.FAILED);

        ProdRegListener prodRegListenerMock1 = mock(ProdRegListener.class);
        RegisteredProduct product1 = new RegisteredProduct("ctn", "serial", null, null, null);
        errorHandler.handleError(product, ProdRegError.INVALID_SERIALNUMBER.getCode(), prodRegListenerMock1);
        verify(prodRegListenerMock).onProdRegFailed(product1);
        verify(userProductMock).updateLocaleCacheOnError(product1, ProdRegError.INVALID_SERIALNUMBER, RegistrationState.FAILED);

        ProdRegListener prodRegListenerMock2 = mock(ProdRegListener.class);
        RegisteredProduct product2 = new RegisteredProduct("ctn", "serial", null, null, null);
        errorHandler.handleError(product, ProdRegError.INVALID_VALIDATION.getCode(), prodRegListenerMock2);
        verify(prodRegListenerMock).onProdRegFailed(product2);
        verify(userProductMock).updateLocaleCacheOnError(product2, ProdRegError.INVALID_VALIDATION, RegistrationState.FAILED);

        ProdRegListener prodRegListenerMock3 = mock(ProdRegListener.class);
        RegisteredProduct product3 = new RegisteredProduct("ctn", "serial", null, null, null);
        errorHandler.handleError(product, ProdRegError.NO_INTERNET_AVAILABLE.getCode(), prodRegListenerMock3);
        verify(prodRegListenerMock).onProdRegFailed(product3);
        verify(userProductMock).updateLocaleCacheOnError(product3, ProdRegError.NO_INTERNET_AVAILABLE, RegistrationState.FAILED);

        ProdRegListener prodRegListenerMock4 = mock(ProdRegListener.class);
        RegisteredProduct product4 = new RegisteredProduct("ctn", "serial", null, null, null);
        errorHandler.handleError(product, ProdRegError.INTERNAL_SERVER_ERROR.getCode(), prodRegListenerMock4);
        verify(prodRegListenerMock).onProdRegFailed(product4);
        verify(userProductMock).updateLocaleCacheOnError(product4, ProdRegError.INTERNAL_SERVER_ERROR, RegistrationState.FAILED);

        ProdRegListener prodRegListenerMock5 = mock(ProdRegListener.class);
        RegisteredProduct product5 = new RegisteredProduct("ctn", "serial", null, null, null);
        errorHandler.handleError(product, ProdRegError.METADATA_FAILED.getCode(), prodRegListenerMock5);
        verify(prodRegListenerMock).onProdRegFailed(product5);
        verify(userProductMock).updateLocaleCacheOnError(product5, ProdRegError.METADATA_FAILED, RegistrationState.FAILED);

        ProdRegListener prodRegListenerMock6 = mock(ProdRegListener.class);
        RegisteredProduct product6 = new RegisteredProduct("ctn", "serial", null, null, null);
        errorHandler.handleError(product, ProdRegError.TIME_OUT.getCode(), prodRegListenerMock6);
        verify(prodRegListenerMock).onProdRegFailed(product6);
        verify(userProductMock).updateLocaleCacheOnError(product6, ProdRegError.TIME_OUT, RegistrationState.FAILED);

        ProdRegListener prodRegListenerMock7 = mock(ProdRegListener.class);
        RegisteredProduct product7 = new RegisteredProduct("ctn", "serial", null, null, null);
        errorHandler.handleError(product, 600, prodRegListenerMock7);
        verify(prodRegListenerMock).onProdRegFailed(product7);
        verify(userProductMock).updateLocaleCacheOnError(product7, ProdRegError.UNKNOWN, RegistrationState.FAILED);
    }
}