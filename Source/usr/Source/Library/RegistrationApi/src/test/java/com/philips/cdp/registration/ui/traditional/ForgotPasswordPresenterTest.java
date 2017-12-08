package com.philips.cdp.registration.ui.traditional;

import android.content.Context;
import android.os.Bundle;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;

/**
 * Created by philips on 11/23/17.
 */

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ForgotPasswordPresenterTest {


    @Mock
    User userMock ;

    @Mock
    RegistrationHelper registrationHelperMock;

    @Mock
    EventHelper eventHelperMock;

    @Mock
    ForgotPasswordContract forgotPasswordContractMock;

    @Mock
    Context contextMock;

    @Mock
    private RegistrationComponent registrationComponentMock;
    @Mock
    private LoggingInterface mockLoggingInterface;

    ForgotPasswordPresenter forgotPasswordPresenter;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        URInterface.setComponent(registrationComponentMock);
        RLog.setMockLogger(mockLoggingInterface);

        forgotPasswordPresenter = new ForgotPasswordPresenter(userMock, registrationHelperMock, eventHelperMock, forgotPasswordContractMock, contextMock);
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
    @Mock
    UserRegistrationFailureInfo userRegistrationFailureInfoMock;

    @Test
    public void onSendForgotPasswordFailedWithError() throws Exception {

        forgotPasswordPresenter.onSendForgotPasswordFailedWithError(userRegistrationFailureInfoMock);
        Mockito.verify(forgotPasswordContractMock).handleSendForgotPasswordFailedWithError(userRegistrationFailureInfoMock);

    }

    @Test
    public void forgotPasswordRequest() throws Exception {

        forgotPasswordPresenter.forgotPasswordRequest("email",userMock);

        Mockito.verify( userMock).forgotPassword("email", forgotPasswordPresenter);
    }

    @Test(expected = NullPointerException.class)
    public void onReceiveResult() throws Exception {

        forgotPasswordPresenter.onReceiveResult(1,new Bundle());
    }

    @Test
    public void createResendSMSIntent() throws Exception {

//        Assert.assertNotNull(forgotPasswordPresenter.createResendSMSIntent("URL"));
    }

    @Test
    public void getBodyContent() throws Exception {
//        Assert.assertNotNull(forgotPasswordPresenter.getBodyContent());
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