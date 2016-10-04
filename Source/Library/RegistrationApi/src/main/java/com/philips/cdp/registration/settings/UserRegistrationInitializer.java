
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.janrain.android.Jump;
import com.philips.cdp.registration.AppIdentityInfo;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;

import java.util.Locale;

public class UserRegistrationInitializer {

    private boolean mIsJumpInitializationInProgress;

    private boolean mReceivedDownloadFlowSuccess;

    private boolean mReceivedProviderFlowSuccess;

    private boolean mJanrainIntialized = false;

    private boolean isRefreshUserSessionInProgress = false;

    private static volatile UserRegistrationInitializer mUserRegistrationInitializer;

    private UserRegistrationInitializer() {
        mHandler = new Handler();
    }

    public void resetInitializationState() {
        mIsJumpInitializationInProgress = false;
        mReceivedDownloadFlowSuccess = false;
        mReceivedProviderFlowSuccess = false;
    }

    private final int CALL_AFTER_DELAY = 500;
    private Handler mHandler;

    public boolean isJumpInitializationInProgress() {
        return mIsJumpInitializationInProgress;
    }

    public void setJumpInitializationInProgress(boolean isInitializationInProgress) {
        this.mIsJumpInitializationInProgress = isInitializationInProgress;
    }


    public JumpFlowDownloadStatusListener getJumpFlowDownloadStatusListener() {
        return mJumpFlowDownloadStatusListener;
    }


    private JumpFlowDownloadStatusListener mJumpFlowDownloadStatusListener;


    private RegistrationSettings mRegistrationSettings;


    public boolean isJanrainIntialized() {
        return mJanrainIntialized;
    }

    public void setJanrainIntialized(boolean janrainIntializationStatus) {
        mJanrainIntialized = janrainIntializationStatus;
    }

    public void registerJumpFlowDownloadListener(JumpFlowDownloadStatusListener pJumpFlowDownloadStatusListener) {
        this.mJumpFlowDownloadStatusListener = pJumpFlowDownloadStatusListener;
    }

    public void unregisterJumpFlowDownloadListener() {
        this.mJumpFlowDownloadStatusListener = null;
    }

    public final BroadcastReceiver janrainStatusReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent != null) {
                Bundle extras = intent.getExtras();
                if (extras.getString("message").equalsIgnoreCase("Download flow Success!!")) {
                    mReceivedDownloadFlowSuccess = true;
                } else if (extras.getString("message").equalsIgnoreCase("Provider flow Success!!")) {
                    mReceivedProviderFlowSuccess = true;
                }
                RLog.i(RLog.ACTIVITY_LIFECYCLE, "janrainStatusReceiver, intent = " + intent.toString());
                if ((Jump.JR_DOWNLOAD_FLOW_SUCCESS.equalsIgnoreCase(intent.getAction()) || Jump.JR_PROVIDER_FLOW_SUCCESS.equalsIgnoreCase(intent.getAction()))
                        && (null != extras)) {

                    if (mReceivedDownloadFlowSuccess && mReceivedProviderFlowSuccess) {
                        mJanrainIntialized = true;
                        mIsJumpInitializationInProgress = false;
                        mReceivedDownloadFlowSuccess = false;
                        mReceivedProviderFlowSuccess = false;
                        if (mJumpFlowDownloadStatusListener != null) {
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (mJumpFlowDownloadStatusListener != null) {
                                        mJumpFlowDownloadStatusListener.onFlowDownloadSuccess();
                                    }
                                }
                            }, CALL_AFTER_DELAY);


                        }
                        EventHelper.getInstance().notifyEventOccurred(RegConstants.JANRAIN_INIT_SUCCESS);

                    }

                } else if (Jump.JR_FAILED_TO_DOWNLOAD_FLOW.equalsIgnoreCase(intent.getAction())
                        && (extras != null)) {
                    mIsJumpInitializationInProgress = false;
                    mJanrainIntialized = false;
                    mReceivedDownloadFlowSuccess = false;
                    mReceivedProviderFlowSuccess = false;
                    if (mJumpFlowDownloadStatusListener != null) {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (mJumpFlowDownloadStatusListener != null) {
                                    mJumpFlowDownloadStatusListener.onFlowDownloadFailure();
                                }
                            }
                        }, CALL_AFTER_DELAY);

                    }
                    EventHelper.getInstance().notifyEventOccurred(RegConstants.JANRAIN_INIT_FAILURE);


                }
            }
        }
    };


    public void initializeConfiguredEnvironment(Context context, Configuration registrationType, String initLocale) {
        mRegistrationSettings = new RegistrationSettingsURL();
        mRegistrationSettings.intializeRegistrationSettings(context, RegistrationConfiguration.getInstance().getRegistrationClientId(registrationType), initLocale);
    }


    public static UserRegistrationInitializer getInstance() {
        if (mUserRegistrationInitializer == null) {
            synchronized (UserRegistrationInitializer.class) {
                if (mUserRegistrationInitializer == null) {
                    mUserRegistrationInitializer = new UserRegistrationInitializer();
                }

            }

        }
        return mUserRegistrationInitializer;
    }

    public RegistrationSettings getRegistrationSettings() {
        return mRegistrationSettings;
    }

    public void initializeEnvironment(Context context, Locale locale) {
        registerJumpInitializationListener(context);


        RLog.i(RLog.JANRAIN_INITIALIZE, "Mixrosite ID : " + RegistrationConfiguration.getInstance().getMicrositeId());

        String mRegistrationType = RegistrationConfiguration.getInstance()
                .getRegistrationEnvironment();
        RLog.i(RLog.JANRAIN_INITIALIZE, "Registration Environment : " + mRegistrationType);

        UserRegistrationInitializer.getInstance().setJanrainIntialized(false);
        UserRegistrationInitializer.getInstance().setJumpInitializationInProgress(true);

        UserRegistrationInitializer.getInstance().initializeConfiguredEnvironment(context, RegUtility.getConfiguration(mRegistrationType), locale.toString());
    }

    private void registerJumpInitializationListener(Context context) {
        IntentFilter flowFilter = new IntentFilter(Jump.JR_DOWNLOAD_FLOW_SUCCESS);

        flowFilter.addAction(Jump.JR_FAILED_TO_DOWNLOAD_FLOW);
        flowFilter.addAction("com.janrain.android.Jump.PROVIDER_FLOW_SUCCESS");
        if (UserRegistrationInitializer.getInstance().janrainStatusReceiver != null) {
            LocalBroadcastManager.getInstance(context).unregisterReceiver(UserRegistrationInitializer.getInstance().janrainStatusReceiver);
        }
        LocalBroadcastManager.getInstance(context).registerReceiver(UserRegistrationInitializer.getInstance().janrainStatusReceiver,
                flowFilter);
    }

    public boolean isJumpInitializated() {

        return !isJumpInitializationInProgress() && isJanrainIntialized();
    }

    public boolean isRegInitializationInProgress() {
        return isJumpInitializationInProgress() && !isJanrainIntialized();

    }

    public boolean isRefreshUserSessionInProgress() {
        return isRefreshUserSessionInProgress;
    }

    public void setRefreshUserSessionInProgress(boolean refreshUserSessionInProgress) {
        isRefreshUserSessionInProgress = refreshUserSessionInProgress;
    }
}
