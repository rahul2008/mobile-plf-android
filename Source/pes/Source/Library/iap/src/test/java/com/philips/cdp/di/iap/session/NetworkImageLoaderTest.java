package com.philips.cdp.di.iap.session;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class NetworkImageLoaderTest {

    @Mock
    private Context mContext;

    NetworkImageLoader mNetworkImageLoader;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSetUpCacheLoader() throws Exception {
        mNetworkImageLoader = new NetworkImageLoader(mContext);
        mNetworkImageLoader.setUpCacheLoader();
    }

    @Test
    public void testGetInstance(){
        assertNotNull(NetworkImageLoader.getInstance(mContext));
    }

    @Test
    public void testGetImageLoader(){
        mNetworkImageLoader = new NetworkImageLoader(mContext);
        assertNotNull(mNetworkImageLoader.getImageLoader());
    }
}