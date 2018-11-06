/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.setupsteps;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.philips.platform.ews.R;
import com.philips.platform.ews.base.BaseFragment;
import com.philips.platform.ews.databinding.FragmentSecondSetupStepsBinding;

public class SecondSetupStepsFragment extends BaseFragment {

    private SecondSetupStepsViewModel viewModel;

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentSecondSetupStepsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second_setup_steps, container, false);
        viewModel = createViewModel();
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @NonNull
    private SecondSetupStepsViewModel createViewModel() {
        return getEWSComponent().secondSetupStepsViewModel();
    }

    @Override
    protected void callTrackPageName() {
        viewModel.trackPageName();
    }
}