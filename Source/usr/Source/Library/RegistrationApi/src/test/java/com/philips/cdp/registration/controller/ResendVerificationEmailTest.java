/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration.controller;


import android.content.Context;

import com.janrain.android.capture.CaptureApiError;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
public class ResendVerificationEmailTest {

    private Context mContext;
    private ResendVerificationEmail resendVerificationEmail;
    @Mock
    public ResendVerificationEmailHandler mResendVerificationEmail;
    @Mock
    private CaptureApiError captureApiError;
    @Mock
    private UserRegistrationFailureInfo userRegistrationFailureInfo;
    @Mock
    private JumpFlowDownloadStatusListener jumpFlowDownloadStatusListener;
    @Mock
    private RegistrationComponent mockComponent;
    @Mock
    private LoggingInterface mockLoggingInterface;

    @Before
    public void setUp() {
        initMocks(this);
        mContext = getInstrumentation().getContext();
        RegistrationConfiguration.getInstance().setComponent(mockComponent);
        RLog.setMockLogger(mockLoggingInterface);

        when(mockComponent.getCloudLoggingInterface()).thenReturn(mockLoggingInterface);
//        AppTagging.setMockAppTaggingInterface(mockAppTaggingInterface);
//        when(mockComponent.getAppTaggingInterface()).thenReturn(mockAppTaggingInterface);
        // when(appInfraInterface.getTagging().createInstanceForComponent("usr", BuildConfig.VERSION_NAME)).thenReturn(mockAppTaggingInterface);

        resendVerificationEmail = new ResendVerificationEmail(mContext, mResendVerificationEmail);
    }

    @Test
    public void testOnSuccess() {
        resendVerificationEmail.onSuccess();
       // verify(mResendVerificationEmail).onResendVerificationEmailSuccess();
    }


    @Test
    public void testOnFailure() {
      //  when(captureApiError.code).thenReturn(200);
//        UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(captureApiError, mContext);
//        userRegistrationFailureInfo.setErrorCode(captureApiError.code);
        resendVerificationEmail.onFailure(captureApiError);
        //verify(mResendVerificationEmail).onResendVerificationEmailFailedWithError(userRegistrationFailureInfo);
    }
    

    @Test(expected = NullPointerException.class)
    public void testResendVerificationMail_WhenJanrainIntialized() {
        UserRegistrationInitializer.getInstance().setJanrainIntialized(true);
        resendVerificationEmail.resendVerificationMail("xyz@gmail.com");
        verify(jumpFlowDownloadStatusListener).onFlowDownloadFailure();
    }

    @Test(expected = NullPointerException.class)
    public void testOnFlowDownloadSuccess() {
        resendVerificationEmail.onFlowDownloadSuccess();
    }

    @Test
    public void testonFlowDownloadFailure() {
        resendVerificationEmail.onFlowDownloadFailure();
        verify(mResendVerificationEmail).onResendVerificationEmailFailedWithError(any(UserRegistrationFailureInfo.class));
    }
}