/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.view;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.FragmentEwsResetWulDeviceBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.viewmodel.EWSResetDeviceViewModel;

import javax.inject.Inject;

public class EWSResetDeviceFragment extends EWSBaseFragment<FragmentEwsResetWulDeviceBinding> {

    public static final int FRAGMENT_HIERARCHY_LEVEL = 5;

    @Inject
    EWSResetDeviceViewModel viewModel;

    @Override
    public int getHierarchyLevel() {
        return FRAGMENT_HIERARCHY_LEVEL;
    }

    @Override
    protected void bindViewModel(final FragmentEwsResetWulDeviceBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ews_reset_wul_device;
    }

    @NonNull
    @Override
    protected String getPageName() {
        return Page.RESET_DEVICE;
    }
}