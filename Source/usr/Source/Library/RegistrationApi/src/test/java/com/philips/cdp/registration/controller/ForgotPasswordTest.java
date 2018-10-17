package com.philips.cdp.registration.controller;

import android.content.Context;
import android.os.Looper;

import com.janrain.android.Jump;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.errors.URError;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.logging.LoggingInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 11/30/17.
 */

@RunWith(MockitoJUnitRunner.class)
public class ForgotPasswordTest extends TestCase {


    @Mock
    Jump.ForgotPasswordResultHandler.ForgetPasswordError forgetPasswordErrorMock;
    @Mock
    JumpFlowDownloadStatusListener jumpFlowDownloadStatusListener;
    @Mock
    private RegistrationComponent mockRegistrationComponent;
    @Mock
    private LoggingInterface mockLoggingInterface;
    @Mock
    private Context contextMock;
    @Mock
    private ForgotPasswordHandler forgotPasswordHandler;
    @Mock
    private UserRegistrationFailureInfo userRegistrationFailureInfo;


    private ForgotPassword forgotPassword;
    @Mock
    private Looper mockLooper;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);
        RLog.setMockLogger(mockLoggingInterface);

        forgotPassword = new ForgotPassword(contextMock, forgotPasswordHandler);
    }

    @Test
    public void onSuccess() {
        when(contextMock.getMainLooper()).thenReturn(mockLooper);
        forgotPassword.onSuccess();
//        verify(forgotPasswordHandler).onSendForgotPasswordSuccess();
    }

    public void onFailure() {
        forgotPassword.onFailure(forgetPasswordErrorMock);
        verify(forgotPasswordHandler).onSendForgotPasswordFailedWithError(userRegistrationFailureInfo);
    }

    @Test
    public void performForgotPassword_IfJanrainInit() {
        UserRegistrationInitializer.getInstance().setJanrainIntialized(true);
        forgotPassword.performForgotPassword("xyz@gmail.com");
    }



    @Test(expected = NullPointerException.class)
    public void onFlowDownloadFailure() {
        forgotPassword.onFlowDownloadFailure();
        verify(forgotPasswordHandler).onSendForgotPasswordFailedWithError(userRegistrationFailureInfo);
    }

}