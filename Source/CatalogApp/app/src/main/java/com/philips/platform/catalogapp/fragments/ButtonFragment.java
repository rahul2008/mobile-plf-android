/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentButtonsAllBinding;
import com.philips.platform.uit.view.widget.Button;

public class ButtonFragment extends Fragment {
    public ObservableBoolean isButtonsEnabled = new ObservableBoolean(Boolean.TRUE);
    public ObservableBoolean showExtraWideButtons = new ObservableBoolean(Boolean.TRUE);

    Drawable shareDrwable;
    FragmentButtonsAllBinding fragmentBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_buttons_all, container, false);
        fragmentBinding.setFrag(this);
        shareDrwable = getShareIcon();
        fragmentBinding.imageShare.setImageDrawable(shareDrwable.mutate());
        return fragmentBinding.getRoot();
    }

    public Drawable getShareIcon() {
        return VectorDrawableCompat.create(getResources(), R.drawable.share, getContext().getTheme());
    }

    public void toggleIcons(boolean isIconToggleChecked) {
        ViewGroup buttonLayout = showExtraWideButtons.get()? fragmentBinding.groupExtraWide : fragmentBinding.groupDefault;
        Drawable drawable = isIconToggleChecked ? shareDrwable : null;
        for (int i = 0; i < buttonLayout.getChildCount() ; i++) {
            View view = buttonLayout.getChildAt(i);
            if(view instanceof Button) {
                ((Button) view).setImageDrawable(drawable);
            }
        }
    }

    public void toggleExtraWideButtons(boolean toggle) {
        showExtraWideButtons.set(toggle);
    }

    public void disableButtons(boolean isChecked) {
        isButtonsEnabled.set(!isChecked);
    }
}