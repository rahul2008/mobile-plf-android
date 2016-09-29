package com.philips.cdp.di.iap.store;//package com.philips.cdp.di.iap.store;

import android.content.Context;
import android.os.Message;

import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.TestUtils;
import com.philips.cdp.di.iap.session.MockSynchronizedNetwork;
import com.philips.cdp.di.iap.session.RequestListener;
import com.philips.cdp.localematch.PILLocale;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class WebStoreConfigTest {

    @Mock Context mContext;
    @Mock StoreConfiguration mStoreConfiguration;

    private Object mLock = new Object();
    private WebStoreConfig mWebsStoreConfig;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mWebsStoreConfig = new WebStoreConfig(mContext, mStoreConfiguration);
    }

    @Test
    public void getLocaleShouldDefaultBeNull() {
        assertNull(mWebsStoreConfig.getLocale());
    }

    @Test
    public void matchMockedPILLocaleIsReturned() {
        mWebsStoreConfig.mPILLocale = mock(PILLocale.class);
        when(mWebsStoreConfig.mPILLocale.getLocaleCode()).thenReturn(NetworkURLConstants.LOCALE);
        assertEquals(NetworkURLConstants.LOCALE, mWebsStoreConfig.getLocale());
    }

 /*   @Test
    public void veryInitConfigCallsRefresh() {
        mWebsStoreConfig.mLocaleManager = mock(PILLocaleManager.class);
        mWebsStoreConfig.refresh("en", "US");
        Mockito.verify(mWebsStoreConfig.mLocaleManager, times(1))
                .refresh(mContext, "en", "US");
    }
*/
    @Test
    public void verifyFailureViaConfigThread() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);

        MockWebStoreConfig config = new MockWebStoreConfig(mContext, mStoreConfiguration);
        config.initConfig("en", "us",mock(RequestListener.class));
        config.startConfigDownloadThread();

        latch.await(1, TimeUnit.SECONDS);
    }
    @Test
    public void verifyFailure() throws InterruptedException {
        WebStoreConfig mockedConfig = new MockWebStoreConfig(mContext, mStoreConfiguration);
        MockSynchronizedNetwork mockNetwork = (MockSynchronizedNetwork) mockedConfig.getSynchronizedNetwork();
        mockNetwork.setVolleyError(new VolleyError());
        mockedConfig.mResponseListener = getRequestListener();

        mockedConfig.requestHybrisConfig();
        assertEquals(null, mockedConfig.getSiteID());
    }

    @Test
    public void verifySuccess() throws InterruptedException {
        WebStoreConfig mockedConfig = new MockWebStoreConfig(mContext, mStoreConfiguration);
        MockSynchronizedNetwork mockNetwork = (MockSynchronizedNetwork) mockedConfig.getSynchronizedNetwork();
        String response = TestUtils.readFile(this.getClass(), "config_response.txt");
        mockNetwork.setResponse(response);
        mockedConfig.mResponseListener = getRequestListener();

        mockedConfig.requestHybrisConfig();
        assertEquals("US_Tuscany", mockedConfig.getSiteID());
    }

/*    @Test
    public void verifySuccessWitoutListener() throws InterruptedException {
        WebStoreConfig mockedConfig = spy(mWebsStoreConfig);
        when(mockedConfig.getSynchronizedNetwork()).thenReturn(new MockSynchronizedNetwork((new
                IAPHurlStack(null).getHurlStack())));
        MockSynchronizedNetwork mockNetwork = (MockSynchronizedNetwork) mockedConfig.getSynchronizedNetwork();
        String response = TestUtils.readFile(this.getClass(), "config_response.txt");
        mockNetwork.setResponse(response);

        mockedConfig.requestHybrisConfig();
        assertEquals("US_Tuscany", mockedConfig.getSiteID());
    }*/

    private RequestListener getRequestListener() {
        return new RequestListener(){

            @Override
            public void onSuccess(final Message msg) {

            }

            @Override
            public void onError(final Message msg) {

            }
        };
    }
}