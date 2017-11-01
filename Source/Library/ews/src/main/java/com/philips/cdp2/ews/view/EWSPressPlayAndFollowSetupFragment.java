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
import com.philips.cdp2.ews.databinding.FragmentEwsPressPlayFollowSetupDeviceBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.util.TextUtil;
import com.philips.cdp2.ews.viewmodel.EWSPressPlayAndFollowSetupViewModel;

import javax.inject.Inject;

public class EWSPressPlayAndFollowSetupFragment extends ConnectPhoneToDeviceAPModeFragment<EWSPressPlayAndFollowSetupViewModel, FragmentEwsPressPlayFollowSetupDeviceBinding> {

    public static final int FRAGMENT_HIERARCHY_LEVEL = 4;

    @Inject
    EWSPressPlayAndFollowSetupViewModel viewModel;

    @Override
    public int getHierarchyLevel() {
        return FRAGMENT_HIERARCHY_LEVEL;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        viewDataBinding.ews0202LabelVerifyReadyTitle.setText(TextUtil.getHTMLText(getString(R.string.ews_04_content)));
        return view;
    }

    @Override
    protected void bindViewModel(final FragmentEwsPressPlayFollowSetupDeviceBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
        viewModel.setFragment(this);
    }

    @NonNull
    @Override
    public String getPageName() {
        return Page.PRESS_PLAY_AND_FOLLOW_SETUP;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ews_press_play_follow_setup_device;
    }

    @Override
    EWSPressPlayAndFollowSetupViewModel getViewModel() {
        return viewModel;
    }
}