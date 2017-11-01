/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.view;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.databinding.TroubleshootCheckRouterSettingsBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.cdp2.ews.viewmodel.TroubleshootCheckRouterSettingsViewModel;

import javax.inject.Inject;

public class TroubleshootCheckRouterSettingsFragment extends EWSBaseFragment<TroubleshootCheckRouterSettingsBinding> {

    public static final int FRAGMENT_STACK_HIERARCHY_LEVEL = 8;
    public static final String ROUTER_ERROR_TYPE = "router_error_type";
    public static final int ROUTER_ERROR_DIFFERENT_NETWORK = 1;
    public static final int ROUTER_ERROR_NO_NETWORK = 2;

    @IntDef({ROUTER_ERROR_DIFFERENT_NETWORK, ROUTER_ERROR_NO_NETWORK})
    public @interface RouterScreenType {
    }

    @Inject
    TroubleshootCheckRouterSettingsViewModel viewModel;
    @VisibleForTesting
    @RouterScreenType int screenType;

    public static TroubleshootCheckRouterSettingsFragment getInstance(@RouterScreenType int type) {
        Bundle arguments = new Bundle();
        arguments.putInt(ROUTER_ERROR_TYPE, type);
        TroubleshootCheckRouterSettingsFragment fragment = new TroubleshootCheckRouterSettingsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        if (getArguments() == null) {
            throw new IllegalStateException("Need to pass the screen type to the fragment");
        }
        //noinspection ResourceType
        screenType = getArguments().getInt(ROUTER_ERROR_TYPE, ROUTER_ERROR_NO_NETWORK);
        viewModel.setScreenType(screenType);
        return view;
    }

    @Override
    public int getHierarchyLevel() {
        return FRAGMENT_STACK_HIERARCHY_LEVEL;
    }

    @Override
    protected void bindViewModel(final TroubleshootCheckRouterSettingsBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
        viewModel.tagWifiRouterSettings();
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @NonNull
    @Override
    public String getPageName() {
        if (screenType == ROUTER_ERROR_DIFFERENT_NETWORK)
            return Tag.PAGE.ROUTER_SETTINGS_WRONG_WIFI;
        else {
            return Tag.PAGE.ROUTER_SETTINGS;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.troubleshoot_check_router_settings;
    }

    @Override
    public int getToolbarTitle() {
        if (screenType == ROUTER_ERROR_DIFFERENT_NETWORK) {
            return R.string.ews_23_header_1;
        } else {
            return R.string.ews_24_header;
        }
    }

    @Override
    public boolean hasMenu() {
        return false;
    }

}