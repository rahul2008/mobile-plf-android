package com.philips.cdp.registration.controller;


import android.content.Context;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.configuration.AppConfiguration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(CustomRobolectricRunner.class)
public class ResendVerificationEmailTest {

    private ResendVerificationEmail resendVerificationEmail;
    @Mock
    public ResendVerificationEmailHandler mResendVerificationEmail;
    @Spy
    private CaptureApiError captureApiError;
    @Mock
    private Context mContext;
    @Mock
    private UserRegistrationFailureInfo userRegistrationFailureInfo;
    @Mock
    private JumpFlowDownloadStatusListener jumpFlowDownloadStatusListener;
    @Mock
    private RegistrationComponent mockComponent;
    @Mock
    private LoggingInterface mockLoggingInterface;
    @Mock
    AppTaggingInterface mockAppTaggingInterface;

    @Before
    public void setUp() {
        initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(mockComponent);
        RLog.setMockLogger(mockLoggingInterface);
        when(mockComponent.getLoggingInterface()).thenReturn(mockLoggingInterface);
        AppTagging.setMockAppTaggingInterface(mockAppTaggingInterface);
        when(mockComponent.getAppTaggingInterface()).thenReturn(mockAppTaggingInterface);
       // when(appInfraInterface.getTagging().createInstanceForComponent("usr", BuildConfig.VERSION_NAME)).thenReturn(mockAppTaggingInterface);

        resendVerificationEmail = new ResendVerificationEmail(mContext, mResendVerificationEmail);
    }

    @Test
    public void testOnSuccess() {
        resendVerificationEmail.onSuccess();
        //  verify(mResendVerificationEmail).onResendVerificationEmailSuccess();
    }


    @Test
    public void testOnFailure() {
        when(captureApiError.code).thenReturn(200);
        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(captureApiError, mContext);
        userRegistrationFailureInfo.setErrorCode(captureApiError.code);
        resendVerificationEmail.onFailure(captureApiError);
        // verify(mResendVerificationEmail).onResendVerificationEmailFailedWithError(userRegistrationFailureInfo);
    }

    @Test
    public void testResendVerificationMail_WhenJanrainNotIntialized() {
        UserRegistrationInitializer.getInstance().setJanrainIntialized(false);
        resendVerificationEmail.resendVerificationMail("xyz@gmail.com");
        verify(jumpFlowDownloadStatusListener).onFlowDownloadFailure();
    }
    @Test
    public void testResendVerificationMail_WhenJanrainIntialized() {
        UserRegistrationInitializer.getInstance().setJanrainIntialized(true);
        resendVerificationEmail.resendVerificationMail("xyz@gmail.com");
        verify(jumpFlowDownloadStatusListener).onFlowDownloadFailure();
    }
    @Test
    public void testOnFlowDownloadSuccess() {
        resendVerificationEmail.onFlowDownloadSuccess();
        // verify(mResendVerificationEmail).onResendVerificationEmailFailedWithError(userRegistrationFailureInfo);
    }

    @Test
    public void testonFlowDownloadFailure() {
        when(userRegistrationFailureInfo.getError()).thenReturn(new CaptureApiError());
        resendVerificationEmail.onFlowDownloadFailure();
        // verify(mResendVerificationEmail).onResendVerificationEmailFailedWithError(userRegistrationFailureInfo);
    }
}