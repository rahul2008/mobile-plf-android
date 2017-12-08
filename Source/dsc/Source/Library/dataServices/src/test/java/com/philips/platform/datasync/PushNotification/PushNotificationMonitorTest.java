package com.philips.platform.datasync.PushNotification;

import com.philips.platform.core.Eventing;
import com.philips.platform.core.events.PushNotificationErrorResponse;
import com.philips.platform.core.events.PushNotificationResponse;
import com.philips.platform.core.events.RegisterDeviceToken;
import com.philips.platform.core.events.UnRegisterDeviceToken;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.RegisterDeviceTokenListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import retrofit.converter.GsonConverter;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class PushNotificationMonitorTest {

    private PushNotificationMonitor mPushNotificationMonitor;
    @Mock
    private PushNotificationController mPushNotificationController;
    @Mock
    private GsonConverter mGsonConverter;
    @Mock
    private UCoreAdapter mUCoreAdapter;
    @Mock
    private RegisterDeviceToken mRegisterDeviceToken;
    @Mock
    private UnRegisterDeviceToken mUnRegisterDeviceToken;
    @Mock
    private PushNotificationResponse mPushNotificationResponse;
    @Mock
    private PushNotificationErrorResponse mPushNotificationErrorResponse;
    @Mock
    private AppComponent mAppComponent;
    @Mock
    private Eventing mEventing;
    @Mock
    private UCoreAccessProvider mUCoreAccessProvider;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponent(mAppComponent);
        mPushNotificationMonitor = new PushNotificationMonitor(mPushNotificationController);
    }

    @Test
    public void registerDeviceTokenEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mPushNotificationMonitor.onEventAsync(mRegisterDeviceToken);
        verify(mEventing, never()).post(isA(RegisterDeviceToken.class));
    }

    @Test
    public void unRegisterDeviceTokenEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mPushNotificationMonitor.onEventAsync(mUnRegisterDeviceToken);
        verify(mEventing, never()).post(isA(UnRegisterDeviceToken.class));
    }

    @Test
    public void pushNotificationResponseEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        RegisterDeviceToken registerDeviceToken = new RegisterDeviceToken("app token", "app variant", "protocol provider", new RegisterDeviceTokenListener() {
            @Override
            public void onResponse(boolean status) {
            }

            @Override
            public void onError(DataServicesError dataServicesError) {

            }
        });
        mPushNotificationMonitor.onEventAsync(registerDeviceToken);
        mPushNotificationMonitor.onEventAsync(mPushNotificationResponse);
        verify(mEventing, never()).post(isA(PushNotificationResponse.class));
    }

    @Test
    public void pushNotificationErrorResponseEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        RegisterDeviceToken registerDeviceToken = new RegisterDeviceToken("app token", "app variant", "protocol provider", new RegisterDeviceTokenListener() {
            @Override
            public void onResponse(boolean status) {
            }

            @Override
            public void onError(DataServicesError dataServicesError) {

            }
        });
        mPushNotificationMonitor.onEventAsync(registerDeviceToken);
        mPushNotificationMonitor.onEventAsync(mPushNotificationErrorResponse);
        verify(mEventing, never()).post(isA(PushNotificationErrorResponse.class));
    }
}