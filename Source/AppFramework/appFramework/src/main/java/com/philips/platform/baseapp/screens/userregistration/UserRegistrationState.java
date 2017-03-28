/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.userregistration;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.util.Locale;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;

/**
 * This class contains all initialization & Launching details of UR
 * Setting configuration using App infra
 */
public abstract class UserRegistrationState extends BaseState implements UserRegistrationListener, UserRegistrationUIEventListener{

    final String AI = "appinfra";
    private Context activityContext;
    private User userObject;
    private FragmentLauncher fragmentLauncher;
    private Context applicationContext;

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
        initializeUserRegistrationLibrary();
        initHSDP();
    }


    @Override
    public void onUserRegistrationComplete(Activity activity) {
        if (null != activity) {
            BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
            BaseState baseState = null;
            try {
                baseState = targetFlowManager.getNextState(targetFlowManager.getCurrentState(), "URComplete");
            } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                    e) {
                Log.d(getClass() + "", e.getMessage());
                Toast.makeText(getFragmentActivity(), getFragmentActivity().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
            if (null != baseState) {
                getFragmentActivity().finish();
                baseState.navigate(new FragmentLauncher(getFragmentActivity(), R.id.frame_container, (ActionBarListener) getFragmentActivity()));
            }
        }
    }

    @Override
    public void updateDataModel() {

    }

    public void initHSDP() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        ((AppFrameworkApplication)applicationContext).getAppInfra().
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.ApplicationName",
                UR,
                "OneBackend",
                configError);

        ((AppFrameworkApplication)applicationContext).getAppInfra().
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.Secret",
                UR,
                "f5b62a26d680e5ae8001522a8e3268f966545a1a14a47ea2040793ea825484cd12fce9c46b43e2c2604cb836db64362a0c8b39eb7b162b8b3e83740143337eda",
                configError);

        ((AppFrameworkApplication)applicationContext).getAppInfra().
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.Shared",
                UR,
                "f52cd90d-c955-43e1-8380-999e03d0d4c0",
                configError);

        ((AppFrameworkApplication)applicationContext).getAppInfra().
                getConfigInterface().setPropertyForKey(
                "HSDPConfiguration.BaseURL",
                UR,
                "https://platforminfra-ds-platforminfrastaging.cloud.pcftest.com",
//                "https://user-registration-assembly-staging.eu-west.philips-healthsuite.com",
                configError);
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
        userObject = new User(getApplicationContext());
        userObject.registerUserRegistrationListener(this);
        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setUserRegistrationUIEventListener(this);
        urLaunchInput.enableAddtoBackStack(true);
        urLaunchInput.setAccountSettings(false);
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

        URDependancies urDependancies = new URDependancies(((AppFrameworkApplication)applicationContext).getAppInfra());
        URSettings urSettings = new URSettings(applicationContext);
        URInterface urInterface = new URInterface();
        urInterface.init(urDependancies, urSettings);
    }

    public void unregisterUserRegistrationListener() {
        userObject.unRegisterUserRegistrationListener(this);
    }


    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

    }

    @Override
    public void onUserLogoutSuccess() {
    }

    @Override
    public void onUserLogoutFailure() {
    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {
    }
}