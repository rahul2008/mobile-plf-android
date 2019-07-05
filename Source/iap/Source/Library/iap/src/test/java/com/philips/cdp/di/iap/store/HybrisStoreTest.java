/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.store;

import android.content.Context;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class HybrisStoreTest {
    @Mock
    private IAPUser mIAPMockedUser;

    private StoreListener mStore;
    private MockIAPSetting mockIAPSetting;
    private MockIAPDependencies mockIAPDependencies;
    private Context mContext;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mIAPMockedUser.getJanRainEmail()).thenReturn(NetworkURLConstants.JANRAIN_EMAIL);
        when(mIAPMockedUser.getJanRainID()).thenReturn(NetworkURLConstants.JANRAIN_ID);
        mContext = getInstrumentation().getContext();
        mockIAPSetting = new MockIAPSetting(mContext);
        mockIAPDependencies = new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class));
        mockIAPSetting.setUseLocalData(false);
        mStore = new MockStore(mContext, mIAPMockedUser).getStore(mockIAPSetting,mockIAPDependencies);
        mStore.initStoreConfig(/*"en", "US",*/ null);
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
    public void confirmCreateCartDetailsURL() {
        assertEquals(NetworkURLConstants.CREATE_CART_URL, mStore.getCreateCartUrl());
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
        mStore.createNewUser(mContext,mockIAPDependencies);
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
    public void verifyLocale() {
        mStore.setLangAndCountry("en", "US");
        assertEquals("en_US", mStore.getLocale());
    }

    @Test
    public void testCreateNewUser() {
        mStore.createNewUser(mContext,mockIAPDependencies);
    }

    @Test
    public void testGetCountryUS() {
        CartModelContainer.getInstance().setCountry("US");
        assertEquals("US", mStore.getCountry());
    }

}