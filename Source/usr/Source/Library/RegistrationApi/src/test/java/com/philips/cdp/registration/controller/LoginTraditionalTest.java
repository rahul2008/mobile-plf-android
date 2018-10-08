package com.philips.cdp.registration.controller;

import android.content.Context;

import com.janrain.android.Jump;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.handlers.LoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.UserRegistrationInitializer;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

public class LoginTraditionalTest {

    private LoginTraditional loginTraditional;
    @Mock
    private Context mContext;
    @Mock
    private LoginHandler loginHandler;
    @Mock
    private UpdateUserRecordHandler updateUserRecordHandler;
    @Mock
    private User mockUser;
    @Mock
    private RegistrationComponent componentMock;
    @Mock
    private Jump.SignInResultHandler signInResultHandler;
    @Mock
    private LoggingInterface mockLoggingInterface;
    @Mock
    private AppTaggingInterface mockAppTaggingInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(componentMock);
        RLog.setMockLogger(mockLoggingInterface);
        when(componentMock.getLoggingInterface()).thenReturn(mockLoggingInterface);
//        AppTagging.setMockAppTaggingInterface(mockAppTaggingInterface);
//        when(componentMock.getAppTaggingInterface()).thenReturn(mockAppTaggingInterface);


        loginTraditional = new LoginTraditional(loginHandler, mContext, updateUserRecordHandler, "email", "password");
    }

//    @Test(expected = NoClassDefFoundError.class)
//    public void testLoginTraditionally_IfJumpNotInitialized() {
//        UserRegistrationInitializer.getInstance().setJanrainIntialized(true);
//        loginTraditional.loginTraditionally("email", "password");
//        Mockito.verify(signInResultHandler).onSuccess();
//    }
//
//    @Test
//    public void testLoginTraditionally_IfJumpInitialized() {
//        UserRegistrationInitializer.getInstance().setJanrainIntialized(false);
//        loginTraditional.loginTraditionally("email", "password");
//        Mockito.verify(signInResultHandler).onSuccess();
//    }
}
