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
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.DemoAppManager;
import com.philips.platform.dscdemo.utility.UserRegistrationHandler;
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
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class URLogoutTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private User user;

    @Mock
    private URLogoutInterface.URLogoutListener urLogoutListener;

    private URLogoutInterface urLogoutInterface;

    @Mock
    private static PushNotificationManager pushNotificationManager;

    @Mock
    private static DataServicesManager dataServicesManager;

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

    private AppConfigurationInterface appConfigurationInterface;

    private AppInfraInterface appInfraInterface;

    @Mock
    private DemoAppManager demoAppManager;

    @Mock
    UserRegistrationHandler userRegistrationHandler;

    @Before
    public void setUp() {
        activityController = Robolectric.buildActivity(TestActivity.class);
        testActivity = activityController.create().start().get();
        TestAppFrameworkApplication testAppFrameworkApplication = ((TestAppFrameworkApplication) RuntimeEnvironment.application);

        appConfigurationInterface = testAppFrameworkApplication.getAppInfra().getConfigInterface();
        demoDataServicesState = testAppFrameworkApplication.getDataServiceState();
        appInfraInterface = testAppFrameworkApplication.getAppInfra();
        urLogoutInterface = new URLogoutMock();
        urLogoutInterface.setUrLogoutListener(urLogoutListener);
        when(demoAppManager.getUserRegistrationHandler()).thenReturn(userRegistrationHandler);
    }

    @Test
    public void logoutSuccessWithDSLoggingDisabledAutoLogoutEnabledTokenSuccesTest() {
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.polling"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(false));
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.autoLogout"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(true));
        urLogoutInterface.performLogout(testActivity, user);
        verify(pushNotificationManager, times(1)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        deregisterTokenListener = deregisterTokenListenerArgumentCaptor.getValue();
        deregisterTokenListener.onSuccess();
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutSuccess();
        verify(pushNotificationManager).saveTokenRegistrationState(any(Context.class), anyBoolean());
        verify(demoDataServicesState).deregisterDSForRegisteringToken();
        verify(demoDataServicesState).deregisterForReceivingPayload();
        verify(urLogoutListener).onLogoutResultSuccess();
    }

    @Test
    public void logoutFailureWithDSLoggingDisabledAutoLogoutEnabledTokenSuccessTest() {
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.polling"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(false));
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.autoLogout"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(true));
        urLogoutInterface.performLogout(testActivity, user);
        verify(pushNotificationManager, times(1)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        deregisterTokenListener = deregisterTokenListenerArgumentCaptor.getValue();
        deregisterTokenListener.onSuccess();
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutFailure(ERROR_CODE, ERROR_MESSAGE);
        verify(pushNotificationManager, times(0)).saveTokenRegistrationState(any(Context.class), anyBoolean());
        verify(demoDataServicesState, times(0)).deregisterDSForRegisteringToken();
        verify(demoDataServicesState, times(0)).deregisterForReceivingPayload();
        verify(urLogoutListener).onLogoutResultFailure(ERROR_CODE, ERROR_MESSAGE);
    }


    @Test
    public void logoutSuccessWithDSLoggingDisabledAutoLogoutEnabledTokenFailureTest() {
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.polling"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(false));
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.autoLogout"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(true));
        urLogoutInterface.performLogout(testActivity, user);
        verify(pushNotificationManager).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        deregisterTokenListener = deregisterTokenListenerArgumentCaptor.getValue();
        deregisterTokenListener.onError();
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutSuccess();
        verify(pushNotificationManager).saveTokenRegistrationState(any(Context.class), anyBoolean());
        verify(demoDataServicesState).deregisterDSForRegisteringToken();
        verify(demoDataServicesState).deregisterForReceivingPayload();
        verify(urLogoutListener).onLogoutResultSuccess();
    }

    @Test
    public void logoutFailureWithDSLoggingDisabledAutoLogoutEnabledTokenFailureTest() {
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.polling"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(false));
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.autoLogout"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(true));
        urLogoutInterface.performLogout(testActivity, user);
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
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.polling"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(true));
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.autoLogout"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(true));
        urLogoutInterface.performLogout(testActivity, user);
        verify(pushNotificationManager, times(0)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutSuccess();
        verify(pushNotificationManager, times(0)).saveTokenRegistrationState(any(Context.class), anyBoolean());
        verify(demoDataServicesState, times(0)).deregisterDSForRegisteringToken();
        verify(demoDataServicesState, times(0)).deregisterForReceivingPayload();
        verify(urLogoutListener).onLogoutResultSuccess();
    }

    @Test
    public void logoutFailureWithDSLoggingEnabledTest() {
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.polling"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(true));
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.autoLogout"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(true));
        urLogoutInterface.performLogout(testActivity, user);
        verify(pushNotificationManager, times(0)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutFailure(ERROR_CODE, ERROR_MESSAGE);
        verify(urLogoutListener).onLogoutResultFailure(ERROR_CODE, ERROR_MESSAGE);
    }

    @Test
    public void logoutSuccessWhenLogoutListenerNullTest() {
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.polling"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(true));
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.autoLogout"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(true));
        urLogoutInterface.removeListener();
        urLogoutInterface.performLogout(testActivity, user);
        verify(pushNotificationManager, times(0)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutSuccess();
        verify(urLogoutListener, times(0)).onLogoutResultSuccess();
    }

    @Test
    public void logoutFailureLogoutListenerNullTest() {
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.polling"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(true));
        when(appConfigurationInterface.getPropertyForKey(eq("PushNotification.autoLogout"), eq("ReferenceApp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(String.valueOf(true));
        urLogoutInterface.removeListener();
        urLogoutInterface.performLogout(testActivity, user);
        verify(pushNotificationManager, times(0)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        verify(user).logout(logoutHandlerArgumentCaptor.capture());
        logoutHandler = logoutHandlerArgumentCaptor.getValue();
        logoutHandler.onLogoutFailure(ERROR_CODE, ERROR_MESSAGE);
        verify(urLogoutListener, times(0)).onLogoutResultFailure(ERROR_CODE, ERROR_MESSAGE);
    }

    @Test
    public void logoutFailureNoInternetTest() {
        RestInterface restInterface = mock(RestInterface.class);
        when(appInfraInterface.getRestClient()).thenReturn(restInterface);
        when(restInterface.isInternetReachable()).thenReturn(false);
        urLogoutInterface.performLogout(testActivity, user);
        verify(pushNotificationManager, times(0)).deregisterTokenWithBackend(any(Context.class), deregisterTokenListenerArgumentCaptor.capture());
        verify(user, times(0)).logout(logoutHandlerArgumentCaptor.capture());
        verify(urLogoutListener, times(1)).onNetworkError(anyString());
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
        appConfigurationInterface = null;
        appInfraInterface = null;
        testActivity = null;
        activityController = null;
        dataServicesManager = null;
    }

    class URLogoutMock extends URLogout {
        @Override
        protected PushNotificationManager getPushNotificationInstance() {
            return pushNotificationManager;
        }

        @Override
        protected DataServicesManager getDataServicesManager() {
            return dataServicesManager;
        }

        @Override
        protected DemoAppManager getInstance() {
            return demoAppManager;
        }
    }
}