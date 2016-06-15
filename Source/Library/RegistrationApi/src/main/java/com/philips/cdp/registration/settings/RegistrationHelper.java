
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.settings;

import android.content.Context;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.configuration.RegistrationStaticConfiguration;
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

    private static volatile RegistrationHelper mRegistrationHelper = null;

    private Locale mLocale;

    private RegistrationHelper() {
    }


    public synchronized static RegistrationHelper getInstance() {
        if (mRegistrationHelper == null) {
            synchronized (RegistrationHelper.class) {
                if (mRegistrationHelper == null) {
                    mRegistrationHelper = new RegistrationHelper();
                }
            }

        }
        return mRegistrationHelper;
    }

    /*
     * Initialize Janrain
     * @param isInitialized true for initialize and false for reinitialize
     * Janrain
     */
    public synchronized void initializeUserRegistration(final Context context) {

        PILLocaleManager localeManager = new PILLocaleManager(context);

        if (localeManager.getLanguageCode() != null && localeManager.getCountryCode() != null) {
            mLocale = new Locale(localeManager.getLanguageCode(), localeManager.getCountryCode());
        }
        if (mLocale == null) {
            throw new RuntimeException("Please set the locale in LocaleMatch");
        }

        mContext = context.getApplicationContext();
        countryCode = mLocale.getCountry();

        if (Tagging.isTagginEnabled() && null == Tagging.getTrackingIdentifer()) {
            throw new RuntimeException("Please set appid for tagging before you invoke registration");
        }
        UserRegistrationInitializer.getInstance().resetInitializationState();
        UserRegistrationInitializer.getInstance().setJanrainIntialized(false);
        generateKeyAndMigrateData();

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                if (!isJsonRead) {
                    isJsonRead = RegistrationStaticConfiguration.getInstance().parseConfigurationJson(mContext, RegConstants.CONFIGURATION_JSON_PATH);
                    EventHelper.getInstance().notifyEventOccurred(RegConstants.PARSING_COMPLETED);
                }

                if (NetworkUtility.isNetworkAvailable(mContext)) {
                    refreshNTPOffset();
                    UserRegistrationInitializer.getInstance().initializeEnvironment(mContext, mLocale);
                } else {
                    if (UserRegistrationInitializer.getInstance().getJumpFlowDownloadStatusListener() != null) {
                        UserRegistrationInitializer.getInstance().getJumpFlowDownloadStatusListener().onFlowDownloadFailure();
                    }
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
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
        //new DataMigration(mContext).checkFileEncryptionStatus();
    }

    private void refreshNTPOffset() {
        ServerTime.init(mContext);
        ServerTime.getInstance().refreshOffset();
    }


    public synchronized String getCountryCode() {
        return countryCode;
    }


    public synchronized void registerUserRegistrationListener(UserRegistrationListener userRegistrationListener) {
        UserRegistrationHelper.getInstance().registerEventNotification(userRegistrationListener);
    }

    public synchronized void unRegisterUserRegistrationListener(UserRegistrationListener userRegistrationListener) {
        UserRegistrationHelper.getInstance().unregisterEventNotification(userRegistrationListener);
    }

    public synchronized UserRegistrationHelper getUserRegistrationListener() {
        return UserRegistrationHelper.getInstance();
    }


    public synchronized void registerNetworkStateListener(NetworStateListener networStateListener) {
        NetworkStateHelper.getInstance().registerEventNotification(networStateListener);
    }

    public synchronized void unRegisterNetworkListener(NetworStateListener networStateListener) {
        NetworkStateHelper.getInstance().unregisterEventNotification(networStateListener);
    }

    public synchronized NetworkStateHelper getNetworkStateListener() {
        return NetworkStateHelper.getInstance();
    }


    public synchronized Locale getLocale(Context context) {
        RLog.i("Locale", "Locale locale  " + mLocale);
        if (null != mLocale) {
            return mLocale;
        }

        String locale = (new PILLocaleManager(mContext)).getInputLocale();
        RLog.i("Locale", "Locale from LOcale match" + locale);

        if (locale == null) {
            return Locale.getDefault();
        }

        return new Locale(locale);
    }

    public synchronized static String getRegistrationApiVersion() {
        return BuildConfig.VERSION_NAME;
    }


}
