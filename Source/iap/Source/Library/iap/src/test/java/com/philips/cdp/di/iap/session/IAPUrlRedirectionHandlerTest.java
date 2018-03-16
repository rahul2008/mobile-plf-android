/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.cdp.di.iap.session;

import com.android.volley.AuthFailureError;
import com.android.volley.VolleyError;
import com.janrain.android.engage.JREngageError;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static org.mockito.Mockito.doThrow;

@RunWith(RobolectricTestRunner.class)
public class IAPUrlRedirectionHandlerTest {
    IAPUrlRedirectionHandler mIAPUrlRedirectionHandler;

    @Mock
    IAPJsonRequest iapJsonRequestMock;

    @Mock
    VolleyError volleyErrorMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mIAPUrlRedirectionHandler = new IAPUrlRedirectionHandler(iapJsonRequestMock,volleyErrorMock);
    }

    @Test
    public void getNewRequestWithRedirectedUrl() throws Exception {
        final IAPJsonRequest newRequestWithRedirectedUrl = mIAPUrlRedirectionHandler.getNewRequestWithRedirectedUrl();
        assert newRequestWithRedirectedUrl!=null;
    }

    @Test
    public void getNewRequestWithRedirectedUrlWithAuthFailure() throws Exception {
        doThrow(AuthFailureError.class).when(iapJsonRequestMock.getParams());
        final IAPJsonRequest newRequestWithRedirectedUrl = mIAPUrlRedirectionHandler.getNewRequestWithRedirectedUrl();
        assert newRequestWithRedirectedUrl==null;
    }

    @Test
    public void logError() throws Exception {
    }

    @Test
    public void getLocation() throws Exception {
    }

    @Test
    public void isRedirectionRequired() throws Exception {
    }

}