/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.common.callbacks.FragmentCallback;
import com.philips.cdp2.ews.databinding.FragmentEwsWifiPairedBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.viewmodel.EWSWiFIPairedViewModel;

import javax.inject.Inject;

public class EWSWiFiPairedFragment extends EWSBaseFragment<FragmentEwsWifiPairedBinding> implements
        FragmentCallback {

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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getEwsActivity().hideCloseButton();
    }

    @Override
    protected void bindViewModel(final FragmentEwsWifiPairedBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
        viewModel.setFragmentCallback(this);
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @NonNull
    @Override
    public String getPageName() {
        return Page.WIFI_PAIRED;
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
    public boolean handleBackEvent() {
        // Do nothing, back disabled in this screen
        return true;
    }

    @Override
    public void finishMicroApp() {
        getActivity().finish();
    }
}