/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appframework.logout;

import android.content.Context;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.BaseAppUtil;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pif.DataInterface.USR.listeners.LogoutSessionListener;


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
    public void performLogout(final Context activityContext) {
        RALog.d(TAG, "performLogout: perform Logout method started");
        if (!BaseAppUtil.isNetworkAvailable(activityContext.getApplicationContext())) {
            RALog.d(TAG, "performLogout: isNetworkAvailable : Network Error");
            if (urLogoutListener != null) {
                urLogoutListener.onNetworkError(activityContext.getString(R.string.RA_DLS_check_internet_connectivity));
                RALog.d(TAG, "performLogout: Network Error");
            }
            return;
        }
        doLogout(activityContext);
    }


    private void doLogout(final Context activityContext) {
        RALog.d(TAG, "doLogout: The method started");
        ((AppFrameworkApplication)activityContext.getApplicationContext()).getUserRegistrationState().getUserDataInterface().logoutSession(new LogoutSessionListener() {
            @Override
            public void logoutSessionSuccess() {
                if (urLogoutListener != null) {
                    RALog.d(TAG, "doLogout: URLogoutListener onLogoutSuccess started");
                    urLogoutListener.onLogoutResultSuccess();
                }
                if (activityContext != null && activityContext.getApplicationContext() != null && ((AppFrameworkApplication) activityContext.getApplicationContext()).getAppInfra() != null) {
                    ((AppFrameworkApplication) activityContext.getApplicationContext()).getAppInfra().getCloudLogging().setHSDPUserUUID(null);
                }
                RALog.d(TAG, "doLogout: onLogoutSuccess");
            }

            @Override
            public void logoutSessionFailed(Error error) {
                if (urLogoutListener != null) {
                    urLogoutListener.onLogoutResultFailure(error.getErrCode(),error.getErrDesc());
                }
                RALog.d(TAG, "doLogout: onLogoutFailure");
            }
        });
    }
}
