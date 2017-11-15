package com.philips.platform.datasync.PushNotification;

import android.support.annotation.NonNull;

import com.philips.platform.core.events.PushNotificationErrorResponse;
import com.philips.platform.core.events.PushNotificationResponse;
import com.philips.platform.core.events.RegisterDeviceToken;
import com.philips.platform.core.events.UnRegisterDeviceToken;
import com.philips.platform.core.listeners.RegisterDeviceTokenListener;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class PushNotificationMonitor extends EventMonitor {

    private PushNotificationController mPushNotificationController;
    private RegisterDeviceTokenListener mRegisterDeviceTokenListener;

    @Inject
    public PushNotificationMonitor(@NonNull PushNotificationController pushNotificationController) {
        mPushNotificationController = pushNotificationController;
        DataServicesManager.getInstance().getAppComponent().injectPushNotificationMonitor(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(RegisterDeviceToken registerDeviceToken) {
        mRegisterDeviceTokenListener = registerDeviceToken.getRegisterDeviceTokenListener();
        UCorePushNotification uCorePushNotification = new UCorePushNotification();
        uCorePushNotification.setProtocolAddress(registerDeviceToken.getDeviceToken());
        uCorePushNotification.setAppVariant(registerDeviceToken.getAppVariant());
        uCorePushNotification.setProtocolProvider(registerDeviceToken.getProtocolProvider());
        mPushNotificationController.registerPushNotification(uCorePushNotification);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(UnRegisterDeviceToken unRegisterDeviceToken) {
        mRegisterDeviceTokenListener = unRegisterDeviceToken.getRegisterDeviceTokenListener();
        mPushNotificationController.unRegisterPushNotification(unRegisterDeviceToken.getAppVariant(), unRegisterDeviceToken.getAppToken());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final PushNotificationResponse pushNotificationResponse) {
        mRegisterDeviceTokenListener.onResponse(pushNotificationResponse.isSuccess());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final PushNotificationErrorResponse pushNotificationErrorResponse) {
        mRegisterDeviceTokenListener.onError(pushNotificationErrorResponse.getDataServicesError());
    }
}
