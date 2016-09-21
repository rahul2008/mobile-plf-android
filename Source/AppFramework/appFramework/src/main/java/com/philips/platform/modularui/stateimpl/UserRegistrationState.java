/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.StringRes;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.modularui.statecontroller.CoCoListener;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

public class UserRegistrationState extends UIState implements UserRegistrationListener ,ActionBarListener ,UserRegistrationUIEventListener {
    private Context mContext;
    private User userObject;
    private CoCoListener userRegistrationListener;
    private FragmentLauncher fragmentLauncher;

    @Override
    public void init(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        if (null != activity) {
            userRegistrationListener.coCoCallBack(mContext);
        }

    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

    }

    public User getUserObject(Context context) {
        userObject = new User(context);
        return userObject;
    }

    public void registerForNextState(CoCoListener setStateCallBack){
        this.userRegistrationListener = (CoCoListener) getPresenter();
    }

    public UserRegistrationState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        mContext = context;
        loadPlugIn();
        runUserRegistration();
    }

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBack();
    }

    private void loadPlugIn(){
        userObject = new User(mContext);
        userObject.registerUserRegistrationListener(this);
    }

    private void runUserRegistration(){
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
