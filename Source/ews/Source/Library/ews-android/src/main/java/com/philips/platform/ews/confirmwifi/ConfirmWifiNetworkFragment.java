/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.confirmwifi;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.philips.platform.ews.R;
import com.philips.platform.ews.base.BaseFragment;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.databinding.FragmentConfirmWifiNetworkBinding;
import com.philips.platform.ews.dialog.EWSAlertDialogFragment;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.util.DialogUtils;

public class ConfirmWifiNetworkFragment extends BaseFragment
        implements ConfirmWifiNetworkViewModel.ViewCallback {

    @VisibleForTesting
    ConfirmWifiNetworkViewModel viewModel;


    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentConfirmWifiNetworkBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_confirm_wifi_network, container, false);
        viewModel = createViewModel();
        viewModel.setViewCallback(this);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @VisibleForTesting
    @NonNull
    ConfirmWifiNetworkViewModel createViewModel() {
        return getEWSComponent().confirmWifiNetworkViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refresh();
    }

    @Override
    protected void callTrackPageName() {
        viewModel.trackPageName();
    }

    @Override
    public void onDestroyView() {
        viewModel.setViewCallback(null);
        super.onDestroyView();
    }

    @Override
    public void showTroubleshootHomeWifiDialog(@NonNull BaseContentConfiguration baseContentConfiguration, @NonNull final EWSTagger ewsTagger) {
        final EWSAlertDialogFragment alertDialogFragment = (EWSAlertDialogFragment) DialogUtils.getInstance().presentTroubleshootHomeWifiDialog(getContext(), getChildFragmentManager(), baseContentConfiguration, ewsTagger);
        ImageView imageView = alertDialogFragment.getDialog().getWindow().findViewById(R.id.ic_close);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callTrackPageName();
                alertDialogFragment.dismiss();
                getChildFragmentManager().popBackStackImmediate();
                viewModel.refresh();
            }
        });
    }
}
