package com.philips.cdp.di.iap.session;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.HurlStack;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by 310241054 on 6/30/2016.
 */
public class VolleyWrapperTest {

    private MockNetworkController mNetworkController;
    @Mock
    private Context mContext;

    @Before
    public void setUP() {
        MockitoAnnotations.initMocks(this);
        mNetworkController = new MockNetworkController(mContext);
    }

    @Test
    public void testNewRequestQueueResponse() throws Exception {
        assertEquals(RequestQueue.class, VolleyWrapper.newRequestQueue(mContext, mNetworkController.mIAPHurlStack));
    }

    @Test
    public void testNewRequestQueueNotNull() throws Exception {
        assertNotNull(VolleyWrapper.newRequestQueue(mContext, mNetworkController.mIAPHurlStack));
    }

}