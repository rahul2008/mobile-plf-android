package com.philips.cdp.prodreg.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.registration.User;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegProcessControllerTest extends MockitoTestCase {

    private ProdRegProcessController prodRegProcessController;
    private ProdRegProcessController.ProcessControllerCallBacks processControllerCallBacksMock;
    private FragmentActivity fragmentActivity;
    private Product productMock;
    private Bundle bundle;
    private User userMock;
    private ProdRegHelper prodRegHelperMock;
    private RegisteredProductsListener registeredProductsListenerMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fragmentActivity = mock(FragmentActivity.class);
        processControllerCallBacksMock = mock(ProdRegProcessController.ProcessControllerCallBacks.class);
        productMock = mock(Product.class);
        userMock = mock(User.class);
        prodRegHelperMock = mock(ProdRegHelper.class);
        registeredProductsListenerMock = mock(RegisteredProductsListener.class);
        bundle = new Bundle();
        when(fragmentActivity.isFinishing()).thenReturn(false);
        prodRegProcessController = new ProdRegProcessController(processControllerCallBacksMock, fragmentActivity) {
            @Override
            protected User getUser() {
                return userMock;
            }

            @NonNull
            @Override
            protected ProdRegHelper getProdRegHelper() {
                return prodRegHelperMock;
            }

            @NonNull
            @Override
            protected RegisteredProductsListener getRegisteredProductsListener() {
                return registeredProductsListenerMock;
            }
        };
        final ArrayList<Product> products = new ArrayList<>();
        products.add(productMock);
        bundle.putSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT, products);
    }

    public void testProcess() {
        when(userMock.isUserSignIn()).thenReturn(true);
        UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        when(prodRegHelperMock.getSignedInUserWithProducts()).thenReturn(userWithProductsMock);
        prodRegProcessController.process(bundle);
        verify(userWithProductsMock).getRegisteredProducts(registeredProductsListenerMock);
        when(userMock.isUserSignIn()).thenReturn(false);
        prodRegProcessController.process(bundle);
        prodRegProcessController.process(bundle);
        verify(processControllerCallBacksMock).dismissLoadingDialog();
        verify(processControllerCallBacksMock).exitProductRegistration();
    }
}