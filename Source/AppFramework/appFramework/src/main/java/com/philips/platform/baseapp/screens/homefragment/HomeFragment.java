/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.homefragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.philips.cdp.registration.listener.UserRegistrationListener;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkBaseFragment;
import com.philips.platform.baseapp.screens.utility.RALog;

/**
 * This is the home fragment the main landing page of the application , once onboarding is completed.
 * All the fragments are added on top of this , handleBack event from all other fragemnts ends up  landing here
 */

public class HomeFragment extends AppFrameworkBaseFragment implements UserRegistrationUIEventListener {
    public static final String TAG = HomeFragment.class.getSimpleName();
    private static final String JAIL_BROKEN_ENABLED = "JAIL_BROKEN";
    private static final String SCREEN_LOCK_DISABLED = "SCREEN_LOCK";
    private static final String JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED = "JAIL_BROKEN_SCREEN_LOCK";

    public HomeFragment() {
    }

    @Override
    public void onResume() {
        RALog.d(TAG, " OnResume Called ");
        super.onResume();
        ((AppFrameworkBaseActivity) getActivity()).updateActionBarIcon(false);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.RA_HomeScreen_Title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.af_home_fragment, container, false);
        setDateToView();
        startAppTagging(TAG);
        return rootView;
    }


    private void setDateToView() {
        Bundle bundle = getArguments();
    }

    @Override
    public void onUserRegistrationComplete(Activity activity) {
        createDialog();
    }

    @Override
    public void onPrivacyPolicyClick(Activity activity) {

    }

    @Override
    public void onTermsAndConditionClick(Activity activity) {

    }

    protected void createDialog() {
        {
            boolean isScreenLockDisabled = !isScreenLockEnabled();
            boolean isDeviceRooted = isDeviceRooted();
            if(getDoNotShowValue(JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED)) {
                return;
            }
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.af_custom_dialog_security);
            dialog.setTitle(getActivity().getString(R.string.RA_SECURITY_SECURE_YOUR_DATA));
            TextView dialogDescTextView = (TextView)dialog.findViewById(R.id.textDesc);
            final CheckBox checkBox = (CheckBox) dialog.findViewById(R.id.checkBox);
            Button btnActivateScreen = (Button) dialog.findViewById(R.id.btnActivateScreen);
            Button btnNoThanks = (Button) dialog.findViewById(R.id.btnNoThanks);
            checkBox.setText(getActivity().getString(R.string.RA_SECURITY_DONT_SHOW_MESSAGE));

            if(isScreenLockDisabled && isDeviceRooted &&
                    !getDoNotShowValue(JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED)) {
                dialogDescTextView.setText(getActivity().getString(R.string.RA_SECURITY_PASSCODE_AND_JAILBREAK_VIOLATION));
                activateScreenLockListener(btnActivateScreen, dialog);
                btnActivateScreen.setVisibility(View.VISIBLE);
                checkBoxListener(checkBox, JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED);
            }
            else if(isDeviceRooted && !getDoNotShowValue(JAIL_BROKEN_ENABLED)) {
                dialogDescTextView.setText(getActivity().getString(R.string.RA_SECURITY_JAILBREAK_VIOLATION));
                btnActivateScreen.setVisibility(View.GONE);
                checkBoxListener(checkBox, JAIL_BROKEN_ENABLED);
            }
            else if(isScreenLockDisabled && !getDoNotShowValue(SCREEN_LOCK_DISABLED)) {
                dialogDescTextView.setText(getActivity().getString(R.string.RA_SECURITY_SCREEN_LOCK));
                btnActivateScreen.setVisibility(View.VISIBLE);
                activateScreenLockListener(btnActivateScreen, dialog);
                checkBoxListener(checkBox, SCREEN_LOCK_DISABLED);
            }
            noThanksClickListener(btnNoThanks, dialog);
            dialog.show();
        }
    }

    protected void checkBoxListener(CheckBox checkBox, final String key) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(key.equalsIgnoreCase(JAIL_BROKEN_ENABLED) || key.equalsIgnoreCase(SCREEN_LOCK_DISABLED)) {
                    setDoNotShowValue(JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED, false);
                }
                setDoNotShowValue(key, b);
            }
        });
    }

    @NonNull
    private Boolean isScreenLockEnabled() {
        return getApplicationContext().getAppInfra().getSecureStorage().deviceHasPasscode();
    }

    @NonNull
    private Boolean isDeviceRooted() {
        String isDeviceRooted = getApplicationContext().getAppInfra().getSecureStorage().getDeviceCapability();
        return Boolean.parseBoolean(isDeviceRooted);
    }

    private AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) getActivity().getApplication();
    }

    protected void activateScreenLockListener(Button btnActivateScreen, final Dialog dialog) {
        btnActivateScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
                getActivity().startActivity(intent);
            }
        });
    }

    protected void noThanksClickListener(Button btnNoThanks, final Dialog dialog) {
        btnNoThanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    protected void setDoNotShowValue(String key, Boolean value) {
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    protected boolean getDoNotShowValue(String key) {
        return getPreferences().getBoolean(key, false);
    }

    private SharedPreferences getPreferences() {
        return getActivity().getPreferences(Activity.MODE_PRIVATE);
    }
}
