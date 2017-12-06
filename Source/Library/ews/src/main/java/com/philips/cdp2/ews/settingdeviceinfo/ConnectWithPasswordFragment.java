/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.settingdeviceinfo;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.base.BaseFragment;
import com.philips.cdp2.ews.databinding.FragmentConnectWithPasswordBinding;
import com.philips.cdp2.ews.util.BundleUtils;

public class ConnectWithPasswordFragment extends BaseFragment {

    private static String DEVICE_FRIENDLY_NAME = "deviceFriendlyName";

    private ConnectWithPasswordViewModel viewModel;


    public static Fragment newInstance(@NonNull String deviceFriendlyName) {
        Bundle data = new Bundle();
        data.putString(DEVICE_FRIENDLY_NAME, deviceFriendlyName);
        ConnectWithPasswordFragment fragment = new ConnectWithPasswordFragment();
        fragment.setArguments(data);
        return fragment;
    }


    @SuppressWarnings("ConstantConditions")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentConnectWithPasswordBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_connect_with_password, container, false);
        viewModel = createViewModel();
        binding.setViewModel(viewModel);

        binding.setInputMethodManager((InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE));

        viewModel.setDeviceFriendlyName(BundleUtils
                .extractStringFromBundleOrThrow(getArguments(), DEVICE_FRIENDLY_NAME));
        return binding.getRoot();
    }

    @NonNull
    private ConnectWithPasswordViewModel createViewModel() {
        return getEWSComponent().connectWithPasswordViewModel();
    }

    @Override
    public boolean handleBackEvent() {
        // Do nothing, back disabled in this screen
        return true;
    }

    @NonNull
    @Override
    protected void callTrackPageName() {
        viewModel.trackPageName();
    }


}