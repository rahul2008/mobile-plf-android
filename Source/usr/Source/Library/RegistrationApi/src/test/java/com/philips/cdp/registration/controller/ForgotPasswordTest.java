package com.philips.cdp.registration.controller;

import android.content.Context;
import android.os.Looper;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.ThreadUtils;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 11/30/17.
 */

@RunWith(CustomRobolectricRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class, sdk = 21)
public class ForgotPasswordTest {


    @Mock
    private RegistrationComponent mockRegistrationComponent;
    @Mock
    private LoggingInterface mockLoggingInterface;
    @Mock
    private Context contextMock;
    @Mock
    private ForgotPasswordHandler forgotPasswordHabdlerMock;
    @Mock
    private Looper mockLooper;

    ForgotPassword forgotPassword;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);
        RLog.setMockLogger(mockLoggingInterface);

        forgotPassword = new ForgotPassword(contextMock, forgotPasswordHabdlerMock);
    }

/*    @Test
    public void onSuccess() throws Exception {
        when(contextMock.getMainLooper()).thenReturn(mockLooper);
        forgotPassword.onSuccess();
    }*/




    @Mock
    Jump.ForgotPasswordResultHandler.ForgetPasswordError forgetPasswordErrorMock;


    @Test(expected = NullPointerException.class)
    public void onFailure() throws Exception {

       forgotPassword.onFailure(forgetPasswordErrorMock);
    }

    @Test(expected = NullPointerException.class)
    public void performForgotPassword() throws Exception {

        forgotPassword.performForgotPassword("email");
    }

    @Test(expected = NullPointerException.class)
    public void onFlowDownloadSuccess() throws Exception {
        forgotPassword.onFlowDownloadSuccess();
    }

/*    @Test
    public void onFlowDownloadFailure() throws Exception {
        when(contextMock.getMainLooper()).thenReturn(mockLooper);
        forgotPassword.onFlowDownloadFailure();
    }*/

}