package com.philips.cdp.registration.ui.utils;

import android.content.Context;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginResult;
import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.mobile.FaceBookContractor;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

/**
 * Created by philips on 5/17/18.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class URFaceBookUtilityTest {

    @Mock
    User userMock;

    @Mock
    RegistrationHelper registrationHelperMock;

    @Mock
    EventHelper eventHelperMock;

    @Mock
    FaceBookContractor faceBookContractorMock;

    @Mock
    Context contextMock;

    @Mock
    UserRegistrationFailureInfo userRegistrationFailureInfoMock;

    @Mock
    private RegistrationComponent registrationComponentMock;

    @Mock
    private LoggingInterface mockLoggingInterface;

    URFaceBookUtility urFaceBookUtility;

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
}