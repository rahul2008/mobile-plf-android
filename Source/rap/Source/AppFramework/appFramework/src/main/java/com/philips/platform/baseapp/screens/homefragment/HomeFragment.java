/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.screens.homefragment;

import android.app.Dialog;
import android.app.admin.DevicePolicyManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseFragment;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;

/**
 * This is the home fragment the main landing page of the application , once onboarding is completed.
 * All the fragments are added on top of this , handleBack event from all other fragemnts ends up  landing here
 */

public class HomeFragment extends AbstractAppFrameworkBaseFragment {
    public static final String TAG = HomeFragment.class.getSimpleName();
    private static final String JAIL_BROKEN_ENABLED = "JAIL_BROKEN";
    private static final String SCREEN_LOCK_DISABLED = "SCREEN_LOCK";
    private static final String JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED = "JAIL_BROKEN_SCREEN_LOCK";


    @BindView(R.id.af_home_image)
    ImageView homeImageView;

    public HomeFragment() {
    }

    @Override
    public void onResume() {
        RALog.d(TAG, " OnResume Called ");
        super.onResume();
        startAppTagging(TAG);
        ((AbstractAppFrameworkBaseActivity) getActivity()).updateActionBarIcon(false);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.RA_HomeScreen_Title);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialiseSecurityDialog();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.af_home_fragment, container, false);

        ButterKnife.bind(this, rootView);
        setDateToView();
        setHasOptionsMenu(true);
        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.menu_theme_settings).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void setImageSize() {
        Drawable d = getResources().getDrawable(R.drawable.ref_app_home_page, getActivity().getTheme());
        int h = d.getIntrinsicHeight();
        int w = d.getIntrinsicWidth();

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        homeImageView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (size.x * h) / w));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE || newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setImageSize();
        }
    }

    protected void initialiseSecurityDialog() {
        boolean isUrLoginSuccess = getPreferences().getBoolean(Constants.UR_LOGIN_COMPLETED, false);
        if(isUrLoginSuccess) {
            setUrCompleted();
        }
        createDialog();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setImageSize();
    }

    private void setDateToView() {
        Bundle bundle = getArguments();
    }

    protected boolean createDialog() {
        {
            boolean isScreenLockDisabled = !isScreenLockEnabled();
            boolean isDeviceRooted = isDeviceRooted();

            if (getDoNotShowValue(JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED)) {
                return false;
            }
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.af_custom_dialog_security);
            dialog.setTitle(getActivity().getString(R.string.RA_SECURITY_SECURE_YOUR_DATA));
            TextView dialogDescTextView = dialog.findViewById(R.id.textDesc);
            final CheckBox checkBox = dialog.findViewById(R.id.checkBox);
            Button btnActivateScreen = dialog.findViewById(R.id.btnActivateScreen);
            Button btnNoThanks = dialog.findViewById(R.id.btnNoThanks);
            checkBox.setText(getActivity().getString(R.string.RA_SECURITY_DONT_SHOW_MESSAGE));

            if (isCodeTampered()) {
                dialogDescTextView.setText(getActivity().getString(R.string.RA_Code_Tampered));
                activateScreenLockListener(btnActivateScreen, dialog);
                btnActivateScreen.setVisibility(View.VISIBLE);
                checkBoxListener(checkBox, JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED);
                noThanksClickListener(btnNoThanks, dialog);
                dialog.show();
            } else if (isScreenLockDisabled && isDeviceRooted &&
                    !getDoNotShowValue(JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED)) {
                dialogDescTextView.setText(getActivity().getString(R.string.RA_SECURITY_PASSCODE_AND_JAILBREAK_VIOLATION));
                activateScreenLockListener(btnActivateScreen, dialog);
                btnActivateScreen.setVisibility(View.VISIBLE);
                checkBoxListener(checkBox, JAIL_BROKEN_ENABLED_AND_SCREEN_LOCK_DISABLED);
                noThanksClickListener(btnNoThanks, dialog);
                dialog.show();
            } else if (isDeviceRooted && !getDoNotShowValue(JAIL_BROKEN_ENABLED)) {
                dialogDescTextView.setText(getActivity().getString(R.string.RA_SECURITY_JAILBREAK_VIOLATION));
                btnActivateScreen.setVisibility(View.GONE);
                checkBoxListener(checkBox, JAIL_BROKEN_ENABLED);
                noThanksClickListener(btnNoThanks, dialog);
                dialog.show();
            } else if (isScreenLockDisabled && !getDoNotShowValue(SCREEN_LOCK_DISABLED)) {
                dialogDescTextView.setText(getActivity().getString(R.string.RA_SECURITY_SCREEN_LOCK));
                btnActivateScreen.setVisibility(View.VISIBLE);
                activateScreenLockListener(btnActivateScreen, dialog);
                checkBoxListener(checkBox, SCREEN_LOCK_DISABLED);
                noThanksClickListener(btnNoThanks, dialog);
                dialog.show();
            }
        }
        return true;
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
    protected Boolean isScreenLockEnabled() {
        return getApplicationContext().getAppInfra().getSecureStorage().deviceHasPasscode();
    }

    @NonNull
    protected Boolean isDeviceRooted() {
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
        editor.apply();
    }

    protected boolean getDoNotShowValue(String key) {
        return getPreferences().getBoolean(key, false);
    }

    protected SharedPreferences getPreferences() {
        return getActivity().getSharedPreferences(Constants.SHARED_PREF, MODE_PRIVATE);
    }

    protected void setUrCompleted() {
        // Making is false because only after login only this dialog has to be shown.
        SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(Constants.UR_LOGIN_COMPLETED, false);
        editor.apply();
    }

    public boolean isCodeTampered() {
        return getApplicationContext().getAppInfra().getSecureStorage().isCodeTampered();
    }
}
