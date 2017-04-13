/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.base;

import android.app.Application;
import android.support.multidex.MultiDex;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.exceptions.JsonFileNotFoundException;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.screens.dataservices.DataServicesState;
import com.philips.platform.baseapp.screens.inapppurchase.IAPRetailerFlowState;
import com.philips.platform.baseapp.screens.inapppurchase.IAPState;
import com.philips.platform.baseapp.screens.productregistration.ProductRegistrationState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationOnBoardingState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;
import com.philips.platform.baseapp.screens.utility.BaseAppUtil;
import com.squareup.leakcanary.LeakCanary;

import java.io.File;
import java.util.Locale;

/**
 * Application class is used for initialization
 */
public class AppFrameworkApplication extends Application implements FlowManagerListener {
    private static final String LEAK_CANARY_BUILD_TYPE = "leakCanary";
    public AppInfraInterface appInfra;
    public  static LoggingInterface loggingInterface;
    protected FlowManager targetFlowManager;
    private UserRegistrationState userRegistrationState;
    private IAPState iapState;
    private DataServicesState dataSyncScreenState;
    private ProductRegistrationState productRegistrationState;
    private boolean isSdCardFileCreated;
    private File tempFile;
    private static boolean isChinaCountry = false;

    @Override
    public void onCreate() {
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(LEAK_CANARY_BUILD_TYPE)) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This proisChinaCountrycess is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
        isSdCardFileCreated = new BaseAppUtil().createDirIfNotExists();
        final int resId = R.string.com_philips_app_fmwk_app_flow_url;
        FileUtility fileUtility = new FileUtility(this);
        tempFile = fileUtility.createFileFromInputStream(resId, isSdCardFileCreated);
        MultiDex.install(this);
        super.onCreate();
        appInfra = new AppInfra.Builder().build(getApplicationContext());
        loggingInterface= appInfra.getLogging();
        setLocale();
        userRegistrationState = new UserRegistrationOnBoardingState();
        userRegistrationState.init(this);
        determineChinaFlow();
        productRegistrationState = new ProductRegistrationState();
        productRegistrationState.init(this);
        dataSyncScreenState = new DataServicesState();
        dataSyncScreenState.init(this);
        /*
         * Initializing tagging class and its interface. Interface initialization needs
         * context to gets started.
         */
        AppFrameworkTagging.getInstance().initAppTaggingInterface(this);
    }

    public LoggingInterface getLoggingInterface() {
        return loggingInterface;
    }

    public AppInfraInterface getAppInfra() {
        return appInfra;
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

    public BaseFlowManager getTargetFlowManager() {
        return targetFlowManager;
    }

    public void setTargetFlowManager() {
        try {
            this.targetFlowManager = new FlowManager();
            this.targetFlowManager.initialize(getApplicationContext(), new BaseAppUtil().getJsonFilePath().getPath(), this);
        } catch (JsonFileNotFoundException e) {
            if (tempFile != null) {
                this.targetFlowManager = new FlowManager();
                this.targetFlowManager.initialize(getApplicationContext(), tempFile.getPath(), this);
            }
        }
    }

    @Override
    public void onParseSuccess() {

    }

    public boolean isChinaFlow() {
        return isChinaCountry;
    }

    public void determineChinaFlow() {
        AppInfraInterface appInfraInterface = getAppInfra();
        ServiceDiscoveryInterface serviceDiscovery = appInfraInterface.getServiceDiscovery();

        serviceDiscovery.getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
            @Override
            public void onSuccess(String s, SOURCE source) {
                if(s.equals("CN")) {
                    isChinaCountry = true;
                } else {
                    isChinaCountry = false;
                }
            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                isChinaCountry = false;
            }
        });
    }
}
