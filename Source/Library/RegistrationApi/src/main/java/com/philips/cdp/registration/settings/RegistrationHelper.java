/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.settings;

import android.content.Context;

import com.philips.cdp.localematch.BuildConfig;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.datamigration.DataMigration;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.events.NetworkStateHelper;
import com.philips.cdp.registration.events.UserRegistrationHelper;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.security.SecureStorage;
import com.philips.ntputils.ServerTime;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.Locale;

/**
 * {@code RegistrationHelper} class represents the entry point for User Registration component.
 * It exposes APIs to be used when User Registration is intended to be integrated by any application.
 */
public class RegistrationHelper {


    private Context mContext;

    private boolean isJsonRead;

    private String countryCode;

    private static volatile RegistrationHelper mRegistrationHelper = null;

    private Locale mLocale;

    public UappSettings getUrSettings() {
        return urSettings;
    }

    public void setUrSettings(UappSettings urSettings) {
        this.urSettings = urSettings;
    }

    private UappSettings urSettings;

    private RegistrationHelper() {
    }

    /**
     * @return instance of this class
     */
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

    private AppInfraInterface appInfra ;

    public void setAppInfraInstance(AppInfraInterface appInfra) {
        this.appInfra =appInfra;
    }

    public AppInfraInterface getAppInfraInstance() {
        return appInfra;
    }

    private AppTaggingInterface mAppTaggingInterface;

    public AppTaggingInterface getAppTaggingInterface() {
        if (mAppTaggingInterface == null) {
            mAppTaggingInterface = getAppInfraInstance().getTagging().
                    createInstanceForComponent("Philips Registration", getRegistrationApiVersion());
            mAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);
        }
        return mAppTaggingInterface;
    }


    /*
     * Initialize Janrain
     * {code @initializeUserRegistration} method represents endpoint for integrating
     * applications. It must be called
      * to initialize User Registration component and use its features.
     *
     */
    public synchronized void initializeUserRegistration(final Context context) {
        RLog.init();
        PILLocaleManager localeManager = new PILLocaleManager(context);
        if (localeManager.getLanguageCode() != null && localeManager.getCountryCode() != null) {
            mLocale = new Locale(localeManager.getLanguageCode(), localeManager.getCountryCode());
        }
        if (mLocale == null) {
            throw new RuntimeException("Please set the locale in LocaleMatch");
        }

        mContext = context.getApplicationContext();
        countryCode = mLocale.getCountry();

        UserRegistrationInitializer.getInstance().resetInitializationState();
        UserRegistrationInitializer.getInstance().setJanrainIntialized(false);
        generateKeyAndMigrateData();

        final Runnable runnable = new Runnable() {

            @Override
            public void run() {

                if (NetworkUtility.isNetworkAvailable(mContext)) {
                    refreshNTPOffset();
                    UserRegistrationInitializer.getInstance().initializeEnvironment(
                            mContext, mLocale);
                } else {
                    if (UserRegistrationInitializer.getInstance().
                            getJumpFlowDownloadStatusListener() != null) {
                        UserRegistrationInitializer.getInstance().
                                getJumpFlowDownloadStatusListener().onFlowDownloadFailure();
                    }
                }
            }
        };
        Thread thread = new Thread(new Runnable() {
            public void run() {
                android.os.Process.setThreadPriority(android.os.Process.
                        THREAD_PRIORITY_MORE_FAVORABLE);
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
        ServerTime.init(getAppInfraInstance().getTime());
        ServerTime.refreshOffset();
    }


    public synchronized String getCountryCode() {
        return countryCode;
    }

    /**
     * {@code registerUserRegistrationListener} method registers a listener in order to listen
     * the callbacks returned by User Registration component. It must be called by
     * integrating applications
     * to be able to listen to User Registration events.
     *
     * @param userRegistrationListener
     */
    public synchronized void registerUserRegistrationListener(
            UserRegistrationListener userRegistrationListener) {
        UserRegistrationHelper.getInstance().registerEventNotification(userRegistrationListener);
    }

    /**
     * {@code unRegisterUserRegistrationListener} method unregisters the listener registered via
     * {@code registerUserRegistrationListener} method. This will make integrating applications
     * to stop listening to User Registration events.
     *
     * @param userRegistrationListener
     */
    public synchronized void unRegisterUserRegistrationListener(
            UserRegistrationListener userRegistrationListener) {
        UserRegistrationHelper.getInstance().unregisterEventNotification(userRegistrationListener);
    }

    public synchronized UserRegistrationHelper getUserRegistrationListener() {
        return UserRegistrationHelper.getInstance();
    }


    private UserRegistrationListener userRegistrationListener;

    public synchronized UserRegistrationListener getUserRegistrationEventListener() {
        return userRegistrationListener;
    }

    public synchronized UserRegistrationListener setUserRegistrationEventListener
            (UserRegistrationListener userRegistrationListener) {
        return this.userRegistrationListener = userRegistrationListener;
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
