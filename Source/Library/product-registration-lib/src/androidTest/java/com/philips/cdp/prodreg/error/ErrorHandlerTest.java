package com.philips.cdp.prodreg.error;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.RegistrationState;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;

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

        RegisteredProduct product = new RegisteredProduct("ctn", null, null);
        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INVALID_CTN.getCode());
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.INVALID_CTN, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INVALID_SERIALNUMBER.getCode());
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.INVALID_SERIALNUMBER, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INVALID_VALIDATION.getCode());
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.INVALID_VALIDATION, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, ProdRegError.NO_INTERNET_AVAILABLE.getCode());
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.NO_INTERNET_AVAILABLE, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, ProdRegError.INTERNAL_SERVER_ERROR.getCode());
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.INTERNAL_SERVER_ERROR, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, ProdRegError.TIME_OUT.getCode());
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.TIME_OUT, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, ProdRegError.ACCESS_TOKEN_INVALID.getCode());
        verify(userWithProductsMock).onAccessTokenExpire(product);

        errorHandler.handleError(userWithProductsMock, product, 600);
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.UNKNOWN, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, 511);
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.NETWORK_ERROR, RegistrationState.FAILED);

        errorHandler.handleError(userWithProductsMock, product, 1);
        verify(userWithProductsMock).updateLocaleCache(product, ProdRegError.PARSE_ERROR, RegistrationState.FAILED);
    }
}