package com.philips.cdp.prodreg.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.constants.RegistrationState;
import com.philips.cdp.prodreg.fragments.ProdRegConnectionFragment;
import com.philips.cdp.prodreg.fragments.ProdRegRegistrationFragment;
import com.philips.cdp.prodreg.listener.MetadataListener;
import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.listener.SummaryListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;
import com.philips.cdp.registration.User;

import java.util.ArrayList;

import static org.mockito.Mockito.atLeastOnce;
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
    private RegisteredProduct productMock;
    private Bundle bundle;
    private User userMock;
    private ProdRegHelper prodRegHelperMock;
    private RegisteredProductsListener registeredProductsListenerMock;
    private MetadataListener metadataListenerMock;
    private ProdRegConnectionFragment prodRegConnectionFragmentMock;
    private ProdRegRegistrationFragment prodRegRegistrationFragmentMock;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fragmentActivity = mock(FragmentActivity.class);
        processControllerCallBacksMock = mock(ProdRegProcessController.ProcessControllerCallBacks.class);
        metadataListenerMock = mock(MetadataListener.class);
        productMock = mock(RegisteredProduct.class);
        userMock = mock(User.class);
        prodRegHelperMock = mock(ProdRegHelper.class);
        prodRegConnectionFragmentMock = mock(ProdRegConnectionFragment.class);
        prodRegRegistrationFragmentMock = mock(ProdRegRegistrationFragment.class);
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

            @NonNull
            @Override
            protected MetadataListener getMetadataListener() {
                return metadataListenerMock;
            }

            @NonNull
            @Override
            protected ProdRegRegistrationFragment getProdRegRegistrationFragment() {
                return prodRegRegistrationFragmentMock;
            }
        };
        final ArrayList<RegisteredProduct> products = new ArrayList<>();
        when(productMock.getCtn()).thenReturn("HC5410/83");
        when(productMock.getSerialNumber()).thenReturn("1344");
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

    public void testGetRegisteredProductsListener() {
        prodRegProcessController = new ProdRegProcessController(processControllerCallBacksMock, fragmentActivity) {
            @NonNull
            @Override
            protected MetadataListener getMetadataListener() {
                return metadataListenerMock;
            }

            @NonNull
            @Override
            protected ProdRegConnectionFragment getConnectionFragment() {
                return prodRegConnectionFragmentMock;
            }
        };
        prodRegProcessController.process(bundle);
        final RegisteredProductsListener registeredProductsListener = prodRegProcessController.getRegisteredProductsListener();
        final ArrayList<RegisteredProduct> registeredProducts = new ArrayList<>();
        registeredProducts.add(new RegisteredProduct("124", null, null));
        registeredProductsListener.getRegisteredProducts(registeredProducts, 0);
        verify(productMock).getProductMetadata(fragmentActivity, metadataListenerMock);
        final RegisteredProduct registeredProduct = new RegisteredProduct("HC5410/83", null, null);
        registeredProduct.setSerialNumber("1344");
        registeredProduct.setRegistrationState(RegistrationState.REGISTERED);
        registeredProducts.add(registeredProduct);
        registeredProductsListener.getRegisteredProducts(registeredProducts, 0);
        verify(processControllerCallBacksMock).dismissLoadingDialog();
        verify(processControllerCallBacksMock).showFragment(prodRegConnectionFragmentMock);
    }

    public void testMetadataListener() {
        final SummaryListener summaryListenerMock = mock(SummaryListener.class);
        prodRegProcessController = new ProdRegProcessController(processControllerCallBacksMock, fragmentActivity) {
            @NonNull
            @Override
            protected SummaryListener getSummaryListener() {
                return summaryListenerMock;
            }
        };
        prodRegProcessController.process(bundle);
        ProductMetadataResponse productMetadataResponse = mock(ProductMetadataResponse.class);
        final MetadataListener metadataListener = prodRegProcessController.getMetadataListener();
        metadataListener.onMetadataResponse(productMetadataResponse);
        verify(productMock).getProductSummary(fragmentActivity, productMock, summaryListenerMock);
        metadataListener.onErrorResponse("error in metadata", -1);
        verify(processControllerCallBacksMock).dismissLoadingDialog();
        verify(processControllerCallBacksMock).showAlertOnError(-1);
    }

    public void testGetSummaryListener() {
        prodRegProcessController.process(bundle);
        final SummaryListener summaryListener = prodRegProcessController.getSummaryListener();
        ProductSummaryResponse productSummaryResponseMock = mock(ProductSummaryResponse.class);
        summaryListener.onSummaryResponse(productSummaryResponseMock);
        verify(processControllerCallBacksMock).dismissLoadingDialog();
        verify(processControllerCallBacksMock).showFragment(prodRegRegistrationFragmentMock);
        summaryListener.onErrorResponse("error in getting summary", -1);
        verify(processControllerCallBacksMock, atLeastOnce()).dismissLoadingDialog();
        verify(processControllerCallBacksMock, atLeastOnce()).showFragment(prodRegRegistrationFragmentMock);
    }
}