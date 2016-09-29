package com.philips.cdp.prodreg.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.philips.cdp.prodreg.R;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.URInterface;
import com.philips.cdp.registration.ui.utils.URLaunchInput;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LaunchFragment extends Fragment implements View.OnClickListener {

    private TextView configurationTextView;
    private Button user_registration_button, pr_button, reg_list_button;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        setUp(view);
        return view;
    }

    private void setUp(final View view) {
        initViews(view);
        setOnClickListeners();
    }

    @NonNull
    public void setOnClickListeners() {
        user_registration_button.setOnClickListener(this);
        pr_button.setOnClickListener(this);
        reg_list_button.setOnClickListener(this);
    }

    private void initViews(final View view) {
        user_registration_button = (Button) view.findViewById(R.id.btn_user_registration);
        pr_button = (Button) view.findViewById(R.id.btn_product_registration);
        reg_list_button = (Button) view.findViewById(R.id.btn_register_list);
        configurationTextView = (TextView) view.findViewById(R.id.configuration);
        configurationTextView.setText(RegistrationConfiguration.getInstance().getRegistrationEnvironment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_registration:
                launchUserRegistration();
                break;
            case R.id.btn_product_registration:
                showFragment(new ManualRegistrationFragment(), ManualRegistrationFragment.TAG);
                break;
            case R.id.btn_register_list:
                showFragment(new ProductListFragment(), ProductListFragment.TAG);
                break;
            default:
                break;
        }
    }

    private void launchUserRegistration() {
        URLaunchInput urLaunchInput;
        ActivityLauncher activityLauncher;
        URInterface urInterface;
        RegistrationHelper.getInstance().getAppTaggingInterface().setPreviousPage("demoapp:home");
        urLaunchInput = new URLaunchInput();
        urLaunchInput.setAccountSettings(true);
        urLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        urLaunchInput.setUserRegistrationUIEventListener(new UserRegistrationUIEventListener() {
            @Override
            public void onUserRegistrationComplete(final Activity activity) {
                activity.finish();
            }

            @Override
            public void onPrivacyPolicyClick(final Activity activity) {

            }

            @Override
            public void onTermsAndConditionClick(final Activity activity) {

            }
        });
        activityLauncher = new ActivityLauncher(ActivityLauncher.
                ActivityOrientation.SCREEN_ORIENTATION_SENSOR, 0);

        urInterface = new URInterface();
        urInterface.launch(activityLauncher, urLaunchInput);
    }

    private void showFragment(final Fragment fragment, final String TAG) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.parent_layout, fragment,
                TAG);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
