/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.capture.CaptureRecord;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.controller.UpdateDateOfBirth;
import com.philips.cdp.registration.handlers.SocialLoginProviderHandler;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.listener.HSDPAuthenticationListener;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.ui.utils.Gender;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.ServerTime;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.robolectric.RobolectricTestRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@PrepareForTest({Jump.class, CaptureRecord.class,UpdateDateOfBirth.class, Log.class})

@RunWith(PowerMockRunner.class)
public class UserTest {

    private User user;
    @Mock
    private User userMock;


    @Mock
    private Context contextMock;

    @Mock
    private SocialLoginProviderHandler socialLoginProviderHandlerMock;


    @Mock
    private Activity activityMock;

    @Mock
    private RegistrationComponent mockRegistrationComponent;

    @Mock
    HSDPAuthenticationListener mockHsdpAuthenticationListener;
    @Mock
    UpdateUserDetailsHandler mockUpdateUserDetailsHandler;

    @Mock
    private LoggingInterface mockLoggingInterface;
    @Mock
    SimpleDateFormat mockSimpleDateFormat;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mockStatic(Jump.class);
        mockStatic(CaptureRecord.class);
        CaptureRecord captureRecord = PowerMockito.mock(CaptureRecord.class);
        whenNew(SimpleDateFormat.class).withAnyArguments().thenReturn(mockSimpleDateFormat);
        // when(CaptureRecord.class, "loadFromDisk").thenReturn(captureRecord);
        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);
        RLog.setMockLogger(mockLoggingInterface);
        user = new User(contextMock);
    }

    @Test
    public void loginUsingTraditional() {
        user.loginUsingTraditional("email", "password", socialLoginProviderHandlerMock);
    }

    @Test
    public void loginUserUsingSocialProvider() {
        user.loginUserUsingSocialProvider(activityMock, "PROVIDER_NAME", socialLoginProviderHandlerMock, "MERGE_TOKEN");
    }

    @Test
    public void loginUserUsingSocialNativeProvider() {
        user.loginUserUsingSocialNativeProvider(activityMock, "PROVIDER_NAME", "ACCESS_TOKEN", "", socialLoginProviderHandlerMock, "MERGE_TOKEN");
    }

    @Mock
    private TraditionalRegistrationHandler traditionalRegistrationHandlerMock;

    @Test
    public void registerUserInfoForTraditional() {
        user.registerUserInfoForTraditional("First Name", "Given_name", "email", "password", true, true, traditionalRegistrationHandlerMock);
    }

    @Test
    public void registerUserInfoForSocial() {
        user.registerUserInfoForSocial("given_name", "display_name", "family_name", "user_email", true, true, socialLoginProviderHandlerMock, "social_registration_token");
    }

    @Test
    public void getUserInstance() {
        user.getUserInstance();
    }

    @Test
    public void isEmailVerified() {
        user.isEmailVerified();
    }

    @Test
    public void isMobileVerified() {
        user.isMobileVerified();
    }

    @Test
    public void isTermsAndConditionAccepted() {
        user.isTermsAndConditionAccepted();
    }

    @Test
    public void handleMergeFlowError() {
        user.handleMergeFlowError("existing_provider");
    }

    @Test
    public void getAccessToken() {
        user.getAccessToken();
    }

    @Test
    public void getEmail() {
        user.getEmail();
    }

    @Test
    public void getMobile() {
        user.getMobile();
    }

    @Test
    public void getGivenName() {
        user.getGivenName();
    }

    @Test
    public void getOlderThanAgeLimit() {
        user.getOlderThanAgeLimit();
    }

    @Test
    public void getReceiveMarketingEmail() {
        user.getReceiveMarketingEmail();
    }

    @Test
    public void getDateOfBirth() {
        user.getDateOfBirth();
    }

    @Test
    public void getGender() {
        user.getGender();
    }

    @Test
    public void getDisplayName() {
        user.getDisplayName();
    }

    @Test
    public void getFamilyName() {
    }

    @Test
    public void getJanrainUUID() {
        user.getJanrainUUID();
    }

    @Test
    public void getHsdpUUID() {
        user.getHsdpUUID();
    }

    @Test
    public void getHsdpAccessToken() {
        user.getHsdpAccessToken();
    }

    @Test
    public void getLanguageCode() {
        user.getLanguageCode();
    }

    @Test
    public void getCountryCode() {
        user.getCountryCode();
    }

    @Mock
    private UserRegistrationListener userRegistrationListenerMock;

    @Test
    public void registerUserRegistrationListener() {
        user.registerUserRegistrationListener(userRegistrationListenerMock);
    }

    @Test
    public void unRegisterUserRegistrationListener() {
        user.unRegisterUserRegistrationListener(userRegistrationListenerMock);
    }

    @Test
    public void registerHSDPAuthenticationListener() {
        user.registerHSDPAuthenticationListener(mockHsdpAuthenticationListener);
    }

    @Test
    public void unRegisterHSDPAuthenticationListener() {
        user.unRegisterHSDPAuthenticationListener(mockHsdpAuthenticationListener);
    }

    @Test
    public void updateDateOfBirth() throws ParseException {
//        Date date = mock(Date.class);
//        String dateString = new String("20140523");
//        when(mockSimpleDateFormat.format(date)).thenReturn(dateString);
//        when(mockSimpleDateFormat.parse(date.toString()));
        when(userMock.getUserLoginState()).thenReturn(UserLoginState.USER_LOGGED_IN);

        verify(mockUpdateUserDetailsHandler).onUpdateFailedWithError(user.getUserLoginState().ordinal());
    }

    @Test
    public void updateGender() {
        user.updateGender(mockUpdateUserDetailsHandler, Gender.MALE);
    }

}