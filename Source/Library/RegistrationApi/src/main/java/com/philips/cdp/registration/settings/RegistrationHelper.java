
package com.philips.cdp.registration.settings;

import android.content.Context;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.configuration.RegistrationStaticConfiguration;
import com.philips.cdp.registration.datamigration.DataMigration;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.events.NetworkStateHelper;
import com.philips.cdp.registration.events.UserRegistrationHelper;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.security.SecureStorage;
import com.philips.cdp.servertime.ServerTime;
import com.philips.cdp.tagging.Tagging;

import java.util.Locale;

public class RegistrationHelper {

    private Context mContext;

    private boolean isJsonRead;

    private String countryCode;

    private static RegistrationHelper mRegistrationHelper = null;

    private Locale mLocale;

    private RegistrationHelper() {
    }


    public synchronized static RegistrationHelper getInstance() {
        if (mRegistrationHelper == null) {
            mRegistrationHelper = new RegistrationHelper();

        }
        return mRegistrationHelper;
    }

    /*
     * Initialize Janrain
     * @param isInitialized true for initialize and false for reinitialize
     * Janrain
     */
    public void initializeUserRegistration(final Context context,
                                           final Locale locale) {
        RLog.i("LOCALE", "App JAnrain Init locale :" + locale.toString());
        mLocale = locale;
        mContext = context.getApplicationContext();
        countryCode = locale.getCountry();

        if (Tagging.isTagginEnabled() && null == Tagging.getTrackingIdentifer()) {
            throw new RuntimeException("Please set appid for tagging before you invoke registration");
        }
        UserRegistrationInitializer.getInstance().resetInitializationState();
        UserRegistrationInitializer.getInstance().setJanrainIntialized(false);
        generateKeyAndMigrateData();

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                refreshNTPOffset();
                if (!isJsonRead) {
                    isJsonRead = RegistrationStaticConfiguration.getInstance().parseConfigurationJson(mContext, RegConstants.CONFIGURATION_JSON_PATH);
                    EventHelper.getInstance().notifyEventOccurred(RegConstants.PARSING_COMPLETED);
                }

                if (NetworkUtility.isNetworkAvailable(mContext)) {
                    UserRegistrationInitializer.getInstance().initializeEnvironment(mContext, locale);
                } else {
                    if (UserRegistrationInitializer.getInstance().getJumpFlowDownloadStatusListener() != null) {
                        UserRegistrationInitializer.getInstance().getJumpFlowDownloadStatusListener().onFlowDownloadFailure();
                    }
                }
            }
        };
        Thread thread = new Thread( new Runnable() {
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_MORE_FAVORABLE);
                runnable.run();
            }
        });
        thread.start();



    }


    private void generateKeyAndMigrateData() {
        SecureStorage.init(mContext);
        SecureStorage.generateSecretKey();
        new DataMigration(mContext).checkFileEncryptionStatus();
    }

    private void refreshNTPOffset() {
        ServerTime.init(mContext);
        ServerTime.getInstance().refreshOffset();
    }


    public String getCountryCode() {
        return countryCode;
    }


    public void registerUserRegistrationListener(UserRegistrationListener userRegistrationListener) {
        UserRegistrationHelper.getInstance().registerEventNotification(userRegistrationListener);
    }

    public void unRegisterUserRegistrationListener(UserRegistrationListener userRegistrationListener) {
        UserRegistrationHelper.getInstance().unregisterEventNotification(userRegistrationListener);
    }

    public UserRegistrationHelper getUserRegistrationListener() {
        return UserRegistrationHelper.getInstance();
    }


    public void registerNetworkStateListener(NetworStateListener networStateListener) {
        NetworkStateHelper.getInstance().registerEventNotification(networStateListener);
    }

    public void unRegisterNetworkListener(NetworStateListener networStateListener) {
        NetworkStateHelper.getInstance().unregisterEventNotification(networStateListener);
    }

    public NetworkStateHelper getNetworkStateListener() {
        return NetworkStateHelper.getInstance();
    }


    public Locale getLocale() {
        return mLocale;
    }

    public static String getRegistrationApiVersion() {
        return BuildConfig.VERSION_NAME;
    }


}
