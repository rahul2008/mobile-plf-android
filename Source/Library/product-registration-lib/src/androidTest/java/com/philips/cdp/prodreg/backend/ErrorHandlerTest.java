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

        /*userProduct.getErrorHandler().handleError(product, ProdRegError.INVALID_SERIALNUMBER.getCode(), prodRegListenerMock);
        prodRegListenerMock.onProdRegFailed(product);
        userProductMock.updateLocaleCacheOnError(product, ProdRegError.INVALID_SERIALNUMBER, RegistrationState.FAILED);

        userProduct.getErrorHandler().handleError(product, ProdRegError.INVALID_VALIDATION.getCode(), prodRegListenerMock);
        prodRegListenerMock.onProdRegFailed(product);
        userProductMock.updateLocaleCacheOnError(product, ProdRegError.INVALID_VALIDATION, RegistrationState.FAILED);

        userProduct.getErrorHandler().handleError(product, ProdRegError.NO_INTERNET_AVAILABLE.getCode(), prodRegListenerMock);
        prodRegListenerMock.onProdRegFailed(product);
        userProductMock.updateLocaleCacheOnError(product, ProdRegError.NO_INTERNET_AVAILABLE, RegistrationState.FAILED);

        userProduct.getErrorHandler().handleError(product, ProdRegError.INTERNAL_SERVER_ERROR.getCode(), prodRegListenerMock);
        prodRegListenerMock.onProdRegFailed(product);
        userProductMock.updateLocaleCacheOnError(product, ProdRegError.INTERNAL_SERVER_ERROR, RegistrationState.FAILED);

        userProduct.getErrorHandler().handleError(product, ProdRegError.METADATA_FAILED.getCode(), prodRegListenerMock);
        prodRegListenerMock.onProdRegFailed(product);
        userProductMock.updateLocaleCacheOnError(product, ProdRegError.METADATA_FAILED, RegistrationState.FAILED);

        userProduct.getErrorHandler().handleError(product, ProdRegError.TIME_OUT.getCode(), prodRegListenerMock);
        prodRegListenerMock.onProdRegFailed(product);
        userProductMock.updateLocaleCacheOnError(product, ProdRegError.TIME_OUT, RegistrationState.FAILED);

        userProduct.getErrorHandler().handleError(product, 600, prodRegListenerMock);
        prodRegListenerMock.onProdRegFailed(product);
        userProductMock.updateLocaleCacheOnError(product, ProdRegError.UNKNOWN, RegistrationState.FAILED);*/
    }
}