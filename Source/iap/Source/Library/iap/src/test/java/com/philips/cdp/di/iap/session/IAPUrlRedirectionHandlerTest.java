/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.cdp.di.iap.session;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.HashMap;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class IAPUrlRedirectionHandlerTest {
    IAPUrlRedirectionHandler mIAPUrlRedirectionHandler;

    @Mock
    IAPJsonRequest iapJsonRequestMock;

    @Mock
    VolleyError volleyErrorMock;

    @Mock
    AuthFailureError authFailureErrorMock;

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
        doThrow(authFailureErrorMock).when(iapJsonRequestMock).getParams();
        final IAPJsonRequest newRequestWithRedirectedUrl = mIAPUrlRedirectionHandler.getNewRequestWithRedirectedUrl();
        assert newRequestWithRedirectedUrl==null;
    }

    @Test
    public void getNewRequestWithRedirectedUrlWithAuthFailureWhenAuthObjectHasProperErrorString() throws Exception {
        when(authFailureErrorMock.getMessage()).thenReturn("auth error");
        doThrow(authFailureErrorMock).when(iapJsonRequestMock).getParams();
        final IAPJsonRequest newRequestWithRedirectedUrl = mIAPUrlRedirectionHandler.getNewRequestWithRedirectedUrl();
        assert newRequestWithRedirectedUrl==null;
    }

    @Test
    public void getLocationNotNull() throws Exception {
        getIAPUrlRedirectionHandlerWithNetworkresponseInVolleyError();
        when(iapJsonRequestMock.getUrl()).thenReturn("ffff");
        final String location = mIAPUrlRedirectionHandler.getLocation();
        assert location != null;
    }

    @Test
    public void getLocationNull(){
        final String location = mIAPUrlRedirectionHandler.getLocation();
        assert location == null;
    }

    private void getIAPUrlRedirectionHandlerWithNetworkresponseInVolleyError() {
        HashMap map = new HashMap();
        map.put("Location","hh");
        NetworkResponse networkResponse = new NetworkResponse(301,null,map,false,12L);
        VolleyError volleyError = new VolleyError(networkResponse);
        mIAPUrlRedirectionHandler = new IAPUrlRedirectionHandler(iapJsonRequestMock,volleyError);
    }

    @Test
    public void isRedirectionRequiredReturnsFalse() throws Exception {
        final boolean redirectionRequired = mIAPUrlRedirectionHandler.isRedirectionRequired();
        assertFalse(redirectionRequired);
    }

    @Test
    public void isRedirectionRequiredReturnsTrue() throws Exception {
        when(iapJsonRequestMock.getUrl()).thenReturn("hh");
        getIAPUrlRedirectionHandlerWithNetworkresponseInVolleyError();
        final boolean redirectionRequired = mIAPUrlRedirectionHandler.isRedirectionRequired();
        assertTrue(redirectionRequired);
    }

}