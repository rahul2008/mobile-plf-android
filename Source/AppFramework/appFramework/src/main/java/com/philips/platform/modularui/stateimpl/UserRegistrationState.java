/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.appframework.introscreen.WelcomeActivity;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

public class UserRegistrationState extends UIState implements UserRegistrationListener ,ActionBarListener ,UserRegistrationUIEventListener {
    Context mContext;
    User userObject;
    int containerID;
    FragmentActivity fragmentActivity;
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
    }
    /**
     * Launch registration fragment
     */
    private void launchRegistrationFragment(boolean isAccountSettings) {

        URLaunchInput urLaunchInput = new URLaunchInput();
        urLaunchInput.setUserRegistrationUIEventListener(this);
        urLaunchInput.setAccountSettings(isAccountSettings);
        urLaunchInput.enableAddtoBackStack(true);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        FragmentLauncher fragmentLauncher = new FragmentLauncher
                (fragmentActivity,containerID,actionBarListener);
        URInterface urInterface = new URInterface();
        urInterface.launch(fragmentLauncher, urLaunchInput);
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
