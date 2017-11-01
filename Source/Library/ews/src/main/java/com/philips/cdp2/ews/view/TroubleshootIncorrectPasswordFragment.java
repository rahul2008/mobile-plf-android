/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.TroubleshootIncorrectPasswordBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.viewmodel.TroubleshootIncorrectPasswordViewModel;
import com.philips.cdp2.ews.wifi.WiFiUtil;

import javax.inject.Inject;

public class TroubleshootIncorrectPasswordFragment extends EWSBaseFragment<TroubleshootIncorrectPasswordBinding> {

    public static final int FRAGMENT_STACK_HIERARCHY_LEVEL = 7;

    @Inject
    TroubleshootIncorrectPasswordViewModel viewModel;

    @Inject
    WiFiUtil wiFiUtil;

    @Override
    public int getHierarchyLevel() {
        return FRAGMENT_STACK_HIERARCHY_LEVEL;
    }

    @Override
    protected void bindViewModel(final TroubleshootIncorrectPasswordBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
        viewDataBinding.setWiFiUtil(wiFiUtil);
        viewModel.tagIncorrectPassword();
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @NonNull
    @Override
    public String getPageName() {
        return Tag.PAGE.INCORRECT_PASSWORD;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.troubleshoot_incorrect_password;
    }

    @StringRes
    @Override
    public int getToolbarTitle() {
        return R.string.ews_22_header;
    }
}