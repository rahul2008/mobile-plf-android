/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentToggleSwitchBinding;

public class ToggleSwitchFragment extends BaseFragment {
    public ObservableBoolean isSwitchEnabled = new ObservableBoolean(Boolean.TRUE);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final FragmentToggleSwitchBinding fragmentToggleSwitchBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_toggle_switch, container, false);
        fragmentToggleSwitchBinding.setFrag(this);

        return fragmentToggleSwitchBinding.getRoot();
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_toggle_switch;
    }

    public void disableSwitches(boolean isChecked) {
        isSwitchEnabled.set(!isChecked);
    }
}