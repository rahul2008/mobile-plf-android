/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.StringRes;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.AppIdentityInfo;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.URConfigurationConstants;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.util.ArrayList;
import java.util.Locale;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;
import static com.philips.platform.appframework.AppFrameworkApplication.appInfra;

/**
 * This class contains all initialization & Launching details of UR
 * Setting configuration using App infra
 */
public class UserRegistrationState extends UIState implements UserRegistrationListener ,ActionBarListener ,UserRegistrationUIEventListener{

    private Context activityContext;
    private User userObject;
    private URStateListener userRegistrationListener;
    private FragmentLauncher fragmentLauncher;
    private Context applicationContext;
    final String AI = "appinfra";

    public UserRegistrationState() {
        super(UIState.UI_USER_REGISTRATION_STATE);
    }

    /**
     * UIState overridden methods
     * @param uiLauncher requires the UiLauncher object
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        activityContext = fragmentLauncher.getFragmentActivity();
        launchUR();
    }

    @Override
    public void init(Context context) {
        this.applicationContext = context;
        initializeUserRegistrationLibrary(Configuration.STAGING);
        initHSDP();
    }

    public void initHSDP() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        appInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.ApplicationName",
                UR,
               // "Datacore",
                "uGrow",
                configError);

        appInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.Secret",
                UR,
                "ad3d0618-be4d-4958-adc9-f6bcd01fde16",
                configError);

        appInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.Shared",
                UR,
                "ba404af2-ee41-4e7c-9157-fd20663f2a6c",
                configError);

        appInfra.
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.BaseURL",
                UR,
                //"https://referenceplatform-ds-platforminfradev.cloud.pcftest.com",
                "https://platforminfra-ds-platforminfrastaging.cloud.pcftest.com",
                configError);
    }

    /**
     * ActionBarListener interface implementation methods
     * @param i
     * @param b
     */
    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }

    /**
     * UserRegistrationUIEventListener interface implementation methods
     * @param activity
     */
    @Override
    public void onUserRegistrationComplete(Activity activity) {
        if (null != activity) {
            userRegistrationListener.onStateComplete(this);
        }
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

    }

    /**
     * UserRegistrationListener interface implementation methods
     */
    @Override
    public void onUserLogoutSuccess() {
        userRegistrationListener.onLogoutSuccess();
    }

    @Override
    public void onUserLogoutFailure() {
        userRegistrationListener.onLogoutFailure();
    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {
        userRegistrationListener.onLogoutSuccess();
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
        this.userRegistrationListener = (URStateListener) getPresenter();
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

    /**For doing dynamic initialisation Of User registration
     *
     */
    public void initializeUserRegistrationLibrary(Configuration configuration) {
        final String UR = URConfigurationConstants.UR ;

        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        AppFrameworkApplication.appInfra.getConfigInterface().setPropertyForKey(URConfigurationConstants.JANRAIN_CONFIGURATION_REGISTRATION_CLIENT_ID_DEVELOPMENT
                , UR,
                "8kaxdrpvkwyr7pnp987amu4aqb4wmnte",
                configError);
        AppFrameworkApplication.appInfra.getConfigInterface().setPropertyForKey(URConfigurationConstants.JANRAIN_CONFIGURATION_REGISTRATION_CLIENT_ID_TESTING
                , UR,
                "g52bfma28yjbd24hyjcswudwedcmqy7c",
                configError);
        AppFrameworkApplication.appInfra.getConfigInterface().setPropertyForKey(URConfigurationConstants.JANRAIN_CONFIGURATION_REGISTRATION_CLIENT_ID_EVALUATION
                , UR,
                "f2stykcygm7enbwfw2u9fbg6h6syb8yd",
                configError);
        AppFrameworkApplication.appInfra.getConfigInterface().setPropertyForKey(URConfigurationConstants.JANRAIN_CONFIGURATION_REGISTRATION_CLIENT_ID_STAGING
                , UR,
                "f2stykcygm7enbwfw2u9fbg6h6syb8yd",
                configError);
        AppFrameworkApplication.appInfra.getConfigInterface().setPropertyForKey(URConfigurationConstants.JANRAIN_CONFIGURATION_REGISTRATION_CLIENT_ID_PRODUCTION
                , UR,
                "9z23k3q8bhqyfwx78aru6bz8zksga54u",
                configError);


        AppFrameworkApplication.appInfra.getConfigInterface().setPropertyForKey(URConfigurationConstants.PILCONFIGURATION_MICROSITE_ID,
                UR,
                "77000",
                configError);
        AppFrameworkApplication.appInfra.getConfigInterface().setPropertyForKey(URConfigurationConstants.PILCONFIGURATION_REGISTRATION_ENVIRONMENT,
                UR,
                configuration.getValue(),
                configError);

        AppFrameworkApplication.appInfra.
                getConfigInterface().setPropertyForKey(URConfigurationConstants.FLOW_EMAIL_VERIFICATION_REQUIRED,
                UR,
                "" + true,
                configError);
        AppFrameworkApplication.appInfra.
                getConfigInterface().setPropertyForKey(URConfigurationConstants.FLOW_TERMS_AND_CONDITIONS_ACCEPTANCE_REQUIRED,
                UR,
                "" + true,
                configError);

        String minAge = "{ \"NL\":12 ,\"GB\":0,\"default\": 16}";
        AppFrameworkApplication.appInfra.
                getConfigInterface().setPropertyForKey(URConfigurationConstants.FLOW_MINIMUM_AGE_LIMIT,
                UR,
                minAge,
                configError);

        ArrayList<String> providers = new ArrayList<String>();
        providers.add("facebook");
        providers.add("googleplus");
        providers.add("sinaweibo");
        providers.add("qq");
        AppFrameworkApplication.appInfra.
                getConfigInterface().setPropertyForKey(URConfigurationConstants.SIGNIN_PROVIDERS +
                        "NL",
                UR,
                providers,
                configError);

        AppFrameworkApplication.appInfra.
                getConfigInterface().setPropertyForKey(URConfigurationConstants.SIGNIN_PROVIDERS +
                        "US",
                UR,
                providers,
                configError);

        AppFrameworkApplication.appInfra.
                getConfigInterface().setPropertyForKey(URConfigurationConstants.SIGNIN_PROVIDERS +
                        URConfigurationConstants.DEFAULT,
                UR,
                providers,
                configError);


        SharedPreferences.Editor editor = applicationContext.getSharedPreferences("reg_dynamic_config", applicationContext.MODE_PRIVATE).edit();
        editor.putString("reg_environment", configuration.getValue());
        editor.commit();


        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(applicationContext);
        localeManager.setInputLocale(languageCode, countryCode);

        initAppIdentity(configuration);
        URDependancies urDependancies = new URDependancies(AppFrameworkApplication.appInfra);
        URSettings urSettings = new URSettings(applicationContext);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);


    }

    private void initAppIdentity(Configuration configuration) {
        AppIdentityInterface mAppIdentityInterface;
        mAppIdentityInterface = AppFrameworkApplication.appInfra.getAppIdentity();
        AppConfigurationInterface appConfigurationInterface = AppFrameworkApplication.appInfra.
                getConfigInterface();

        //Dynamically set the values to appInfar and app state

        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        AppFrameworkApplication.appInfra.
                getConfigInterface().setPropertyForKey(
                "appidentity.micrositeId",
                AI,
                "77000",
                configError);

        AppFrameworkApplication.appInfra.
                getConfigInterface().setPropertyForKey(
                "appidentity.sector",
                AI,
                "b2c",
                configError);

        AppFrameworkApplication.appInfra.
                getConfigInterface().setPropertyForKey(
                "appidentity.serviceDiscoveryEnvironment",
                AI,
                "Production",
                configError);


        switch (configuration) {
            case EVALUATION:
                AppFrameworkApplication.appInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "ACCEPTANCE",
                        configError);
                break;
            case DEVELOPMENT:
                AppFrameworkApplication.appInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "DEVELOPMENT",
                        configError);

                break;
            case PRODUCTION:
                AppFrameworkApplication.appInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "PRODUCTION",
                        configError);
                break;
            case STAGING:
                AppFrameworkApplication.appInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "STAGING",
                        configError);

                break;
            case TESTING:
                AppFrameworkApplication.appInfra.
                        getConfigInterface().setPropertyForKey(
                        "appidentity.appState",
                        AI,
                        "TEST",
                        configError);
                break;
        }

        AppIdentityInfo appIdentityInfo = new AppIdentityInfo();
        appIdentityInfo.setAppLocalizedNAme(mAppIdentityInterface.getLocalizedAppName());
        appIdentityInfo.setSector(mAppIdentityInterface.getSector());
        appIdentityInfo.setMicrositeId(mAppIdentityInterface.getMicrositeId());
        appIdentityInfo.setAppName(mAppIdentityInterface.getAppName());
        appIdentityInfo.setAppState(mAppIdentityInterface.getAppState().toString());
        appIdentityInfo.setAppVersion(mAppIdentityInterface.getAppVersion());
        appIdentityInfo.setServiceDiscoveryEnvironment(mAppIdentityInterface.getServiceDiscoveryEnvironment());

    }

}
