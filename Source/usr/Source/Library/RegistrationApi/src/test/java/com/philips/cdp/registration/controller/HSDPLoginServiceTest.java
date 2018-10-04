package com.philips.cdp.registration.controller;

import android.content.Context;

import com.janrain.android.Jump;
import com.janrain.android.capture.Capture;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.handlers.LoginHandler;
import com.philips.cdp.registration.handlers.SocialLoginProviderHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.listener.HSDPAuthenticationListener;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class HSDPLoginServiceTest {

    private HSDPLoginService hsdpLoginService;
    @Mock
    private Context mContext;
    @Mock
    private User mUser;
    @Mock
    private HSDPAuthenticationListener mockHsdpAuthenticationListener;
//    @Captor
//    private ArgumentCaptor<HSDPAuthenticationListener> hsdpAuthenticationListener;
    @Mock
    private  SocialLoginProviderHandler socialLoginProviderHandler;
    @Mock
    private RegistrationComponent componentMock;
    @Mock
    private NetworkUtility networkUtility;
    @Captor
    private ArgumentCaptor<LoginHandler> loginHandlerArgumentCaptor;
    @Mock
    private HsdpUser mockHsdpUser;

    @Mock
    private SecureStorageInterface mSecureStorageInterface;
    @Mock
    private SecureStorageInterface.SecureStorageError secureStorageError;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(componentMock);
        hsdpLoginService = new HSDPLoginService(mContext);
        when(mSecureStorageInterface.fetchValueForKey(Capture.JR_REFRESH_SECRET,secureStorageError )).thenReturn("sfsdfsdf");
    }

    @Test
    public void shouldGetUserEmailOrMobileWithEmail() {
        when(mUser.getEmail()).thenReturn("xyz@philips.com");
        hsdpLoginService.getUserEmailOrMobile(mUser);
    }

    @Test
    public void shouldGetUserEmailOrMobileWithMobile() {
        when(mUser.getEmail()).thenReturn("234242342424");
        hsdpLoginService.getUserEmailOrMobile(mUser);
        Mockito.verify(mUser).getMobile();
    }



    @Test(expected = NullPointerException.class)
    public void shouldHsdpLoginOnSuccess() {
        hsdpLoginService.hsdpUser = mockHsdpUser;
        when(networkUtility.isNetworkAvailable()).thenReturn(true);
        ArgumentCaptor<HSDPAuthenticationListener> myEventHandlerCaptor = ArgumentCaptor.forClass(HSDPAuthenticationListener.class);

        ArgumentCaptor<LoginHandler> myLoginHandlerCaptor = ArgumentCaptor.forClass(LoginHandler.class);
        hsdpLoginService.hsdpLogin("dfsdfsd","xyz@gmail.com",myLoginHandlerCaptor.capture());
        verify(mockHsdpUser).login(eq("dfsdfsd"),eq("xyz@gmail.com"), myLoginHandlerCaptor.capture());
        LoginHandler value = myLoginHandlerCaptor.getValue();
        value.onLoginSuccess();

    }
//    @Test
//    public void shouldHsdpLoginOnSuccess() {
//        hsdpLoginService.hsdpUser = mockHsdpUser;
//        when(networkUtility.isNetworkAvailable()).thenReturn(true);
//
//        hsdpLoginService.hsdpLogin("accessToken","xyz@philips.com", mockHsdpAuthenticationListener);
//        verify(hsdpLoginService).login(eq("accessToken"),eq("xyz@philips.com"), loginHandlerArgumentCaptor.capture());
//        loginHandlerArgumentCaptor.getValue().onLoginSuccess();
//
////        socialLoginProviderHandler.onLoginSuccess();
//        verify(hsdpAuthenticationListener).onHSDPLoginSuccess();
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void shouldLoginFailedWithError() {
//        when(networkUtility.isNetworkAvailable()).thenReturn(true);
//        HSDPLoginService.hsdpLogin("dfsdfs", "fsfds@gmail.com", hsdpAuthenticationListener);
//        socialLoginProviderHandler.onLoginSuccess();
//        verify(hsdpAuthenticationListener).onHSDPLoginFailure(1001,"Already HSDP logged In" );
//    }

}