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
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.FileUtility;
import com.philips.platform.baseapp.screens.inapppurchase.IAPRetailerFlowState;
import com.philips.platform.baseapp.screens.inapppurchase.IAPState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationOnBoardingState;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

import java.io.File;
import java.util.Locale;

import static junit.framework.Assert.assertTrue;

@RunWith(GradleRunner.class)
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
        appInfra = new AppInfra.Builder().build(getApplicationContext());
        loggingInterface = appInfra.getLogging();
        setLocale();
        userRegistrationOnBoardingState = new UserRegistrationOnBoardingState();
        userRegistrationOnBoardingState.init(this);
        final int resId = R.string.com_philips_app_fmwk_app_flow_url;
        FileUtility fileUtility = new FileUtility(this);
        tempFile = fileUtility.createFileFromInputStream(resId, false);
        setTargetFlowManager();
        iapState = new IAPRetailerFlowState();
        iapState.init(this);

    }

    private void setLocale() {
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(languageCode, countryCode);
    }
    public void setTargetFlowManager() {
        if (tempFile != null) {
            this.targetFlowManager = new FlowManager();
            this.targetFlowManager.initialize(getApplicationContext(), tempFile.getPath(), this);
        }
    }

}