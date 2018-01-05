package com.philips.cdp.registration;

import android.app.Activity;
import android.content.Context;

import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.ConsumerArray;
import com.philips.cdp.registration.handlers.AddConsumerInterestHandler;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.handlers.ResendVerificationEmailHandler;
import com.philips.cdp.registration.handlers.SocialLoginHandler;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.handlers.UpdateUserDetailsHandler;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.ui.traditional.ForgotPasswordFragment;
import com.philips.cdp.registration.ui.traditional.mobile.AddSecureEmailPresenter;
import com.philips.cdp.registration.ui.utils.Gender;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by philips on 11/23/17.
 */

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class UserTest {

    User user;

    @Mock
    Context contextMock;

    @Mock
    TraditionalLoginHandler traditionalLoginHandlerMock;


    @Mock
    SocialProviderLoginHandler socialProviderLoginHandlerMock;

    @Mock
    Activity activityMock;

    @Mock
    RegistrationComponent mockRegistrationComponent;

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
    public void loginUsingTraditional() throws Exception {
        user.loginUsingTraditional("email","password",traditionalLoginHandlerMock);
    }

    @Test
    public void loginUserUsingSocialProvider() throws Exception {
        user.loginUserUsingSocialProvider(activityMock,"PROVIDER_NAME",socialProviderLoginHandlerMock,"MERGE_TOKEN");
    }

    @Test
    public void loginUserUsingSocialNativeProvider() throws Exception {
        user.loginUserUsingSocialNativeProvider(activityMock,"PROVIDER_NAME","ACCESS_TOKEN","",socialProviderLoginHandlerMock,"MERGE_TOKEN");
    }

    @Mock
    TraditionalRegistrationHandler traditionalRegistrationHandlerMock;

    @Test
    public void registerUserInfoForTraditional() throws Exception {
        user.registerUserInfoForTraditional("First Name","Given_name","email","password",true,true,traditionalRegistrationHandlerMock);
    }

    @Mock
    ForgotPasswordHandler forgotPasswordHandlerMock;
    @Test
    public void forgotPassword() throws Exception {
//        user.forgotPassword("email",forgotPasswordHandlerMock);
    }

    @Mock
    RefreshLoginSessionHandler refreshLoginSessionHandlerMock;

    @Test
    public void refreshLoginSession() throws Exception {
//        user.refreshLoginSession(refreshLoginSessionHandlerMock);
    }

    @Mock
    ResendVerificationEmailHandler resendVerificationEmailHandlerMock;

    @Test
    public void resendVerificationMail() throws Exception {
//        user.resendVerificationMail("email",resendVerificationEmailHandlerMock);
    }

    @Test
    public void mergeToTraditionalAccount() throws Exception {
     //   user.mergeToTraditionalAccount("email","password","merge_token",traditionalLoginHandlerMock);
    }

    @Test
    public void registerUserInfoForSocial() throws Exception {
        user.registerUserInfoForSocial("given_name","display_name","family_name","user_email",true,true,socialProviderLoginHandlerMock,"social_registration_token");
    }

    @Test
    public void getUserInstance() throws Exception {
        user.getUserInstance();
    }

    @Test
    public void getEmailVerificationStatus() throws Exception {
        user.getEmailVerificationStatus();
    }

    @Test
    public void isEmailVerified() throws Exception {
        user.isEmailVerified();
    }

    @Test
    public void isMobileVerified() throws Exception {
        user.isMobileVerified();
    }

    @Test
    public void isUserSignIn() throws Exception {
        user.isUserSignIn();
    }

    @Test
    public void isTermsAndConditionAccepted() throws Exception {
        user.isTermsAndConditionAccepted();
    }

    @Test
    public void handleMergeFlowError() throws Exception {
        user.handleMergeFlowError("existing_provider");
    }

    @Mock
    UpdateUserDetailsHandler updateUserDetailsHandlerMock;

    @Test
    public void updateReceiveMarketingEmail() throws Exception {
//        user.updateReceiveMarketingEmail(updateUserDetailsHandlerMock,true);
    }

    @Mock
    Date dateMock;
    @Test
    public void updateDateOfBirth() throws Exception {
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
    public void addConsumerInterest() throws Exception {
//        user.addConsumerInterest(addConsumerInterestHandlerMock,consumerArrayMock);
    }

    @Mock
    LogoutHandler logoutHandlerMock;

    @Test
    public void logout() throws Exception {
//        user.logout(logoutHandlerMock);
    }

    @Test
    public void getAccessToken() throws Exception {
        user.getAccessToken();
    }

    @Mock
    RefreshUserHandler refreshUserHandlerMock;

    @Test
    public void refreshUser() throws Exception {
//        user.refreshUser(refreshUserHandlerMock);
    }

    @Test
    public void getEmail() throws Exception {
        user.getEmail();
    }

    @Test
    public void getMobile() throws Exception {
        user.getMobile();
    }

    @Test
    public void getPassword() throws Exception {
        user.getPassword();
    }

    @Test
    public void getGivenName() throws Exception {
        user.getGivenName();
    }

    @Test
    public void getOlderThanAgeLimit() throws Exception {
        user.getOlderThanAgeLimit();
    }

    @Test
    public void getReceiveMarketingEmail() throws Exception {
        user.getReceiveMarketingEmail();
    }

    @Test
    public void getDateOfBirth() throws Exception {
        user.getDateOfBirth();
    }

    @Test
    public void getGender() throws Exception {
        user.getGender();
    }

    @Test
    public void getDisplayName() throws Exception {
        user.getDisplayName();
    }

    @Test
    public void getFamilyName() throws Exception {
    }

    @Test
    public void getJanrainUUID() throws Exception {
        user.getJanrainUUID();
    }

    @Test
    public void getHsdpUUID() throws Exception {
        user.getHsdpUUID();
    }

    @Test
    public void getHsdpAccessToken() throws Exception {
        user.getHsdpAccessToken();
    }

    @Test
    public void getLanguageCode() throws Exception {
        user.getLanguageCode();
    }

    @Test
    public void getCountryCode() throws Exception {
        user.getCountryCode();
    }

    @Mock
    UserRegistrationListener userRegistrationListenerMock;

    @Test
    public void registerUserRegistrationListener() throws Exception {
        user.registerUserRegistrationListener(userRegistrationListenerMock);
    }

    @Test
    public void unRegisterUserRegistrationListener() throws Exception {
        user.unRegisterUserRegistrationListener(userRegistrationListenerMock);
    }

}