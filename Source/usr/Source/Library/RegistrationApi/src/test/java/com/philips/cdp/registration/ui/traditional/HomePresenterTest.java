/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration.ui.traditional;

import android.content.Context;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginResult;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URFaceBookUtility;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

/**
 * Created by philips on 5/14/18.
 */
@RunWith(RobolectricTestRunner.class)
public class HomePresenterTest {

    @Mock
    private HomeContract homeContractMock;

    @Mock
    private RegistrationComponent registrationComponentMock;

    @Mock
    private LoggingInterface mockLoggingInterface;

    @Mock
    private LoginResult loginResultMock;

    @Mock
    private URFaceBookUtility urFaceBookUtilityMock;

    private AccessToken accessToken;

    private HomePresenter homePresenter;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(registrationComponentMock);
        RLog.setMockLogger(mockLoggingInterface);
        CallbackManager callbackManagerMock = CallbackManager.Factory.create();
        accessToken = new AccessToken("someToken", "someApplicationId", "someUserId", null, null,null, null, null, null,null);
        Mockito.when(loginResultMock.getAccessToken()).thenReturn(accessToken);
        homePresenter = new HomePresenter(homeContractMock, callbackManagerMock);

    }

    @Test
    public void shouldRegisterFacebookCallBack() throws Exception {
        Mockito.when(homeContractMock.getURFaceBookUtility()).thenReturn(urFaceBookUtilityMock);
        homePresenter.registerFaceBookCallBack();
        Mockito.verify(homeContractMock.getURFaceBookUtility()).registerFaceBookCallBack();
    }
}