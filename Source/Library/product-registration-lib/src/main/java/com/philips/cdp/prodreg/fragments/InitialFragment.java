package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.registration.listener.RegistrationTitleBarListener;
import com.philips.cdp.registration.ui.traditional.RegistrationFragment;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class InitialFragment extends ProdRegBaseFragment {
    Button activity, fragment;

    @Override
    public String getActionbarTitle() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        Log.d(getClass() + "", "onCreate called");
        View view = inflater.inflate(R.layout.initial_fragment, container, false);
        activity = (Button) view.findViewById(R.id.activity);
        fragment = (Button) view.findViewById(R.id.fragment);
        activity.setOnClickListener(onClickActivity());
        fragment.setOnClickListener(onClickFragment());
        return view;
    }

    public View.OnClickListener onClickActivity() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                RegistrationLaunchHelper.launchDefaultRegistrationActivity(getActivity());
            }
        };
    }

    public View.OnClickListener onClickFragment() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                launchRegistrationFragment(R.id.parent_layout, getActivity(), false);
            }
        };
    }

    private void launchRegistrationFragment(int container, FragmentActivity
            fragmentActivity, boolean isAccountSettings) {
        try {
            FragmentManager mFragmentManager = fragmentActivity.getSupportFragmentManager();
            RegistrationFragment registrationFragment = new RegistrationFragment();
            Bundle bundle = new Bundle();
            bundle.putBoolean(RegConstants.ACCOUNT_SETTINGS, isAccountSettings);
            registrationFragment.setArguments(bundle);
            registrationFragment.setOnUpdateTitleListener(new RegistrationTitleBarListener() {
                @Override
                public void updateRegistrationTitle(final int i) {

                }

                @Override
                public void updateRegistrationTitleWithBack(final int i) {

                }

                @Override
                public void updateRegistrationTitleWithOutBack(final int i) {

                }
            });
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.replace(container, registrationFragment,
                    RegConstants.REGISTRATION_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
