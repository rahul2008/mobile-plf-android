/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.troubleshooting.homewifi;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.common.callbacks.DialogCallback;
import com.philips.cdp2.ews.common.util.DateUtil;
import com.philips.cdp2.ews.databinding.TroubleshootHomeWifiFragmentBinding;
import com.philips.cdp2.ews.injections.DaggerEWSComponent;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.injections.EWSModule;
import com.philips.cdp2.ews.tagging.Pages;
import com.philips.cdp2.ews.util.TextUtil;
import com.philips.cdp2.ews.view.EWSBaseFragment;

import javax.inject.Inject;

public class TroubleshootHomeWiFiFragment extends EWSBaseFragment<TroubleshootHomeWifiFragmentBinding>
        implements DialogCallback {

    public static final String HIERARCHY_LEVEL = "hierarchyLevel";

    @Inject
    TroubleshootHomeWiFiViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        String explanation = String.format(DateUtil.getSupportedLocale(), getString(R.string.label_ews_home_network_body), getString(R.string.af_app_name));
        viewDataBinding.labelEwsHomeNetworkBody.setText(TextUtil.getHTMLText(explanation));
        return view;
    }

    @Override
    public int getHierarchyLevel() {
        return getArguments().getInt(HIERARCHY_LEVEL);
    }

    @Override
    protected void bindViewModel(final TroubleshootHomeWifiFragmentBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
        viewModel.setDialogCallback(this);
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @NonNull
    @Override
    public String getPageName() {
        return Pages.HOME_WIFI_OFF;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.troubleshoot_home_wifi_fragment;
    }

    @Override
    protected boolean hasMenu() {
        return false;
    }

    @Override
    protected EWSComponent getEwsComponent() {
        return DaggerEWSComponent.builder().eWSModule(new EWSModule(getContext(), getFragmentManager())).build();
    }

    @Override
    public void hideDialog() {
        getActivity().finish();
    }
}