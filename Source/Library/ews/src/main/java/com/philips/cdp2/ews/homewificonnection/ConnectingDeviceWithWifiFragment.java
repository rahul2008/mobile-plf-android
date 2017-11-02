/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.homewificonnection;

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

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.databinding.FragmentConnectingDeviceWithWifiBinding;
import com.philips.cdp2.ews.logger.EWSLogger;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.util.BundleUtils;
import com.philips.cdp2.ews.view.BaseFragment;
import com.philips.cdp2.ews.view.EWSActivity;

import javax.inject.Inject;

public class ConnectingDeviceWithWifiFragment extends BaseFragment
        implements ConnectingDeviceWithWifiViewModel.ConnectingDeviceToWifiCallback {

    public final static String HOME_WIFI_SSID = "homeWiFiSSID";
    private final static String TAG = "ConnectingDeviceWithWifiFragment";
    public final static String APP_NAME = "appName";
    private final static String HOME_WIFI_PWD = "homeWiFiPassword";
    private final static String DEVICE_NAME = "deviceName";
    private final static String DEVICE_FRIENDLY_NAME = "deviceFriendlyName";

    @Nullable ConnectingDeviceWithWifiViewModel viewModel;

    @Inject
    BaseContentConfiguration baseContentConfiguration;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((EWSActivity) getActivity()).getEWSComponent().inject(ConnectingDeviceWithWifiFragment.this);
    }

    public static Fragment newInstance(@NonNull String homeWiFiSSID,
                                       @NonNull String homeWiFiPassword,
                                       @NonNull String deviceName,
                                       @NonNull String deviceFriendlyName) {
        Bundle data = new Bundle();
        data.putString(HOME_WIFI_SSID, homeWiFiSSID);
        data.putString(HOME_WIFI_PWD, homeWiFiPassword);
        data.putString(DEVICE_NAME, deviceName);
        data.putString(DEVICE_FRIENDLY_NAME, deviceFriendlyName);

        ConnectingDeviceWithWifiFragment fragment = new ConnectingDeviceWithWifiFragment();
        fragment.setArguments(data);
        return fragment;
    }

    public static Fragment newInstance(@Nullable Bundle bundle) {
        ConnectingDeviceWithWifiFragment fragment = new ConnectingDeviceWithWifiFragment();
        fragment.setArguments(bundle);
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
        FragmentConnectingDeviceWithWifiBinding viewDataBinding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_connecting_device_with_wifi,
                        container, false);

        if (viewModel == null) {
            invokeViewModel();
            viewModel.startConnecting(createStartConnectionModel(getArguments()));
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
        return ((EWSActivity) getActivity()).getEWSComponent().connectingDeviceWithWifiViewModel();
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
            EWSLogger.d(TAG, e.toString());
        }
    }

    @Override
    public Bundle getBundle() {
        return getArguments();
    }

    @Override
    public void showCancelDialog() {
        handleCancelButtonClicked(baseContentConfiguration.getDeviceName());
    }

    @Override
    public boolean handleBackEvent() {
        // Do nothing, back disabled in this screen
        return true;
    }

    @Override
    protected String getPageName() {
        return Page.CONNECTING_DEVICE_WITH_WIFI;
    }
}
