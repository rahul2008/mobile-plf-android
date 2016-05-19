package com.philips.cdp.di.iap.store;//package com.philips.cdp.di.iap.store;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
@RunWith(RobolectricTestRunner.class)
public class StoreTest {
    @Mock
    Context mContext;
    @Mock IAPUser mIAPMockedUser;
    @Mock StoreConfiguration mStoreConfig;
    private Store mStore;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mIAPMockedUser.getJanRainEmail()).thenReturn(NetworkURLConstants.JANRAIN_EMAIL);
        when(mIAPMockedUser.getJanRainID()).thenReturn(NetworkURLConstants.JANRAIN_ID);
        mStore =  new MockStore(mContext,mIAPMockedUser).getStore();
        mStore.initStoreConfig("en","US",null);
    }

    @Test
    public void confirmOAuthURL() {
        mStore.updateJanRainIDBasedUrls();
        assertEquals(NetworkURLConstants.OAUTH_URL, mStore.getOauthUrl());
    }

    @Test
    public void confirmRefreshOAuthURL() {
        assertEquals(NetworkURLConstants.OAUTH_REFRESH_URL, mStore.getOauthRefreshUrl());
    }

    @Test
    public void confirmCurrentCartDetailsURL() {
        assertEquals(NetworkURLConstants.CART_DETAIL_URL, mStore.getCurrentCartDetailsUrl());
    }

    @Test
    public void confirmCreateCartDetailsURL() {
        assertEquals(NetworkURLConstants.CART_CREATE_URL, mStore.getCreateCartUrl());
    }

    @Test
    public void confirmAddToCartDetailsURL() {
        assertEquals(NetworkURLConstants.CART_ADD_TO_URL, mStore.getAddToCartUrl());
    }

    @Test
    public void confirmModifyProductURL() {
        assertEquals(NetworkURLConstants.CART_MODIFY_PRODUCT_URL, mStore.getModifyProductUrl
                (NetworkURLConstants.DUMMY_PRODUCT_NUBMBER));
    }

    @Test
    public void confirmPaymentDetailsURL() {
        assertEquals(NetworkURLConstants.CART_PAYMENT_DETAILS_URL, mStore.getPaymentDetailsUrl());
    }

    @Test
    public void confirmAddressDetailURL() {
        assertEquals(NetworkURLConstants.ADDRESS_DETAILS_URL, mStore.getAddressDetailsUrl());
    }

    @Test
    public void confirmAddressAlterURL() {
        assertEquals(NetworkURLConstants.ADDRESS_ALTER_URL, mStore.getAddressAlterUrl
                (NetworkURLConstants.DUMMY_PRODUCT_ID));
    }

    @Test
    public void confirmUpdateDeliveryModeURL() {
        assertEquals(NetworkURLConstants.UPDATE_DELIVERY_MODE_URL, mStore.getUpdateDeliveryModeUrl());
    }

    @Test
    public void confirmUpdateDeliveryAddressURL() {
        assertEquals(NetworkURLConstants.UPDATE_DELIVERY_ADDRESS_URL, mStore.getUpdateDeliveryAddressUrl());
    }

    @Test
    public void confirmSetPaymentURL() {
        assertEquals(NetworkURLConstants.PAYMENT_SET_URL, mStore.getSetPaymentUrl
                (NetworkURLConstants.DUMMY_PRODUCT_ID));
    }

    @Test
    public void confirmPlaceOrderURL() {
        assertEquals(NetworkURLConstants.PLACE_ORDER_URL, mStore.getPlaceOrderUrl());
    }

    @Test
    public void confirmSetPaymentDetailsURL() {
        assertEquals(NetworkURLConstants.PAYMENT_DETAILS_URL, mStore.getSetPaymentDetailsUrl());
    }

    @Test
    public void confirmProductCatalogURL() {
        assertEquals(NetworkURLConstants.PRODUCT_CATALOG_URL, mStore.getProductCatalogUrl());
    }

    @Test
    public void confirmJanRainEmail(){
        assertEquals(NetworkURLConstants.JANRAIN_EMAIL, mStore.getJanRainEmail());
    }

    @Test
    public void refreshLoginSession(){
        mStore.refreshLoginSession();
        assertEquals(NetworkURLConstants.JANRAIN_EMAIL, mStore.getJanRainEmail());
    }

    @Test
    public void confirmGetLocale(){
        assertEquals(NetworkURLConstants.LCOALE, mStore.getLocale());
    }

    @Test
    public void verifyUserLogOut() {
        mStore.setUserLogout(true);
        assertTrue(mStore.isUserLoggedOut());
    }

    @Test
    public void checkSetNewUserNotSameAsMockedUser() {
        mStore.setNewUser(mContext);
    }

    @Test
    public void getUserIsSameAsMockedUser() {
        assertEquals(mIAPMockedUser, mStore.getUser());
    }

    @Test
    public void setNewCountryMakesStoreInvalid() {
        mStore.setLangAndCountry("uk", "US");
        assertEquals(false, mStore.isStoreInitialized());
    }

    @Test
    public void setNewLanguageMakesStoreInvalid() {
        mStore.setLangAndCountry("uk", "US");
        assertEquals(false, mStore.isStoreInitialized());
    }

    @Test
    public void getRegionsUrlIsNotNull() {
        assertNotNull(mStore.getRegionsUrl());
    }
}