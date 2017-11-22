/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.setupsteps;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.dialog.EWSAlertDialogFragment;
import com.philips.cdp2.ews.setupsteps.SecondSetupStepsViewModel.LocationPermissionFlowInterface;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.base.BaseFragment;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.databinding.FragmentSecondSetupStepsBinding;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

public class SecondSetupStepsFragment extends BaseFragment implements LocationPermissionFlowInterface {

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
        viewModel.setFragmentCallback(this);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @NonNull
    private SecondSetupStepsViewModel createViewModel() {
        return getEWSComponent().secondSetupStepsViewModel();
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

    @Override
    protected void callTrackPageName() {
        viewModel.trackPageName();
    }

    @Override
    public void showGPSEnableDialog(@NonNull BaseContentConfiguration baseContentConfiguration) {
        Context context = getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.enable_gps_settings,
                null, false);

        AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(context)
                .setDialogView(view)
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDimLayer(DialogConstants.DIM_STRONG)
                .setCancelable(true);
        final EWSAlertDialogFragment alertDialogFragment = (EWSAlertDialogFragment) builder.create(new EWSAlertDialogFragment());
        alertDialogFragment.setFragmentLifeCycleListener(new EWSAlertDialogFragment.FragmentLifeCycleListener() {
            @Override
            public void onStart() {

            }

            @Override
            public void onStop() {

            }

            @Override
            public void onDismiss(DialogInterface dialog) {

            }

            @Override
            public void onCancel(DialogInterface dialog) {

            }

            @Override
            public void onActivityCreated(Bundle savedInstanceState) {

            }
        });
        alertDialogFragment.showAllowingStateLoss(getChildFragmentManager(), AlertDialogFragment.class.getCanonicalName());
        getChildFragmentManager().executePendingTransactions();

        Button yesButton = view.findViewById(R.id.ews_04_02_button_cancel_setup_yes);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel != null) {
                    getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    alertDialogFragment.dismiss();
                }
            }
        });
    }
}