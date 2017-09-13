/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.referenceapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.referenceapp.interfaces.HandleNotificationPayloadInterface;
import com.philips.platform.referenceapp.interfaces.PushNotificationTokenRegistrationInterface;
import com.philips.platform.referenceapp.interfaces.RegistrationCallbacks;
import com.philips.platform.referenceapp.services.PlatformInstanceIDListenerService;
import com.philips.platform.referenceapp.services.RegistrationIntentService;
import com.philips.platform.referenceapp.utils.PNLog;
import com.philips.platform.referenceapp.utils.PushNotificationConstants;

import org.json.JSONException;
import org.json.JSONObject;
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
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ServiceController;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * @author Ritesh.jha@philips.com
 *
 * Test cases for PushNotificationManager.java
 */

@RunWith(CustomRobolectricRunner.class)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest({PreferenceManager.class, TextUtils.class})
public class PushNotificationManagerTest {
    private static final String TAG = "PushNotificationTest";
    private static final String PUSH_NOTIFICATION_TOKEN = "Push_Notification_Token";
    private PushNotificationManager pushNotificationManager;
    private PushNotificationUserRegistationWrapperInterface pnUserRegistrationInterface = null;
    private TextUtils textUtils;

    private Context context;
    private AppInfraInterface appInfraInterface = null;
    private AppInfra appInfra = null;
    private SharedPreferences sharedPreferences;
    private PreferenceManager preferenceManager;
    private SharedPreferences.Editor editor;
    private PushNotificationTokenRegistrationInterface pushNotificationTokenRegistrationInterface;

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Before
    public void setUp() throws Exception {
        mockStatic(PreferenceManager.class);
        mockStatic(TextUtils.class);

        initMocks(this);

        PNLog.disablePNLogging();

        initialMocking();

        /*  Whitebox -> Various utilities for accessing internals of a class.
         *  invokeConstructor -> Invoke a constructor. Useful for testing classes with a private constructor.
         */
        try {
            pushNotificationManager = Whitebox.invokeConstructor(PushNotificationManager.class);
        } catch (Exception e) {
            PNLog.d(TAG, "Registering component for handling payload");
        }

        PowerMockito.when(PreferenceManager.getDefaultSharedPreferences(context)).thenReturn(sharedPreferences);
    }

    private void initialMocking() {
        context = PowerMockito.mock(Context.class);
        appInfraInterface = PowerMockito.mock(AppInfraInterface.class);
        appInfra = PowerMockito.mock(AppInfra.class);
        sharedPreferences = PowerMockito.mock(SharedPreferences.class);
        preferenceManager = PowerMockito.mock(PreferenceManager.class);
        textUtils = PowerMockito.mock(TextUtils.class);
        editor = PowerMockito.mock(SharedPreferences.Editor.class);
        pnUserRegistrationInterface = PowerMockito.mock(PushNotificationUserRegistationWrapperInterface.class);
        pushNotificationTokenRegistrationInterface = PowerMockito.mock(PushNotificationTokenRegistrationInterface.class);
    }

    @Test
    public void testGetTokenNotEmpty() throws Exception {
        PowerMockito.when(sharedPreferences.getString(anyString(), anyString())).thenReturn(PUSH_NOTIFICATION_TOKEN);
        assertEquals(PUSH_NOTIFICATION_TOKEN, pushNotificationManager.getToken(context));
    }

    @Test
    public void testGetPushNotificationUserRegistationWrapperInterface() {
        pushNotificationManager.init(appInfraInterface, pnUserRegistrationInterface);
        assertEquals(pnUserRegistrationInterface, pushNotificationManager.getPushNotificationUserRegistationWrapperInterface());
    }

    @Test
    public void testGetTokenWhenEmpty() throws Exception {
        PowerMockito.when(sharedPreferences.getString(anyString(), anyString())).thenReturn("");
        assertEquals("", pushNotificationManager.getToken(context));
    }

    @Test
    public void testStartPushNotificationRegistrationWhenTokenEmpty() throws Exception {
        setExpectationTrueWhenPreferenceIsEmpty();

        pushNotificationManager.startPushNotificationRegistration(context);

        ServiceController<TestService> controller;
        controller = Robolectric.buildService(TestService.class);
        RegistrationIntentService service = controller.create().get();
        Intent intent = new Intent(RuntimeEnvironment.application, TestService.class);
        service.onStart(intent, 0);
        assertEquals(TestService.class.getName(), intent.getComponent().getClassName());
    }

    private void setExpectationTrueWhenPreferenceIsEmpty() {
        PowerMockito.when(sharedPreferences.getString(anyString(), anyString())).thenReturn("");
        PowerMockito.when(textUtils.isEmpty("")).thenReturn(true);
        PowerMockito.when(sharedPreferences.edit()).thenReturn(editor);
    }

    @Test
    public void testStartPushNotificationRegistrationRegisterToken() throws Exception {
        setExpectationFalseWhenPreferenceIsEmpty();
        MockInternetReacheablity();

        PushNotificationUserRegistationWrapperInterface pushNotificationUserRegistationWrapperInterface =
                getPushNotificationUserRegistationWrapperInterface();

        pushNotificationManager.init(appInfra, pushNotificationUserRegistationWrapperInterface);
        PNLog.disablePNLogging();

        final Boolean[] isRegisterTokenApiInvoked = {false};

        PushNotificationTokenRegistrationInterface pushNotificationTokenRegistrationInterface =
                getRegistrationInterfaceRegisterToken(isRegisterTokenApiInvoked);

        final Boolean[] isResponseSuccess = {false};

        RegistrationCallbacks.RegisterCallbackListener registerCallbackListener = getRegisterCallbackListener(isResponseSuccess);

        pushNotificationManager.registerForTokenRegistration(pushNotificationTokenRegistrationInterface, registerCallbackListener);

        pushNotificationManager.startPushNotificationRegistration(context);

        assertTrue(isRegisterTokenApiInvoked[0]);
        assertTrue(isResponseSuccess[0]);
    }

    @NonNull
    private PushNotificationUserRegistationWrapperInterface getPushNotificationUserRegistationWrapperInterface() {
        return new PushNotificationUserRegistationWrapperInterface() {
            @Override
            public boolean isUserSignedIn(Context appContext) {
                return true;
            }
        };
    }

    @NonNull
    private RegistrationCallbacks.RegisterCallbackListener getRegisterCallbackListener(final Boolean[] isResponseSuccess) {
        return new RegistrationCallbacks.RegisterCallbackListener() {
            @Override
            public void onResponse(boolean isRegistered) {
                isResponseSuccess[0] = true;
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                isResponseSuccess[0] = false;
            }
        };
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

    @Test
    public void testSendPayloadToCoCo() {
        final Boolean[] isResponseSuccess = {false};

        HandleNotificationPayloadInterface handleNotificationPayloadInterface =
                handleNotificationPayloadInterfaceForSuccess(isResponseSuccess);

        pushNotificationManager.registerForTokenRegistration(pushNotificationTokenRegistrationInterface);
        pushNotificationManager.registerForPayload(handleNotificationPayloadInterface);

        String jsonData = getJsonString();
        Bundle bundle = getBundle(jsonData, PushNotificationConstants.PLATFORM_KEY);
        PNLog.disablePNLogging();
        pushNotificationManager.sendPayloadToCoCo(bundle);
        assertTrue(isResponseSuccess[0]);

        bundle = getBundle(jsonData, "non_platform");
        pushNotificationManager.sendPayloadToCoCo(bundle);
        assertFalse(isResponseSuccess[0]);
    }

    @NonNull
    private HandleNotificationPayloadInterface handleNotificationPayloadInterfaceForSuccess(final Boolean[] isResponseSuccess) {
        return new HandleNotificationPayloadInterface() {
                @Override
                public void handlePayload(JSONObject payloadObject) throws JSONException {
                    isResponseSuccess[0] = true;
                }

                @Override
                public void handlePushNotification(String message) {
                    isResponseSuccess[0] = false;
                }
            };
    }

    @NonNull
    private String getJsonString() {
        return "{ \"dsc\": { \"dataSync\": \"moment\" } }";
    }

    @NonNull
    protected Bundle getBundle(String dataWithCorrectJson, String key) {
        Bundle bundle = new Bundle();
        bundle.putCharSequence(key, dataWithCorrectJson);
        return bundle;
    }

    @Test
    public void testRegisterTokenWithBackendWhenTokenRegistrationIsTrue() throws Exception {
        setExpectationFalseWhenPreferenceIsEmpty();

        final Boolean[] isRegisterTokenApiInvoked = {false};

        PushNotificationTokenRegistrationInterface pushNotificationTokenRegistrationInterface =
                getRegistrationInterfaceRegisterToken(isRegisterTokenApiInvoked);

        final Boolean[] isResponseSuccess = {false};

        RegistrationCallbacks.RegisterCallbackListener registerCallbackListener =
                getRegisterCallbackListener(isResponseSuccess);

        pushNotificationManager.registerForTokenRegistration(pushNotificationTokenRegistrationInterface, registerCallbackListener);
        pushNotificationManager.registerTokenWithBackend(context);

        assertTrue(isRegisterTokenApiInvoked[0]);
        assertTrue(isResponseSuccess[0]);
    }

    @NonNull
    private PushNotificationTokenRegistrationInterface getRegistrationInterfaceRegisterToken(final Boolean[] isRegisterTokenApiInvoked) {
        return new PushNotificationTokenRegistrationInterface() {
            @Override
            public void registerToken(String deviceToken, String appVariant, String protocolProvider, RegistrationCallbacks.RegisterCallbackListener registerCallbackListener) {
                isRegisterTokenApiInvoked[0] = true;
                registerCallbackListener.onResponse(true);
            }

            @Override
            public void deregisterToken(String appToken, String appVariant, RegistrationCallbacks.DergisterCallbackListener dergisterCallbackListener) {
                isRegisterTokenApiInvoked[0] = false;
                dergisterCallbackListener.onResponse(false);
            }
        };
    }

    private void setExpectationFalseWhenPreferenceIsEmpty() {
        PowerMockito.when(sharedPreferences.getString(anyString(), anyString())).thenReturn("");
        PowerMockito.when(textUtils.isEmpty("")).thenReturn(false);
        PowerMockito.when(sharedPreferences.edit()).thenReturn(editor);
    }

    @Test
    public void testRegisterTokenWithBackendWhenTokenRegistrationErrorCondition() throws Exception {
        setExpectationFalseWhenPreferenceIsEmpty();

        final Boolean[] isRegisterTokenApiInvoked = {false};

        PushNotificationTokenRegistrationInterface pushNotificationTokenRegistrationInterface =
                getRegistrationInterfaceRegisterTokenErrorCondition(isRegisterTokenApiInvoked);

        final Boolean[] isResponseSuccess = {false};

        RegistrationCallbacks.RegisterCallbackListener registerCallbackListener =
                getRegisterCallbackListenerErrorCondition(isResponseSuccess);

        pushNotificationManager.registerForTokenRegistration(pushNotificationTokenRegistrationInterface, registerCallbackListener);
        pushNotificationManager.registerTokenWithBackend(context);

        assertTrue(isRegisterTokenApiInvoked[0]);
        assertTrue(isResponseSuccess[0]);
    }

    @NonNull
    private RegistrationCallbacks.RegisterCallbackListener getRegisterCallbackListenerErrorCondition(final Boolean[] isResponseSuccess) {
        return new RegistrationCallbacks.RegisterCallbackListener() {
            @Override
            public void onResponse(boolean isRegistered) {
                isResponseSuccess[0] = false;
            }

            @Override
            public void onError(int errorCode, String errorMessage) {
                isResponseSuccess[0] = true;
            }
        };
    }

    @NonNull
    private PushNotificationTokenRegistrationInterface getRegistrationInterfaceRegisterTokenErrorCondition(final Boolean[] isRegisterTokenApiInvoked) {
        return new PushNotificationTokenRegistrationInterface() {
            @Override
            public void registerToken(String deviceToken, String appVariant, String protocolProvider, RegistrationCallbacks.RegisterCallbackListener registerCallbackListener) {
                isRegisterTokenApiInvoked[0] = true;
                registerCallbackListener.onError(1, "dummy");
            }

            @Override
            public void deregisterToken(String appToken, String appVariant, RegistrationCallbacks.DergisterCallbackListener dergisterCallbackListener) {
                isRegisterTokenApiInvoked[0] = false;
                dergisterCallbackListener.onResponse(false);
            }
        };
    }

    @Test
    public void testDegisterTokenWithBackend() throws Exception {
        setExpectationFalseWhenPreferenceIsEmpty();
        MockInternetReacheablity();

        pushNotificationManager.init(appInfra, pnUserRegistrationInterface);
        PNLog.disablePNLogging();

        final Boolean[] isDeregisterTokenApiInvoked = {false};

        PushNotificationTokenRegistrationInterface pushNotificationTokenRegistrationInterface =
                getDeregistrationInterfaceRegisterToken(isDeregisterTokenApiInvoked);

        final Boolean[] isResponseSuccess = {false};

        PushNotificationManager.DeregisterTokenListener deregisterTokenListener =
                getDeregisterTokenListener(isResponseSuccess);

        pushNotificationManager.registerForTokenRegistration(pushNotificationTokenRegistrationInterface);
        pushNotificationManager.deregisterTokenWithBackend(context, deregisterTokenListener);

        assertTrue(isDeregisterTokenApiInvoked[0]);
        assertTrue(isResponseSuccess[0]);
    }

    @NonNull
    private PushNotificationManager.DeregisterTokenListener getDeregisterTokenListener(final Boolean[] isResponseSuccess) {
        return new
                    PushNotificationManager.DeregisterTokenListener() {
                        @Override
                        public void onSuccess() {
                            isResponseSuccess[0] = true;
                        }

                        @Override
                        public void onError() {
                            isResponseSuccess[0] = false;
                        }
                    };
    }

    @NonNull
    private PushNotificationTokenRegistrationInterface getDeregistrationInterfaceRegisterToken(final Boolean[] isDeregisterTokenApiInvoked) {
        return new PushNotificationTokenRegistrationInterface() {
            @Override
            public void registerToken(String deviceToken, String appVariant, String protocolProvider, RegistrationCallbacks.RegisterCallbackListener registerCallbackListener) {
                isDeregisterTokenApiInvoked[0] = false;
                registerCallbackListener.onResponse(false);
            }

            @Override
            public void deregisterToken(String appToken, String appVariant, RegistrationCallbacks.DergisterCallbackListener dergisterCallbackListener) {
                isDeregisterTokenApiInvoked[0] = true;
                dergisterCallbackListener.onResponse(true);
            }
        };
    }

    @Test
    public void testDegisterTokenWithBackendForErrorCondition() throws Exception {
        setExpectationFalseWhenPreferenceIsEmpty();
        MockInternetReacheablity();
        pushNotificationManager.init(appInfra, pnUserRegistrationInterface);
        PNLog.disablePNLogging();

        final Boolean[] isDeregisterTokenApiInvoked = {false};

        PushNotificationTokenRegistrationInterface pushNotificationTokenRegistrationInterface =
                new PushNotificationTokenRegistrationInterface() {
                    @Override
                    public void registerToken(String deviceToken, String appVariant, String protocolProvider, RegistrationCallbacks.RegisterCallbackListener registerCallbackListener) {
                        isDeregisterTokenApiInvoked[0] = false;
                        registerCallbackListener.onResponse(false);
                    }

                    @Override
                    public void deregisterToken(String appToken, String appVariant, RegistrationCallbacks.DergisterCallbackListener dergisterCallbackListener) {
                        isDeregisterTokenApiInvoked[0] = true;
                        dergisterCallbackListener.onResponse(false);
                    }
                };

        final Boolean[] isResponseSuccess = {false};

        PushNotificationManager.DeregisterTokenListener deregisterTokenListener = new
                PushNotificationManager.DeregisterTokenListener() {
                    @Override
                    public void onSuccess() {
                        isResponseSuccess[0] = false;
                    }

                    @Override
                    public void onError() {
                        isResponseSuccess[0] = true;
                    }
                };


        pushNotificationManager.registerForTokenRegistration(pushNotificationTokenRegistrationInterface);
        pushNotificationManager.deregisterTokenWithBackend(context, deregisterTokenListener);

        assertTrue(isDeregisterTokenApiInvoked[0]);
        assertTrue(isResponseSuccess[0]);
    }

    private void MockInternetReacheablity() {
        RestInterface restInterface = PowerMockito.mock(RestInterface.class);
        PowerMockito.when(appInfra.getRestClient()).thenReturn(restInterface);
        PowerMockito.when(restInterface.isInternetReachable()).thenReturn(true);
    }

    @After
    public void tearDown() throws Exception {
        PNLog.disablePNLogging();
        pushNotificationManager.deregisterForTokenRegistration();
        pushNotificationManager.deRegisterForPayload();
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
        public void onCreate() {
            super.onCreate();
            onTokenRefresh();
        }

        @Override
        public void onTokenRefresh() {
            super.onTokenRefresh();
            stopSelf();
        }
    }
}