/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.applocal;

import android.content.Context;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.store.LocalStore;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@RunWith(RobolectricTestRunner.class)
public class LocalStoreTest {
    private Context mContext;
    private LocalStore mAppLocalStore;

    @Before
    public void setUp() throws Exception {
        mContext = getInstrumentation().getContext();
        MockitoAnnotations.initMocks(this);
        mAppLocalStore = new LocalStore(mContext);
    }

    @Test
    public void testSetNewUser() {
        mAppLocalStore.createNewUser(mContext, Mockito.mock(IAPDependencies.class));
    }

    @Test
    public void testInitStoreConfig() {
        mAppLocalStore.initStoreConfig(/*"en", "US",*/ null);
    }

    @Test
    public void testGetCountry() {
        mAppLocalStore.getCountry();
    }

    @Test
    public void testGetLocale() {
        mAppLocalStore.setLangAndCountry("en", "US");
        mAppLocalStore.getLocale();
    }

    @Test
    public void testGetLocaleWithNullParams() {
        mAppLocalStore.setLangAndCountry(null, null);
        mAppLocalStore.getLocale();
    }

    @Test
    public void testGetLocaleWithLangNull() {
        mAppLocalStore.setLangAndCountry(null, "US");
        mAppLocalStore.getLocale();
    }

    @Test
    public void testGetOauthUrl() {
        mAppLocalStore.getOauthUrl();
    }

    @Test
    public void testGetOauthRefreshUrl() {
        mAppLocalStore.getOauthRefreshUrl();
    }

    @Test
    public void testGetJanRainEmail() {
        mAppLocalStore.getJanRainEmail();
    }

    @Test
    public void testGetUser() {
        mAppLocalStore.getUser();
    }

    @Test
    public void getCartsUrl() {
        mAppLocalStore.getCartsUrl();
    }

    @Test
    public void getCurrentCartUrl() {
        mAppLocalStore.getCurrentCartUrl();
    }

    @Test
    public void getCreateCartUrl() {
        mAppLocalStore.getCreateCartUrl();
    }

    @Test
    public void getAddToCartUrl() {
        mAppLocalStore.getAddToCartUrl();
    }

    @Test
    public void getUpdateProductUrl() {
        mAppLocalStore.getUpdateProductUrl("");
    }

    @Test
    public void getPaymentDetailsUrl() {
        mAppLocalStore.getPaymentDetailsUrl();
    }

    @Test
    public void getAddressesUrl() {
        mAppLocalStore.getAddressesUrl();
    }

    @Test
    public void getRegionsUrl() {
        mAppLocalStore.getRegionsUrl();
    }

    @Test
    public void getEditAddressUrl() {
        mAppLocalStore.getEditAddressUrl("");
    }

    @Test
    public void getSetDeliveryModeUrl() {
        mAppLocalStore.getSetDeliveryModeUrl();
    }

    @Test
    public void getSetDeliveryAddressUrl() {
        mAppLocalStore.getSetDeliveryAddressUrl();
    }

    @Test
    public void getMakePaymentUrl() {
        mAppLocalStore.getMakePaymentUrl("");
    }

    @Test
    public void getPlaceOrderUrl() {
        mAppLocalStore.getPlaceOrderUrl();
    }

    @Test
    public void getSetPaymentDetailsUrl() {
        mAppLocalStore.getSetPaymentDetailsUrl();
    }

    @Test
    public void refreshLoginSession() {
        mAppLocalStore.refreshLoginSession();
    }

    @Test
    public void setUserLogout() {
        mAppLocalStore.setNewUser(false);
    }

    @Test
    public void isUserLoggedOut() {
        mAppLocalStore.isNewUser();
    }

    @Test
    public void isStoreInitialized() {
        mAppLocalStore.isStoreInitialized();
    }

    @Test
    public void getOrderDetailUrl() {
        mAppLocalStore.getOrderDetailUrl("");
    }

    @Test
    public void getOrderHistoryUrl() {
        mAppLocalStore.getOrderHistoryUrl("");
    }

    @Test
    public void getDeliveryModesUrl() {
        mAppLocalStore.getDeliveryModesUrl();
    }

    @Test
    public void getUserUrl() {
        mAppLocalStore.getUserUrl();
    }

    @Test
    public void getDeleteCartUrl() {
        mAppLocalStore.getDeleteCartUrl();
    }

    @Test
    public void getPhoneContactUrl() {
        mAppLocalStore.getPhoneContactUrl("");
    }

    @Test
    public void getSearchProductUrl() {
        mAppLocalStore.getSearchProductUrl("");
    }

    @Test
    public void getProductCatalogUrl() {
        mAppLocalStore.getProductCatalogUrl(0, 20);
    }
}