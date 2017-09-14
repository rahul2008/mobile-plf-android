/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.base;

import android.app.Application;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.StrictMode;

import com.crittercism.app.Crittercism;
import com.philips.cdp.uikit.utils.UikitLocaleHelper;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.appframework.stateimpl.DemoDataServicesState;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.languagepack.LanguagePackInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.screens.inapppurchase.IAPRetailerFlowState;
import com.philips.platform.baseapp.screens.inapppurchase.IAPState;
import com.philips.platform.baseapp.screens.productregistration.ProductRegistrationState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationOnBoardingState;
import com.philips.platform.baseapp.screens.userregistration.UserRegistrationState;
import com.philips.platform.baseapp.screens.utility.BaseAppUtil;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.receivers.ConnectivityChangeReceiver;
import com.philips.platform.referenceapp.PushNotificationManager;
import com.squareup.leakcanary.LeakCanary;

/**
 * Application class is used for initialization
 */
public class AppFrameworkApplication extends Application {
    private static final String LOG = AppFrameworkApplication.class.getSimpleName();
    private static final String LEAK_CANARY_BUILD_TYPE = "leakCanary";
    public static final String Apteligent_APP_ID = "69bb94377f0949908f3eeba142b8b21100555300";
    public AppInfraInterface appInfra;
    public static LoggingInterface loggingInterface;
    protected FlowManager targetFlowManager;
    private UserRegistrationState userRegistrationState;
    private IAPState iapState;
    private DemoDataServicesState dataSyncScreenState;
    private ProductRegistrationState productRegistrationState;
    private static boolean isChinaCountry = false;
    private PushNotificationManager pushNotificationManager;
    private LanguagePackInterface languagePackInterface;
    private ConnectivityChangeReceiver connectivityChangeReceiver;

    public boolean isShopingCartVisible() {
        return isShopingCartVisible;
    }

    public void setShopingCartVisible(boolean shopingCartVisible) {
        isShopingCartVisible = shopingCartVisible;
    }

    public boolean isShopingCartVisible = false;


    @Override
    public void onCreate() {
        applyStrictMode();
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(LEAK_CANARY_BUILD_TYPE)) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This proisChinaCountrycess is dedicated to LeakCanary for heap analysis.
                // You should not initialise your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
        super.onCreate();

        /*
         * Apteligent initialization.
         */
        Crittercism.setLoggingLevel(Crittercism.LoggingLevel.Silent);
        Crittercism.initialize(getApplicationContext(), Apteligent_APP_ID);
    }

    /**
     * Initialize app states
     *
     * @param callback
     */
    public void initialize(AppInitializationCallback.AppStatesInitializationCallback callback) {

        RALog.d(LOG, "UR state begin::");
        initUserRegistrationState();
        RALog.d(LOG, "UR state end::");
        RALog.d(LOG, "China flow state begin::");
        determineChinaFlow();
        RALog.d(LOG, "China flow state end::");
        RALog.d(LOG, "PR state begin::");
        productRegistrationState = new ProductRegistrationState();
        productRegistrationState.init(this);
        RALog.d(LOG, "PR state end::");
      //  determineHybrisFlow();
        RALog.d(LOG, "DS state begin::");
        initDataServiceState();
        RALog.d(LOG, "DS state end::");
        initializeIAP();
        /*
         * Initializing tagging class and its interface. Interface initialization needs
         * context to gets started.
         */
        RALog.d(LOG, "PN state begin::");
        if (BaseAppUtil.isDSPollingEnabled(getApplicationContext())) {
            RALog.d(PushNotificationManager.TAG, "Polling is enabled");
        } else {
            RALog.d(PushNotificationManager.TAG, "Push notification is enabled");
            pushNotificationManager = PushNotificationManager.getInstance();
            pushNotificationManager.init(appInfra, getDataServiceState());
            pushNotificationManager.startPushNotificationRegistration(getApplicationContext());
            connectivityChangeReceiver = new ConnectivityChangeReceiver();
            registerReceiver(connectivityChangeReceiver,
                    new IntentFilter(
                            ConnectivityManager.CONNECTIVITY_ACTION));
        }
        RALog.d("test", "onCreate end::");
        callback.onAppStatesInitialization();
    }



    public void initUserRegistrationState() {
        userRegistrationState = new UserRegistrationOnBoardingState();
        userRegistrationState.init(this);
    }

    public UserRegistrationState getUserRegistrationState() {
        return userRegistrationState;
    }

    public void initDataServiceState() {
        dataSyncScreenState = new DemoDataServicesState();
        dataSyncScreenState.init(this);
    }

    public LoggingInterface getLoggingInterface() {
        return loggingInterface;
    }

    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

    public String getAppState() {
        return getAppInfra().getAppIdentity().getAppState().toString();
    }

    public IAPState getIap() {
        return iapState;
    }

    public BaseFlowManager getTargetFlowManager() {
        return targetFlowManager;
    }

    public void setTargetFlowManager(FlowManagerListener flowManagerListener) {
        this.targetFlowManager = new FlowManager();
        this.targetFlowManager.initialize(getApplicationContext(), R.raw.appflow, flowManagerListener);
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
                if (s.equals("CN")) {
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

    
    public DemoDataServicesState getDataServiceState() {
        if (dataSyncScreenState == null) {
            initDataServiceState();
        }
        return dataSyncScreenState;
    }

    public void initializeIAP() {
        RALog.d(LOG, "IAP state begin::");
        iapState = new IAPRetailerFlowState();
        iapState.init(this);
        try{
            getIap().isCartVisible();

        }
        catch (RuntimeException rte)
        {
            setShopingCartVisible(false);
        }
        RALog.d(LOG, "IAP state end::");
    }

    @Override
    public void onTerminate() {
        if (connectivityChangeReceiver != null) {
            unregisterReceiver(connectivityChangeReceiver);
        }
        AppFrameworkTagging.getInstance().releaseApteligentReceiver();

        super.onTerminate();
    }

    private void applyStrictMode() {
        if (BuildConfig.DEBUG) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                    .detectAll()   // or .detectAll() for all detectable problems
                    .penaltyLog()
                    .build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
                    .detectAll()
                    .penaltyLog()
                    .build());
        }
    }

    /***
     * Initializar app infra
     *
     * @param appInfraInitializationCallback
     */
    public void initializeAppInfra(AppInitializationCallback.AppInfraInitializationCallback appInfraInitializationCallback) {

        appInfra = createAppInfraInstance();
        loggingInterface = appInfra.getLogging();
        RALog.init(appInfra);
        AppFrameworkTagging.getInstance().initAppTaggingInterface(this);
        appInfraInitializationCallback.onAppInfraInitialization();
        languagePackInterface = appInfra.getLanguagePack();
        languagePackInterface.refresh(new LanguagePackInterface.OnRefreshListener() {
            @Override
            public void onError(AILPRefreshResult ailpRefreshResult, String s) {
                RALog.e(LOG,ailpRefreshResult.toString()+"---"+s);
            }

            @Override
            public void onSuccess(AILPRefreshResult ailpRefreshResult) {
                languagePackInterface.activate(new LanguagePackInterface.OnActivateListener() {
                    @Override
                    public void onSuccess(String filePath) {
                        UikitLocaleHelper.getUikitLocaleHelper().setFilePath(filePath);
                        RALog.d(LOG,"Success langauge pack activate "+"---"+filePath);
                    }

                    @Override
                    public void onError(AILPActivateResult ailpActivateResult, String s) {
                        RALog.e(LOG,ailpActivateResult.toString()+"---"+s);
                    }
                });
            }
        });
    }

    protected AppInfra createAppInfraInstance() {
        return new AppInfra.Builder().build(getApplicationContext());
    }
}
