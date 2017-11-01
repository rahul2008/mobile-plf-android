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
import com.philips.cdp2.ews.databinding.FragmentBlinkingAccessPointBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.util.TextUtil;
import com.philips.cdp2.ews.viewmodel.BlinkingAccessPointViewModel;

import javax.inject.Inject;

public class BlinkingAccessPointFragment extends ConnectPhoneToDeviceAPModeFragment<BlinkingAccessPointViewModel, FragmentBlinkingAccessPointBinding> {

    public static final int FRAGMENT_HIERARCHY_LEVEL = 5;

    @Inject
    BlinkingAccessPointViewModel viewModel;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        viewDataBinding.blinkingAPModeTitle.setText(TextUtil.getHTMLText(getString(R.string.ews_13_content)));
        return view;
    }

    @Override
    public int getHierarchyLevel() {
        return FRAGMENT_HIERARCHY_LEVEL;
    }

    @Override
    protected void bindViewModel(final FragmentBlinkingAccessPointBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
        viewModel.setFragment(this);
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blinking_access_point;
    }

    @NonNull
    @Override
    protected String getPageName() {
        return Tag.PAGE.BLINKING_ACCESS_POINT;
    }

    @Override
    BlinkingAccessPointViewModel getViewModel() {
        return viewModel;
    }
}
