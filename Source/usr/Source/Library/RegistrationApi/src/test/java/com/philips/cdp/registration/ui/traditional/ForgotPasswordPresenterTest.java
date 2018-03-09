package com.philips.cdp.registration.ui.traditional;

import android.content.Context;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

/**
 * Created by philips on 11/23/17.
 */

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ForgotPasswordPresenterTest {


    @Mock
    User userMock;

    @Mock
    RegistrationHelper registrationHelperMock;

    @Mock
    EventHelper eventHelperMock;

    @Mock
    ForgotPasswordContract forgotPasswordContractMock;

    @Mock
    Context contextMock;

    @Mock
    UserRegistrationFailureInfo userRegistrationFailureInfoMock;

    @Mock
    private RegistrationComponent registrationComponentMock;

    @Mock
    private LoggingInterface mockLoggingInterface;

    private ForgotPasswordPresenter forgotPasswordPresenter;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(registrationComponentMock);
        RLog.setMockLogger(mockLoggingInterface);

        forgotPasswordPresenter = new ForgotPasswordPresenter(registrationHelperMock, eventHelperMock, forgotPasswordContractMock, contextMock);
    }

    @Test
    public void onNetWorkStateReceived() throws Exception {
        forgotPasswordPresenter.onNetWorkStateReceived(true);
        Mockito.verify(forgotPasswordContractMock).handleUiState(true);
    }

    @Test
    public void registerListener() throws Exception {
        forgotPasswordPresenter.registerListener();
        Mockito.verify(registrationHelperMock).registerNetworkStateListener(forgotPasswordPresenter);
        Mockito.verify(eventHelperMock).registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, forgotPasswordPresenter);
    }

    @Test
    public void unRegisterListener() throws Exception {
        forgotPasswordPresenter.unRegisterListener();
        Mockito.verify(registrationHelperMock).unRegisterNetworkListener(forgotPasswordPresenter);
        Mockito.verify(eventHelperMock).unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, forgotPasswordPresenter);
    }

    @Test
    public void onEventReceived() throws Exception {
        forgotPasswordPresenter.onEventReceived(RegConstants.JANRAIN_INIT_SUCCESS);
        Mockito.verify(forgotPasswordContractMock).handleUiStatus();
    }

    @Test
    public void onSendForgotPasswordSuccess() throws Exception {
        forgotPasswordPresenter.onSendForgotPasswordSuccess();
        Mockito.verify(forgotPasswordContractMock).handleSendForgotPasswordSuccess();
    }

    @Test
    public void onSendForgotPasswordFailedWithError() throws Exception {
        forgotPasswordPresenter.onSendForgotPasswordFailedWithError(userRegistrationFailureInfoMock);
        Mockito.verify(forgotPasswordContractMock).handleSendForgotPasswordFailedWithError(userRegistrationFailureInfoMock);

    }

    @Test
    public void forgotPasswordRequest() throws Exception {
        forgotPasswordPresenter.forgotPasswordRequest("email", userMock);
        Mockito.verify(userMock).forgotPassword("email", forgotPasswordPresenter);
    }

    @Test(expected = NullPointerException.class)
    public void initateCreateResendSMSIntent() throws Exception {
        forgotPasswordPresenter.initateCreateResendSMSIntent("email");
    }

    @Test
    public void clearDisposable() throws Exception {
        forgotPasswordPresenter.clearDisposable();
    }

}