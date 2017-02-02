/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.userregistration;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.net.URL;
import java.util.Locale;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_APPLICATION_NAME;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_BASE_URL;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SECRET;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.HSDP_CONFIGURATION_SHARED;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;

/**
 * This class contains all initialization & Launching details of UR
 * Setting configuration using App infra
 */
public abstract class UserRegistrationState extends BaseState implements UserRegistrationListener,UserRegistrationUIEventListener{

    final String AI = "appinfra";
    private Context activityContext;
    private User userObject;
    private FragmentLauncher fragmentLauncher;
    private Context applicationContext;
    String mDataCoreUrl = null;
    private final String APP_NAME = "appname";
    private final String DATACORE_FALLBACK_URL = "dataCoreUrl";
    private final String DATASERVICES_KEY = "dataservices";
    private AppInfraInterface mAppInfra;
    private ServiceDiscoveryInterface mServiceDiscoveryInterface;
    private AppConfigurationInterface.AppConfigurationError mConfigurationError;

    /**
     * AppFlowState constructor
     *
     */
    public UserRegistrationState(String stateID) {
        super(stateID);
    }

    /**
     * BaseState overridden methods
     * @param uiLauncher requires the UiLauncher object
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        activityContext = getFragmentActivity();
        updateDataModel();
        launchUR();
    }

    public FragmentActivity getFragmentActivity() {
        return fragmentLauncher.getFragmentActivity();
    }

    public AppFrameworkApplication getApplicationContext(){
        return (AppFrameworkApplication) getFragmentActivity().getApplicationContext();
    }
    @Override
    public void init(Context context) {
        this.applicationContext = context;
        initAppInfra();
        initializeUserRegistrationLibrary();
        fetchDataServicesUrl();
        //initHSDP();
    }

    @Override
    public void updateDataModel() {

    }

    public void initHSDP() {
        mAppInfra.
                getConfigInterface().setPropertyForKey(
                HSDP_CONFIGURATION_APPLICATION_NAME,
                UR,
                loadAppNameFromConfigParams(APP_NAME),
                mConfigurationError);

        mAppInfra.
                getConfigInterface().setPropertyForKey(
                HSDP_CONFIGURATION_SECRET,
                UR,
                "ad3d0618-be4d-4958-adc9-f6bcd01fde16",
                mConfigurationError);

        mAppInfra.
                getConfigInterface().setPropertyForKey(
                HSDP_CONFIGURATION_SHARED,
                UR,
                "ba404af2-ee41-4e7c-9157-fd20663f2a6c",
                mConfigurationError);

        mAppInfra.
                getConfigInterface().setPropertyForKey(
                HSDP_CONFIGURATION_BASE_URL,
                UR,
                mDataCoreUrl,
                mConfigurationError);
    }


    public User getUserObject(Context context) {
        userObject = new User(context);
        return userObject;
    }

    /**
     * Registering for UIStateListener callbacks
     * @param uiStateListener
     */
    public void registerUIStateListener(URStateListener uiStateListener){
    }


    /**
     * Launch registration fragment
     */
    private void launchUR() {
        userObject = new User(activityContext);
        userObject.registerUserRegistrationListener(this);
        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setUserRegistrationUIEventListener(this);
        urLaunchInput.setAccountSettings(false);
        urLaunchInput.enableAddtoBackStack(true);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        URInterface urInterface = new URInterface();
        urInterface.launch(fragmentLauncher, urLaunchInput);
    }

    private void initAppInfra() {
        mAppInfra = ((AppFrameworkApplication) applicationContext).getAppInfra();
        mServiceDiscoveryInterface = mAppInfra.getServiceDiscovery();
        mConfigurationError = new
                AppConfigurationInterface.AppConfigurationError();
    }

    /**
     * For doing dynamic initialisation Of User registration
     */
    public void initializeUserRegistrationLibrary() {
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(applicationContext);
        localeManager.setInputLocale(languageCode, countryCode);

        URDependancies urDependancies = new URDependancies(((AppFrameworkApplication)applicationContext).getAppInfra());
        URSettings urSettings = new URSettings(applicationContext);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);
    }

    public void unregisterUserRegistrationListener() {
        userObject.unRegisterUserRegistrationListener(this);
    }

    protected void fetchDataServicesUrl() {

        mServiceDiscoveryInterface.getServiceUrlWithCountryPreference("ds.dataservice", new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        DSLog.e(DSLog.LOG,"Error");
                        mDataCoreUrl = loadAppNameFromConfigParams(DATACORE_FALLBACK_URL);
                        initHSDP();
                    }

                    @Override
                    public void onSuccess(URL url) {
                        DSLog.e(DSLog.LOG,"Success = " + url);
                        if (url.toString().isEmpty()) {
                            mDataCoreUrl = loadAppNameFromConfigParams(DATACORE_FALLBACK_URL);
                        } else {

                            mDataCoreUrl = url.toString();
                        }
                        initHSDP();
                    }
                });
    }

    private String loadAppNameFromConfigParams(String propertyKey) {
        String appname = (String)  ((AppFrameworkApplication)applicationContext).
                getAppInfra().getConfigInterface().getPropertyForKey(propertyKey, DATASERVICES_KEY, mConfigurationError);
        if (mConfigurationError.getErrorCode() != null) {
            DSLog.e(DSLog.LOG, "VerticalAppConfig ==loadConfigurationFromAsset " + mConfigurationError.getErrorCode().toString());
        }
        return appname;
    }
}