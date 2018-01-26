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
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.BaseAppUtil;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.dscdemo.DemoAppManager;
import com.philips.platform.dscdemo.utility.SyncScheduler;
import com.philips.platform.referenceapp.PushNotificationManager;

import java.util.List;


public class URLogout implements URLogoutInterface {

    private static final String TAG = URLogout.class.getSimpleName();

    private URLogoutListener urLogoutListener;

    public void setUrLogoutListener(URLogoutListener urLogoutListener) {
        this.urLogoutListener = urLogoutListener;
    }

    public void removeListener() {
        this.urLogoutListener = null;
    }

    @Override
    public void performLogout(final Context activityContext, final User user) {

        if (!BaseAppUtil.isNetworkAvailable(activityContext.getApplicationContext())) {
            if (urLogoutListener != null) {
                urLogoutListener.onNetworkError(activityContext.getString(R.string.RA_DLS_check_internet_connectivity));
            }
            return;
        }

        if (!BaseAppUtil.isDSPollingEnabled(activityContext.getApplicationContext()) && BaseAppUtil.isAutoLogoutEnabled(activityContext.getApplicationContext())) {
            getPushNotificationInstance().deregisterTokenWithBackend(activityContext.getApplicationContext(), new PushNotificationManager.DeregisterTokenListener() {
                @Override
                public void onSuccess() {
                    RALog.d(TAG, " Logout Success is returned ");
                    doLogout(activityContext, user);
                }

                @Override
                public void onError() {
                    RALog.d(TAG, " Logout Error is returned ");

                    doLogout(activityContext, user);
                }
            });
        } else {
            doLogout(activityContext, user);
        }
    }

    protected PushNotificationManager getPushNotificationInstance() {
        return PushNotificationManager.getInstance();
    }

    protected DataServicesManager getDataServicesManager() {
        return DataServicesManager.getInstance();
    }

    private void doLogout(final Context activityContext, User user) {
        user.logout(new LogoutHandler() {
            @Override
            public void onLogoutSuccess() {
                if (urLogoutListener != null) {
                    urLogoutListener.onLogoutResultSuccess();
                }
                clearDataInDataServiceMicroApp();
                stopDataSync(activityContext);
            }

            @Override
            public void onLogoutFailure(int i, String errorMessage) {
                if (urLogoutListener != null) {
                    urLogoutListener.onLogoutResultFailure(i, errorMessage);
                }
            }
        });
    }

    protected void stopDataSync(Context activityContext) {
        if (BaseAppUtil.isDSPollingEnabled(activityContext.getApplicationContext())) {
            ((Activity) activityContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SyncScheduler.getInstance().stopSync();
                }
            });
        } else {
            getPushNotificationInstance().saveTokenRegistrationState(activityContext.getApplicationContext(), false);
            ((AppFrameworkApplication) activityContext.getApplicationContext()).getDataServiceState().deregisterDSForRegisteringToken();
            ((AppFrameworkApplication) activityContext.getApplicationContext()).getDataServiceState().deregisterForReceivingPayload();
        }
    }

    protected void clearDataInDataServiceMicroApp() {
        getInstance().getUserRegistrationHandler().clearAccessToken();
    }

    protected DemoAppManager getInstance() {
        return DemoAppManager.getInstance();
    }

}
