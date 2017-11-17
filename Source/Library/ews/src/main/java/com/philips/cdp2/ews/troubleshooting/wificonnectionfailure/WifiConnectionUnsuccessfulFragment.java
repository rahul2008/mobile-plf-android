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
import com.philips.cdp2.ews.base.BaseFragment;
import com.philips.cdp2.ews.databinding.FragmentWifiConnectionUnsuccessfulBinding;
import com.philips.cdp2.ews.util.BundleUtils;

public class WifiConnectionUnsuccessfulFragment extends BaseFragment {
    private final static String WIFI_SSID = "wifi_ssid";
    private final static String DEVICE_NAME = "deviceName";
    @Nullable
    WIFIConnectionUnsuccessfulViewModel viewModel;
    @Nullable
    FragmentWifiConnectionUnsuccessfulBinding binding;

    public static Fragment newInstance(String deviceName, String wifiSSID) {
        WifiConnectionUnsuccessfulFragment fragment = new WifiConnectionUnsuccessfulFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DEVICE_NAME, deviceName);
        bundle.putString(WIFI_SSID, wifiSSID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wifi_connection_unsuccessful, container, false);
        viewModel = createViewModel();
        String deviceName = BundleUtils.extractStringFromBundleOrThrow(getArguments(), DEVICE_NAME);
        String wifiSSID = BundleUtils.extractStringFromBundleOrThrow(getArguments(), WIFI_SSID);
        viewModel.setUpperBody(getString(R.string.label_ews_phone_not_found_on_network_upper_body, deviceName));
        viewModel.setStep2(getString(R.string.label_ews_phone_not_found_on_network_step_2,deviceName));
        viewModel.setUpperHelper(getString(R.string.label_ews_phone_not_found_on_network_upper_helper, deviceName,wifiSSID));
        viewModel.setLowerHelper(getString(R.string.label_ews_phone_not_found_on_network_lower_helper, deviceName));
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @NonNull
    private WIFIConnectionUnsuccessfulViewModel createViewModel() {
        return getEWSComponent().wIFIConnectionUnsuccessfulViewModel();
    }

    @NonNull
    @Override
    protected void callTrackPageName() {
        viewModel.trackPageName();
    }
}
