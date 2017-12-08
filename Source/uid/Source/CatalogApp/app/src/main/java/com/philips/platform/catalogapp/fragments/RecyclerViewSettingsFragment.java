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
    public ObservableBoolean isIconTemplateSelected = new ObservableBoolean(Boolean.TRUE);

    private FragmentRecyclerviewSettingsBinding recyclerviewSettingsBinding;
    private RecyclerViewSettingsHelper settingsHelper;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        recyclerviewSettingsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recyclerview_settings, container, false);
        recyclerviewSettingsBinding.setFrag(this);

        settingsHelper = new RecyclerViewSettingsHelper(getContext());
        initSavedSettings();

        return recyclerviewSettingsBinding.getRoot();
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_recyclerview_settings;
    }

    @Override
    public void onPause() {
        super.onPause();
        getCheckedRadioButtonTemplate();
        settingsHelper.setHeaderEnabled(isHeaderEnabled.get());
        settingsHelper.setSeperatorEnabled(isSeparatorEnabled.get());
        settingsHelper.setIconTemplateSelected(isIconTemplateSelected.get());
    }

    private void initSavedSettings() {
        setHeaderEnabled(settingsHelper.isHeaderEnabled());
        setSeparatorEnabled(settingsHelper.isSeperatorEnabled());
        setIsIconTemplateSelected(settingsHelper.isIconTemplateSelected());
    }

    public void setHeaderEnabled(boolean isheaderEnabled) {
        this.isHeaderEnabled.set(isheaderEnabled);
    }

    public void setSeparatorEnabled(boolean isSeparatorEnabled) {
        this.isSeparatorEnabled.set(isSeparatorEnabled);
    }

    public void setIsIconTemplateSelected(boolean isIconTemplateSelected) {
        this.isIconTemplateSelected.set(isIconTemplateSelected);
    }

    private void getCheckedRadioButtonTemplate() {
        int checkedRadioButtonId = recyclerviewSettingsBinding.radioGroupTemplates.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.single_line_with_icon) {
            setIsIconTemplateSelected(true);
        } else if (checkedRadioButtonId == R.id.two_lines) {
            setIsIconTemplateSelected(false);
        }
    }
}
