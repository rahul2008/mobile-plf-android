/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.stateimpl;

import android.app.NotificationManager;
import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.dscdemo.DSDemoAppuAppDependencies;
import com.philips.platform.dscdemo.DSDemoAppuAppInterface;
import com.philips.platform.referenceapp.interfaces.RegistrationCallbacks;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowNotificationManager;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.robolectric.RuntimeEnvironment.application;
import static org.robolectric.Shadows.shadowOf;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class DemoDataServicesStateTest {
    private ActivityLauncher activityLauncher;

    @Mock
    DemoDataServiceStateMock demoDataServiceStateMock;

    @Mock
    DSDemoAppuAppDependencies dsDemoAppuAppDependencies;

    @Mock
    DSDemoAppuAppInterface dsDemoAppuAppInterface;

    @Mock
    AppInfra appInfra;

    @Mock
    RegistrationCallbacks.RegisterCallbackListener registerCallbackListener;

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        demoDataServiceStateMock = new DemoDataServiceStateMock();
        demoDataServiceStateMock.init(getApplicationContext());
        demoDataServiceStateMock.updateDataModel();
        activityLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT, 0);
    }

    public AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) application;
    }


    @Test
    public void testLaunchDemoDataServicesState() {
        demoDataServiceStateMock.navigate(activityLauncher);
        verify(dsDemoAppuAppInterface).launch(any(UiLauncher.class), any(UappLaunchInput.class));
    }


    @Test
    public void sendNotificationTest() {
        demoDataServiceStateMock.handlePushNotification("This is reference app");
        NotificationManager notificationService = (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
        ShadowNotificationManager shadowNotificationManager = shadowOf(notificationService);
        assertEquals(shadowNotificationManager.size(), 1);
    }

    /*@Config(sdk = 26)
    @Test
    public void sendNotificationAndroidOTest() {
        demoDataServiceStateMock.handlePushNotification("This is reference app");
        NotificationManager notificationService = (NotificationManager) application.getSystemService(Context.NOTIFICATION_SERVICE);
        ShadowNotificationManager shadowNotificationManager = shadowOf(notificationService);
        assertEquals(shadowNotificationManager.size(), 1);
    }*/

    @Test
    public void getUappDependenciesTest() {
        assertNotNull(new DemoDataServicesState().getUappDependencies(application));
    }

    @Test
    public void getDsDemoAppuAppInterface() {
        assertNotNull(new DemoDataServicesState().getDsDemoAppuAppInterface());
    }

    @After
    public void tearDown() {
        activityLauncher = null;
        demoDataServiceStateMock = null;
        dsDemoAppuAppDependencies = null;
        dsDemoAppuAppInterface = null;
        registerCallbackListener = null;
    }

    class DemoDataServiceStateMock extends DemoDataServicesState {

        @NonNull
        protected DSDemoAppuAppInterface getDsDemoAppuAppInterface() {
            return dsDemoAppuAppInterface;
        }

        @NonNull
        @Override
        protected DSDemoAppuAppDependencies getUappDependencies(Context context) {
            return dsDemoAppuAppDependencies;
        }
    }
}