/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.appframework.introscreen.WelcomeActivity;
import com.philips.platform.modularui.statecontroller.UIState;

public class UserRegistrationState extends UIState implements UserRegistrationListener,RegistrationTitleBarListener {
    Context mContext;
    User userObject;
    int containerID;
    FragmentActivity fragmentActivity;

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
        launchRegistrationFragment(containerID,fragmentActivity, true);
    }
    /**
     * Launch registration fragment
     */
    private void launchRegistrationFragment(int container, FragmentActivity fragmentActivity, boolean isAccountSettings ) {
        try {
            FragmentManager mFragmentManager = fragmentActivity.getSupportFragmentManager();
            RegistrationFragment registrationFragment = new RegistrationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, isAccountSettings);
            registrationFragment.setArguments(bundle);
            registrationFragment.setOnUpdateTitleListener(this);
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(container, registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationActivity :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
    }

    /*
    Callbacks from interface implemented
     */
    @Override
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
