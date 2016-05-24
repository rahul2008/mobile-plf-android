package com.philips.cdp.prodreg.error;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.RegistrationState;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
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
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.INVALID_CTN, RegistrationState.FAILED);

        RegisteredProduct product1 = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INVALID_SERIALNUMBER.getCode(), prodRegListenerMock);
        verify(prodRegListenerMock, atLeastOnce()).onProdRegFailed(product1, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCache(product1, ProdRegError.INVALID_SERIALNUMBER, RegistrationState.FAILED);

        RegisteredProduct product2 = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INVALID_VALIDATION.getCode(), prodRegListenerMock);
        verify(prodRegListenerMock, atLeastOnce()).onProdRegFailed(product2, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCache(product2, ProdRegError.INVALID_VALIDATION, RegistrationState.FAILED);

        RegisteredProduct product3 = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, ProdRegError.NO_INTERNET_AVAILABLE.getCode(), prodRegListenerMock);
        verify(prodRegListenerMock, atLeastOnce()).onProdRegFailed(product3, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCache(product3, ProdRegError.NO_INTERNET_AVAILABLE, RegistrationState.FAILED);

        RegisteredProduct product4 = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INTERNAL_SERVER_ERROR.getCode(), prodRegListenerMock);
        verify(prodRegListenerMock, atLeastOnce()).onProdRegFailed(product4, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCache(product4, ProdRegError.INTERNAL_SERVER_ERROR, RegistrationState.FAILED);

        RegisteredProduct product6 = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, ProdRegError.TIME_OUT.getCode(), prodRegListenerMock);
        verify(prodRegListenerMock, atLeastOnce()).onProdRegFailed(product6, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCache(product6, ProdRegError.TIME_OUT, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, ProdRegError.ACCESS_TOKEN_INVALID.getCode(), prodRegListenerMock);
        verify(userWithProductsMock).onAccessTokenExpire(product, prodRegListenerMock);

        RegisteredProduct product8 = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, 600, prodRegListenerMock);
        verify(prodRegListenerMock, atLeastOnce()).onProdRegFailed(product8, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCache(product8, ProdRegError.UNKNOWN, RegistrationState.FAILED);

        RegisteredProduct product9 = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, 511, prodRegListenerMock);
        verify(prodRegListenerMock, atLeastOnce()).onProdRegFailed(product9, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCache(product9, ProdRegError.NETWORK_ERROR, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, 1, prodRegListenerMock);
        verify(prodRegListenerMock, atLeastOnce()).onProdRegFailed(product9, userWithProductsMock);
        verify(userWithProductsMock).updateLocaleCache(product9, ProdRegError.PARSE_ERROR, RegistrationState.FAILED);
    }
}