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

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.base.BaseFragment;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.databinding.FragmentSecondSetupStepsBinding;
import com.philips.cdp2.ews.dialog.EWSAlertDialogFragment;
import com.philips.cdp2.ews.injections.AppModule;
import com.philips.cdp2.ews.injections.DaggerEWSComponent;
import com.philips.cdp2.ews.injections.EWSConfigurationModule;
import com.philips.cdp2.ews.injections.EWSModule;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;
import com.philips.cdp2.ews.setupsteps.SecondSetupStepsViewModel.LocationPermissionFlowCallback;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

public class SecondSetupStepsFragment extends BaseFragment implements LocationPermissionFlowCallback {

    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 10;

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
        return DaggerEWSComponent.builder()
                .eWSModule(new EWSModule(this.getActivity()
                        , EWSLauncherInput.getFragmentManager()
                        , EWSLauncherInput.getContainerFrameId(), AppModule.getCommCentral()))
                .eWSConfigurationModule(new EWSConfigurationModule(this.getActivity(), AppModule.getContentConfiguration()))
                .build().secondSetupStepsViewModel();
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
        alertDialogFragment.setDialogLifeCycleListener(new EWSAlertDialogFragment.DialogLifeCycleListener() {
            @Override
            public void onStart() {
                if (viewModel != null) {
                    viewModel.tagLocationPermission();
                }
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
                        viewModel.tagLocationPermissionAllow();
                        requestPermissions(new String[]{SecondSetupStepsViewModel.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSIONS_REQUEST_CODE);
                    }
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel != null) {
                    viewModel.tagLocationPermissionCancel();
                }
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
        alertDialogFragment.setDialogLifeCycleListener(new EWSAlertDialogFragment.DialogLifeCycleListener() {
            @Override
            public void onStart() {
                if (viewModel != null) {
                    viewModel.tagLocationDisabled();
                }
            }
        });
        alertDialogFragment.showAllowingStateLoss(getChildFragmentManager(), AlertDialogFragment.class.getCanonicalName());
        getChildFragmentManager().executePendingTransactions();
        ((Label) view.findViewById(R.id.ews_02_02b_enable_location_body)).setText(getString(R.string.label_ews_location_services_body, getString(baseContentConfiguration.getDeviceName())));
        view.findViewById(R.id.ews_02_02b_enable_location_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel != null) {
                    viewModel.tagLocationOpenSettings();
                    getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    alertDialogFragment.dismiss();
                }
            }
        });
    }
}