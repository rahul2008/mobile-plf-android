package com.philips.cdp.prodreg.register;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.fragments.ProdRegSuccessFragment;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponseData;
import com.philips.cdp.prodreg.model.summary.Data;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ProdRegRegistrationControllerTest extends MockitoTestCase {

    private ProdRegRegistrationController prodRegRegistrationController;
    private FragmentActivity fragmentActivity;
    private ProdRegRegistrationController.RegisterControllerCallBacks registerControllerCallBacksMock;
    private RegisteredProduct registeredProductMock;
    private LocalRegisteredProducts localRegisteredProductsMock;
    private ProdRegSuccessFragment prodRegSuccessFragmentMock;
    private Bundle bundleMock;
    private ProductMetadataResponseData productMetadataResponseData;
    private Data summaryDataMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        fragmentActivity = mock(FragmentActivity.class);
        registerControllerCallBacksMock = mock(ProdRegRegistrationController.RegisterControllerCallBacks.class);
        registeredProductMock = mock(RegisteredProduct.class);
        localRegisteredProductsMock = mock(LocalRegisteredProducts.class);
        prodRegSuccessFragmentMock = mock(ProdRegSuccessFragment.class);
        bundleMock = new Bundle();
        productMetadataResponseData = mock(ProductMetadataResponseData.class);
        summaryDataMock = mock(Data.class);
        prodRegRegistrationController = new ProdRegRegistrationController(registerControllerCallBacksMock, fragmentActivity) {
            @Override
            public RegisteredProduct getRegisteredProduct() {
                return registeredProductMock;
            }

            @NonNull
            @Override
            protected LocalRegisteredProducts getLocalRegisteredProducts() {
                return localRegisteredProductsMock;
            }

            @NonNull
            @Override
            protected ProdRegSuccessFragment getSuccessFragment() {
                return prodRegSuccessFragmentMock;
            }
        };
        when(fragmentActivity.isFinishing()).thenReturn(false);
        when(productMetadataResponseData.getRequiresDateOfPurchase()).thenReturn("true");
        when(productMetadataResponseData.getRequiresSerialNumber()).thenReturn("true");
        when(productMetadataResponseData.getSerialNumberFormat()).thenReturn("[0-9]-[0-9]-[0-9]");
        bundleMock.putSerializable(ProdRegConstants.PROD_REG_PRODUCT, registeredProductMock);
        bundleMock.putSerializable(ProdRegConstants.PROD_REG_PRODUCT_METADATA, productMetadataResponseData);
        bundleMock.putSerializable(ProdRegConstants.PROD_REG_PRODUCT_SUMMARY, summaryDataMock);
    }

    public void testHandleState() {
        when(registeredProductMock.isProductAlreadyRegistered(localRegisteredProductsMock)).thenReturn(true);
        prodRegRegistrationController.handleState();
        verify(registerControllerCallBacksMock).showFragment(prodRegSuccessFragmentMock);
    }

    public void testInit() {
        prodRegRegistrationController.init(null);
        verify(registerControllerCallBacksMock).exitProductRegistration();
        prodRegRegistrationController.init(bundleMock);
        verify(registerControllerCallBacksMock).requireFields(true, true);
        verify(registerControllerCallBacksMock).setSummaryView(summaryDataMock);
        verify(registerControllerCallBacksMock).setProductView(registeredProductMock);
        verify(registeredProductMock, atLeastOnce()).setSerialNumber(registeredProductMock.getSerialNumber());
        verify(registeredProductMock, atLeastOnce()).setPurchaseDate(registeredProductMock.getPurchaseDate());
        verify(registeredProductMock, atLeastOnce()).sendEmail(registeredProductMock.getEmail());
        verify(registeredProductMock, atLeastOnce()).setFriendlyName(registeredProductMock.getFriendlyName());
    }

    public void testIsValidSerialNumber() {
        prodRegRegistrationController.init(bundleMock);
        assertFalse(prodRegRegistrationController.isValidSerialNumber("1234"));
        verify(registerControllerCallBacksMock).isValidSerialNumber(false, "[0-9]-[0-9]-[0-9]");
    }

    public void testIsValidDate() {
        prodRegRegistrationController.init(bundleMock);
        assertTrue(prodRegRegistrationController.isValidDate("2016-01-22"));
        verify(registerControllerCallBacksMock).isValidDate(true);
        assertFalse(prodRegRegistrationController.isValidDate("2099-01-01"));
        verify(registerControllerCallBacksMock).isValidDate(false);
    }
}
