/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.base;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.os.StrictMode;
import android.support.multidex.MultiDexApplication;

import com.philips.cdp.cloudcontroller.CloudController;
import com.philips.cdp.cloudcontroller.DefaultCloudController;
import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.wifirefuapp.SampleApplianceFactory;
import com.philips.cdp.wifirefuapp.SampleKpsConfigurationInfo;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
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
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.receivers.ConnectivityChangeReceiver;
import com.philips.platform.referenceapp.PushNotificationManager;
import com.squareup.leakcanary.LeakCanary;

import java.util.Locale;

/**
 * Application class is used for initialization
 */
public class AppFrameworkApplication extends MultiDexApplication {
    private static final String LOG = AppFrameworkApplication.class.getSimpleName();
    private static final String LEAK_CANARY_BUILD_TYPE = "leakCanary";
    public AppInfraInterface appInfra;
    public static LoggingInterface loggingInterface;
    protected FlowManager targetFlowManager;
    private UserRegistrationState userRegistrationState;
    private IAPState iapState;
    private DataServicesState dataSyncScreenState;
    private ProductRegistrationState productRegistrationState;
    private static boolean isChinaCountry = false;
    private PushNotificationManager pushNotificationManager;
    private ConnectivityChangeReceiver connectivityChangeReceiver;
    private CommCentral commCentral;

    @Override
    public void onCreate() {
        applyStrictMode();
        if (BuildConfig.BUILD_TYPE.equalsIgnoreCase(LEAK_CANARY_BUILD_TYPE)) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                // This proisChinaCountrycess is dedicated to LeakCanary for heap analysis.
                // You should not init your app in this process.
                return;
            }
            LeakCanary.install(this);
        }
        super.onCreate();
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
        RALog.d(LOG, "IAP state begin::");
        iapState = new IAPRetailerFlowState();
        iapState.init(this);
        RALog.d(LOG, "IAP state end::");
        RALog.d(LOG, "DS state begin::");
        initDataServiceState();
        RALog.d(LOG, "DS state end::");
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
        initiliazeDiComm();

    }

    private void initiliazeDiComm() {
        final CloudController cloudController = setupCloudController();
        final LanTransportContext lanTransportContext = new LanTransportContext(this);
        final SampleApplianceFactory applianceFactory = new SampleApplianceFactory(lanTransportContext);
        this.commCentral = new CommCentral(applianceFactory, lanTransportContext);

//        if (null == DICommClientWrapper.getContext()) {
            DICommClientWrapper.initializeDICommLibrary(this, applianceFactory, null, cloudController);
//        }
    }

    @NonNull
    private CloudController setupCloudController() {
        final CloudController cloudController = new DefaultCloudController(this, new SampleKpsConfigurationInfo());

        String ICPClientVersion = cloudController.getICPClientVersion();
        DICommLog.i(DICommLog.ICPCLIENT, "ICPClientVersion :" + ICPClientVersion);

        return cloudController;
    }

    public void initUserRegistrationState() {
        userRegistrationState = new UserRegistrationOnBoardingState();
        userRegistrationState.init(this);
    }

    public void initDataServiceState() {
        dataSyncScreenState = new DataServicesState();
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

    public DataServicesState getDataServiceState() {
        if (dataSyncScreenState == null) {
            initDataServiceState();
        }
        return dataSyncScreenState;
    }


    @Override
    public void onTerminate() {
        if (connectivityChangeReceiver != null) {
            unregisterReceiver(connectivityChangeReceiver);
        }
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
        appInfra = new AppInfra.Builder().build(getApplicationContext());
        loggingInterface = appInfra.getLogging();
        RALog.init(appInfra);
        RALog.enableLogging();
        AppFrameworkTagging.getInstance().initAppTaggingInterface(this);
        appInfraInitializationCallback.onAppInfraInitialization();
    }
}
