/*
 * Copyright (c) Koninklijke Philips N.V., 2015, 2016, 2017.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.AlarmConfig;
import com.philips.pins.shinelib.capabilities.SHNCapabilityNotifications;

import java.util.List;

public class SHNCapabilityNotificationsWrapper implements SHNCapabilityNotifications {

    private final Handler internalHandler;
    private final Handler userHandler;
    private final SHNCapabilityNotifications wrappedShnCapabilityNotifications;

    public SHNCapabilityNotificationsWrapper(SHNCapabilityNotifications shnCapabilityNotifications, Handler internalHandler, Handler userHandler) {
        this.internalHandler = internalHandler;
        this.userHandler = userHandler;
        wrappedShnCapabilityNotifications = shnCapabilityNotifications;
    }

    // implements SHNCapabilityNotifications
    @Override
    public void showNotificationForType(final Type type, final byte[] imageData, final SHNResultListener shnResultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityNotifications.showNotificationForType(type, imageData, new SHNResultListener() {
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
        internalHandler.post(command);
    }

    @Override
    public void hideNotificationForType(final Type type, final SHNResultListener shnResultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityNotifications.hideNotificationForType(type, new SHNResultListener() {
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
        internalHandler.post(command);
    }

    @Override
    public void getNotificationCapabilities(final ResultListener<List<TransferGetCapabilitiesWithResult>> shnResultListener) {

        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityNotifications.getNotificationCapabilities(new ResultListener<List<TransferGetCapabilitiesWithResult>>() {
                    @Override
                    public void onActionCompleted(List<TransferGetCapabilitiesWithResult> value, @NonNull SHNResult result) {
                        shnResultListener.onActionCompleted(value, result);
                    }
                });

            }
        };
        internalHandler.post(command);
    }


    @Override
    public void getNotifications(final NotificationID notificationID, final ResultListener<AlarmConfig> resultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityNotifications.getNotifications(notificationID, new ResultListener<AlarmConfig>() {
                    @Override
                    public void onActionCompleted(AlarmConfig value, @NonNull SHNResult result) {
                        resultListener.onActionCompleted(value, result);
                    }
                });
            }
        };
        internalHandler.post(command);
    }

    @Override
    public void setAlarmFinal(final AlarmConfig alarmConfig, final SHNResultListener resultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityNotifications.setAlarmFinal(alarmConfig,
                        new SHNResultListener() {
                            @Override
                            public void onActionCompleted(SHNResult result) {
                                resultListener.onActionCompleted(result);
                            }
                        });
            }
        };
        internalHandler.post(command);
    }

    @Override
    public void removeAllNotification(final SHNResultListener shnResultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityNotifications.removeAllNotification(new SHNResultListener() {
                    @Override
                    public void onActionCompleted(SHNResult result) {
                        shnResultListener.onActionCompleted(result.SHNOk);
                    }

                });
            }
        };
        internalHandler.post(command);
    }

    @Override
    public void removedAtIndex(final NotificationID notificationID, final NotificationType
            notificationType, final ResultListener<GetNotificationResult> shnResultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityNotifications.removedAtIndex(notificationID, notificationType,
                        new ResultListener<GetNotificationResult>() {
                            @Override
                            public void onActionCompleted(GetNotificationResult value,
                                                          @NonNull SHNResult result) {
                                shnResultListener.onActionCompleted(value, result.SHNOk);
                            }
                        }
                );
            }
        };
        internalHandler.post(command);

    }

}