/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.setupsteps;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.base.BaseFragment;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.databinding.FragmentSecondSetupStepsBinding;
import com.philips.cdp2.ews.dialog.EWSAlertDialogFragment;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class SecondSetupStepsFragment extends BaseFragment implements SecondSetupStepsViewModel.LocationPermissionFlowCallback {

    public static final int LOCATION_PERMISSIONS_REQUEST_CODE = 10;

    private SecondSetupStepsViewModel viewModel;
    private boolean pendingPermissionResultRequest;

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentSecondSetupStepsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_second_setup_steps, container, false);
        viewModel = createViewModel();
        viewModel.setLocationPermissionFlowCallback(this);
        viewModel.setFragment(this);
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
    public void showLocationPermissionDialog(@NonNull BaseContentConfiguration baseContentConfiguration) {
        Context context = getContext();
        View view = LayoutInflater.from(context).cloneInContext(UIDHelper.getPopupThemedContext(context)).inflate(R.layout.ews_location_permission,
                null, false);

        EWSAlertDialogFragment.Builder builder = new EWSAlertDialogFragment.Builder(context)
                .setDialogView(view)
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDimLayer(DialogConstants.DIM_STRONG)
                .setCancelable(false);
        final EWSAlertDialogFragment alertDialogFragment = (EWSAlertDialogFragment) builder.create(new EWSAlertDialogFragment());
        alertDialogFragment.setFragmentLifeCycleListener(new EWSAlertDialogFragment.FragmentLifeCycleListener() {
            @Override
            public void onStart() {
                //todo: add analytics tag : EWSTagger.trackPage("");
            }
        });
        alertDialogFragment.showAllowingStateLoss(getChildFragmentManager(), AlertDialogFragment.class.getCanonicalName());
        getChildFragmentManager().executePendingTransactions();
        ((Label) view.findViewById(R.id.ews_label_body)).setText(getString(R.string.label_ews_location_permission_body, getString(baseContentConfiguration.getDeviceName())));

        Button yesButton = view.findViewById(R.id.ews_button_yes);
        Button noButton = view.findViewById(R.id.ews_button_no);
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel != null) {
                    alertDialogFragment.dismiss();
                    if (isAdded()) {
                        requestPermissions(new String[]{ACCESS_COARSE_LOCATION}, LOCATION_PERMISSIONS_REQUEST_CODE);
                    }
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getActivity().finish();
            }
        });
    }

    @Override
    public void showGPSEnableDialog(@NonNull BaseContentConfiguration baseContentConfiguration) {
        Context context = getContext();
        View view = LayoutInflater.from(context).cloneInContext(UIDHelper.getPopupThemedContext(context)).inflate(R.layout.enable_gps_settings,
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
                //todo: add analytics tag : EWSTagger.trackPage("");
            }
        });
        alertDialogFragment.showAllowingStateLoss(getChildFragmentManager(), AlertDialogFragment.class.getCanonicalName());
        getChildFragmentManager().executePendingTransactions();
        ((Label) view.findViewById(R.id.ews_verify_device_body)).setText(getString(R.string.label_ews_enable_gps_settings_body, getString(baseContentConfiguration.getDeviceName())));
        view.findViewById(R.id.ews_04_02_button_cancel_setup_yes).setOnClickListener(new View.OnClickListener() {
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