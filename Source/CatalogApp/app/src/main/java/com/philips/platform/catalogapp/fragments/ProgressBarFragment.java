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
import com.philips.platform.uid.view.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProgressBarFragment extends BaseFragment {

    @Bind(R.id.progress_bar_seekbar_value)
    SeekBar valueSeekbar;

    @Bind(R.id.progress_bar_seekbar_scale)
    SeekBar scaleSeekbar;

    @Bind(R.id.progress_bar)
    ProgressBar progressBar;

    @Bind(R.id.progress_bar_secondary)
    ProgressBar secondaryProgressBar;

    @Bind(R.id.progress_bar_wide)
    ProgressBar wideProgressBar;

    @Bind(R.id.progress_bar_determinate_circular_small)
    ProgressBar determinateSmallCircularProgressBar;

    @Bind(R.id.progress_bar_indeterminate_circular_small)
    ProgressBar indeterminateSmallCircularProgressBar;

    @Bind(R.id.progress_bar_determinate_circular_middle)
    ProgressBar determinateMiddleCircularProgressBar;

    @Bind(R.id.progress_bar_indeterminate_circular_middle)
    ProgressBar indeterminateMiddleCircularProgressBar;

    @Bind(R.id.progress_bar_determinate_circular_big)
    ProgressBar determinateBigCircularProgressBar;

    @Bind(R.id.progress_bar_indeterminate_circular_big)
    ProgressBar indeterminateBigCircularProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_progress_bar, container, false);
        ButterKnife.bind(this, root);

        valueSeekbar.setOnSeekBarChangeListener(onSeekBarChangedListener);
        scaleSeekbar.setOnSeekBarChangeListener(onScaleSeekbarChangedListener);
        return root;
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_progress_bar;
    }

    private void updateLinearProgressBarValues(int progress) {
        progressBar.setProgress(progress);
        secondaryProgressBar.setProgress(progress);
        secondaryProgressBar.setSecondaryProgress(progress + 10);
        wideProgressBar.setProgress(progress);
        wideProgressBar.setSecondaryProgress(progress + 10);
    }

    private void updateCircularProgressBarValues(int progress) {
        determinateSmallCircularProgressBar.setProgress(progress);
        determinateMiddleCircularProgressBar.setProgress(progress);
        determinateBigCircularProgressBar.setProgress(progress);
        indeterminateSmallCircularProgressBar.setProgress(progress);
        indeterminateMiddleCircularProgressBar.setProgress(progress);
        indeterminateBigCircularProgressBar.setProgress(progress);
    }

    private void setCircularProgressBarVisibile(ProgressBar.CircularProgressBarSize bar) {
        switch (bar) {
            case SMALL:
                setSmallCircularProgressBarVisible(true);
                setMiddleCircularProgressBarVisible(false);
                setBigCircularProgressBarVisible(false);
                break;
            case MIDDLE:
                setSmallCircularProgressBarVisible(false);
                setMiddleCircularProgressBarVisible(true);
                setBigCircularProgressBarVisible(false);
                break;
            case BIG:
                setSmallCircularProgressBarVisible(false);
                setMiddleCircularProgressBarVisible(false);
                setBigCircularProgressBarVisible(true);
                break;
        }
    }

    private void setSmallCircularProgressBarVisible(boolean visible) {
        if (visible) {
            determinateSmallCircularProgressBar.setVisibility(View.VISIBLE);
            indeterminateSmallCircularProgressBar.setVisibility(View.VISIBLE);
        } else {
            determinateSmallCircularProgressBar.setVisibility(View.GONE);
            indeterminateSmallCircularProgressBar.setVisibility(View.GONE);
        }
    }

    private void setMiddleCircularProgressBarVisible(boolean visible) {
        if (visible) {
            determinateMiddleCircularProgressBar.setVisibility(View.VISIBLE);
            indeterminateMiddleCircularProgressBar.setVisibility(View.VISIBLE);
        } else {
            determinateMiddleCircularProgressBar.setVisibility(View.GONE);
            indeterminateMiddleCircularProgressBar.setVisibility(View.GONE);
        }
    }

    private void setBigCircularProgressBarVisible(boolean visible) {
        if (visible) {
            determinateBigCircularProgressBar.setVisibility(View.VISIBLE);
            indeterminateBigCircularProgressBar.setVisibility(View.VISIBLE);
        } else {
            determinateBigCircularProgressBar.setVisibility(View.GONE);
            indeterminateBigCircularProgressBar.setVisibility(View.GONE);
        }
    }

    private OnSeekBarChangeListener onSeekBarChangedListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
            updateLinearProgressBarValues(progress);
            updateCircularProgressBarValues(progress);
        }

        @Override
        public void onStartTrackingTouch(final SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(final SeekBar seekBar) {
        }
    };

    private OnSeekBarChangeListener onScaleSeekbarChangedListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(final SeekBar seekBar, final int progress, final boolean fromUser) {
            if (progress >= 0 && progress <= 2) {
                setCircularProgressBarVisibile(ProgressBar.CircularProgressBarSize.values()[progress]);
            }
        }

        @Override
        public void onStartTrackingTouch(final SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(final SeekBar seekBar) {

        }
    };
}