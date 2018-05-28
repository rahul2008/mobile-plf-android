package com.philips.cdp.registration.ui.traditional;

import android.app.Activity;
import android.content.Context;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookException;
import com.facebook.FacebookRequestError;
import com.facebook.FacebookSdk;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URFaceBookUtility;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.hamcrest.core.AnyOf;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.matchers.Any;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by philips on 5/14/18.
 */

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class HomePresenterTest {

    @Mock
    User userMock;

    @Mock
    RegistrationHelper registrationHelperMock;

    @Mock
    EventHelper eventHelperMock;

    @Mock
    HomeContract homeContractMock;

    @Mock
    Activity contextMock;

    @Mock
    UserRegistrationFailureInfo userRegistrationFailureInfoMock;

    @Mock
    private RegistrationComponent registrationComponentMock;

    @Mock
    private LoggingInterface mockLoggingInterface;

    HomePresenter homePresenter;

    @Mock
    LoginResult loginResultMock;


    AccessToken accessTokenMock;

    //@Mock
    CallbackManager callbackManagerMock;

    @Mock
    URFaceBookUtility urFaceBookUtilityMock;



    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(registrationComponentMock);
        RLog.setMockLogger(mockLoggingInterface);
        callbackManagerMock = CallbackManager.Factory.create();
        Mockito.when(loginResultMock.getAccessToken()).thenReturn(accessTokenMock);
        homePresenter = new HomePresenter(homeContractMock,callbackManagerMock);

    }

    @Test
    public void shouldRegisterFacebookCallBack() throws Exception {
        Mockito.when(homeContractMock.getURFaceBookUtility()).thenReturn(urFaceBookUtilityMock);
        homePresenter.registerFaceBookCallBack();
        Mockito.verify(homeContractMock.getURFaceBookUtility()).registerFaceBookCallBack();
    }

    @Mock
    SocialProviderLoginHandler socialProviderLoginHandlerMock;

    @Test
    public void shouldStartAccessTokenFacebookAuth() throws Exception {
//        homePresenter.startAccessTokenAuthForFacebook();
  //      Mockito.when(homeContractMock.getURFaceBookUtility()).thenReturn(urFaceBookUtilityMock);
   //     Mockito.verify( homeContractMock.getURFaceBookUtility()).startAccessTokenAuthForFacebook(userMock, contextMock, socialProviderLoginHandlerMock, "accessToken", null);
    }
}