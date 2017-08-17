/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.referenceapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.referenceapp.services.PlatformInstanceIDListenerService;
import com.philips.platform.referenceapp.services.RegistrationIntentService;
import com.philips.platform.referenceapp.utils.PNLog;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.powermock.reflect.Whitebox;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ServiceController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author Ritesh.jha@philips.com
 *
 * Test cases for PushNotificationManager.java
 */

@RunWith(RobolectricTestRunner.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@Config(manifest=Config.NONE, constants = BuildConfig.class, /*application = TestAppFrameworkApplication.class,*/ sdk = 25)
@PrepareForTest({PushNotificationManager.class, PreferenceManager.class, TextUtils.class})
public class PushNotificationManagerTest {
    private static final String TAG = "PushNotificationTest";
    private static final String PUSH_NOTIFICATION_TOKEN = "Push_Notification_Token";
    private PushNotificationManager pushNotificationManager;
    private PushNotificationUserRegistationWrapperInterface pnUserRegistrationInterface = null;
    private TextUtils textUtils;

    private Context context;
    private AppInfraInterface appInfraInterface = null;
    private SharedPreferences sharedPreferences;
    private PreferenceManager preferenceManager;
    private SharedPreferences.Editor editor;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() throws Exception {
        mockStatic(PushNotificationManager.class);
        mockStatic(PreferenceManager.class);
        mockStatic(TextUtils.class);

        initMocks(this);

        context = PowerMockito.mock(Context.class);
        appInfraInterface = PowerMockito.mock(AppInfraInterface.class);
        sharedPreferences = PowerMockito.mock(SharedPreferences.class);
        preferenceManager = PowerMockito.mock(PreferenceManager.class);
        textUtils = PowerMockito.mock(TextUtils.class);
        editor = PowerMockito.mock(SharedPreferences.Editor.class);

        /*  Whitebox -> Various utilities for accessing internals of a class.
         *  invokeConstructor -> Invoke a constructor. Useful for testing classes with a private constructor.
         */
        try {
            pushNotificationManager = Whitebox.invokeConstructor(PushNotificationManager.class);
        } catch (Exception e) {
            PNLog.d(TAG, "Registering component for handling payload");
        }

        PowerMockito.when(PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(sharedPreferences);

        PNLog.d("testing", "PreferenceManager.getDefaultSharedPreferences(context) -- " + PreferenceManager.getDefaultSharedPreferences(context));
        PNLog.d("testing", "sharedPreferences.getString(PUSH_NOTIFICATION_TOKEN, PUSH_NOTIFICATION_TOKEN) -- " + sharedPreferences.getString(PUSH_NOTIFICATION_TOKEN, PUSH_NOTIFICATION_TOKEN));
    }

    @Test
    public void testGetTokenNotEmpty() throws Exception {
        PowerMockito.when(sharedPreferences.getString(anyString(), anyString())).thenReturn(PUSH_NOTIFICATION_TOKEN);
        assertEquals(PUSH_NOTIFICATION_TOKEN, pushNotificationManager.getToken(context));
    }

    @Test
    public void testGetTokenWhenEmpty() throws Exception {
        PowerMockito.when(sharedPreferences.getString(anyString(), anyString())).thenReturn("");
        assertEquals("", pushNotificationManager.getToken(context));
    }

    @Test
    public void testStartPushNotificationRegistrationWhenTokenEmpty() throws Exception {
        PowerMockito.when(sharedPreferences.getString(anyString(), anyString())).thenReturn("");
        PowerMockito.when(textUtils.isEmpty("")).thenReturn(true);
        PowerMockito.when(sharedPreferences.edit()).thenReturn(editor);
        pushNotificationManager.startPushNotificationRegistration(context);

        ServiceController<TestService> controller;
        controller = Robolectric.buildService(TestService.class);
        RegistrationIntentService service = controller.create().get();
        Intent intent = new Intent(RuntimeEnvironment.application, TestService.class);
        service.onStart(intent, 0);
        assertEquals(TestService.class.getName(), intent.getComponent().getClassName());
    }

    @Test
    public void testStartPushNotificationRegistrationPlatformInstanceIDListenerService() throws Exception {
        ServiceController<TestPlatformInstanceIDListenerService> controller;
        controller = Robolectric.buildService(TestPlatformInstanceIDListenerService.class);
        TestPlatformInstanceIDListenerService service = controller.create().get();
        Intent intent = new Intent(RuntimeEnvironment.application, TestPlatformInstanceIDListenerService.class);
        service.onStartCommand(intent, 0, 0);
        assertEquals(TestPlatformInstanceIDListenerService.class.getName(), intent.getComponent().getClassName());
    }

    @After
    public void tearDown() throws Exception {
        pushNotificationManager = null;
    }
    
    private static class TestService extends RegistrationIntentService {
        @Override
        public void onStart(Intent intent, int startId) {
            // same logic as in internal ServiceHandler.handleMessage()
            // but runs on same thread as Service
            onHandleIntent(intent);
            stopSelf(startId);
        }
    }

    private static class TestPlatformInstanceIDListenerService extends PlatformInstanceIDListenerService {
        @Override
        public int onStartCommand(Intent intent, int startId, int i) {
            onTokenRefresh();
            stopSelf(startId);
            return super.onStartCommand(intent, startId, i);
        }
    }
}