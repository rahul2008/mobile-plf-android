/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform;

import android.content.Context;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppInitializationCallback;
import com.philips.platform.baseapp.screens.inapppurchase.IAPState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationOnBoardingState;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.File;
import java.util.Locale;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 24)
public class TestAppFrameworkApplication extends AppFrameworkApplication {

    private File tempFile;
    public AppInfraInterface appInfra;
    private UserRegistrationOnBoardingState userRegistrationOnBoardingState;
    private IAPState iapState;

    @Test
    public void shouldPass() {
        assertTrue(true);
    }


    @Override
    protected void attachBaseContext(Context base) {
        try {
            super.attachBaseContext(base);
        } catch (RuntimeException ignored) {
            // Multidex support doesn't play well with Robolectric yet
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initializeAppInfra(new AppInitializationCallback.AppInfraInitializationCallback() {
            @Override
            public void onAppInfraInitialization() {

            }
        });
        initialize(new AppInitializationCallback.AppStatesInitializationCallback() {
            @Override
            public void onAppStatesInitialization() {

            }
        });
        appInfra = new AppInfra.Builder().build(getApplicationContext());
        loggingInterface = appInfra.getLogging();
        setLocale();
        userRegistrationOnBoardingState = new UserRegistrationOnBoardingState();
        userRegistrationOnBoardingState.init(this);
        setTargetFlowManager();

    }

    public IAPState getIap() {
        return iapState;
    }

    public  void setIapState(IAPState state) {
        iapState = state;
    }

    private void setLocale() {
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);
    }
    public void setTargetFlowManager() {
        this.targetFlowManager = new FlowManager();
        this.targetFlowManager.initialize(getApplicationContext(), R.raw.appflow, new FlowManagerListener() {
            @Override
            public void onParseSuccess() {

            }
        });
    }

}