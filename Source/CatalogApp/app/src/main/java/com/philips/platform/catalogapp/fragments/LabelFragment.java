/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentLabelsBinding;

public class LabelFragment extends BaseFragment {
    public ObservableField<String> description = new ObservableField<>("0");
    public ObservableInt soundProgress = new ObservableInt(0);

    private static final int SOUND_PROGRESS_JUMP = 25;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        FragmentLabelsBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_labels, container, false);
        binding.setLabelFrag(this);
        restoreUI(savedInstanceState);
        return binding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }


    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putInt("progress", Integer.valueOf(description.get()));
        outState.putInt("soundProgress", soundProgress.get());
        super.onSaveInstanceState(outState);
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_label;
    }

    public void setProgress(int progress) {
        description.set(String.valueOf(progress));
    }

    public void setSoundProgress(int progress, boolean frmUser) {
        if (frmUser) {
            soundProgress.set(progress);
        }
    }

    public void incrementSound() {
        soundProgress.set(Math.min(100, soundProgress.get() + SOUND_PROGRESS_JUMP));
    }

    public void decrementSound() {
        soundProgress.set(Math.max(0, soundProgress.get() - SOUND_PROGRESS_JUMP));
    }

    private void restoreUI(final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            setProgress(savedInstanceState.getInt("progress"));
            soundProgress.set(savedInstanceState.getInt("soundProgress"));
        }
    }
}