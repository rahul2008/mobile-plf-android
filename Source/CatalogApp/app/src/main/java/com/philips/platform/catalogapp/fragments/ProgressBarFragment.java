/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.philips.platform.catalogapp.R;
import com.philips.platform.uit.view.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProgressBarFragment extends BaseFragment {

    @Bind(R.id.progress_bar_seekbar_value)
    SeekBar valueSeekbar;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.progress_bar_secondary)
    ProgressBar secondaryProgressBar;

    @Bind(R.id.progress_bar_wide)
    ProgressBar wideProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_progress_bar, container, false);
        ButterKnife.bind(this, root);

        valueSeekbar.setOnSeekBarChangeListener(onSeekBarChangeListener);

        return root;
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_progress_bar;
    }

    private void updateProgressBarValues(int progress) {
        progressBar.setProgress(progress);
        secondaryProgressBar.setProgress(progress);
        secondaryProgressBar.setSecondaryProgress(progress+10);
        wideProgressBar.setProgress(progress);
        wideProgressBar.setSecondaryProgress(progress+10);
    }

    private OnSeekBarChangeListener onSeekBarChangeListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
            updateProgressBarValues(progress);
        }

        @Override
        public void onStartTrackingTouch(final SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(final SeekBar seekBar) {

        }
    };
}