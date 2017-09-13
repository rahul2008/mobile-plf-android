/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.IAPNetworkError;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.mock;

public class NetworkUtilityTest extends TestCase {

    private final int DEFAULT_THEME = R.style.Theme_DLS_GroupBlue_UltraLight;

    @Mock
    Log mockLog;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void testShowNetworkError() {
        new NetworkUtility();
        String alertTitle = "Network Error";
        String alertBody = "No network available. Please check your network settings and try again.";
        assertEquals(alertTitle, "Network Error");
        assertEquals(alertBody, "No network available. Please check your network settings and try again.");
    }

    @Test
    public void testDismissErrorDialog() {
        NetworkUtility.getInstance().dismissErrorDialog();
    }

    @Test
    public void testShowErrorMessage() {
        NetworkUtility.getInstance().showErrorMessage(mock(Message.class), mock(FragmentManager.class), mock(Context.class));
    }

    @Test
    public void testgetErrorTitleMessageFromErrorCode() {
        NetworkUtility.getInstance().getErrorTitleMessageFromErrorCode(mock(Context.class), 0);
    }

    @Test(expected = RuntimeException.class)
    public void testGetErrorDescriptionMessageFromErrorCode() {
        NetworkUtility.getInstance().getErrorDescriptionMessageFromErrorCode(mock(Context.class), mock(IAPNetworkError.class));
    }

    @Test
    public void testIsNetworkAvailable() {
        final ConnectivityManager connectivityManager = Mockito.mock(ConnectivityManager.class);
        final NetworkInfo networkInfo = Mockito.mock(NetworkInfo.class);
        Mockito.when(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)).thenReturn(networkInfo);
        Mockito.when(networkInfo.isAvailable()).thenReturn(true);
        Mockito.when(networkInfo.isConnected()).thenReturn(true);
        NetworkUtility.getInstance().isNetworkAvailable(connectivityManager);
    }

}