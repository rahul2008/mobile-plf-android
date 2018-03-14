package com.philips.cdp.di.iap.store;

import android.content.Context;
import android.os.Message;

import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.session.MockSynchronizedNetwork;
import com.philips.cdp.di.iap.session.RequestListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(RobolectricTestRunner.class)
public class StoreControllerTest {

    @Mock
    Context mContext;
    @Mock
    StoreConfiguration mStoreConfiguration;

    private StoreController mWebsStoreConfig;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mWebsStoreConfig = new StoreController(mContext, mStoreConfiguration);
    }

//    @Test
//    public void getLocaleShouldDefaultBeNull() {
//        assertNull(mWebsStoreConfig.getLocale());
//    }

    @Test
    public void matchMockedPILLocaleIsReturned() {
        // mWebsStoreConfig.mPILLocale = mock(PILLocale.class);
//        when(mWebsStoreConfig.mPILLocale.getLocaleCode()).thenReturn(NetworkURLConstants.LOCALE);
        MockStoreController config = new MockStoreController(mContext, mStoreConfiguration);
        config.getLocale();
        assertEquals(NetworkURLConstants.LOCALE, config.getLocale());
    }

    @Test
    public void verifyFailureViaConfigThread() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        MockStoreController config = new MockStoreController(mContext, mStoreConfiguration);
        config.initConfig(mock(RequestListener.class));
//        config.startConfigDownloadThread();

        latch.await(1, TimeUnit.SECONDS);
    }

    @Test
    public void verifyFailure() throws InterruptedException {
        StoreController mockedConfig = new MockStoreController(mContext, mStoreConfiguration);
        MockSynchronizedNetwork mockNetwork = (MockSynchronizedNetwork) mockedConfig.getSynchronizedNetwork();
        mockNetwork.setVolleyError(new VolleyError());
        mockedConfig.mRequestListener = getRequestListener();

        mockedConfig.fetchConfiguration();
        assertEquals("US_TUSCANY", mockedConfig.getSiteID());
    }

    @Test
    public void verifySuccess() throws InterruptedException {
        StoreController mockedConfig = new MockStoreController(mContext, mStoreConfiguration);
        MockSynchronizedNetwork mockNetwork = (MockSynchronizedNetwork) mockedConfig.getSynchronizedNetwork();
        String response = TestUtils.readFile(this.getClass(), "config_response.txt");
        mockNetwork.setResponse(response);
        mockedConfig.mRequestListener = getRequestListener();

         mockedConfig.fetchConfiguration();
        assertEquals("US_TUSCANY", mockedConfig.getSiteID());
    }

    private RequestListener getRequestListener() {
        return new RequestListener() {

            @Override
            public void onSuccess(final Message msg) {

            }

            @Override
            public void onError(final Message msg) {

            }
        };
    }

    @Test
    public void verifygenerateStoreUrls() {
        mStoreConfiguration.generateStoreUrls();
    }

    @Test
    public void verifygetWebStoreConfig() {
        mStoreConfiguration.getWebStoreConfig(mContext);
    }
}