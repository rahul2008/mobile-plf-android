/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.settings;

import android.content.Context;
import android.os.LocaleList;

import com.janrain.android.Jump;
import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.datamigration.DataMigration;
import com.philips.cdp.registration.events.NetworkStateHelper;
import com.philips.cdp.registration.events.NetworkStateListener;
import com.philips.cdp.registration.events.UserRegistrationHelper;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.security.SecureStorage;
import com.philips.ntputils.ServerTime;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;
import com.philips.platform.uappframework.uappinput.UappSettings;
import com.philips.platform.uid.thememanager.ThemeConfiguration;

import java.util.Locale;

import javax.inject.Inject;

/**
 * {@code RegistrationHelper} class represents the entry point for User Registration component.
 * It exposes APIs to be used when User Registration is intended to be integrated by any application.
 */
public class RegistrationHelper {

    @Inject
    NetworkUtility networkUtility;

    @Inject
    TimeInterface timeInterface;

    @Inject
    ABTestClientInterface abTestClientInterface;

    ThemeConfiguration themeConfiguration;

    int theme;

    private String countryCode;

    private static volatile RegistrationHelper mRegistrationHelper = null;

    private  RegistrationSettingsURL registrationSettingsURL = new RegistrationSettingsURL();

    private Locale mLocale;

    public UappSettings getUrSettings() {
        return urSettings;
    }

    public void setUrSettings(UappSettings urSettings) {
        this.urSettings = urSettings;
    }

    private UappSettings urSettings;

    private RegistrationHelper() {
        URInterface.getComponent().inject(this);
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

    /*
     * Initialize Janrain
     * {code @initializeUserRegistration} method represents endpoint for integrating
     * applications. It must be called
      * to initialize User Registration component and use its features.
     *
     */
    public void initializeUserRegistration(final Context context) {
        RLog.init();


        if (mLocale == null) {
            String languageCode;
            String countryCode;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                languageCode = LocaleList.getDefault().get(0).getLanguage();
                countryCode = LocaleList.getDefault().get(0).getCountry();
            }else{
                languageCode = Locale.getDefault().getLanguage();
                countryCode = Locale.getDefault().getCountry();
            }

            setLocale(languageCode,countryCode);

        }

      //  countryCode = mLocale.getCountry();

        UserRegistrationInitializer.getInstance().resetInitializationState();
        UserRegistrationInitializer.getInstance().setJanrainIntialized(false);
        generateKeyAndMigrateData(context);
        refreshNTPOffset();
        final Runnable runnable = new Runnable() {

            @Override
            public void run() {
                deleteLegacyDIProfileFile(context);
                if (networkUtility.isNetworkAvailable()) {

                    UserRegistrationInitializer.getInstance().initializeEnvironment(context, mLocale);
                    //AB Testing initialization
                    abTestClientInterface.updateCache(new ABTestClientInterface.
                            OnRefreshListener() {
                        @Override
                        public void onSuccess() {
                            RLog.d(RLog.AB_TESTING, "SUCCESS ");
                        }

                        @Override
                        public void onError(ERRORVALUES error, String message) {
                            RLog.d(RLog.AB_TESTING, "ERROR AB : " + message);
                        }
                    });
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

    private void deleteLegacyDIProfileFile(Context context) {
        context.deleteFile(RegConstants.DI_PROFILE_FILE);
        Jump.getSecureStorageInterface().removeValueForKey(RegConstants.DI_PROFILE_FILE);
    }
    private void generateKeyAndMigrateData(final Context context) {
        SecureStorage.generateSecretKey();
        new DataMigration(context).checkFileEncryptionStatus();
    }

    private void refreshNTPOffset() {
        ServerTime.init(timeInterface);
        ServerTime.refreshOffset();
    }

    public synchronized String getCountryCode() {
        return countryCode;
    }

    public synchronized void setCountryCode(String country) {
        countryCode = country;
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


    public synchronized void registerNetworkStateListener(NetworkStateListener networStateListener) {
        NetworkStateHelper.getInstance().registerEventNotification(networStateListener);
    }

    public synchronized void unRegisterNetworkListener(NetworkStateListener networStateListener) {
        NetworkStateHelper.getInstance().unregisterEventNotification(networStateListener);
    }

    public synchronized NetworkStateHelper getNetworkStateListener() {
        return NetworkStateHelper.getInstance();
    }


    public void setLocale(String languageCode,String countryCode ){
        RLog.i("Locale", "setLocale language"+ languageCode+" country"+ countryCode);
        mLocale = new Locale(languageCode, countryCode);
    }
    public synchronized Locale getLocale(Context context) {
        RLog.i("Locale", "Locale locale  " + mLocale);
        return mLocale;
    }

    public synchronized static String getRegistrationApiVersion() {
        return BuildConfig.VERSION_NAME;
    }
    public boolean isMobileFlow(){
        return registrationSettingsURL.isMobileFlow();
    }

    public ThemeConfiguration getThemeConfiguration() {
        return themeConfiguration;
    }

    public void setThemeConfiguration(ThemeConfiguration themeConfiguration) {
        this.themeConfiguration = themeConfiguration;
    }

    public int getTheme() {
        return theme;
    }

    public void setTheme(int theme) {
        this.theme = theme;
    }
}
