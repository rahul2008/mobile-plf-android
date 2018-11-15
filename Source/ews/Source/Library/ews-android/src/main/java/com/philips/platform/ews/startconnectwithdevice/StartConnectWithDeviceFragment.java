/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.startconnectwithdevice;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.philips.platform.ews.EWSActivity;
import com.philips.platform.ews.R;
import com.philips.platform.ews.base.BaseFragment;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.databinding.FragmentStartConnectWithDeviceBinding;
import com.philips.platform.ews.dialog.EWSAlertDialogFragment;
import com.philips.platform.ews.microapp.EwsResultListener;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.tagging.Page;
import com.philips.platform.ews.util.DialogUtils;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import java.util.Locale;

public class StartConnectWithDeviceFragment extends BaseFragment implements StartConnectWithDeviceViewModel.ViewCallback, StartConnectWithDeviceViewModel.LocationPermissionFlowCallback {

    private StartConnectWithDeviceViewModel viewModel;
    private static final int LOCATION_PERMISSIONS_REQUEST_CODE = 10;
    private boolean pendingPermissionResultRequest;
    private EwsResultListener ewsResultListener;

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentStartConnectWithDeviceBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_start_connect_with_device, container, false);
        viewModel = createViewModel();
        viewModel.setViewCallback(this);
        viewModel.setLocationPermissionFlowCallback(this);
        viewModel.setFragment(this);
        binding.setViewModel(viewModel);
        binding.getRoot().setFocusableInTouchMode(true);
        binding.getRoot().requestFocus();
        binding.getRoot().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    ewsResultListener = viewModel.getEwsResultListener();
                    performEWSCancel(ewsResultListener);
                }
                return false;
            }
        });
        return binding.getRoot();
    }

    @NonNull
    private StartConnectWithDeviceViewModel createViewModel() {
        return getEWSComponent().ewsGettingStartedViewModel();
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
            viewModel.onGettingStartedButtonClicked();
        }
    }

    public void performEWSCancel(EwsResultListener ewsResultListener) {
        if(ewsResultListener == null) {
            try {
                ewsResultListener = ((EwsResultListener) getContext());
            } catch (ClassCastException ignored) {
            }
            if (ewsResultListener != null) {
                ewsResultListener.onEWSCancelled();
            }
        } else {
            ewsResultListener.onEWSCancelled();
        }
    }

    @Override
    protected void callTrackPageName() {
        viewModel.trackPageName();
    }

    @Override
    public void showTroubleshootHomeWifiDialog(@NonNull BaseContentConfiguration baseContentConfiguration, @NonNull final EWSTagger ewsTagger) {
        final EWSAlertDialogFragment alertDialogFragment = (EWSAlertDialogFragment) DialogUtils.presentTroubleshootHomeWifiDialog(getContext(), getChildFragmentManager(), baseContentConfiguration, ewsTagger);
        ImageView imageView = alertDialogFragment.getDialog().getWindow().findViewById(R.id.ic_close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callTrackPageName();
                alertDialogFragment.dismiss();
                getChildFragmentManager().popBackStackImmediate();
            }
        });
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
                        requestPermissions(new String[]{StartConnectWithDeviceViewModel.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSIONS_REQUEST_CODE);
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

    @Override
    public void onDestroy() {
        if (!(getActivity() instanceof EWSActivity)) {
            if (viewModel != null) {
                viewModel.onDestroy();
            }
        }
        super.onDestroy();
    }
}