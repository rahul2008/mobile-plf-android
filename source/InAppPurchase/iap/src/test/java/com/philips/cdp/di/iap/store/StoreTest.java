package com.philips.cdp.di.iap.store;

import android.content.Context;

import com.philips.cdp.di.iap.session.OAuthHandler;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class StoreTest {
    @Test
    public void getBaseUrlIsNotNull() throws Exception {
        Context context = Mockito.mock(Context.class);
        Store store = new Store(context,"hostport","webroot","userid","janRainID");
        assertEquals(store.getBaseURl(),"https://hostport/webroot");
    }

    @Test
    public void verifySameUserID() {
        Context context = Mockito.mock(Context.class);
        Store store = new Store(context,"hostport","webroot","userid","janRainID");
        assertEquals(store.getUser(),"userid");
        assertNotNull(store.getUser());
    }

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
    }
}