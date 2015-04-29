package com.pins.philips.shinelib.wrappers;

import android.os.Handler;

import com.pins.philips.shinelib.SHNResult;
import com.pins.philips.shinelib.SHNResultListener;
import com.pins.philips.shinelib.capabilities.SHNCapabilityNotifications;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by 310188215 on 29/04/15.
 */
public class SHNCapabilityNotificationsWrapper implements SHNCapabilityNotifications {

    private final ScheduledThreadPoolExecutor shnExecutor;
    private final Handler userHandler;
    private final SHNCapabilityNotifications wrappedShnCapabilityNotifications;

    public SHNCapabilityNotificationsWrapper(SHNCapabilityNotifications shnCapabilityNotifications, ScheduledThreadPoolExecutor shnExecutor, Handler userHandler) {
        this.shnExecutor = shnExecutor;
        this.userHandler = userHandler;
        wrappedShnCapabilityNotifications = shnCapabilityNotifications;
    }

    // implements SHNCapabilityNotifications
    @Override
    public void showNotificationForType(final SHNNotificationType shnNotificationType, final SHNResultListener shnResultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityNotifications.showNotificationForType(shnNotificationType, new SHNResultListener() {
                    @Override
                    public void onActionCompleted(final SHNResult result) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                shnResultListener.onActionCompleted(result);
                            }
                        };
                        userHandler.post(resultRunnable);
                    }
                });
            }
        };
        shnExecutor.execute(command);
    }

    @Override
    public void hideNotificationForType(final SHNNotificationType shnNotificationType, final SHNResultListener shnResultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityNotifications.hideNotificationForType(shnNotificationType, new SHNResultListener() {
                    @Override
                    public void onActionCompleted(final SHNResult result) {
                        Runnable resultRunnable = new Runnable() {
                            @Override
                            public void run() {
                                shnResultListener.onActionCompleted(result);
                            }
                        };
                        userHandler.post(resultRunnable);
                    }
                });
            }
        };
        shnExecutor.execute(command);
    }
}
