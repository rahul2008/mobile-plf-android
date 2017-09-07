/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.support.v4.app.FragmentManager;

import com.philips.cdp.di.iap.session.IAPNetworkError;

import junit.framework.TestCase;

import org.junit.Test;

import static org.mockito.Mockito.mock;

public class NetworkUtilityTest extends TestCase {

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
    public void testgetErrorDescriptionMessageFromErrorCode() {
        NetworkUtility.getInstance().getErrorDescriptionMessageFromErrorCode(mock(Context.class), mock(IAPNetworkError.class));
    }

    @Test(expected = NullPointerException.class)
    public void testisNetworkAvailable() {
        NetworkUtility.getInstance().isNetworkAvailable(mock(Context.class));
    }

    @Test
    public void testcreateIAPErrorMessage() {
        NetworkUtility.getInstance().createIAPErrorMessage("", "");
    }
}