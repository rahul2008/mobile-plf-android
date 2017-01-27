/*
 * (C) Koninklijke Philips N.V., 2017.
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
import com.philips.platform.catalogapp.RecyclerViewSettingsHelper;
import com.philips.platform.catalogapp.databinding.FragmentRecyclerviewSettingsBinding;

public class RecyclerViewSettingsFragment extends BaseFragment {

    public ObservableBoolean isSeparatorEnabled = new ObservableBoolean(Boolean.TRUE);
    public ObservableBoolean isHeaderEnabled = new ObservableBoolean(Boolean.TRUE);

    private FragmentRecyclerviewSettingsBinding recyclerviewSettingsBinding;
    private RecyclerViewSettingsHelper settingsHelper;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        recyclerviewSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recyclerview_settings, container, false);
        recyclerviewSettingsBinding.setFrag(this);

        settingsHelper = new RecyclerViewSettingsHelper(getContext());
        setHeaderEnabled(settingsHelper.isHeaderEnabled());
        setSeparatorEnabled(settingsHelper.isSeperatorEnabled());

        return recyclerviewSettingsBinding.getRoot();
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_recyclerview_settings;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        settingsHelper.setHeaderEnabled(isHeaderEnabled.get());
        settingsHelper.setSeperatorEnabled(isSeparatorEnabled.get());
    }

    @Override
    public void onPause() {
        super.onPause();
        settingsHelper.setHeaderEnabled(isHeaderEnabled.get());
        settingsHelper.setSeperatorEnabled(isSeparatorEnabled.get());
    }

    public void setHeaderEnabled(boolean isheaderEnabled) {
        this.isHeaderEnabled.set(isheaderEnabled);
    }

    public void setSeparatorEnabled(boolean isSeparatorEnabled) {
        this.isSeparatorEnabled.set(isSeparatorEnabled);
    }
}
