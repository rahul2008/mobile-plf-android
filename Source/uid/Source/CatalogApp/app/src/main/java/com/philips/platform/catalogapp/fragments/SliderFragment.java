/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentSliderBinding;

public class SliderFragment extends BaseFragment {

    private FragmentSliderBinding fragmentSliderBinding;

    @Override
    public int getPageTitle() {
        return R.string.page_title_slider;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentSliderBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_slider, container, false);
        fragmentSliderBinding.setFrag(this);
        return fragmentSliderBinding.getRoot();
    }

}
