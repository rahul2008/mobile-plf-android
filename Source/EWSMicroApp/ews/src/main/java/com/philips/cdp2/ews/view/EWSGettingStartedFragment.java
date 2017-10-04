/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.FragmentEwsGettingStartedBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Pages;
import com.philips.cdp2.ews.viewmodel.EWSGettingStartedViewModel;

import javax.inject.Inject;

public class EWSGettingStartedFragment extends EWSBaseFragment<FragmentEwsGettingStartedBinding> {

    @Inject
    EWSGettingStartedViewModel viewModel;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_ews_getting_started;
    }

    @Override
    protected void bindViewModel(final FragmentEwsGettingStartedBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @NonNull
    @Override
    public String getPageName() {
        return Pages.WIFI_SETUP;
    }

    @Override
    public int getHierarchyLevel() {
        return 1;
    }

    @Override
    protected boolean hasMenu() {
        return true;
    }

    @Override
    public boolean onBackPressed() {
        viewModel.onBackPressed();
        return true;
    }
}
