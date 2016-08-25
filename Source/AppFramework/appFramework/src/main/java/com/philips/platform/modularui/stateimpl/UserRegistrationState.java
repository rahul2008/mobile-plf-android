/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.configuration.RegistrationBaseConfiguration;
import com.philips.cdp.registration.configuration.RegistrationClientId;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.URDependancies;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.cdp.registration.ui.utils.URSettings;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.appframework.introscreen.WelcomeActivity;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UserRegistrationState extends UIState implements UserRegistrationListener ,ActionBarListener ,UserRegistrationUIEventListener {
    Context mContext;
    User userObject;
    int containerID;
    FragmentActivity fragmentActivity;
    URSettings urSettings;
    URLaunchInput urLaunchInput;
    private ActionBarListener actionBarListener;

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        if (null != activity) {
            setStateCallBack.setNextState(mContext);
        }

    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

    }

    /**
     * Interface to have callbacks for updating the title from UserRegistration CoCo callbacks.
     */
    public interface SetStateCallBack{
        void setNextState(Context contexts);
        void updateTitle(int titleResourceID,Context context);
        void updateTitleWithBack(int titleResourceID,Context context);
        void updateTitleWIthoutBack(int titleResourceID,Context context);
    }
    SetStateCallBack setStateCallBack;

    public User getUserObject(Context context) {
        userObject = new User(context);
        return userObject;
    }

    public void registerForNextState(SetStateCallBack setStateCallBack){
        this.setStateCallBack = (SetStateCallBack) getPresenter();
    }

    public UserRegistrationState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        mContext = context;

        if(context instanceof HomeActivity){
            containerID = R.id.frame_container;
            actionBarListener  = (HomeActivity) context;
        }
        if(context instanceof WelcomeActivity){
            containerID = R.id.fragment_frame_container;
            actionBarListener  = (WelcomeActivity) context;
        }
        loadPlugIn();
        runUserRegistration();
    }

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBack();
    }

    private void loadPlugIn(){
        RegistrationHelper.getInstance().registerUserRegistrationListener(this);
    }

    private void runUserRegistration(){
        if(mContext instanceof WelcomeActivity){
            containerID = R.id.fragment_frame_container;
            fragmentActivity = (WelcomeActivity)mContext;
        }else if(mContext instanceof HomeActivity){
            containerID = R.id.frame_container;
            fragmentActivity = (HomeActivity)mContext;
        }
        launchRegistrationFragment(false);
    //    launchRegistrationFragment(containerID,fragmentActivity, true);
    }
    /**
     * Launch registration fragment
     */
    private void launchRegistrationFragment(int container, FragmentActivity fragmentActivity, boolean isAccountSettings ) {
        try {
            /*FragmentManager mFragmentManager = fragmentActivity.getSupportFragmentManager();
            RegistrationFragment registrationFragment = new RegistrationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, isAccountSettings);
            registrationFragment.setArguments(bundle);
            registrationFragment.setOnUpdateTitleListener(mContext);
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(container, registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();*/

            com.philips.platform.uappframework.launcher.FragmentLauncher launcher =
                    new com.philips.platform.uappframework.launcher.FragmentLauncher
                            (fragmentActivity, containerID, actionBarListener);
         //    urSettings=new URSettings(mContext);
            urLaunchInput= new URLaunchInput();
            urLaunchInput.setUserRegistrationUIEventListener(this);
            //    URDependancies urDependancies= new URDependancies(AppInfraSingleton.getInstance());
          //  URInterface.init(urDependancies,urSettings);
           // URInterface.launch(launcher,urLaunchInput);

            URDependancies urDependancies = new URDependancies(AppInfraSingleton.getInstance());
            URSettings urSettings = new URSettings(mContext);

           /* RegistrationBaseConfiguration mRegistrationBaseConfiguration=new RegistrationBaseConfiguration();
            mRegistrationBaseConfiguration.getPilConfiguration().setMicrositeId("77000");
            RegistrationClientId registrationClientId = new RegistrationClientId();
            registrationClientId.setStagingId("4r36zdbeycca933nufcknn2hnpsz6gxu");
            mRegistrationBaseConfiguration.getJanRainConfiguration().setClientIds(registrationClientId);
            mRegistrationBaseConfiguration.getFlow().setEmailVerificationRequired(true);
            mRegistrationBaseConfiguration.getFlow().setTermsAndConditionsAcceptanceRequired(true);

            //Configure Signin Providers
            HashMap<String, ArrayList<String>> providers = new HashMap<String, ArrayList<String>>();
            ArrayList<String> values1 = new ArrayList<String>();
            ArrayList<String> values2 = new ArrayList<String>();
            ArrayList<String> values3 = new ArrayList<String>();
            values1.add("facebook");
            values1.add("googleplus");
            values2.add("facebook");
            values2.add("googleplus");
            values3.add("facebook");
            values3.add("googleplus");
            providers.put("NL", values1);
            providers.put("US", values2);
            providers.put("DEFAULT", values3);
            mRegistrationBaseConfiguration.getSignInProviders().setProviders(providers);

            mRegistrationBaseConfiguration.getPilConfiguration().setRegistrationEnvironment(Configuration.EVALUATION);*/
       //     urSettings.setRegistrationConfiguration(mRegistrationBaseConfiguration);
            URInterface urInterface = new URInterface();
           // urInterface.init(urDependancies,urSettings);
            urInterface.launch(launcher,urLaunchInput);


        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationActivity :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
    }
    private void launchRegistrationFragment(boolean isAccountSettings) {
        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setUserRegistrationUIEventListener(this);
        urLaunchInput.setAccountSettings(isAccountSettings);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        FragmentLauncher fragmentLauncher = new FragmentLauncher
                (fragmentActivity,containerID,actionBarListener);
        URInterface urInterface = new URInterface();
        urInterface.launch(fragmentLauncher, urLaunchInput);
    }


    /*
    Callbacks from interface implemented
     */
    /*@Override
    public void updateRegistrationTitle(int titleResourceID) {
        setStateCallBack.updateTitle(titleResourceID,mContext);
    }

    @Override
    public void updateRegistrationTitleWithBack(int titleResourceID) {
        setStateCallBack.updateTitleWithBack(titleResourceID,mContext);
    }

    @Override
    public void updateRegistrationTitleWithOutBack(int titleResourceID) {
        setStateCallBack.updateTitleWIthoutBack(titleResourceID,mContext);
    }

*/





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
