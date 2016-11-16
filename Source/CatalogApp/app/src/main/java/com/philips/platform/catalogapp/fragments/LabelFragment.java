/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentLabelsBinding;

public class LabelFragment extends BaseFragment {
    public ObservableField<String> description = new ObservableField<>("0");

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        FragmentLabelsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_labels, container, false);
        binding.setLabelFrag(this);
        return binding.getRoot();
    }

    @Override
    public int getPageTitle() {
        return R.string.label_title_text;
    }

    public void setProgress(int progress) {
        description.set(String.valueOf(progress));
    }
}