/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentButtonsAllBinding;
import com.philips.platform.uit.view.widget.Button;

public class ButtonFragment extends BaseFragment {
    public ObservableBoolean isButtonsEnabled = new ObservableBoolean(Boolean.TRUE);
    public ObservableBoolean showExtraWideButtons = new ObservableBoolean(Boolean.TRUE);

    Drawable shareDrawable;
    FragmentButtonsAllBinding fragmentBinding;
    boolean showingIcons;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_buttons_all, container, false);
        fragmentBinding.setFrag(this);
        shareDrawable = getShareIcon();
        restoreViews(savedInstanceState);
        fragmentBinding.imageShare.setImageDrawable(shareDrawable.mutate());
        return fragmentBinding.getRoot();
    }

    private void restoreViews(Bundle savedInstance) {
        if (savedInstance != null) {
            toggleIcons(savedInstance.getBoolean("showingIcons"));
            toggleExtraWideButtons(savedInstance.getBoolean("showExtraWideButtons"));
            disableButtons(!savedInstance.getBoolean("isButtonsEnabled"));
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean("showingIcons", showingIcons);
        outState.putBoolean("showExtraWideButtons", showExtraWideButtons.get());
        outState.putBoolean("isButtonsEnabled", isButtonsEnabled.get());
        super.onSaveInstanceState(outState);
    }

    public Drawable getShareIcon() {
        return VectorDrawableCompat.create(getResources(), R.drawable.share, getContext().getTheme());
    }

    public void toggleIcons(boolean isIconToggleChecked) {
        showingIcons = isIconToggleChecked;
        Drawable drawable = isIconToggleChecked ? shareDrawable : null;
        setIcons(fragmentBinding.groupExtraWide, drawable);
        setIcons(fragmentBinding.groupDefault, drawable);
        setIcons(fragmentBinding.groupLeftAlignedExtraWide, drawable);
    }

    private void setIcons(final ViewGroup buttonLayout, final Drawable drawable) {
        for (int i = 0; i < buttonLayout.getChildCount(); i++) {
            View view = buttonLayout.getChildAt(i);
            Drawable mutateDrawable = drawable;
            if (drawable != null) {
                mutateDrawable = drawable.getConstantState().newDrawable().mutate();
            }
            if (view instanceof Button) {
                ((Button) view).setImageDrawable(mutateDrawable);
            }
        }
    }

    public void toggleExtraWideButtons(boolean toggle) {
        showExtraWideButtons.set(toggle);
    }

    public void disableButtons(boolean isChecked) {
        isButtonsEnabled.set(!isChecked);
    }

    @Override
    public int getPageTitle() {
        return R.string.page_tittle_buttons;
    }
}