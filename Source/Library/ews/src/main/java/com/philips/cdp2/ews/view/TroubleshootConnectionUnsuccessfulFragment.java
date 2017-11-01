/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.TroubleshootConnectionUnsuccessfulBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.util.TextUtil;
import com.philips.cdp2.ews.viewmodel.TroubleshootConnectionUnsuccessfulViewModel;

import javax.inject.Inject;

public class TroubleshootConnectionUnsuccessfulFragment extends EWSBaseFragment<TroubleshootConnectionUnsuccessfulBinding> {

    public static final int FRAGMENT_STACK_HIERARCHY_LEVEL = 6;

    @Inject
    TroubleshootConnectionUnsuccessfulViewModel viewModel;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        viewDataBinding.incorrectPasswordTitle.setText(TextUtil.getHTMLText(getString(R.string.ews_20_content_1)));
        return view;
    }

    @Override
    public int getHierarchyLevel() {
        return FRAGMENT_STACK_HIERARCHY_LEVEL;
    }


    @Override
    protected void bindViewModel(final TroubleshootConnectionUnsuccessfulBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @NonNull
    @Override
    public String getPageName() {
        return Page.CONNECTION_UNSUCCESSFUL;
    }

    @Override
    public int getNavigationIconId() {
        return 0;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.troubleshoot_connection_unsuccessful;
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @StringRes
    @Override
    public int getToolbarTitle() {
        return R.string.ews_20_header;
    }

    @Override
    public boolean hasMenu() {
        return true;
    }
}