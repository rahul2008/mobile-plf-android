package com.philips.cdp.registration.controller;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class RefreshLoginSessionTest extends TestCase {
    RefreshLoginSession mRefreshLoginSession;

    @Mock
    private CaptureApiError mockCaptureApiError;
    @Mock
    RefreshLoginSessionHandler mRefreshLoginSessionHandler;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mRefreshLoginSession = new RefreshLoginSession(mRefreshLoginSessionHandler);
    }

    @Test
    public void testOnSuccess() {
        mRefreshLoginSession.onSuccess();
        Mockito.verify(mRefreshLoginSessionHandler).onRefreshLoginSessionSuccess();
    }

    @Test
    public void testOnFailure() {
        mRefreshLoginSession.onFailure(mockCaptureApiError);
        Mockito.verify(mRefreshLoginSessionHandler).onRefreshLoginSessionFailedWithError(mockCaptureApiError.code);
    }
}