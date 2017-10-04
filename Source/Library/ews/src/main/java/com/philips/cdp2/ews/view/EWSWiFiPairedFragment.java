/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.FragmentEwsWifiPairedBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Pages;
import com.philips.cdp2.ews.viewmodel.EWSWiFIPairedViewModel;

import javax.inject.Inject;

public class EWSWiFiPairedFragment extends EWSBaseFragment<FragmentEwsWifiPairedBinding> {

    @Inject
    EWSWiFIPairedViewModel viewModel;

    @Override
    public int getHierarchyLevel() {
        return 6;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    protected void bindViewModel(final FragmentEwsWifiPairedBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @NonNull
    @Override
    public String getPageName() {
        return Pages.WIFI_PAIRED;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ews_wifi_paired;
    }

    @Override
    protected boolean hasMenu() {
        return false;
    }

    @Override
    public int getNavigationIconId() {
        return 0;//don't show
    }
}