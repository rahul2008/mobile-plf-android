//package com.philips.cdp.di.iap.store;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
//@RunWith(MockitoJUnitRunner.class)
//public class StoreTest {
//    private static final String HOST_PORT = "acc.occ.shop.philips.com";
//    private static final String SITE = "US_TUSCANY";
//    private static final String JANRAIN_EMAIL = "a@b.com";
//    private static final String JANRAIN_ID = "sometoken";
//    private static final String OAUTH_URL ="https://" + HOST_PORT + "/pilcommercewebservices/oauth/token?janrain=%s&grant_type=janrain&client_id=mobile_android&client_secret=secret" ;
//
//    private String BASE_URL ="https://" + HOST_PORT + "/pilcommercewebservices/" + "v2/" + SITE +
//            "/users/" + JANRAIN_EMAIL;
//
//    @Mock Context mContext;
//    @Mock IAPUser mIAPUser;
//    @Mock StoreConfiguration mStoreConfig;
//    private Store mStore;
//
//    @Before
//    public void setUp() {
//        when(mStoreConfig.getHostPort()).thenReturn(HOST_PORT);
//        when(mStoreConfig.getSite()).thenReturn(SITE);
//        when(mIAPUser.getJanRainEmail()).thenReturn(JANRAIN_EMAIL);
//        when(mIAPUser.getJanRainID()).thenReturn(JANRAIN_ID);
//        mStore =  getStore();
//    }

//    @Test
//    public void checkBaseUrl() throws Exception {
//        assertEquals(mStore.mBaseURl, BASE_URL);
//
//    }
//
//    @Test
//    public void checkOAuthUrl() {
//        assertEquals(mStore.getOauthUrl(), String.format(OAUTH_URL,JANRAIN_ID));
//    }
/*@Test
    @Test
    public void verifySameJanRainID() {
        Context context = Mockito.mock(Context.class);
        Store store = new Store(context,"hostport","webroot","userid","janRainID");
        Assert.assertEquals(store.getJanRainToken(),"janRainID");
    }

    @Test (expected = NullPointerException.class)
    public void shouldThrowNPEExceptionIfContextIsNullForOAuth() {
        Store store = new Store(null,"hostport","webroot","userid","janRainID");
        store.getAuthToken();
    }

    @Test (expected = NullPointerException.class)
    public void shouldThrowNPEExceptionIfOAuthHandlerNotSet() {
        Store store = new Store(null,"hostport","webroot","userid","janRainID");
        store.getAuthToken();
    }

    @Test
    public void oAuthTokenIsSameFromHandlerStoreAndOAuthHandler() {
        Context context = Mockito.mock(Context.class);
        Store store = new Store(context,"hostport","webroot","userid","janRainID");
        OAuthHandler oAuth = Mockito.mock(OAuthHandler.class);
        store.setAuthHandler(oAuth);
        Mockito.when(store.getAuthToken()).thenReturn("dummyToken");
        Mockito.when(oAuth.generateToken(context,"userid","janRaindID")).thenReturn("dummyToken");

        assertEquals(store.getAuthToken(),oAuth.generateToken(context,"userid","janRaindID"));
    }*/

//    private Store getStore() {
//        return new Store(mContext) {
//            @Override
//            protected StoreConfiguration setStoreConfig(final Context context) {
//                return mStoreConfig;
//            }
//        };
//    }
//}