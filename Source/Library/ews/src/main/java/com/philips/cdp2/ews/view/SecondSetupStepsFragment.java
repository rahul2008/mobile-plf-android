/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.FragmentSecondSetupStepsBinding;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.viewmodel.SecondSetupStepsViewModel;

public class SecondSetupStepsFragment extends BaseFragment {

    public static final int LOCATION_PERMISSIONS_REQUEST_CODE = 10;

    private SecondSetupStepsViewModel viewModel;
    private boolean pendingPermissionResultRequest;

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentSecondSetupStepsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second_setup_steps, container, false);
        viewModel = createViewModel();
        viewModel.setFragment(this);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @NonNull
    private SecondSetupStepsViewModel createViewModel() {
        return ((EWSActivity) getActivity()).getEWSComponent().secondSetupStepsViewModel();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSIONS_REQUEST_CODE) {
            pendingPermissionResultRequest = viewModel.areAllPermissionsGranted(grantResults);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pendingPermissionResultRequest) {
            pendingPermissionResultRequest = false;
            viewModel.connectPhoneToDeviceHotspotWifi();
        }
    }

    @NonNull
    @Override
    protected String getPageName() {
        return Page.SETUP_STEP2;
    }
}