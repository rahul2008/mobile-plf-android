package com.philips.cdp.registration.ui.traditional;

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
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
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
    Context contextMock;

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
    public void shouldTestOnFaceBookSucces() throws Exception {
        HomePresenter anotherObjSpy = Mockito.spy(homePresenter);
        anotherObjSpy.onSuccess(loginResultMock);
        Mockito.verify(anotherObjSpy).requestUserProfile(loginResultMock);
    }

    @Test
    public void shouldTestOnFaceBookLogInCancel() throws Exception {
        homePresenter.onCancel();
        Mockito.verify(homeContractMock).doHideProgressDialog();
    }

    @Mock
    FacebookException facebookExceptionMock;

    @Test
    public void shouldTestOnFaceBookError() throws Exception {
        Mockito.when(facebookExceptionMock.getMessage()).thenReturn("Facebook authentication failed");
        homePresenter.onError(facebookExceptionMock);
        Mockito.verify(homeContractMock).doHideProgressDialog();
    }

    @Mock
    JSONObject jsonObjectMock;
    @Mock
    GraphResponse graphResponseMock;



    @Test
    public void hideProgressBarWhenFetchFaceBookGraphComesWithError() throws Exception {
        homePresenter.onCompleted(jsonObjectMock,graphResponseMock);
    }
}