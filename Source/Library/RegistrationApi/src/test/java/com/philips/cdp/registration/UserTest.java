package com.philips.cdp.registration;

import android.app.Activity;
import android.content.Context;

import com.philips.cdp.registration.handlers.SocialLoginHandler;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.ui.traditional.mobile.AddSecureEmailPresenter;
import com.philips.cdp.registration.ui.utils.URInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

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


    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
        URInterface.setComponent(mockRegistrationComponent);
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
    }

    @Test
    public void registerUserInfoForTraditional() throws Exception {
    }

    @Test
    public void forgotPassword() throws Exception {
    }

    @Test
    public void refreshLoginSession() throws Exception {
    }

    @Test
    public void resendVerificationMail() throws Exception {
    }

    @Test
    public void mergeToTraditionalAccount() throws Exception {
    }

    @Test
    public void registerUserInfoForSocial() throws Exception {
    }

    @Test
    public void getUserInstance() throws Exception {
    }

    @Test
    public void getEmailVerificationStatus() throws Exception {
    }

    @Test
    public void isEmailVerified() throws Exception {
    }

    @Test
    public void isMobileVerified() throws Exception {
    }

    @Test
    public void isUserSignIn() throws Exception {
    }

    @Test
    public void isTermsAndConditionAccepted() throws Exception {
    }

    @Test
    public void handleMergeFlowError() throws Exception {
    }

    @Test
    public void updateReceiveMarketingEmail() throws Exception {
    }

    @Test
    public void updateDateOfBirth() throws Exception {
    }

    @Test
    public void updateGender() throws Exception {
    }

    @Test
    public void addConsumerInterest() throws Exception {
    }

    @Test
    public void logout() throws Exception {
    }

    @Test
    public void getAccessToken() throws Exception {
    }

    @Test
    public void refreshUser() throws Exception {
    }

    @Test
    public void getEmail() throws Exception {
    }

    @Test
    public void getMobile() throws Exception {
    }

    @Test
    public void getPassword() throws Exception {
    }

    @Test
    public void getGivenName() throws Exception {
    }

    @Test
    public void getOlderThanAgeLimit() throws Exception {
    }

    @Test
    public void getReceiveMarketingEmail() throws Exception {
    }

    @Test
    public void getDateOfBirth() throws Exception {
    }

    @Test
    public void getGender() throws Exception {
    }

    @Test
    public void getDisplayName() throws Exception {
    }

    @Test
    public void getFamilyName() throws Exception {
    }

    @Test
    public void getJanrainUUID() throws Exception {
    }

    @Test
    public void getHsdpUUID() throws Exception {
    }

    @Test
    public void getHsdpAccessToken() throws Exception {
    }

    @Test
    public void getLanguageCode() throws Exception {
    }

    @Test
    public void getCountryCode() throws Exception {
    }

    @Test
    public void registerUserRegistrationListener() throws Exception {
    }

    @Test
    public void unRegisterUserRegistrationListener() throws Exception {
    }

}