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
        RALog.d(TAG, "performLogout: perform Logout method started");
        if (!BaseAppUtil.isNetworkAvailable(activityContext.getApplicationContext())) {
            RALog.d(TAG, "performLogout: isNetworkAvailable : Network Error");
            if (urLogoutListener != null) {
                urLogoutListener.onNetworkError(activityContext.getString(R.string.RA_DLS_check_internet_connectivity));
                RALog.d(TAG, "performLogout: Network Error");
            }
            return;
        }
        RALog.d(TAG, "performLogout: doLogout Being called in BaseAppUtil polling enabled true");
        doLogout(activityContext, user);
        RALog.d(TAG, "performLogout: BaseAppUtil.isDSPollingEnabled: True");
    }


    private void doLogout(final Context activityContext, User user) {
        RALog.d(TAG, "doLogout: The method started");
        user.logout(new LogoutHandler() {
            @Override
            public void onLogoutSuccess() {
                if (urLogoutListener != null) {
                    RALog.d(TAG, "doLogout: URLogoutListener onLogoutSuccess started");
                    urLogoutListener.onLogoutResultSuccess();
                }
                if (activityContext != null && activityContext.getApplicationContext() != null && ((AppFrameworkApplication) activityContext.getApplicationContext()).getAppInfra() != null) {
                    ((AppFrameworkApplication) activityContext.getApplicationContext()).getAppInfra().getLogging().setHSDPUserUUID(null);
                }
                stopDataSync(activityContext);
                RALog.d(TAG, "doLogout: onLogoutSuccess");
            }

            @Override
            public void onLogoutFailure(int i, String errorMessage) {
                if (urLogoutListener != null) {
                    urLogoutListener.onLogoutResultFailure(i, errorMessage);
                }
                RALog.d(TAG, "doLogout: onLogoutFailure");
            }
        });
    }

    protected void stopDataSync(Context activityContext) {
        RALog.d(TAG, "stopDataSync: method started");
        if (BaseAppUtil.isDSPollingEnabled(activityContext.getApplicationContext())) {
            ((Activity) activityContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    RALog.d(TAG, "stopDataSync: stopSync");

                }
            });
        } else {
            RALog.d(TAG, "stopDataSync: savingTokenRegState");

            RALog.d(TAG, "stopDataSync: deregisterDSForRegisteringToken");
            RALog.d(TAG, "stopDataSync: deregisterForReceivingPayload");

        }
    }


}
