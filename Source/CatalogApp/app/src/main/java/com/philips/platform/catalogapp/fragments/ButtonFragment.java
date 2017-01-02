/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.ProgressIndicatorButton;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class ButtonFragment extends BaseFragment {
    private Drawable shareDrawable;
    private boolean showingIcons;
    private Handler handler = new Handler();

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

    @Bind(R.id.groupProgressButtonsNormal)
    ViewGroup groupProgressButtonNormal;

    @Bind(R.id.groupProgressButtonsExtraWide)
    ViewGroup groupProgressButtonExtraWide;

    @Bind(R.id.progressButtonsNormalDeterminate)
    ProgressIndicatorButton progressIndicatorButtonDeterminate;

    @Bind(R.id.progressButtonsNormalIndeterminate)
    ProgressIndicatorButton progressIndicatorButtonIndeterminate;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_buttons, container, false);
        shareDrawable = getShareIcon();
        ButterKnife.bind(this, root);
        toggleIcons(toggleIcons.isChecked());
        restoreViews(savedInstanceState);
        imageShare.setImageDrawable(shareDrawable);
        //Need to create a new drawable instead of using old stored shareDrawable.
        quietIconOnly.setImageDrawable(getShareIcon());

        setOnClickListeners();
        return root;
    }

    private void setOnClickListeners() {
        addOnClickListenerToChilderen(groupProgressButtonExtraWide, buttonOnClickListener);
        progressIndicatorButtonDeterminate.setOnClickListener(buttonOnClickListener);
        progressIndicatorButtonIndeterminate.setOnClickListener(buttonOnClickListener);
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

        hideAllProgressIndicators();
    }

    public Drawable getShareIcon() {
        return VectorDrawableCompat.create(getResources(), R.drawable.ic_share_icon, getContext().getTheme()).mutate();
    }

    @OnCheckedChanged(R.id.toggleicon)
    public void toggleIcons(boolean isIconToggleChecked) {
        hideAllProgressIndicators();

        showingIcons = isIconToggleChecked;
        Drawable drawable = isIconToggleChecked ? shareDrawable : null;
        setIcons(groupExtraWide, drawable);
        setIcons(groupDefault, drawable);
        setIcons(groupLeftAlignedExtraWide, drawable);
        setIcons(groupProgressButtonExtraWide, drawable);
        progressIndicatorButtonDeterminate.setDrawable(drawable);
        progressIndicatorButtonIndeterminate.setDrawable(drawable);
    }

    private void hideAllProgressIndicators() {
        handler.removeCallbacksAndMessages(null);

        hideProgressIndicatorOnButtonGroup(groupProgressButtonExtraWide);
        progressIndicatorButtonDeterminate.hideProgressIndicator();
        progressIndicatorButtonIndeterminate.hideProgressIndicator();
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
            } else if (view instanceof ProgressIndicatorButton) {
                ((ProgressIndicatorButton) view).setDrawable(mutateDrawable);
            }
        }
    }

    private void addOnClickListenerToChilderen(final ViewGroup buttonLayout, final View.OnClickListener listener) {
        for (int i = 0; i < buttonLayout.getChildCount(); i++) {
            View view = buttonLayout.getChildAt(i);
            if (view instanceof ProgressIndicatorButton) {
                view.setOnClickListener(listener);
            }
        }
    }

    @OnCheckedChanged(R.id.toggleextraWide)
    public void toggleExtraWideButtons(boolean toggle) {
        hideAllProgressIndicators();

        groupDefault.setVisibility(toggle ? View.GONE : View.VISIBLE);
        groupExtraWide.setVisibility(toggle ? View.VISIBLE : View.GONE);
        groupLeftAlignedExtraWide.setVisibility(toggle ? View.VISIBLE : View.GONE);
        groupProgressButtonExtraWide.setVisibility(toggle ? View.VISIBLE : View.GONE);
        groupProgressButtonNormal.setVisibility(toggle ? View.GONE : View.VISIBLE);
    }

    @OnCheckedChanged(R.id.toggleDisable)
    public void disableButtons(boolean isChecked) {
        hideAllProgressIndicators();

        final boolean checked = !toggleDisable.isChecked();
        disableEnableButtons(checked, groupDefault, groupExtraWide, groupIconOnly, groupLeftAlignedExtraWide, groupProgressButtonExtraWide);
        progressIndicatorButtonIndeterminate.setEnabled(checked);
        progressIndicatorButtonDeterminate.setEnabled(checked);
    }

    private void disableEnableButtons(final boolean checked, final ViewGroup... viewGroup) {
        for (final ViewGroup group : viewGroup) {
            for (int j = 0; j < group.getChildCount(); j++) {
                View view = group.getChildAt(j);
                if (view instanceof AppCompatButton) {
                    view.setEnabled(checked);
                } else if (view instanceof ProgressIndicatorButton) {
                    view.setEnabled(checked);
                }
            }
        }
    }

    private void hideProgressIndicatorOnButtonGroup(final ViewGroup... viewGroup) {
        for (final ViewGroup group : viewGroup) {
            for (int j = 0; j < group.getChildCount(); j++) {
                View view = group.getChildAt(j);
                if (view instanceof ProgressIndicatorButton) {
                    ((ProgressIndicatorButton) view).hideProgressIndicator();
                }
            }
        }
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_buttons;
    }

    private View.OnClickListener buttonOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            if (v instanceof ProgressIndicatorButton) {
                ((ProgressIndicatorButton) v).showProgressIndicator();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((ProgressIndicatorButton) v).hideProgressIndicator();
                    }
                }, 5000);
            }
        }
    };
}