package com.philips.cdp.prodreg.error;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.RegistrationState;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ErrorHandlerTest extends MockitoTestCase {

    ErrorHandler errorHandler;
    private UserWithProducts userWithProductsMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        userWithProductsMock = mock(UserWithProducts.class);
        errorHandler = new ErrorHandler();
    }

    public void testErrorHandle() {

        ProdRegListener prodRegListenerMock = mock(ProdRegListener.class);
        RegisteredProduct product = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INVALID_CTN.getCode(), prodRegListenerMock);
        verify(prodRegListenerMock).onProdRegFailed(product, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCacheOnError(product, ProdRegError.INVALID_CTN, RegistrationState.FAILED);

        ProdRegListener prodRegListenerMock1 = mock(ProdRegListener.class);
        RegisteredProduct product1 = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INVALID_SERIALNUMBER.getCode(), prodRegListenerMock1);
        verify(prodRegListenerMock).onProdRegFailed(product1, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCacheOnError(product1, ProdRegError.INVALID_SERIALNUMBER, RegistrationState.FAILED);

        ProdRegListener prodRegListenerMock2 = mock(ProdRegListener.class);
        RegisteredProduct product2 = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INVALID_VALIDATION.getCode(), prodRegListenerMock2);
        verify(prodRegListenerMock).onProdRegFailed(product2, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCacheOnError(product2, ProdRegError.INVALID_VALIDATION, RegistrationState.FAILED);

        ProdRegListener prodRegListenerMock3 = mock(ProdRegListener.class);
        RegisteredProduct product3 = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, ProdRegError.NO_INTERNET_AVAILABLE.getCode(), prodRegListenerMock3);
        verify(prodRegListenerMock).onProdRegFailed(product3, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCacheOnError(product3, ProdRegError.NO_INTERNET_AVAILABLE, RegistrationState.FAILED);

        ProdRegListener prodRegListenerMock4 = mock(ProdRegListener.class);
        RegisteredProduct product4 = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INTERNAL_SERVER_ERROR.getCode(), prodRegListenerMock4);
        verify(prodRegListenerMock).onProdRegFailed(product4, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCacheOnError(product4, ProdRegError.INTERNAL_SERVER_ERROR, RegistrationState.FAILED);

        ProdRegListener prodRegListenerMock6 = mock(ProdRegListener.class);
        RegisteredProduct product6 = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, ProdRegError.TIME_OUT.getCode(), prodRegListenerMock6);
        verify(prodRegListenerMock).onProdRegFailed(product6, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCacheOnError(product6, ProdRegError.TIME_OUT, RegistrationState.FAILED);

        ProdRegListener prodRegListenerMock7 = mock(ProdRegListener.class);
        errorHandler.handleError(userWithProductsMock, product, ProdRegError.ACCESS_TOKEN_INVALID.getCode(), prodRegListenerMock7);
        verify(userWithProductsMock).onAccessTokenExpire(product, prodRegListenerMock7);

        ProdRegListener prodRegListenerMock8 = mock(ProdRegListener.class);
        RegisteredProduct product8 = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, 600, prodRegListenerMock8);
        verify(prodRegListenerMock).onProdRegFailed(product8, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCacheOnError(product8, ProdRegError.UNKNOWN, RegistrationState.FAILED);

        ProdRegListener prodRegListenerMock9 = mock(ProdRegListener.class);
        RegisteredProduct product9 = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, 511, prodRegListenerMock9);
        verify(prodRegListenerMock).onProdRegFailed(product9, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCacheOnError(product9, ProdRegError.NETWORK_ERROR, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, 1, prodRegListenerMock9);
        verify(prodRegListenerMock).onProdRegFailed(product9, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCacheOnError(product9, ProdRegError.PARSE_ERROR, RegistrationState.FAILED);
    }
}