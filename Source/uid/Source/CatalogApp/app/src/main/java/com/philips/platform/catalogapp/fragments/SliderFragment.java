/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.adapters.SeekBarBindingAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentSliderBinding;
import com.philips.platform.uid.view.widget.DiscreteSlider;
import com.philips.platform.uid.view.widget.Label;

public class SliderFragment extends BaseFragment{

    public ObservableField<String> sliderProgress = new ObservableField<>();

    @Override
    public int getPageTitle() {
        return R.string.page_title_slider;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentSliderBinding fragmentSliderBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_slider, container, false);
        fragmentSliderBinding.setFrag(this);
        updateProgress(fragmentSliderBinding.discreteSlider.getProgress());
        fragmentSliderBinding.discreteSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return fragmentSliderBinding.getRoot();
    }

    private void updateProgress(int progress){
        sliderProgress.set(String.valueOf(progress));
    }
}
