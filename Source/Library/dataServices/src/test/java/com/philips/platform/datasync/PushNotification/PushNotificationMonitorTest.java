package com.philips.platform.datasync.PushNotification;

import com.philips.platform.core.events.PushNotificationResponse;
import com.philips.platform.core.events.RegisterDeviceToken;
import com.philips.platform.core.events.UnRegisterDeviceToken;
import com.philips.platform.core.injection.AppComponent;
import com.philips.platform.core.listeners.RegisterDeviceTokenListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.datasync.UCoreAccessProvider;
import com.philips.platform.datasync.UCoreAdapter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;

import java.util.List;

import retrofit.converter.GsonConverter;

import static org.junit.Assert.*;
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
    private AppComponent mAppComponent;
    @Mock
    private UCoreAccessProvider mUCoreAccessProvider;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        DataServicesManager.getInstance().setAppComponant(mAppComponent);
        mPushNotificationMonitor = new PushNotificationMonitor(mPushNotificationController);
    }

    @Test
    public void registerDeviceTokenEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mPushNotificationMonitor.onEventAsync(mRegisterDeviceToken);
    }

    @Test
    public void unRegisterDeviceTokenEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        mPushNotificationMonitor.onEventAsync(mUnRegisterDeviceToken);
    }

    @Test
    public void pushNotificationResponseEventTest() throws Exception {
        when(mUCoreAccessProvider.isLoggedIn()).thenReturn(true);
        when(mUCoreAccessProvider.getAccessToken()).thenReturn("676786768898");
        RegisterDeviceToken registerDeviceToken = new RegisterDeviceToken("app token", "app variant", "protocol provider", new RegisterDeviceTokenListener() {
            @Override
            public void onResponse(boolean status) {
            }
        });
        mPushNotificationMonitor.onEventAsync(registerDeviceToken);
        mPushNotificationMonitor.onEventAsync(mPushNotificationResponse);
    }
}