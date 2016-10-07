package com.philips.cdp.di.iap.store;//package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.platform.appinfra.AppInfra;

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
public class HybrisStoreTest {
    @Mock
    Context mContext;
    @Mock
    IAPUser mIAPMockedUser;
    @Mock
    StoreConfiguration mStoreConfig;
    @Mock
    AppInfra mAppInfra;
    private StoreListener mStore;
    private MockIAPSetting mockIAPSetting;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mIAPMockedUser.getJanRainEmail()).thenReturn(NetworkURLConstants.JANRAIN_EMAIL);
        when(mIAPMockedUser.getJanRainID()).thenReturn(NetworkURLConstants.JANRAIN_ID);
        mockIAPSetting = new MockIAPSetting(mContext);
        mockIAPSetting.setUseLocalData(false);
        mStore = new MockStore(mContext, mIAPMockedUser).getStore(mockIAPSetting);

        mStore.initStoreConfig("en", "US", null);
    }

    @Test
    public void confirmOAuthURL() {
        if (mStore instanceof HybrisStore) {
            ((HybrisStore) mStore).updateJanRainIDBasedUrls();
            assertEquals(NetworkURLConstants.OAUTH_URL, mStore.getOauthUrl());
        }
    }

    @Test
    public void confirmRefreshOAuthURL() {
        assertEquals(NetworkURLConstants.OAUTH_REFRESH_URL, mStore.getOauthRefreshUrl());
    }

    @Test
    public void confirmCurrentCartDetailsURL() {
        assertEquals(NetworkURLConstants.GET_CARTS_URL, mStore.getCartsUrl());
    }

    @Test
    public void confirmCreateCartDetailsURL() {
        assertEquals(NetworkURLConstants.CREATE_CART_URL, mStore.getCreateCartUrl());
    }

    @Test
    public void confirmAddToCartDetailsURL() {
        assertEquals(NetworkURLConstants.ADD_TO_CART_URL, mStore.getAddToCartUrl());
    }

    @Test
    public void confirmModifyProductURL() {
        assertEquals(NetworkURLConstants.CART_MODIFY_PRODUCT_URL, mStore.getUpdateProductUrl
                (NetworkURLConstants.DUMMY_PRODUCT_NUMBER));
    }

    @Test
    public void confirmPaymentDetailsURL() {
        assertEquals(NetworkURLConstants.GET_PAYMENT_DETAILS_URL, mStore.getPaymentDetailsUrl());
    }

    @Test
    public void confirmAddressDetailURL() {
        assertEquals(NetworkURLConstants.GET_ADDRESSES_URL, mStore.getAddressesUrl());
    }

    @Test
    public void confirmAddressAlterURL() {
        assertEquals(NetworkURLConstants.EDIT_ADDRESS_URL, mStore.getEditAddressUrl
                (NetworkURLConstants.DUMMY_PRODUCT_ID));
    }

    @Test
    public void confirmUpdateDeliveryModeURL() {
        assertEquals(NetworkURLConstants.SET_DELIVERY_MODE_URL, mStore.getSetDeliveryModeUrl());
    }

    @Test
    public void confirmUpdateDeliveryAddressURL() {
        assertEquals(NetworkURLConstants.SET_DELIVERY_ADDRESS_URL, mStore.getSetDeliveryAddressUrl());
    }

    @Test
    public void confirmSetPaymentURL() {
        assertEquals(NetworkURLConstants.MAKE_PAYMENT_URL, mStore.getMakePaymentUrl
                (NetworkURLConstants.DUMMY_PRODUCT_ID));
    }

    @Test
    public void confirmPlaceOrderURL() {
        assertEquals(NetworkURLConstants.PLACE_ORDER_URL, mStore.getPlaceOrderUrl());
    }

    @Test
    public void confirmSetPaymentDetailsURL() {
        assertEquals(NetworkURLConstants.SET_PAYMENT_DETAIL_URL, mStore.getSetPaymentDetailsUrl());
    }

    @Test
    public void confirmProductCatalogURL() {
        assertEquals(NetworkURLConstants.PRODUCT_CATALOG_URL, mStore.getProductCatalogUrl(0, 1));
    }

    @Test
    public void confirmJanRainEmail() {
        assertEquals(NetworkURLConstants.JANRAIN_EMAIL, mStore.getJanRainEmail());
    }

    @Test
    public void refreshLoginSession() {
        mStore.refreshLoginSession();
        assertEquals(NetworkURLConstants.JANRAIN_EMAIL, mStore.getJanRainEmail());
    }

    @Test
    public void confirmGetLocale() {
        assertEquals(NetworkURLConstants.LOCALE, mStore.getLocale());
    }

    @Test
    public void verifyUserLogOut() {
        mStore.setNewUser(true);
        assertTrue(mStore.isNewUser());
    }

    @Test
    public void checkSetNewUserNotSameAsMockedUser() {
        mStore.createNewUser(mContext);
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

    @Test
    public void getOrderHistorylURLValid() {
        assertEquals(NetworkURLConstants.ORDER_HISTORY_URL, mStore.getOrderHistoryUrl(NetworkURLConstants.DUMMY_PAGE_NUMBER));
    }

    @Test
    public void getOrderDetailURLValid() {
        assertEquals(NetworkURLConstants.ORDER_DETAIL_URL, mStore.getOrderDetailUrl(NetworkURLConstants.DUMMY_ORDER_ID));
    }
}