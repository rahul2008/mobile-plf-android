/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.logout;

import android.app.Activity;
import android.content.Context;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.handlers.LogoutHandler;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.dscdemo.utility.SyncScheduler;
import com.philips.platform.referenceapp.PushNotificationManager;


public class URLogout implements URLogoutInterface{

    private static final String TAG = URLogout.class.getSimpleName();

    private URLogoutListener urLogoutListener;

    public void setUrLogoutListener(URLogoutListener urLogoutListener) {
        this.urLogoutListener = urLogoutListener;
    }

    public void removeListener() {
        this.urLogoutListener = null;
    }

    @Override
    public void performLogout(final Context activityContext, final User user, final boolean isDSPollingEnabled, boolean isAutoLogoutEnabled) {

        if (!isDSPollingEnabled && isAutoLogoutEnabled) {
            getPushNotificationInstance().deregisterTokenWithBackend(activityContext.getApplicationContext(), new PushNotificationManager.DeregisterTokenListener() {
                @Override
                public void onSuccess() {
                    RALog.d(TAG, " Logout Success is returned ");
                    doLogout(activityContext, user, isDSPollingEnabled);
                }

                @Override
                public void onError() {
                    RALog.d(TAG, " Logout Error is returned ");

                    doLogout(activityContext, user, isDSPollingEnabled);
                }
            });
        } else {
            doLogout(activityContext, user, isDSPollingEnabled);
        }
    }

    protected PushNotificationManager getPushNotificationInstance() {
        return PushNotificationManager.getInstance();
    }

    private void doLogout(final Context activityContext, User user, final boolean isDSPollingEnabled) {
        user.logout(new LogoutHandler() {
            @Override
            public void onLogoutSuccess() {
                if (urLogoutListener != null) {
                    urLogoutListener.onLogoutResultSuccess();
                }

                if (!isDSPollingEnabled) {
                    getPushNotificationInstance().saveTokenRegistrationState(activityContext.getApplicationContext(), false);
                    ((AppFrameworkApplication) activityContext.getApplicationContext()).getDataServiceState().deregisterDSForRegisteringToken();
                    ((AppFrameworkApplication) activityContext.getApplicationContext()).getDataServiceState().deregisterForReceivingPayload();
                    ((Activity) activityContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SyncScheduler.getInstance().stopSync();
                        }
                    });

                }
            }

            @Override
            public void onLogoutFailure(int i, String errorMessage) {
                if (urLogoutListener != null) {
                    urLogoutListener.onLogoutResultFailure(i, errorMessage);
                }
            }
        });
    }
}
