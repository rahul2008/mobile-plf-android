/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import android.support.annotation.NonNull;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.ProductSupportViewBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.viewmodel.ProductSupportViewModel;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import javax.inject.Inject;

public class EWSProductSupportFragment extends EWSBaseFragment<ProductSupportViewBinding> {

    public static final int PRODUCT_SUPPORT_HIERARCHY_LEVEL = 9;

    @Inject
    ProductSupportViewModel viewModel;

    @Override
    public int getHierarchyLevel() {
        return PRODUCT_SUPPORT_HIERARCHY_LEVEL;
    }

    @Override
    protected void bindViewModel(final ProductSupportViewBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
        viewModel.showProductSupportScreen(getFragmentLauncher());
    }

    private UiLauncher getFragmentLauncher() {
        return new FragmentLauncher(getActivity(), R.id.product_support_view, viewModel);
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
        return R.layout.product_support_view;
    }

    @Override
    protected boolean hasMenu() {
        return false;
    }
}