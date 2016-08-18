package com.philips.cdp.registration.controller;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/18/2016.
 */
public class RefreshLoginSessionTest extends InstrumentationTestCase{
    @Mock
    RefreshLoginSession mRefreshLoginSessionTest;

    @Mock
    RefreshLoginSessionHandler mRefreshLoginSessionHandler;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
        super.setUp();
        context = getInstrumentation().getTargetContext();

        mRefreshLoginSessionHandler = new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {

            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int error) {

            }

            @Override
            public void onRefreshLoginSessionInProgress(String message) {

            }
        };
        mRefreshLoginSessionTest = new RefreshLoginSession(mRefreshLoginSessionHandler);
    }

    @Test
    public void testOnFailure() throws Exception {
        assertNotNull(mRefreshLoginSessionHandler);
        CaptureApiError error = new CaptureApiError();
        mRefreshLoginSessionTest.onFailure(error);
        assertNotNull(mRefreshLoginSessionTest);

    }
}