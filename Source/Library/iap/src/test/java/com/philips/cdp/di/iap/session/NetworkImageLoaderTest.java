package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.philips.cdp.di.iap.TestUtils;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;

/**
 * Created by sangamesh on 27/09/16.
 */
public class NetworkImageLoaderTest {

    NetworkImageLoader networkImageLoader;
    @Mock
    NetworkImageLoader networkImageLoaderMock;
    @Mock
    private Context contextMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = UnsatisfiedLinkError.class)
    public void testGetImageLoader() throws Exception {
        networkImageLoaderMock=new NetworkImageLoader(contextMock);
        assertNotNull(networkImageLoaderMock.getImageLoader());
    }

    @Test(expected = UnsatisfiedLinkError.class)
    public void shouldTestSetUpCacheLoader() throws Exception {
        networkImageLoaderMock=new NetworkImageLoader(contextMock);
        networkImageLoaderMock.setUpCacheLoader();
    }
}