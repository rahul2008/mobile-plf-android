package com.philips.platform.pim.migration;

import android.content.Context;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pif.DataInterface.USR.enums.Error;
import com.philips.platform.pim.listeners.RefreshUSRTokenListener;
import com.philips.platform.pim.manager.PIMSettingManager;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

public class PIMMigrator implements RefreshUSRTokenListener {

    private final LoggingInterface mLoggingInterface;
    private Context context;
    private final String TAG = PIMMigrator.class.getSimpleName();
    private USRTokenManager usrTokenManager;

    public PIMMigrator(Context context) {
        this.context = context;
        PIMSettingManager pimSettingManager = PIMSettingManager.getInstance();
        mLoggingInterface = pimSettingManager.getLoggingInterface();
        usrTokenManager = new USRTokenManager(pimSettingManager.getAppInfraInterface());
    }


    public void migrateUSRToPIM() {
        if (usrTokenManager.isUSRUserAvailable()) {
            usrTokenManager.fetchRefreshedAccessToken(this);
        } else {
            mLoggingInterface.log(DEBUG, TAG, "USR user is not available so assertion not required");
        }
    }

    @Override
    public void onRefreshTokenSuccess(String accessToken) {
        PIMMigrationManager pimMigrationManager = new PIMMigrationManager(context);
        pimMigrationManager.migrateUser(accessToken);
    }

    @Override
    public void onRefreshTokenFailed(Error error) {
        mLoggingInterface.log(DEBUG, TAG, "Refresh access token failed.");
    }
}
