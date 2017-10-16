/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.logout;

import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.stateimpl.DemoDataServicesState;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.referenceapp.PushNotificationManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class URLogoutTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private User user;

    @Mock
    private URLogoutListener urLogoutListener;

    private URLogoutInterface urLogoutInterface;

    @Mock
    private AppConfigurationInterface appConfigurationInterface;

    @Mock
    private static PushNotificationManager pushNotificationManager;

    @Captor
    private ArgumentCaptor<LogoutHandler> logoutHandlerArgumentCaptor;

    @Captor
    private ArgumentCaptor<PushNotificationManager.DeregisterTokenListener> deregisterTokenListenerArgumentCaptor;

    private LogoutHandler logoutHandler;

    private PushNotificationManager.DeregisterTokenListener deregisterTokenListener;

    private ActivityController<TestActivity> activityController;

    private TestActivity testActivity;

    private DemoDataServicesState demoDataServicesState;

    private static final int ERROR_CODE = 1;

    private static final String ERROR_MESSAGE = "logout_error";

    @Before
    public void setUp() {
        activityController = Robolectric.buildActivity(TestActivity.class);
        testActivity = activityController.create().start().get();
        TestAppFrameworkApplication testAppFrameworkApplication = ((TestAppFrameworkApplication) RuntimeEnvironment.application);
        demoDataServicesState = testAppFrameworkApplication.getDataServiceState();
        urLogoutInterface = new URLogoutMock();
        urLogoutInterface.setUrLogoutListener(urLogoutListener);
    }

    @Test
    public void logoutSuccessWithDSLoggingDisabledAutoLogoutEnabledTokenSuccesTest() {
        urLogoutInterface.performLogout(testActivity, user, false, true);
        verify(pushNotificationManager, times(1)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        deregisterTokenListener = deregisterTokenListenerArgumentCaptor.getValue();
        deregisterTokenListener.onSuccess();
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutSuccess();
        verify(pushNotificationManager, times(1)).saveTokenRegistrationState(any(Context.class), anyBoolean());
        verify(demoDataServicesState, times(1)).deregisterDSForRegisteringToken();
        verify(demoDataServicesState, times(1)).deregisterForReceivingPayload();
        verify(urLogoutListener, times(1)).onLogoutResultSuccess();
    }

    @Test
    public void logoutFailureWithDSLoggingDisabledAutoLogoutEnabledTokenSuccessTest() {
        urLogoutInterface.performLogout(testActivity, user, false, true);
        verify(pushNotificationManager, times(1)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        deregisterTokenListener = deregisterTokenListenerArgumentCaptor.getValue();
        deregisterTokenListener.onSuccess();
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutFailure(ERROR_CODE, ERROR_MESSAGE);
        verify(pushNotificationManager, times(0)).saveTokenRegistrationState(any(Context.class), anyBoolean());
        verify(demoDataServicesState, times(0)).deregisterDSForRegisteringToken();
        verify(demoDataServicesState, times(0)).deregisterForReceivingPayload();
        verify(urLogoutListener, times(1)).onLogoutResultFailure(ERROR_CODE, ERROR_MESSAGE);
    }


    @Test
    public void logoutSuccessWithDSLoggingDisabledAutoLogoutEnabledTokenFailureTest() {
        urLogoutInterface.performLogout(testActivity, user, false, true);
        verify(pushNotificationManager, times(1)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        deregisterTokenListener = deregisterTokenListenerArgumentCaptor.getValue();
        deregisterTokenListener.onError();
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutSuccess();
        verify(pushNotificationManager, times(1)).saveTokenRegistrationState(any(Context.class), anyBoolean());
        verify(demoDataServicesState, times(1)).deregisterDSForRegisteringToken();
        verify(demoDataServicesState, times(1)).deregisterForReceivingPayload();
        verify(urLogoutListener, times(1)).onLogoutResultSuccess();
    }

    @Test
    public void logoutFailureWithDSLoggingDisabledAutoLogoutEnabledTokenFailureTest() {
        urLogoutInterface.performLogout(testActivity, user, false, true);
        verify(pushNotificationManager, times(1)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        deregisterTokenListener = deregisterTokenListenerArgumentCaptor.getValue();
        deregisterTokenListener.onError();
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutFailure(ERROR_CODE, ERROR_MESSAGE);
        verify(pushNotificationManager, times(0)).saveTokenRegistrationState(any(Context.class), anyBoolean());
        verify(demoDataServicesState, times(0)).deregisterDSForRegisteringToken();
        verify(demoDataServicesState, times(0)).deregisterForReceivingPayload();
        verify(urLogoutListener, times(1)).onLogoutResultFailure(ERROR_CODE, ERROR_MESSAGE);
    }


    @Test
    public void logoutSuccessWithDSLoggingEnabledTest() {
        urLogoutInterface.performLogout(testActivity, user, true, true);
        verify(pushNotificationManager, times(0)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutSuccess();
        verify(pushNotificationManager, times(0)).saveTokenRegistrationState(any(Context.class), anyBoolean());
        verify(demoDataServicesState, times(0)).deregisterDSForRegisteringToken();
        verify(demoDataServicesState, times(0)).deregisterForReceivingPayload();
        verify(urLogoutListener, times(1)).onLogoutResultSuccess();
    }

    @Test
    public void logoutFailureWithDSLoggingEnabledTest() {
        urLogoutInterface.performLogout(testActivity, user, true, true);
        verify(pushNotificationManager, times(0)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutFailure(ERROR_CODE, ERROR_MESSAGE);
        verify(urLogoutListener, times(1)).onLogoutResultFailure(ERROR_CODE, ERROR_MESSAGE);
    }

    @Test
    public void logoutSuccessWhenLogoutListenerNullTest() {
        urLogoutInterface.removeListener();
        urLogoutInterface.performLogout(testActivity, user, true, true);
        verify(pushNotificationManager, times(0)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutSuccess();
        verify(urLogoutListener, times(0)).onLogoutResultSuccess();
    }

    @Test
    public void logoutFailureLogoutListenerNullTest() {
        urLogoutInterface.removeListener();
        urLogoutInterface.performLogout(testActivity, user, true, true);
        verify(pushNotificationManager, times(0)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutFailure(ERROR_CODE, ERROR_MESSAGE);
        verify(urLogoutListener, times(0)).onLogoutResultFailure(ERROR_CODE, ERROR_MESSAGE);
    }

    @Test
    public void testGetPushNotificationInstance() {
        assertNotNull(new URLogout().getPushNotificationInstance());
    }

    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        user = null;
        urLogoutListener = null;
        urLogoutInterface = null;
        appConfigurationInterface = null;
        pushNotificationManager = null;
        logoutHandlerArgumentCaptor = null;
        deregisterTokenListenerArgumentCaptor = null;
        logoutHandler = null;
        deregisterTokenListener = null;
        demoDataServicesState = null;
        testActivity = null;
        activityController = null;
    }

    class URLogoutMock extends URLogout {
        @Override
        protected PushNotificationManager getPushNotificationInstance() {
            return pushNotificationManager;
        }
    }
}