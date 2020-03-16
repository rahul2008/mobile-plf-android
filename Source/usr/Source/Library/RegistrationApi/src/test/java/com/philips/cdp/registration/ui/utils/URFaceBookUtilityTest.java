/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration.ui.utils;

import android.app.Activity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.handlers.SocialLoginProviderHandler;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.ui.traditional.mobile.FaceBookContractor;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by philips on 5/17/18.
 */
@RunWith(RobolectricTestRunner.class)
public class URFaceBookUtilityTest {
    
    @Mock
    private User userMock;

    @Mock
    private FaceBookContractor faceBookContractorMock;

    @Mock
    private RegistrationComponent registrationComponentMock;

    @Mock
    private LoggingInterface mockLoggingInterface;

    @Mock
    private LoginResult loginResultMock;

    @Mock
    private FacebookException facebookExceptionMock;

    @Mock
    private Activity activityMock;

    @Mock
    private SocialLoginProviderHandler socialProviderSocialLoginProviderHandlerMock;

    private CallbackManager callbackManagerMock;
    private URFaceBookUtility urFaceBookUtility;
    private AccessToken accessToken;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        accessToken = new AccessToken("someToken", "someApplicationId", "someUserId", null, null, null, null, null, null,null);

        RegistrationConfiguration.getInstance().setComponent(registrationComponentMock);
        RLog.setMockLogger(mockLoggingInterface);
        callbackManagerMock = CallbackManager.Factory.create();
        Mockito.when(loginResultMock.getAccessToken()).thenReturn(accessToken);
        urFaceBookUtility = new URFaceBookUtility(faceBookContractorMock);
    }

    @Test
    public void shouldReturnCallBackManager() throws Exception {
        urFaceBookUtility.getCallBackManager();
    }

    @Test
    public void shouldRequestUserProfileOnSuccessCalled() throws Exception {
        URFaceBookUtility urFaceBookUtilitySpy = Mockito.spy(urFaceBookUtility);
        urFaceBookUtilitySpy.onSuccess(loginResultMock);
        Mockito.verify(urFaceBookUtilitySpy).requestUserProfile(loginResultMock);
    }

    @Test
    public void hideProgressBarWhenFacebookLogInIsCanceled() throws Exception {
        urFaceBookUtility.onCancel();
        Mockito.verify(faceBookContractorMock).doHideProgressDialog();
    }

    @Test
    public void hideProgressBarWhenFacebookExceptionComes() throws Exception {
        urFaceBookUtility.onError(facebookExceptionMock);
        Mockito.verify(faceBookContractorMock).doHideProgressDialog();
    }

    @Test
    public void shouldStartAccessTokenAuthForFacebook() throws Exception {
        urFaceBookUtility.startAccessTokenAuthForFacebook(userMock, activityMock, socialProviderSocialLoginProviderHandlerMock, "accessToken", "mergeToken");
        Mockito.verify(userMock).startTokenAuthForNativeProvider(activityMock,
                RegConstants.SOCIAL_PROVIDER_FACEBOOK, socialProviderSocialLoginProviderHandlerMock, "mergeToken", "accessToken");
    }
}