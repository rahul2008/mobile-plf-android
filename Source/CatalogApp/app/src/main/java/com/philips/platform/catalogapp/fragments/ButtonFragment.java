/**
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */
package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentButtonsBinding;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ProgressBarButton;

public class ButtonFragment extends BaseFragment {
    private static final int PROGRESS_INDICATOR_VISIBILITY_TIME = 6000;
    private static final int PROGRESS_INDICATOR_PROGRESS_OFFSET = 20;
    private static final int PROGRESS_INDICATOR_PROGRESS_UPDATE_TIME = 1000;
    public ObservableBoolean isButtonsEnabled = new ObservableBoolean(Boolean.TRUE);
    public ObservableBoolean showExtraWideButtons = new ObservableBoolean(Boolean.TRUE);
    public ObservableBoolean showingIcons = new ObservableBoolean(Boolean.TRUE);
    private Drawable shareDrawable;
    private Handler handler = new Handler();
    private FragmentButtonsBinding fragmentBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_buttons, container, false);
        fragmentBinding.setFrag(this);
        shareDrawable = getShareIcon();
        restoreViews(savedInstanceState);
        fragmentBinding.imageShare.setImageDrawable(shareDrawable);
        fragmentBinding.quietIconOnly.setVectorResource(R.drawable.ic_share_icon);
        fragmentBinding.socialIconFacebook.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.uid_social_media_facebook_icon, getContext().getTheme()));
        fragmentBinding.socialIconTwitter.setImageDrawable(VectorDrawableCompat.create(getResources(), R.drawable.uid_social_media_wechat_icon, getContext().getTheme()));

        return fragmentBinding.getRoot();
    }

    public void showGridFragment() {
        showFragment(new SocialIconsListFragment());
    }

    private void restoreViews(Bundle savedInstance) {
        if (savedInstance != null) {
            toggleIcons(savedInstance.getBoolean("showingIcons"));
            toggleExtraWideButtons(savedInstance.getBoolean("showExtraWideButtons"));
            disableButtons(!savedInstance.getBoolean("isButtonsEnabled"));
        } else {
            toggleIcons(showingIcons.get());
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean("showingIcons", showingIcons.get());
        outState.putBoolean("showExtraWideButtons", showExtraWideButtons.get());
        outState.putBoolean("isButtonsEnabled", isButtonsEnabled.get());
        super.onSaveInstanceState(outState);

        hideAllProgressIndicators();
    }

    public Drawable getShareIcon() {
        return VectorDrawableCompat.create(getResources(), R.drawable.ic_share_icon, getContext().getTheme());
    }

    public void toggleIcons(boolean isIconToggleChecked) {
        hideAllProgressIndicators();

        showingIcons.set(isIconToggleChecked);
        Drawable drawable = isIconToggleChecked ? shareDrawable : null;

        setIcons(fragmentBinding.groupExtraWide, drawable);
        setIcons(fragmentBinding.groupDefault, drawable);
        setIcons(fragmentBinding.groupLeftAlignedExtraWide, drawable);
        setIcons(fragmentBinding.groupProgressButtonsExtraWide, drawable);
        fragmentBinding.progressButtonsNormalIndeterminate.setDrawable(drawable);
        fragmentBinding.progressButtonsNormalDeterminate.setDrawable(drawable);
    }

    private void hideAllProgressIndicators() {
        handler.removeCallbacksAndMessages(null);
        if (fragmentBinding != null) {
            fragmentBinding.progressButtonsNormalDeterminate.hideProgressIndicator();
            fragmentBinding.progressButtonsNormalIndeterminate.hideProgressIndicator();
            fragmentBinding.buttonsProgressIndicatorExtraWideDeterminate.hideProgressIndicator();
            fragmentBinding.buttonsProgressIndicatorExtraWideIndeterminate.hideProgressIndicator();
        }
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
            } else if (view instanceof ProgressBarButton) {
                ((ProgressBarButton) view).setDrawable(mutateDrawable);
            }
        }
    }

    public void toggleExtraWideButtons(boolean toggle) {
        hideAllProgressIndicators();
        showExtraWideButtons.set(toggle);
    }

    public void disableButtons(boolean isChecked) {
        hideAllProgressIndicators();
        isButtonsEnabled.set(!isChecked);
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_buttons;
    }

    public void onProgressIndicatorButtonClicked(final View v) {
        if (v instanceof ProgressBarButton) {
            ((ProgressBarButton) v).showProgressIndicator();

            startDeterminateProgressUpdate(v);

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((ProgressBarButton) v).hideProgressIndicator();
                }
            }, PROGRESS_INDICATOR_VISIBILITY_TIME);
        }
    }

    private void startDeterminateProgressUpdate(final View view) {
        new CountDownTimer(PROGRESS_INDICATOR_VISIBILITY_TIME, PROGRESS_INDICATOR_PROGRESS_UPDATE_TIME) {
            public void onTick(long millisUntilFinished) {
                long progress = ((PROGRESS_INDICATOR_VISIBILITY_TIME - millisUntilFinished) / 50) + PROGRESS_INDICATOR_PROGRESS_OFFSET;
                ((ProgressBarButton) view).setProgress((int) progress);
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }
}