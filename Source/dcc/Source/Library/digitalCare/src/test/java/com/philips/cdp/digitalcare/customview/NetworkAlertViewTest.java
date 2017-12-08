package com.philips.cdp.digitalcare.customview;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.philips.platform.uid.view.widget.AlertDialogFragment;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Created by philips on 23/11/17.
 */

public class NetworkAlertViewTest extends TestCase {

    @Mock
    Log mockLog;

    @Mock
    AlertDialogFragment alertDialogFragmentMock;

    @Mock
    DialogFragment dialogFragmentMock;

    @Mock
    Fragment fragmentMock;

    @Mock
    FragmentManager fragmentManagerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);


    }

    @Test
    public void testShowNetworkError() {

        String alertTitle = "Network Error";
        String alertBody = "No network available. Please check your network settings and try again.";
        String buttontext = "Ok";
        new NetworkAlertView().showAlertBox(fragmentMock, alertTitle, alertBody , buttontext);
    }


}
