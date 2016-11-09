/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.uit.view.widget.Button;
import com.philips.platform.uit.view.widget.ImageButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class ButtonFragment extends BaseFragment {
    private Drawable shareDrawable;
    private boolean showingIcons;

    @Bind(R.id.toggleicon)
    SwitchCompat toggleIcons;

    @Bind(R.id.toggleextraWide)
    SwitchCompat toggleExtrawide;

    @Bind(R.id.toggleDisable)
    SwitchCompat toggleDisable;

    @Bind(R.id.imageShare)
    ImageButton imageShare;

    @Bind(R.id.quiet_icon_only)
    ImageButton quietIconOnly;

    @Bind(R.id.groupExtraWide)
    ViewGroup groupExtraWide;

    @Bind(R.id.groupDefault)
    ViewGroup groupDefault;

    @Bind(R.id.groupIconOnly)
    ViewGroup groupIconOnly;

    @Bind(R.id.groupLeftAlignedExtraWide)
    ViewGroup groupLeftAlignedExtraWide;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_buttons, container, false);
        shareDrawable = getShareIcon();
        ButterKnife.bind(this, root);
        restoreViews(savedInstanceState);
        imageShare.setImageDrawable(getShareIcon());
        quietIconOnly.setImageResource(R.drawable.ic_share_icon);
        return root;
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
        outState.putBoolean("showExtraWideButtons", toggleExtrawide.isChecked());
        outState.putBoolean("isButtonsEnabled", toggleDisable.isChecked());
        super.onSaveInstanceState(outState);
    }

    public Drawable getShareIcon() {
        return VectorDrawableCompat.create(getResources(), R.drawable.ic_share_icon, getContext().getTheme());
    }

    @OnCheckedChanged(R.id.toggleicon)
    public void toggleIcons(boolean isIconToggleChecked) {
        showingIcons = isIconToggleChecked;
        Drawable drawable = isIconToggleChecked ? shareDrawable : null;
        setIcons(groupExtraWide, drawable);
        setIcons(groupDefault, drawable);
        setIcons(groupLeftAlignedExtraWide, drawable);
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

    @OnCheckedChanged(R.id.toggleextraWide)
    public void toggleExtraWideButtons(boolean toggle) {
        groupDefault.setVisibility(toggle ? View.GONE : View.VISIBLE);
        groupExtraWide.setVisibility(toggle ? View.VISIBLE : View.GONE);
        groupLeftAlignedExtraWide.setVisibility(toggle ? View.VISIBLE : View.GONE);
    }

    @OnCheckedChanged(R.id.toggleDisable)
    public void disableButtons(boolean isChecked) {
        final boolean checked = !toggleDisable.isChecked();
        disableEnableButtons(checked, groupDefault, groupExtraWide, groupIconOnly, groupLeftAlignedExtraWide);
    }

    private void disableEnableButtons(final boolean checked, final ViewGroup... viewGroup) {
        for (final ViewGroup group : viewGroup) {
            for (int j = 0; j < group.getChildCount(); j++) {
                View view = group.getChildAt(j);
                if (view instanceof AppCompatButton) {
                    view.setEnabled(checked);
                }
            }
        }
    }

    @Override
    public int getPageTitle() {
        return R.string.page_tittle_buttons;
    }
}