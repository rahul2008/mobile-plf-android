/*
 * Copyright (c) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.pins.shinelib.wrappers;

import android.os.Handler;
import android.support.annotation.NonNull;

import com.philips.pins.shinelib.ResultListener;
import com.philips.pins.shinelib.SHNResult;
import com.philips.pins.shinelib.SHNResultListener;
import com.philips.pins.shinelib.capabilities.SHNCapabilityNotifications;

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
    public void getMaxImageSizeForType(final Type type, final ResultListener<ImageSize> resultListener) {
        internalHandler.post(new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityNotifications.getMaxImageSizeForType(type, new WrappedResultListener<>(userHandler, resultListener));
            }
        });
    }


    @Override
    public void getNotificationCapabilities(final Type type, final ResultListener<TransferGetCapabilitiesWithResult> shnResultListener) {

        Runnable command = new Runnable() {
            @Override
            public void run() {
                wrappedShnCapabilityNotifications.getNotificationCapabilities(type, new ResultListener<TransferGetCapabilitiesWithResult>() {
                    @Override
                    public void onActionCompleted(TransferGetCapabilitiesWithResult value, @NonNull SHNResult result) {
                        shnResultListener.onActionCompleted(value, result);
                    }
                });

            }
        };
        internalHandler.post(command);
    }

    @Override
    public void setAlarm(final TransferGetCapabilitiesWithResult transferGetCapabilitiesWithResult, final int hours_u, final int minutes_u,
                         final boolean repeatNotification, final short lifeTimeSeconds_u,
                         final ResultListener<GetNotificationResult> shnResultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {


                wrappedShnCapabilityNotifications.setAlarm(transferGetCapabilitiesWithResult, hours_u, minutes_u, repeatNotification,
                        lifeTimeSeconds_u, new ResultListener<GetNotificationResult>() {
                            @Override
                            public void onActionCompleted(GetNotificationResult value, @NonNull SHNResult result) {
                                shnResultListener.onActionCompleted(value, result);
                            }


                        });

            }
        };
        internalHandler.post(command);
    }

    @Override
    public void removeAllNotification(final ResultListener<GetRemoveNotification> shnResultListener) {
        Runnable command = new Runnable() {
            @Override
            public void run() {


                wrappedShnCapabilityNotifications.removeAllNotification(new ResultListener<GetRemoveNotification>() {
                    @Override
                    public void onActionCompleted(GetRemoveNotification value, @NonNull SHNResult result) {
                        shnResultListener.onActionCompleted(value, result.SHNOk);
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