/*
 * (C) Koninklijke Philips N.V., 2017.
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
import com.philips.cdp2.ews.common.util.DateUtil;
import com.philips.cdp2.ews.databinding.TroubleshootWrongWifiFragmentBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Page;
import com.philips.cdp2.ews.util.TextUtil;
import com.philips.cdp2.ews.viewmodel.TroubleshootWrongWiFiViewModel;

import javax.inject.Inject;

public class TroubleshootWrongWiFiFragment extends EWSBaseFragment<TroubleshootWrongWifiFragmentBinding> {

    private static final int WRONG_WIFI_HIERARCHY_LEVEL = 7;

    @Inject
    TroubleshootWrongWiFiViewModel viewModel;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        String wifiSteps = String.format(DateUtil.getSupportedLocale(), getString(R.string.label_ews_phone_reconnected_different_network_body), viewModel.getHomeWifi());
        viewDataBinding.labelEwsTroubleshootWrongWifi.setText(TextUtil.getHTMLText(wifiSteps));
        return view;
    }

    @Override
    public int getHierarchyLevel() {
        return WRONG_WIFI_HIERARCHY_LEVEL;
    }

    @Override
    protected void bindViewModel(final TroubleshootWrongWifiFragmentBinding viewDataBinding) {
        viewDataBinding.setViewModel(viewModel);
        viewModel.tagWrongWifi();
    }

    @Override
    protected void inject(final EWSComponent ewsComponent) {
        ewsComponent.inject(this);
    }

    @NonNull
    @Override
    public String getPageName() {
        return Page.WRONG_WIFI;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.troubleshoot_wrong_wifi_fragment;
    }

    @StringRes
    @Override
    public int getToolbarTitle() {
        return R.string.ews_21_header;
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.start();
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.stop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        viewModel.unregister();
    }

}
