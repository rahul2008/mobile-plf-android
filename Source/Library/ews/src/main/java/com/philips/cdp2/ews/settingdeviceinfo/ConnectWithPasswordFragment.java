/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.settingdeviceinfo;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.FragmentEwsConnectDeviceBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.util.BundleUtils;
import com.philips.cdp2.ews.view.EWSBaseFragment;

import javax.inject.Inject;

public class ConnectWithPasswordFragment extends EWSBaseFragment<FragmentEwsConnectDeviceBinding> {

    private static String DEVICE_FRIENDLY_NAME = "deviceFriendlyName";
    @Inject
    ConnectWithPasswordViewModel viewModel;


    public static Fragment newInstance(@NonNull String deviceFriendlyName) {
        Bundle data = new Bundle();
        data.putString(DEVICE_FRIENDLY_NAME, deviceFriendlyName);
        ConnectWithPasswordFragment fragment = new ConnectWithPasswordFragment();
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public int getHierarchyLevel() {
        return 5;
    }

    @Override
    protected void bindViewModel(final FragmentEwsConnectDeviceBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
        viewDataBinding.setInputMethodManager((InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE));

        viewModel.setDeviceFriendlyName(BundleUtils
                .extractStringFromBundleOrThrow(getArguments(), DEVICE_FRIENDLY_NAME));
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @NonNull
    @Override
    public String getPageName() {
        return Tag.PAGE.CONNECT_WIFI;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ews_connect_device;
    }

    @Override
    public int getNavigationIconId() {
        return 0;// do not show icon
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean handleBackEvent() {
        // Do nothing, back disabled in this screen
        return true;
    }
}