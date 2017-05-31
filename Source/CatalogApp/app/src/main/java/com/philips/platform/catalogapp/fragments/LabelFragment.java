/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentLabelsBinding;

public class LabelFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        FragmentLabelsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_labels, container, false);
        binding.setLabelFrag(this);
        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_label;
    }
}