/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.settingdeviceinfo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.inputmethod.InputMethodManager;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.FragmentEwsConnectDeviceBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Pages;
import com.philips.cdp2.ews.view.EWSBaseFragment;

import javax.inject.Inject;

public class SetDeviceInfoFragment extends EWSBaseFragment<FragmentEwsConnectDeviceBinding> {

    @Inject
    SetDeviceInfoViewModel viewModel;

    @Override
    public int getHierarchyLevel() {
        return 5;
    }

    @Override
    protected void bindViewModel(final FragmentEwsConnectDeviceBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
        viewDataBinding.setInputMethodManager((InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE));
        viewModel.fetchDeviceFriendlyName();
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @NonNull
    @Override
    public String getPageName() {
        return Pages.CONNECT_WIFI;
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
        if (viewModel != null) {
            viewModel.clear();
        }
        super.onDestroyView();
    }

    @Override
    public boolean handleBackEvent() {
        // Do nothing, back disabled in this screen
        return true;
    }
}