package com.philips.cdp.prodreg.backend;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.handler.ProdRegError;
import com.philips.cdp.prodreg.handler.ProdRegListener;
import com.philips.cdp.prodreg.model.RegisteredProduct;
import com.philips.cdp.prodreg.model.RegistrationState;

import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ErrorHandlerTest extends MockitoTestCase {

    ErrorHandler errorHandlerMock;
    UserProduct userProduct;
    private Context context;
    private UserProduct userProductMock;
    private LocalRegisteredProducts localRegisteredProducts;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        errorHandlerMock = mock(ErrorHandler.class);
        userProductMock = mock(UserProduct.class);
        localRegisteredProducts = mock(LocalRegisteredProducts.class);
        userProduct = new UserProduct(context) {
            @NonNull
            @Override
            UserProduct getUserProduct() {
                return userProductMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProductsInstance() {
                return localRegisteredProducts;
            }

            @Override
            protected ErrorHandler getErrorHandler() {
                return errorHandlerMock;
            }
        };
    }

    public void testErrorhandleFoCTN() {
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        RegisteredProduct registeredProduct = new RegisteredProduct("ctn", "Serial", "2016-03-22", null, null);
        registeredProduct.setRegistrationState(RegistrationState.PENDING);
        RegisteredProduct registeredProduct1 = new RegisteredProduct("ctn1", "Serial1", "2016-04-22", null, null);
        registeredProduct1.setRegistrationState(RegistrationState.FAILED);
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        registeredProducts.add(registeredProduct);
        registeredProducts.add(registeredProduct1);
        when(userProductMock.isUserSignedIn(context)).thenReturn(true);
        userProduct.registerCachedProducts(registeredProducts, prodRegListener);
        userProduct.getErrorHandler().handleError(registeredProduct, ProdRegError.INVALID_CTN.getCode(), prodRegListener);
        verify(userProductMock).updateLocaleCacheOnError(registeredProduct, ProdRegError.INVALID_DATE, RegistrationState.FAILED);
        verify(prodRegListener, Mockito.atLeastOnce()).onProdRegFailed(registeredProduct);
    }

    public void testErrorhandleInvalidate() {
        ProdRegListener prodRegListener = mock(ProdRegListener.class);
        RegisteredProduct registeredProduct = new RegisteredProduct("ctn", "Serial", "2016-03-22", null, null);
        registeredProduct.setRegistrationState(RegistrationState.PENDING);
        RegisteredProduct registeredProduct1 = new RegisteredProduct("ctn1", "Serial1", "2016-04-22", null, null);
        registeredProduct1.setRegistrationState(RegistrationState.FAILED);
        ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        registeredProducts.add(registeredProduct);
        registeredProducts.add(registeredProduct1);
        when(userProductMock.isUserSignedIn(context)).thenReturn(true);
        userProduct.registerCachedProducts(registeredProducts, prodRegListener);
        userProduct.getErrorHandler().handleError(registeredProduct, ProdRegError.INVALID_VALIDATION.getCode(), prodRegListener);
        verify(userProductMock).updateLocaleCacheOnError(registeredProduct, ProdRegError.INVALID_DATE, RegistrationState.FAILED);
        verify(prodRegListener, Mockito.atLeastOnce()).onProdRegFailed(registeredProduct);
    }
}