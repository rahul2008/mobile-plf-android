/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.FragmentEwsPluginDeviceBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.util.TextUtil;
import com.philips.cdp2.ews.viewmodel.EWSDevicePowerOnViewModel;

import javax.inject.Inject;

public class EWSDevicePowerOnFragment extends EWSBaseFragment<FragmentEwsPluginDeviceBinding> {

    public static final int FRAGMENT_HIERARCHY_LEVEL = 3;

    @Inject
    EWSDevicePowerOnViewModel viewModel;

    @Override
    public int getHierarchyLevel() {
        return FRAGMENT_HIERARCHY_LEVEL;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        viewDataBinding.ewsHotspotBlinkingDesc.setText(TextUtil.getHTMLText(getString(R.string.ews_03_content_2)));

        return view;
    }

    @Override
    protected void bindViewModel(final FragmentEwsPluginDeviceBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ews_plugin_device;
    }

    @NonNull
    @Override
    protected String getPageName() {
        return Page.PLUGIN_DEVICE;
    }
}