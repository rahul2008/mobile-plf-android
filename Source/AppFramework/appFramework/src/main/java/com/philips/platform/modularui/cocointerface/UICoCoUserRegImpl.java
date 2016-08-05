/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.cocointerface;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

public class UICoCoUserRegImpl implements UICoCoInterface,UserRegistrationListener,RegistrationTitleBarListener {
    ImageView arrowImage;
    TextView textView;
    int containerID;
    FragmentActivity fragmentActivity;
    private UICoCoUserRegImpl(){

    }

    private static UICoCoUserRegImpl instance = new UICoCoUserRegImpl();

    public static UICoCoUserRegImpl getInstance(){
        if(null == instance){
            instance = new UICoCoUserRegImpl();
        }
        return instance;
    }
    Context context;
    User userObject;
    public interface SetStateCallBack{
        void setNextState(Context contexts);
    }
    SetStateCallBack setStateCallBack;

    public User getUserObject(Context context) {
        userObject = new User(context);
        return userObject;
    }

    public void registerForNextState(SetStateCallBack setStateCallBack){
        this.setStateCallBack = setStateCallBack;
    }

    public void setFragmentContainer(int containerID){
        this.containerID = containerID;
    }
    @Override
    public void loadPlugIn(Context context) {
        this.context = context;
        RegistrationHelper.getInstance().registerUserRegistrationListener(this);
        initCustomActionBar();

    }

    @Override
    public void runCoCo(Context context) {
        //if(context instanceof UserRegistrationActivity) {
            launchRegistrationFragment(containerID,fragmentActivity, true);
        //}

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
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            RLog.e(RLog.EXCEPTION,
                    "RegistrationActivity :FragmentTransaction Exception occured in addFragment  :"
                            + e.getMessage());
        }
    }
    private void initCustomActionBar() {
        ActionBar mActionBar = this.getSupportActionBar();
        if (mActionBar != null) {
            mActionBar.setDisplayShowHomeEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);
            mActionBar.setDisplayShowCustomEnabled(true);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(//Center the text view in the ActionBar !
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER);
            View mCustomView = LayoutInflater.from(this).inflate(R.layout.af_home_action_bar, null); // layout which contains your button.


            final FrameLayout frameLayout = (FrameLayout) mCustomView.findViewById(R.id.UpButton);
            frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    onBackPressed();
                }
            });
            arrowImage = (ImageView) mCustomView
                    .findViewById(R.id.arrow_left);
            textView = (TextView) mCustomView.findViewById(R.id.action_bar_text);
            //noinspection deprecation
            arrowImage.setBackground(getResources().getDrawable(R.drawable.left_arrow));
            mActionBar.setCustomView(mCustomView, params);
            textView.setText(R.string.af_app_name);
        }
    }

    @Override
    public void unloadCoCo() {
        RegistrationHelper.getInstance().unRegisterUserRegistrationListener(this);

    }
    @Override
    public void setActionbar(ActionbarUpdateListener actionBarClickListener) {

    }

    @Override
    public void setFragActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }
    @Override
    public void updateRegistrationTitle(int titleResourceID) {
        arrowImage.setVisibility(View.VISIBLE);
        textView.setText(R.string.af_app_name);
    }

    @Override
    public void updateRegistrationTitleWithBack(int titleResourceID) {
        arrowImage.setVisibility(View.VISIBLE);
        textView.setText(R.string.af_app_name);


    }

    @Override
    public void updateRegistrationTitleWithOutBack(int titleResourceID) {
        arrowImage.setVisibility(View.INVISIBLE);
        textView.setText(R.string.af_app_name);

    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        if (null != activity) {
            setStateCallBack.setNextState(context);
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
