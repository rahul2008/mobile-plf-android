/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.registration.ui.utils;

import android.app.Activity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.login.LoginResult;
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
import org.robolectric.RobolectricTestRunner;

/**
 * Created by philips on 5/24/18.
 */
@RunWith(RobolectricTestRunner.class)
public class SMSBroadCastReceiverTest {

    @Mock
    User userMock;

    @Mock
    RegistrationHelper registrationHelperMock;

    @Mock
    EventHelper eventHelperMock;

    @Mock
    FaceBookContractor faceBookContractorMock;

    @Mock
    Activity contextMock;

    @Mock
    UserRegistrationFailureInfo userRegistrationFailureInfoMock;

    @Mock
    private RegistrationComponent registrationComponentMock;

    @Mock
    private LoggingInterface mockLoggingInterface;

    SMSBroadCastReceiver smsBroadCastReceiver;

    @Mock
    LoginResult loginResultMock;


    AccessToken accessTokenMock;

    //@Mock
    CallbackManager callbackManagerMock;

    @Mock
    SMSBroadCastReceiver.ReceiveAndRegisterOTPListener receiveAndRegisterOTPListenerMock;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RegistrationConfiguration.getInstance().setComponent(registrationComponentMock);
        RLog.setMockLogger(mockLoggingInterface);

        smsBroadCastReceiver = new SMSBroadCastReceiver(receiveAndRegisterOTPListenerMock);
    }

    @Test
    public void shouldExtractOTPFromMessageAndSendTheCallBack() throws Exception {
        smsBroadCastReceiver.getOTPFromMessage("demo 123456 is your otp");
        Mockito.verify(receiveAndRegisterOTPListenerMock).onOTPReceived("123456");
    }

    @Mock
    SMSBroadCastReceiver broadcastReceiverMock;

    @Test
    public void shouldRegisterReceiver() throws Exception {
        Mockito.when(receiveAndRegisterOTPListenerMock.getActivityContext()).thenReturn(contextMock);
        Mockito.when(receiveAndRegisterOTPListenerMock.getSMSBroadCastReceiver()).thenReturn(broadcastReceiverMock);
        smsBroadCastReceiver.registerReceiver();
        Mockito.verify(contextMock).registerReceiver(broadcastReceiverMock,SMSBroadCastReceiver.intentFilter);
    }

    @Test
    public void shouldUnRegisterReceiver() throws Exception {
        Mockito.when(receiveAndRegisterOTPListenerMock.getSMSBroadCastReceiver()).thenReturn(broadcastReceiverMock);
        Mockito.when(receiveAndRegisterOTPListenerMock.getActivityContext()).thenReturn(contextMock);
        smsBroadCastReceiver.unRegisterReceiver();
        Mockito.verify(contextMock).unregisterReceiver(broadcastReceiverMock);
    }
}