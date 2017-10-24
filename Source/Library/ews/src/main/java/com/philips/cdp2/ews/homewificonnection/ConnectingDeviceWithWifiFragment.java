/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.homewificonnection;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.util.BundleUtils;
import com.philips.cdp2.ews.view.BaseFragment;
import com.philips.cdp2.ews.view.EWSActivity;

public class ConnectingDeviceWithWifiFragment extends BaseFragment implements ConnectingDeviceWithWifiViewModel.ConnectingDeviceToWifiCallback {
    public final static String HOME_WIFI_SSID = "homeWiFiSSID";
    public final static String APP_NAME = "appName";
    private final static String HOME_WIFI_PWD = "homeWiFiPassword";
    private final static String DEVICE_NAME = "deviceName";
    @Nullable
    ConnectingDeviceWithWifiViewModel viewModel;

    public static Fragment newInstance(@Nullable String homeWiFiSSID, @Nullable String homeWiFiPassword, @Nullable String deviceName) {
        Bundle data = new Bundle();
        data.putString(HOME_WIFI_SSID, homeWiFiSSID);
        data.putString(HOME_WIFI_PWD, homeWiFiPassword);
        data.putString(DEVICE_NAME, deviceName);
        ConnectingDeviceWithWifiFragment fragment = new ConnectingDeviceWithWifiFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public static Fragment newInstance(@Nullable Bundle bundle) {
        Bundle data = new Bundle();
        data.putString(HOME_WIFI_SSID, BundleUtils.extractStringFromBundleOrThrow(bundle, HOME_WIFI_SSID));
        data.putString(HOME_WIFI_PWD, BundleUtils.extractStringFromBundleOrThrow(bundle, HOME_WIFI_PWD));
        data.putString(DEVICE_NAME, BundleUtils.extractStringFromBundleOrThrow(bundle, DEVICE_NAME));
        ConnectingDeviceWithWifiFragment fragment = new ConnectingDeviceWithWifiFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EWSActivity activity = (EWSActivity) getActivity();
        activity.hideCloseButton();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        String homeWiFiSSID = BundleUtils.extractStringFromBundleOrThrow(getBundle(), HOME_WIFI_SSID);
        if (viewModel == null) {
            viewModel = createViewModel();
            viewModel.startConnecting(
                    homeWiFiSSID, BundleUtils.extractStringFromBundleOrThrow(getBundle(), HOME_WIFI_PWD),
                    BundleUtils.extractStringFromBundleOrThrow(getBundle(), DEVICE_NAME));
        } else {
            viewModel.connectToHomeWifi(homeWiFiSSID);
        }
        viewModel.setFragmentCallback(this);

        return inflater.inflate(R.layout.fragment_connecting_phone_to_hotspot_layout, container,
                false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (viewModel != null) {
                    viewModel.onCancelButtonClicked();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (viewModel != null) {
            viewModel.clear();
        }
    }

    @NonNull
    private ConnectingDeviceWithWifiViewModel createViewModel() {
        return ((EWSActivity) getActivity()).getEWSComponent().connectingDeviceWithWifiViewModel();
    }

    @Override
    public void registerReceiver(@NonNull BroadcastReceiver receiver, @NonNull IntentFilter filter) {
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver(@NonNull BroadcastReceiver receiver) {
        try {
            getActivity().unregisterReceiver(receiver);
        } catch (IllegalArgumentException ignored) {
        }
    }

    @Override
    public Bundle getBundle() {
        return getArguments();
    }

    @Override
    public void showCancelDialog() {
        handleCancelButtonClicked();
    }

    @Override
    public boolean handleBackEvent() {
        // Do nothing, back disabled in this screen
        return true;
    }
}
