//package com.philips.cdp.registration.hsdp;
//
//import android.content.Context;
//
//import com.philips.cdp.registration.BuildConfig;
//import com.philips.cdp.registration.CustomRobolectricRunner;
//import com.philips.cdp.registration.configuration.RegistrationConfiguration;
//import com.philips.cdp.registration.controller.ForgotPassword;
//import com.philips.cdp.registration.handlers.LogoutHandler;
//import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
//import com.philips.cdp.registration.handlers.SocialLoginHandler;
//import com.philips.cdp.registration.injection.RegistrationComponent;
//import com.philips.cdp.registration.ui.utils.RLog;
//import com.philips.cdp.registration.ui.utils.URInterface;
//import com.philips.platform.appinfra.logging.LoggingInterface;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.junit.Assert.*;
//
///**
// * Created by philips on 11/30/17.
// */
//
//@RunWith(CustomRobolectricRunner.class)
//@org.robolectric.annotation.Config(constants = BuildConfig.class, sdk = 21)
//public class HsdpUserTest {
//
//
//    @Mock
//    private RegistrationComponent mockRegistrationComponent;
//    @Mock
//    private LoggingInterface mockLoggingInterface;
//    @Mock
//    private Context contextMock;
//
//    HsdpUser hsdpUser;
//    @Mock
//    private RefreshLoginSessionHandler refreshLogInRefreshHandlerMock;
//
//    @Mock
//    private SocialLoginHandler loginHandlerMock;
//
//    @Before
//    public void setUp() throws Exception {
//        MockitoAnnotations.initMocks(this);
//        RegistrationConfiguration.getInstance().setComponent(mockRegistrationComponent);
//        RLog.setMockLogger(mockLoggingInterface);
//        hsdpUser=new HsdpUser(contextMock);
//
//    }
//
//    @Mock
//    LogoutHandler logoutHandlerMock;
//
//    @Test(expected = NullPointerException.class)
//    public void logOut() throws Exception {
//
//        hsdpUser.logOut(logoutHandlerMock);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void refreshToken() throws Exception {
//
//        hsdpUser.refreshToken(refreshLogInRefreshHandlerMock);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void getHsdpUserRecord() throws Exception {
//        hsdpUser.getHsdpUserRecord();
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void deleteFromDisk() throws Exception {
//        hsdpUser.deleteFromDisk();
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void socialLogin() throws Exception {
//        hsdpUser.socialLogin("email","token","refresh_token",loginHandlerMock);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void isHsdpUserSignedIn() throws Exception {
//        hsdpUser.isHsdpUserSignedIn();
//    }
//
//}