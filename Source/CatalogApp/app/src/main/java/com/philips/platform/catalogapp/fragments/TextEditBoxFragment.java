/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
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
import com.philips.platform.catalogapp.databinding.FragmentTexteditboxBinding;

public class TextEditBoxFragment extends BaseFragment {
    public ObservableBoolean disableEditBoxes = new ObservableBoolean(Boolean.FALSE);
    public ObservableBoolean isWithLabel = new ObservableBoolean(Boolean.TRUE);

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final FragmentTexteditboxBinding texteditboxBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_texteditbox, container, false);
        texteditboxBinding.setTexteditBoxfragment(this);

        return texteditboxBinding.getRoot();
    }

    private void restoreViews(Bundle savedInstance) {
        if (savedInstance != null) {
            disabledEditboxes(savedInstance.getBoolean("disableEditBoxes"));
            showWithLabel(savedInstance.getBoolean("isWithLabel"));
        }
    }
    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean("disableEditBoxes", disableEditBoxes.get());
        outState.putBoolean("isWithLabel", isWithLabel.get());
        super.onSaveInstanceState(outState);
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_textbox;
    }

    public void disabledEditboxes(boolean toggle) {
        disableEditBoxes.set(toggle);
    }

    public void showWithLabel(boolean isChecked) {
        isWithLabel.set(isChecked);
    }
}
