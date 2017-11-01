/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.FragmentChooseSetupStateBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.viewmodel.ChooseSetupStateViewModel;

import javax.inject.Inject;

public class ChooseSetupStateFragment extends EWSBaseFragment<FragmentChooseSetupStateBinding> {

    public static final int FRAGMENT_HIERARCHY_LEVEL = 4;

    @Inject
    ChooseSetupStateViewModel viewModel;

    @Override
    public int getHierarchyLevel() {
        return FRAGMENT_HIERARCHY_LEVEL;
    }

    @Override
    protected void bindViewModel(final FragmentChooseSetupStateBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_choose_setup_state;
    }

    @NonNull
    @Override
    protected String getPageName() {
        return Page.CHOOSE_SETUP_STATE;
    }
}