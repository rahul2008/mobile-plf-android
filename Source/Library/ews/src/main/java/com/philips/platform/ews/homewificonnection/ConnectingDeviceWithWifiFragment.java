/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.platform.ews.homewificonnection;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ews.R;
import com.philips.platform.ews.base.BaseFragment;
import com.philips.platform.ews.databinding.FragmentConnectingDeviceWithWifiBinding;
import com.philips.platform.ews.microapp.EWSActionBarListener;
import com.philips.platform.ews.util.BundleUtils;

public class ConnectingDeviceWithWifiFragment extends BaseFragment
        implements ConnectingDeviceWithWifiViewModel.ConnectingDeviceToWifiCallback {

    public final static String HOME_WIFI_SSID = "homeWiFiSSID";
    public final static String DEVICE_NAME = "deviceName";
    private final static String TAG = "ConnectingDeviceWithWifiFragment";
    private final static String HOME_WIFI_PWD = "homeWiFiPassword";
    private final static String DEVICE_FRIENDLY_NAME = "deviceFriendlyName";
    private final static String FROM_WRONG_WIFI_SCREEN = "fromWrongWifiScreen";
    @Nullable
    private ConnectingDeviceWithWifiViewModel viewModel;

    public static Fragment newInstance(@NonNull String homeWiFiSSID,
                                       @NonNull String homeWiFiPassword,
                                       @NonNull String deviceName,
                                       @NonNull String deviceFriendlyName) {
        Bundle data = new Bundle();
        data.putString(HOME_WIFI_SSID, homeWiFiSSID);
        data.putString(HOME_WIFI_PWD, homeWiFiPassword);
        data.putString(DEVICE_NAME, deviceName);
        data.putString(DEVICE_FRIENDLY_NAME, deviceFriendlyName);
        data.putBoolean(FROM_WRONG_WIFI_SCREEN, false);
        ConnectingDeviceWithWifiFragment fragment = new ConnectingDeviceWithWifiFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public static Fragment newInstance(@Nullable Bundle bundle, Boolean fromWifiScreen) {
        ConnectingDeviceWithWifiFragment fragment = new ConnectingDeviceWithWifiFragment();

        if (bundle != null) {
            bundle.putBoolean(FROM_WRONG_WIFI_SCREEN, fromWifiScreen);
        } else {
            bundle = new Bundle();
            bundle.putBoolean(FROM_WRONG_WIFI_SCREEN, fromWifiScreen);
        }

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((EWSActionBarListener) getContext()).closeButton(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentConnectingDeviceWithWifiBinding viewDataBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_connecting_device_with_wifi,
                        container, false);

        if (viewModel == null) {
            invokeViewModel();
            viewModel.startConnecting(createStartConnectionModel(getArguments()),
                    BundleUtils.extractBooleanFromBundleOrThrow(getArguments(), FROM_WRONG_WIFI_SCREEN));
        } else {
            invokeViewModel();
            viewModel.connectToHomeWifi(
                    BundleUtils.extractStringFromBundleOrThrow(getBundle(), HOME_WIFI_SSID));
        }

        viewDataBinding.setViewModel(viewModel);

        return viewDataBinding.getRoot();
    }

    private void invokeViewModel() {
        if (viewModel == null) {
            viewModel = createViewModel();
        }
        viewModel.setFragmentCallback(this);
    }

    @NonNull
    private StartConnectionModel createStartConnectionModel(Bundle bundle) {
        return new StartConnectionModel(
                BundleUtils.extractStringFromBundleOrThrow(bundle, HOME_WIFI_SSID),
                BundleUtils.extractStringFromBundleOrThrow(bundle, HOME_WIFI_PWD),
                BundleUtils.extractStringFromBundleOrThrow(bundle, DEVICE_NAME),
                BundleUtils.extractStringFromBundleOrThrow(bundle, DEVICE_FRIENDLY_NAME));
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
        return getEWSComponent().connectingDeviceWithWifiViewModel();
    }

    @Override
    public void registerReceiver(@NonNull BroadcastReceiver receiver,
                                 @NonNull IntentFilter filter) {
        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void unregisterReceiver(@NonNull BroadcastReceiver receiver) {
        try {
            getActivity().unregisterReceiver(receiver);
        } catch (IllegalArgumentException e) {
            if (viewModel != null) {
                viewModel.getEwsLogger().d(TAG, e.toString());
            }
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

    @Override
    protected void callTrackPageName() {
        if (viewModel != null) {
            viewModel.trackPageName();
        }
    }
}
