/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentProgressBarBinding;

public class ProgressBarFragment extends BaseFragment {

    public ObservableBoolean showLabels = new ObservableBoolean(Boolean.TRUE);
    public ObservableInt progress = new ObservableInt(50);
    public ObservableInt secondaryProgress = new ObservableInt(progress.get() + 10);

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentProgressBarBinding fragmentProgressBarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_progress_bar, container, false);
        fragmentProgressBarBinding.setFrag(this);

        return fragmentProgressBarBinding.getRoot();
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_progress_bar;
    }

    public void toggleLabels(boolean checked) {
        showLabels.set(checked);
    }

    public void setProgress(int progress, boolean frmUser) {
        if (frmUser) {
            this.progress.set(progress);
            this.secondaryProgress.set(progress + 10);
        }
    }
}