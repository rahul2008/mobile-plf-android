/**
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
import com.philips.platform.catalogapp.databinding.FragmentRadiobuttonBinding;

public class RadioButtonFragment extends BaseFragment {
    public ObservableBoolean isRadioButtonEnabled = new ObservableBoolean(Boolean.TRUE);
    private FragmentRadiobuttonBinding fragmentRadiobuttonBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentRadiobuttonBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_radiobutton, container, false);
        fragmentRadiobuttonBinding.setFrag(this);
        return fragmentRadiobuttonBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean("isRadioButtonEnabled", isRadioButtonEnabled.get());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            setEnabled(savedInstanceState.getBoolean("isRadioButtonEnabled"));
        }
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_radiobutton;
    }

    public void setEnabled(boolean enabled) {
        isRadioButtonEnabled.set(enabled);
    }
}
