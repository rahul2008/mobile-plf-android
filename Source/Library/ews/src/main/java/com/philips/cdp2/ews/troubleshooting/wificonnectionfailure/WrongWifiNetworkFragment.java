/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.troubleshooting.wificonnectionfailure;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.FragmentWrongWifiNetworkBinding;
import com.philips.cdp2.ews.homewificonnection.ConnectingDeviceWithWifiFragment;
import com.philips.cdp2.ews.tagging.EWSTagger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.util.BundleUtils;
import com.philips.cdp2.ews.view.BaseFragment;
import com.philips.cdp2.ews.view.EWSActivity;

public class WrongWifiNetworkFragment extends BaseFragment {

    @Nullable
    WrongWifiNetworkViewModel viewModel;
    @Nullable
    FragmentWrongWifiNetworkBinding binding;

    public static Fragment newInstance(@NonNull Bundle bundle) {
        WrongWifiNetworkFragment fragment = new WrongWifiNetworkFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wrong_wifi_network, container, false);
        viewModel = createViewModel();
        String wifiSSID = BundleUtils.extractStringFromBundleOrThrow(getArguments(), ConnectingDeviceWithWifiFragment.HOME_WIFI_SSID);
        viewModel.setDescription(getString(R.string.label_ews_phone_reconnected_different_network_body, wifiSSID, wifiSSID, getString(viewModel.getAppName())));
        viewModel.setBundle(getArguments());
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @NonNull
    private WrongWifiNetworkViewModel createViewModel() {
        return ((EWSActivity) getActivity()).getEWSComponent().wrongWifiNetworkViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        String pageName = getPageName();
        if (pageName != null) {
            EWSTagger.trackPage(pageName);
        }
    }

    @NonNull
    protected String getPageName() {
        return Page.WRONG_WIFI_NETWORK;
    }
}
