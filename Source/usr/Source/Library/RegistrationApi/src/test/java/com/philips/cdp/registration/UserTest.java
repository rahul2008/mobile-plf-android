package com.philips.cdp.registration;

import android.app.Activity;
import android.content.Context;

import com.philips.cdp.registration.app.infra.AppInfraWrapper;
import com.philips.cdp.registration.configuration.AppConfiguration;
import com.philips.cdp.registration.configuration.HSDPConfiguration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.ConsumerArray;
import com.philips.cdp.registration.handlers.AddConsumerInterestHandler;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.handlers.SocialLoginProviderHandler;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
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
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

/**
 * Created by philips on 11/23/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserTest extends TestCase {

    User user;

    @Mock
    Context contextMock;

    @Mock
    SocialLoginProviderHandler socialLoginProviderHandlerMock;


    @Mock
    Activity activityMock;

    @Mock
    RegistrationComponent mockRegistrationComponent;

    @Mock
    HSDPAuthenticationListener mockHsdpAuthenticationListener;

    @Mock
    LoggingInterface mockLoggingInterface;

    @Mock
    RegistrationConfiguration registrationConfiguration;

    @Mock
    AppConfiguration appConfiguration;
    @Mock
    AppInfraWrapper appInfraWrapper;
    @Mock
    HSDPConfiguration hsdpConfiguration;


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
    TraditionalRegistrationHandler traditionalRegistrationHandlerMock;

    @Test
    public void registerUserInfoForTraditional() {
        user.registerUserInfoForTraditional("First Name", "Given_name", "email", "password", true, true, traditionalRegistrationHandlerMock);
    }

    @Mock
    ForgotPasswordHandler forgotPasswordHandlerMock;

    @Test
    public void forgotPassword() {
//        user.forgotPassword("email",forgotPasswordHandlerMock);
    }

    @Mock
    RefreshLoginSessionHandler refreshLoginSessionHandlerMock;

    @Test
    public void refreshLoginSession() {
//        user.refreshLoginSession(refreshLoginSessionHandlerMock);
    }

    @Mock
    ResendVerificationEmailHandler resendVerificationEmailHandlerMock;

    @Test
    public void resendVerificationMail() {
//        user.resendVerificationMail("email",resendVerificationEmailHandlerMock);
    }

    @Test
    public void mergeToTraditionalAccount() {
        //   user.mergeToTraditionalAccount("email","password","merge_token",traditionalLoginHandlerMock);
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
    public void getEmailVerificationStatus() {
        user.getEmailOrMobileVerificationStatus();
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
    public void isUserSignIn() {
        user.getUserLoginState();
    }

    @Test
    public void isTermsAndConditionAccepted() {
        user.isTermsAndConditionAccepted();
    }

    @Test
    public void handleMergeFlowError() {
        user.handleMergeFlowError("existing_provider");
    }

    @Mock
    UpdateUserDetailsHandler updateUserDetailsHandlerMock;

    @Test
    public void updateReceiveMarketingEmail() {
//        user.updateReceiveMarketingEmail(updateUserDetailsHandlerMock,true);
    }

    @Mock
    Date dateMock;

    @Test
    public void updateDateOfBirth() {
//        user.updateDateOfBirth(updateUserDetailsHandlerMock,dateMock);
    }

   /* @Mock
    Gender genderMock;*/

   /* @Test
    public void updateGender() throws Exception {
        user.updateGender(updateUserDetailsHandlerMock,genderMock);
    }*/

    @Mock
    AddConsumerInterestHandler addConsumerInterestHandlerMock;

    @Mock
    ConsumerArray consumerArrayMock;

    @Test
    public void addConsumerInterest() {
//        user.addConsumerInterest(addConsumerInterestHandlerMock,consumerArrayMock);
    }

    @Mock
    LogoutHandler logoutHandlerMock;

    @Test
    public void logout() {
//        user.logout(logoutHandlerMock);
    }

    @Test
    public void getAccessToken() {
        user.getAccessToken();
    }

    @Mock
    RefreshUserHandler refreshUserHandlerMock;

    @Test
    public void refreshUser() {
//        user.refreshUser(refreshUserHandlerMock);
    }

    @Test
    public void getEmail() {
        user.getEmail();
    }

    @Test
    public void getMobile() {
        user.getMobile();
    }

//    @Test
//    public void getPassword() throws Exception {
//        user.getPassword();
//    }

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
    UserRegistrationListener userRegistrationListenerMock;

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


//    @Test
//    public void shouldAuthoriseHSDP() {
//        Mockito.when(registrationConfiguration.isHsdpFlow()).thenReturn(true);
//        Mockito.when(hsdpConfiguration.getHsdpSecretId()).thenReturn("sdfsdf");
//        Mockito.when(hsdpConfiguration.getHsdpSharedId()).thenReturn("sdfsfsd");
//        Mockito.when(appConfiguration.getRegistrationEnvironment()).thenReturn("ACCEPTANCE");
//        user.authorizeHSDP(mockHsdpAuthenticationListener);
//
//    }
}