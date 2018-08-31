/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.ews.startconnectwithdevice;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.philips.platform.ews.EWSActivity;
import com.philips.platform.ews.R;
import com.philips.platform.ews.base.BaseFragment;
import com.philips.platform.ews.configuration.BaseContentConfiguration;
import com.philips.platform.ews.confirmwifi.ConfirmWifiNetworkViewModel;
import com.philips.platform.ews.databinding.FragmentStartConnectWithDeviceBinding;
import com.philips.platform.ews.dialog.EWSAlertDialogFragment;
import com.philips.platform.ews.tagging.EWSTagger;
import com.philips.platform.ews.tagging.Page;
import com.philips.platform.ews.util.DialogUtils;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Label;

import java.util.Locale;

public class StartConnectWithDeviceFragment extends BaseFragment implements ConfirmWifiNetworkViewModel.ViewCallback {

    private StartConnectWithDeviceViewModel viewModel;

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentStartConnectWithDeviceBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_start_connect_with_device, container, false);
        viewModel = createViewModel();
        viewModel.setViewCallback(this);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @NonNull
    private StartConnectWithDeviceViewModel createViewModel() {
        return getEWSComponent().ewsGettingStartedViewModel();
    }

    @Override
    protected void callTrackPageName() {
        viewModel.trackPageName();
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
