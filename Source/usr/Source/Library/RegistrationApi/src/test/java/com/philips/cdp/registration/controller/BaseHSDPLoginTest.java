package com.philips.cdp.registration.controller;

import android.content.Context;

import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.handlers.SocialLoginProviderHandler;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.listener.HSDPAuthenticationListener;
import com.philips.cdp.registration.ui.utils.NetworkUtility;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunner.class)
public class BaseHSDPLoginTest {

    private BaseHSDPLogin baseHSDPLogin;
    @Mock
    Context mContext;
    @Mock
    User mUser;
    @Mock
    HSDPAuthenticationListener hsdpAuthenticationListener;
    @Mock
    SocialLoginProviderHandler socialLoginProviderHandler;
    @Mock
    HsdpUser hsdpUser;
    @Mock
    RegistrationComponent componentMock;
    @Mock
    NetworkUtility networkUtility;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(componentMock);
        baseHSDPLogin = new BaseHSDPLogin(mContext);
    }

    @Test
    public void shouldGetUserEmailOrMobileWithEmail() {
        when(mUser.getEmail()).thenReturn("xyz@philips.com");
        baseHSDPLogin.getUserEmailOrMobile(mUser);
    }

    @Test
    public void shouldGetUserEmailOrMobileWithMobile() {
        when(mUser.getEmail()).thenReturn("234242342424");
        baseHSDPLogin.getUserEmailOrMobile(mUser);
        Mockito.verify(mUser).getMobile();
    }

    @Test(expected = NullPointerException.class)
    public void shouldHsdpLoginOnSuccess() {
        when(networkUtility.isNetworkAvailable()).thenReturn(true);
        baseHSDPLogin.hsdpLogin("dfsdfs", "fsfds@gmail.com", hsdpAuthenticationListener);
        socialLoginProviderHandler.onLoginSuccess();
        verify(hsdpAuthenticationListener).onHSDPLoginSuccess();
    }

    @Test(expected = NullPointerException.class)
    public void shouldLoginFailedWithError() {
        when(networkUtility.isNetworkAvailable()).thenReturn(true);
        baseHSDPLogin.hsdpLogin("dfsdfs", "fsfds@gmail.com", hsdpAuthenticationListener);
        socialLoginProviderHandler.onLoginSuccess();
        verify(hsdpAuthenticationListener).onHSDPLoginFailure(1001,"Already HSDP logged In" );
    }

}