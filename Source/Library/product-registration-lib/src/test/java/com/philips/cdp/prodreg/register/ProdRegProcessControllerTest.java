package com.philips.cdp.prodreg.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.constants.RegistrationState;
import com.philips.cdp.prodreg.fragments.ProdRegConnectionFragment;
import com.philips.cdp.prodreg.fragments.ProdRegRegistrationFragment;
import com.philips.cdp.prodreg.listener.MetadataListener;
import com.philips.cdp.prodreg.listener.RegisteredProductsListener;
import com.philips.cdp.prodreg.listener.SummaryListener;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponse;
import com.philips.cdp.prodreg.model.summary.ProductSummaryResponse;
import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.User;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ProdRegProcessControllerTest extends TestCase {

    private ProdRegProcessController prodRegProcessController;
    private ProdRegProcessController.ProcessControllerCallBacks processControllerCallBacksMock;
    private FragmentActivity fragmentActivity;
    private RegisteredProduct productMock;
    private Bundle bundle;
    private User userMock;
    private ProdRegHelper prodRegHelperMock;
    private RegisteredProductsListener registeredProductsListenerMock;
    private MetadataListener metadataListenerMock;
    private ProdRegRegistrationFragment prodRegRegistrationFragmentMock;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        fragmentActivity = mock(FragmentActivity.class);
        processControllerCallBacksMock = mock(ProdRegProcessController.ProcessControllerCallBacks.class);
        metadataListenerMock = mock(MetadataListener.class);
        productMock = mock(RegisteredProduct.class);
        userMock = mock(User.class);
        prodRegHelperMock = mock(ProdRegHelper.class);
        prodRegRegistrationFragmentMock = mock(ProdRegRegistrationFragment.class);
        registeredProductsListenerMock = mock(RegisteredProductsListener.class);
        bundle = new Bundle();
        when(fragmentActivity.isFinishing()).thenReturn(false);
        prodRegProcessController = new ProdRegProcessController(processControllerCallBacksMock, fragmentActivity) {

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



    @Test
    public void testGetRegisteredProductsListener() {
        final ProdRegConnectionFragment prodRegConnectionFragmentMock = mock(ProdRegConnectionFragment.class);
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

    @Test
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

    @Test
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

    @Test
    public void testProcess() {
        when(userMock.isUserSignIn()).thenReturn(true);
        UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        when(prodRegHelperMock.getSignedInUserWithProducts()).thenReturn(userWithProductsMock);
        prodRegProcessController.process(bundle);
        verify(userWithProductsMock).getRegisteredProducts(registeredProductsListenerMock);
        when(userMock.isUserSignIn()).thenReturn(false);
        bundle.putSerializable(ProdRegConstants.MUL_PROD_REG_CONSTANT, null);
        prodRegProcessController.process(bundle);
        verify(processControllerCallBacksMock).exitProductRegistration();
    }
}