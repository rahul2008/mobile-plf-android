package com.philips.cdp.prodreg.fragment;

import android.content.Context;
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
import com.philips.cdp.prodreg.Util;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class LaunchFragment extends Fragment implements View.OnClickListener {

    private TextView txt_title, configurationTextView;
    private Button user_registration_button, pr_button, reg_list_button;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        setUp(view);
        return view;
    }

    private void setUp(final View view) {
        context = getActivity();
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
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        user_registration_button = (Button) view.findViewById(R.id.btn_user_registration);
        pr_button = (Button) view.findViewById(R.id.btn_product_registration);
        reg_list_button = (Button) view.findViewById(R.id.btn_register_list);
        configurationTextView = (TextView) view.findViewById(R.id.configuration);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_user_registration:
                RegistrationHelper.getInstance().getAppTaggingInterface().setPreviousPage("demoapp:home");
                RegistrationLaunchHelper.launchRegistrationActivityWithAccountSettings(context);
                Util.navigateFromUserRegistration();
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

    private void showFragment(final Fragment fragment, final String TAG) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.parent_layout, fragment,
                TAG);
        fragmentTransaction.addToBackStack(TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
