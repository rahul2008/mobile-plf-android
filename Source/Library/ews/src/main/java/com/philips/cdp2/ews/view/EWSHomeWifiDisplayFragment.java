/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.view;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.databinding.FragmentEwsHomeWifiDisplayScreenBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Pages;
import com.philips.cdp2.ews.viewmodel.EWSHomeWifiDisplayViewModel;

import javax.inject.Inject;

public class EWSHomeWifiDisplayFragment extends EWSBaseFragment<FragmentEwsHomeWifiDisplayScreenBinding> {

    @Inject
    EWSHomeWifiDisplayViewModel viewModel;

    @Override
    public int getHierarchyLevel() {
        return 2;
    }

    @Override
    protected void bindViewModel(final FragmentEwsHomeWifiDisplayScreenBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ews_home_wifi_display_screen;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.refresh();
    }

    @NonNull
    @Override
    protected String getPageName() {
        return Pages.CONFIRM_WIFI;
    }
}
