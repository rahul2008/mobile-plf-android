/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.userregistration;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
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
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.dataservices.utility.SyncScheduler;
import com.philips.platform.baseapp.screens.utility.BaseAppUtil;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.referenceapp.PushNotificationManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.UR;

/**
 * This class contains all initialization & Launching details of UR
 * Setting configuration using App infra
 */
public abstract class UserRegistrationState extends BaseState implements UserRegistrationListener, UserRegistrationUIEventListener {

    private static final String TAG = UserRegistrationState.class.getSimpleName();
    private Context activityContext;
    private User userObject;
    private FragmentLauncher fragmentLauncher;
    private Context applicationContext;
    private static final String HSDP_CONFIGURATION_APPLICATION_NAME = "HSDPConfiguration.ApplicationName";
    private static final String HSDP_CONFIGURATION_SECRET = "HSDPConfiguration.Secret";
    private static final String HSDP_CONFIGURATION_SHARED = "HSDPConfiguration.Shared";
    private static final String CHINA_CODE = "CN";
    private static final String DEFAULT = "default";
    private static final String URL_ENCODING = "UTF-8";

    /**
     * AppFlowState constructor
     */
    public UserRegistrationState(String stateID) {
        super(stateID);
    }

    /**
     * BaseState overridden methods
     *
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

    public AppFrameworkApplication getApplicationContext() {
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
            getApplicationContext().determineChinaFlow();

            //Register GCM token with data services on login success
            if (BaseAppUtil.isDSPollingEnabled(activity.getApplicationContext())) {
                RALog.d(PushNotificationManager.TAG, "Polling is enabled");
                SyncScheduler.getInstance().scheduleSync();

            } else {
                RALog.d(PushNotificationManager.TAG, "Push notification is enabled");
                ((AppFrameworkApplication) activity.getApplicationContext()).getDataServiceState().registerForReceivingPayload();
                ((AppFrameworkApplication) activity.getApplicationContext()).getDataServiceState().registerDSForRegisteringToken();
                PushNotificationManager.getInstance().startPushNotificationRegistration(activity.getApplicationContext());
            }
            BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
            BaseState baseState = null;
            try {
                baseState = targetFlowManager.getNextState(targetFlowManager.getCurrentState(), "URComplete");
            } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                    e) {
                RALog.d(TAG, e.getMessage());
                Toast.makeText(getFragmentActivity(), getFragmentActivity().getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
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
        AppInfraInterface appInfra = ((AppFrameworkApplication) applicationContext).getAppInfra();

        AppConfigurationInterface appConfigurationInterface = appInfra.getConfigInterface();

        Map<String, String> hsdpAppNames = new HashMap<>();
        hsdpAppNames.put(CHINA_CODE, "OneBackend");
        hsdpAppNames.put(DEFAULT, "OneBackend");

        appConfigurationInterface.setPropertyForKey(HSDP_CONFIGURATION_APPLICATION_NAME,
                UR, hsdpAppNames, configError);

        Map<String, String> hsdpSecrets = new HashMap<>();
        hsdpSecrets.put(CHINA_CODE, "a3a3d09e2c74b93a409bc242956a6101bd5ff78cfd21473faa7aa21a8ec8493b66fa905dd4916b8ba4325cb988b442f9c6054089b9b36d09bb1538f985b47b22");
        hsdpSecrets.put(DEFAULT, "f5b62a26d680e5ae8001522a8e3268f966545a1a14a47ea2040793ea825484cd12fce9c46b43e2c2604cb836db64362a0c8b39eb7b162b8b3e83740143337eda");
        appConfigurationInterface.setPropertyForKey(HSDP_CONFIGURATION_SECRET,
                UR, hsdpSecrets, configError);

        Map<String, String> hsdpSharedIds = new HashMap<>();
        hsdpSharedIds.put(CHINA_CODE, "6036461d-0914-4afe-9e6e-eefe27fb529a");
        hsdpSharedIds.put(DEFAULT, "f52cd90d-c955-43e1-8380-999e03d0d4c0");

        appConfigurationInterface.setPropertyForKey(HSDP_CONFIGURATION_SHARED,
                UR, hsdpSharedIds, configError);
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
    }


    /**
     * Launch registration fragment
     */
    private void launchUR() {
        RALog.d(TAG," LaunchUr called ");
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
        RALog.d(TAG," initializeUserRegistrationLibrary called ");
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
        RALog.d(TAG," unregisterUserRegistrationListener called ");
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
        RALog.d(TAG," User Logout success  ");

    }

    @Override
    public void onUserLogoutFailure() {

        RALog.d(TAG, "User logout failed");
    }

    @Override
    public void onUserLogoutSuccessWithInvalidAccessToken() {
    }

    public String getVersion() {
        return RegistrationHelper.getRegistrationApiVersion();
    }

    public String getComponentID(Context c) {
        return c.getResources().getString(R.string.RA_COCO_UR);
    }
}