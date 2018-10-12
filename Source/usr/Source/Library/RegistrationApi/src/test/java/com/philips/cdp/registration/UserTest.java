package com.philips.cdp.registration;

import android.app.Activity;
import android.content.Context;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.handlers.SocialLoginProviderHandler;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.listener.HSDPAuthenticationListener;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.logging.LoggingInterface;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserTest extends TestCase {

    private User user;

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
    private LoggingInterface mockLoggingInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
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

}