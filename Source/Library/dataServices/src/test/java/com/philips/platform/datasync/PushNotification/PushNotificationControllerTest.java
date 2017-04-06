package com.philips.platform.datasync.PushNotification;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.BackendResponse;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.ArrayList;

import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedString;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PushNotificationControllerTest {
    private String TEST_ACCESS_TOKEN = "TEST_ACCESS_TOKEN";
    private String TEST_USER_ID = "TEST_USER_ID";

    private PushNotificationController mPushNotificationController;
    private Response mResponse;

    @Mock
    private Eventing mEventing;
    @Mock
    private RetrofitError mRetrofitError;
    @Mock
    private UCoreAccessProvider mUCoreAccessProvider;
    @Mock
    private UCoreAdapter mUCoreAdapter;
    @Mock
    private PushNotificationClient mPushNotificationClient;
    @Mock
    private GsonConverter mGsonConverter;
    @Mock
    private AppComponent mAppComponent;

    @Captor
    private ArgumentCaptor<BackendResponse> mBackendResponseArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(mAppComponent);
        mPushNotificationController = new PushNotificationController(mUCoreAdapter, mGsonConverter);
        mPushNotificationController.uCoreAccessProvider = mUCoreAccessProvider;
        mPushNotificationController.eventing = mEventing;
    }

    @Test
    public void registerPushNotificationTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(PushNotificationClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mPushNotificationClient);
        UCorePushNotification uCorePushNotification = new UCorePushNotification();
        uCorePushNotification.setAppVariant("test app variant");
        uCorePushNotification.setProtocolAddress("test token");
        uCorePushNotification.setProtocolProvider("Push.Gcma");
        assertTrue(uCorePushNotification.getAppVariant() != null);
        assertTrue(uCorePushNotification.getProtocolAddress() != null);
        assertTrue(uCorePushNotification.getProtocolProvider() != null);
        mPushNotificationController.registerPushNotification(uCorePushNotification);
    }

    @Test
    public void registerPushNotificationWithNullObjectTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(PushNotificationClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mPushNotificationClient);
        mPushNotificationController.registerPushNotification(null);
    }

    @Test
    public void registerPushNotificationWithInvalidUserTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mPushNotificationController.isUserInvalid()).thenReturn(true);
        when(mUCoreAdapter.getAppFrameworkClient(PushNotificationClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mPushNotificationClient);
        mPushNotificationController.registerPushNotification(null);
    }

    @Test
    public void unRegisterPushNotificationTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(PushNotificationClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mPushNotificationClient);
        mPushNotificationController.unRegisterPushNotification("test app variant", "test token");
    }

    @Test
    public void unRegisterPushNotificationWithNullParamsTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(PushNotificationClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mPushNotificationClient);
        mPushNotificationController.unRegisterPushNotification(null, null);
    }

    @Test
    public void unRegisterPushNotificationWithInvalidUserTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mPushNotificationController.isUserInvalid()).thenReturn(true);
        when(mUCoreAdapter.getAppFrameworkClient(PushNotificationClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mPushNotificationClient);
        mPushNotificationController.unRegisterPushNotification(null, null);
    }

    @Test
    public void userInvalidRegisterTokenTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(false);
        UCorePushNotification uCorePushNotification = new UCorePushNotification();
        uCorePushNotification.setAppVariant("test app variant");
        uCorePushNotification.setProtocolAddress("test token");
        uCorePushNotification.setProtocolProvider("Push.Gcma");
        mPushNotificationController.registerPushNotification(uCorePushNotification);
    }

    @Test
    public void userInvalidUnregisterTokenTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(false);
        mPushNotificationController.unRegisterPushNotification(null, null);
    }

    @Test
    public void nullAccessProviderTest() throws Exception {
        mPushNotificationController.uCoreAccessProvider = null;
        assertThat(mPushNotificationController.isUserInvalid()).isFalse();
    }

    @Test
    public void retrofitErrorWhileRegisterDeviceTokenTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(PushNotificationClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mPushNotificationClient);
        UCorePushNotification uCorePushNotification = new UCorePushNotification();
        uCorePushNotification.setAppVariant("test app variant");
        uCorePushNotification.setProtocolAddress("test token");
        uCorePushNotification.setProtocolProvider("Push.Gcma");

        final RetrofitError retrofitError = mock(RetrofitError.class);
        mResponse = new Response("", 403, "Test error", new ArrayList<Header>(), new TypedString("ERROR"));
        when(retrofitError.getResponse()).thenReturn(mResponse);
        when(mPushNotificationClient.registerDeviceToken(TEST_USER_ID, TEST_USER_ID, 13, uCorePushNotification)).thenThrow(retrofitError);
        mPushNotificationController.registerPushNotification(uCorePushNotification);
    }

    @Test
    public void retrofitErrorWhileUnRegisterDeviceTokenTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn(TEST_ACCESS_TOKEN);
        when(mUCoreAccessProvider.getUserId()).thenReturn(TEST_USER_ID);
        when(mUCoreAdapter.getAppFrameworkClient(PushNotificationClient.class, TEST_ACCESS_TOKEN, mGsonConverter)).thenReturn(mPushNotificationClient);

        final RetrofitError retrofitError = mock(RetrofitError.class);
        mResponse = new Response("", 403, "Test error", new ArrayList<Header>(), new TypedString("ERROR"));
        when(retrofitError.getResponse()).thenReturn(mResponse);
        when(mPushNotificationClient.unRegisterDeviceToken(TEST_USER_ID, TEST_USER_ID, 13, "app variant", "token")).thenThrow(retrofitError);
        mPushNotificationController.unRegisterPushNotification("app variant", "token");
    }

}