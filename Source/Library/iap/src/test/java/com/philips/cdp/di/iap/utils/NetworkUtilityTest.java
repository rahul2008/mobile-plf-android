/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;

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

    @Test(expected = NullPointerException.class)
    public void testShowErrorDialog() {
        //  UIDHelper.init(new ThemeConfiguration(mock(Context.class), ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));
        NetworkUtility.getInstance().showErrorDialog(mock(Activity.class), mock(FragmentManager.class), "", "", "");
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
        //mock(Context.class).getSystemService(Context.CONNECTIVITY_SERVICE);
        // UIDHelper.init(new ThemeConfiguration(mock(Context.class), ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));
        // Setup
        final ConnectivityManager connectivityManager = Mockito.mock( ConnectivityManager.class );
        final NetworkInfo networkInfo = Mockito.mock( NetworkInfo.class );
        Mockito.when( connectivityManager.getNetworkInfo( ConnectivityManager.TYPE_WIFI )).thenReturn( networkInfo );
        Mockito.when( networkInfo.isAvailable() ).thenReturn( true );
        Mockito.when( networkInfo.isConnected() ).thenReturn( true );
        NetworkUtility.getInstance().isNetworkAvailable(connectivityManager);
    }

    @Test
    public void testCreateIAPErrorMessage() {
        //UIDHelper.init(new ThemeConfiguration(mock(Context.class), ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));
        NetworkUtility.getInstance().createIAPErrorMessage("", "");
    }

    private void initTheme() {
        int themeIndex = mock(Intent.class).getIntExtra(IAPConstant.IAP_KEY_ACTIVITY_THEME, DEFAULT_THEME);
        if (themeIndex <= 0) {
            themeIndex = DEFAULT_THEME;
        }
        mock(Context.class).getTheme().applyStyle(themeIndex, true);
        UIDHelper.init(new ThemeConfiguration(mock(Context.class), ContentColor.ULTRA_LIGHT, NavigationColor.BRIGHT, AccentRange.ORANGE));

    }
}