package com.philips.cdp.di.iap.applocal;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by indrajitkumar on 27/09/16.
 */
@RunWith(RobolectricTestRunner.class)
public class AppLocalStoreTest {
    @Mock
    Context mContext;
    AppLocalStore mAppLocalStore;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mAppLocalStore = new AppLocalStore(mContext);
    }

    @Test
    public void setNewUser() {
        mAppLocalStore.setNewUser(mContext);
    }

    @Test
    public void initStoreConfig() {
        mAppLocalStore.initStoreConfig("en", "US", null);
    }

    @Test
    public void getCountry() {
        mAppLocalStore.getCountry();
    }

    @Test
    public void getLocale() {
        mAppLocalStore.getLocale();
    }

    @Test
    public void getOauthUrl() {
        mAppLocalStore.getOauthUrl();
    }

    @Test
    public void getOauthRefreshUrl() {
        mAppLocalStore.getOauthRefreshUrl();
    }

    @Test
    public void getJanRainEmail() {
        mAppLocalStore.getJanRainEmail();
    }

    @Test
    public void getUser() {
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
        mAppLocalStore.setUserLogout(false);
    }

    @Test
    public void isUserLoggedOut() {
        mAppLocalStore.isUserLoggedOut();
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