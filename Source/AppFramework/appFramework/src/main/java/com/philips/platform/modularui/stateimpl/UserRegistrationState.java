/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.datasevices.registration.UserRegistrationFacadeImpl;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.util.Locale;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;
import static com.philips.platform.appframework.AppFrameworkApplication.appInfra;

/**
 * This class contains all initialization & Launching details of UR
 * Setting configuration using App infra
 */
public class UserRegistrationState extends UIState implements UserRegistrationListener, ActionBarListener, UserRegistrationUIEventListener {

    private Context activityContext;
    private User userObject;
    private URStateListener userRegistrationListener;
    private FragmentLauncher fragmentLauncher;
    private Context applicationContext;

    public UserRegistrationState() {
        super(UIState.UI_USER_REGISTRATION_STATE);
    }

    /**
     * UIState overridden methods
     *
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
        initializeUserRegistrationLibrary();
       // initHSDP();
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
     *
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
     *
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
        UserRegistrationFacadeImpl userRegistrationFacade = new UserRegistrationFacadeImpl(activityContext, getUserObject(activityContext));
        userRegistrationFacade.clearUserData();
    }

    @Override
    public void onUserLogoutFailure() {
        userRegistrationListener.onLogoutFailure();
    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {
        userRegistrationListener.onLogoutSuccess();
        UserRegistrationFacadeImpl userRegistrationFacade = new UserRegistrationFacadeImpl(activityContext, getUserObject(activityContext));
        userRegistrationFacade.clearUserData();
    }


    public User getUserObject(Context context) {
        userObject = new User(context);
        return userObject;
    }

    /**
     * Registering for UIStateListener callbacks
     *
     * @param uiStateListener
     */
    public void registerUIStateListener(URStateListener uiStateListener) {
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

    /**
     * For doing dynamic initialisation Of User registration
     */
    public void initializeUserRegistrationLibrary() {
        String languageCode = Locale.getDefault().getLanguage();
        String countryCode = Locale.getDefault().getCountry();

        PILLocaleManager localeManager = new PILLocaleManager(applicationContext);
        localeManager.setInputLocale(languageCode, countryCode);

        URDependancies urDependancies = new URDependancies(AppFrameworkApplication.appInfra);
        URSettings urSettings = new URSettings(applicationContext);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);
    }

}
