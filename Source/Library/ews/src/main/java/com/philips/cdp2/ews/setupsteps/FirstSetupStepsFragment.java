/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.setupsteps;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.base.BaseFragment;
import com.philips.cdp2.ews.databinding.FragmentFirstSetupStepsBinding;
import com.philips.cdp2.ews.injections.AppModule;
import com.philips.cdp2.ews.injections.DaggerEWSComponent;
import com.philips.cdp2.ews.injections.EWSConfigurationModule;
import com.philips.cdp2.ews.injections.EWSModule;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;

public class FirstSetupStepsFragment extends BaseFragment {

    private FirstSetupStepsViewModel viewModel;


    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentFirstSetupStepsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_first_setup_steps, container, false);
        viewModel = createViewModel();
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @NonNull
    private FirstSetupStepsViewModel createViewModel() {
        return getEWSComponent().firstSetupStepsViewModel();
    }

    @Override
    protected void callTrackPageName() {
        viewModel.trackPageName();
    }
}