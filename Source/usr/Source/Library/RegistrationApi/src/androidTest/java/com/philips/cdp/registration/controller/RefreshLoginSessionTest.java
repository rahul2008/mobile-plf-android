package com.philips.cdp.registration.controller;

import android.content.Context;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;


public class RefreshLoginSessionTest extends RegistrationApiInstrumentationBase {
    @Mock
    RefreshLoginSession mRefreshLoginSessionTest;

    @Mock
    RefreshLoginSessionHandler mRefreshLoginSessionHandler;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
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
        mRefreshLoginSessionTest.onSuccess();
        assertNotNull(mRefreshLoginSessionTest);

    }
}