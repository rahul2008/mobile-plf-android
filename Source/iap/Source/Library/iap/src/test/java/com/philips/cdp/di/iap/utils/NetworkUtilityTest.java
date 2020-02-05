/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Message;
import androidx.fragment.app.FragmentManager;

import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.philips.cdp.di.iap.utils.NetworkUtility.ALERT_DIALOG_TAG;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class NetworkUtilityTest {

    @Mock
    private Context contextMock;

    @Mock
    private FragmentManager fragmentManagerMock;

    @Mock
    private AlertDialogFragment alertDialogFragmentMock;

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
        NetworkUtility.getInstance().showErrorMessage(mock(Message.class), mock(FragmentManager.class), getInstrumentation().getContext());
    }

    @Test
    public void testgetErrorTitleMessageFromErrorCode() {
        NetworkUtility.getInstance().getErrorTitleMessageFromErrorCode(getInstrumentation().getContext(), 0);
    }

    @Test
    public void testGetErrorDescriptionMessageFromErrorCode() {
        NetworkUtility.getInstance().getErrorDescriptionMessageFromErrorCode(getInstrumentation().getContext(), mock(IAPNetworkError.class));
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

    @Test
    public void shouldShowAlertDialog() throws Exception {
        NetworkUtility.getInstance().showDLSDialog(contextMock,"Hi","Hi","Hi",fragmentManagerMock);
    }

    @Test
    public void shouldDismissProgressDialog() throws Exception {
        NetworkUtility.getInstance().dismissAlertFragmentDialog(alertDialogFragmentMock,fragmentManagerMock);

    }

    @Test
    public void testShouldDissMissAlertFragmentWhenItIsNull() throws Exception {
     when(fragmentManagerMock.findFragmentByTag(ALERT_DIALOG_TAG)).thenReturn(alertDialogFragmentMock);
        NetworkUtility.getInstance().dismissAlertFragmentDialog(null,fragmentManagerMock);
    }
}