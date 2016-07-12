package com.philips.cdp.prodreg.register;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.prodreg.MockitoTestCase;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.fragments.ProdRegConnectionFragment;
import com.philips.cdp.prodreg.fragments.ProdRegSuccessFragment;
import com.philips.cdp.prodreg.listener.ProdRegListener;
import com.philips.cdp.prodreg.localcache.ProdRegCache;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponseData;
import com.philips.cdp.prodreg.model.summary.Data;
import com.philips.cdp.prodreg.tagging.AnalyticsConstants;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;

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
    private Bundle bundle;
    private ProductMetadataResponseData productMetadataResponseData;
    private Data summaryDataMock;
    private Context context;
    private ProdRegCache prodRegCacheMock;
    private ProdRegHelper prodRegHelperMock;
    private ProdRegConnectionFragment prodRegConnectionFragmentMock;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        fragmentActivity = mock(FragmentActivity.class);
        prodRegHelperMock = mock(ProdRegHelper.class);
        prodRegCacheMock = mock(ProdRegCache.class);
        registerControllerCallBacksMock = mock(ProdRegRegistrationController.RegisterControllerCallBacks.class);
        registeredProductMock = mock(RegisteredProduct.class);
        localRegisteredProductsMock = mock(LocalRegisteredProducts.class);
        prodRegSuccessFragmentMock = mock(ProdRegSuccessFragment.class);
        prodRegConnectionFragmentMock = mock(ProdRegConnectionFragment.class);
        bundle = new Bundle();
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

            @NonNull
            @Override
            protected ProdRegConnectionFragment getConnectionFragment() {
                return prodRegConnectionFragmentMock;
            }

            @NonNull
            @Override
            protected ProdRegHelper getProdRegHelper() {
                return prodRegHelperMock;
            }

            @NonNull
            @Override
            protected ProdRegCache getProdRegCache() {
                return prodRegCacheMock;
            }
        };
        when(fragmentActivity.isFinishing()).thenReturn(false);
        when(productMetadataResponseData.getRequiresDateOfPurchase()).thenReturn("true");
        when(productMetadataResponseData.getRequiresSerialNumber()).thenReturn("true");
        when(productMetadataResponseData.getSerialNumberFormat()).thenReturn("[0-9]-[0-9]-[0-9]");
        bundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT, registeredProductMock);
        bundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT_METADATA, productMetadataResponseData);
        bundle.putSerializable(ProdRegConstants.PROD_REG_PRODUCT_SUMMARY, summaryDataMock);
    }

    public void testHandleState() {
        when(registeredProductMock.isProductAlreadyRegistered(localRegisteredProductsMock)).thenReturn(true);
        prodRegRegistrationController.handleState();
        verify(registerControllerCallBacksMock).showFragment(prodRegSuccessFragmentMock);
    }

    public void testInit() {
        prodRegRegistrationController.init(null);
        verify(registerControllerCallBacksMock).exitProductRegistration();
        prodRegRegistrationController.init(bundle);
        verify(registerControllerCallBacksMock).requireFields(true, true);
        verify(registerControllerCallBacksMock).setSummaryView(summaryDataMock);
        verify(registerControllerCallBacksMock).setProductView(registeredProductMock);
        verify(registeredProductMock, atLeastOnce()).setSerialNumber(registeredProductMock.getSerialNumber());
        verify(registeredProductMock, atLeastOnce()).setPurchaseDate(registeredProductMock.getPurchaseDate());
        verify(registeredProductMock, atLeastOnce()).sendEmail(registeredProductMock.getEmail());
        verify(registeredProductMock, atLeastOnce()).setFriendlyName(registeredProductMock.getFriendlyName());
    }

    public void testIsValidSerialNumber() {
        prodRegRegistrationController.init(bundle);
        assertFalse(prodRegRegistrationController.isValidSerialNumber("1234"));
        verify(registerControllerCallBacksMock).isValidSerialNumber(false, "[0-9]-[0-9]-[0-9]");
    }

    public void testIsValidDate() {
        assertTrue(prodRegRegistrationController.isValidDate("2016-01-22"));
        verify(registerControllerCallBacksMock).isValidDate(true);
        assertFalse(prodRegRegistrationController.isValidDate("2099-01-01"));
        verify(registerControllerCallBacksMock).isValidDate(false);
    }

    public void testRegisterEvent() {
        AppInfraSingleton.setInstance(new AppInfra.Builder().build(context));
        when(prodRegCacheMock.getIntData(AnalyticsConstants.Product_REGISTRATION_START_COUNT)).thenReturn(0);
        UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        when(prodRegHelperMock.getSignedInUserWithProducts()).thenReturn(userWithProductsMock);
        prodRegRegistrationController.init(bundle);
        prodRegRegistrationController.registerProduct("2016-04-28", "1-2-3");
        verify(registerControllerCallBacksMock).showLoadingDialog();
        verify(userWithProductsMock).registerProduct(registeredProductMock);
    }

    public void testGetProdRegListener() {
        AppInfraSingleton.setInstance(new AppInfra.Builder().build(context));
        when(prodRegCacheMock.getIntData(AnalyticsConstants.Product_REGISTRATION_COMPLETED_COUNT)).thenReturn(0);
        ProdRegListener prodRegListener = prodRegRegistrationController.getProdRegListener();
        UserWithProducts userWithProductsMock = mock(UserWithProducts.class);
        prodRegListener.onProdRegSuccess(registeredProductMock, userWithProductsMock);
        verify(registerControllerCallBacksMock).dismissLoadingDialog();
        verify(registerControllerCallBacksMock).showFragment(prodRegSuccessFragmentMock);

        when(registeredProductMock.getProdRegError()).thenReturn(ProdRegError.PRODUCT_ALREADY_REGISTERED);
        prodRegListener.onProdRegFailed(registeredProductMock, userWithProductsMock);
        verify(registerControllerCallBacksMock).showFragment(prodRegConnectionFragmentMock);

        when(registeredProductMock.getProdRegError()).thenReturn(ProdRegError.INVALID_CTN);
        prodRegListener.onProdRegFailed(registeredProductMock, userWithProductsMock);
        verify(registerControllerCallBacksMock).showAlertOnError(registeredProductMock.getProdRegError().getCode());
    }

    public void testGetMethods() {
        assertTrue(prodRegRegistrationController.getConnectionFragment() != null);
        assertTrue(prodRegRegistrationController.getLocalRegisteredProducts() != null);
        assertTrue(prodRegRegistrationController.getProdRegCache() != null);
        assertTrue(prodRegRegistrationController.getProdRegHelper() != null);
        assertTrue(prodRegRegistrationController.getSuccessFragment() != null);
        assertTrue(prodRegRegistrationController.getRegisteredProduct() != null);
    }
}
