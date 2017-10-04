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
import com.philips.cdp2.ews.common.util.DateUtil;
import com.philips.cdp2.ews.databinding.TroubleshootHomeWifiFragmentBinding;
import com.philips.cdp2.ews.injections.EWSComponent;
import com.philips.cdp2.ews.tagging.Pages;
import com.philips.cdp2.ews.util.TextUtil;
import com.philips.cdp2.ews.viewmodel.TroubleshootHomeWiFiViewModel;

import javax.inject.Inject;

public class TroubleshootHomeWiFiFragment extends EWSBaseFragment<TroubleshootHomeWifiFragmentBinding> {

    public static final String HIERARCHY_LEVEL = "hierarchyLevel";

    @Inject
    TroubleshootHomeWiFiViewModel viewModel;

    /**
     * hierarchyLevel is dynamic here and should be +1 from where its called.
     * 1.hierarchyLevel in FragmentManager stack should be 2 if this fragment is shown from @see {@link EWSGettingStartedFragment}
     * 2.hierarchyLevel in FragmentManager stack should be 3 if this fragment is shown from @see {@link EWSHomeWifiDisplayFragment}
     *
     * @return instance of TroubleshootHomeWiFiFragment.
     */
    public static TroubleshootHomeWiFiFragment getInstance(final int hierarchyLevel) {
        Bundle arguments = new Bundle();
        arguments.putInt(HIERARCHY_LEVEL, hierarchyLevel);
        TroubleshootHomeWiFiFragment fragment = new TroubleshootHomeWiFiFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

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
    public void onResume() {
        super.onResume();
        viewModel.checkHomeWiFiNetwork();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.unregister();
    }
}