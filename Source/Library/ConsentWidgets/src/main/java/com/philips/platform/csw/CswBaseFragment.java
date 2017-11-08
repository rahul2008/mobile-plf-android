/*
 * Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * * in whole or in part is prohibited without the prior written
 * * consent of the copyright holder.
 * /
 */

package com.philips.platform.csw;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;


public abstract class CswBaseFragment extends Fragment {

    protected int mLeftRightMarginPort;

    protected int mLeftRightMarginLand;

    protected abstract void setViewParams(Configuration config, int width);

    protected abstract void handleOrientation(final View view);

    public abstract int getTitleResourceId();

    protected static int mWidth = 0;
    protected static int mHeight = 0;

    private final int JELLY_BEAN = 16;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLeftRightMarginPort = (int) getResources().getDimension(com.philips.cdp.registration.R.dimen.reg_layout_margin_port);
        mLeftRightMarginLand = (int) getResources().getDimension(com.philips.cdp.registration.R.dimen.reg_layout_margin_land);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setCurrentTitle();
    }

    private void setCurrentTitle() {
        CswFragment fragment = getCswFragment();
        if (fragment != null) {
            if (fragment.getUpdateTitleListener() != null) {
                fragment.getUpdateTitleListener().updateActionBar(getTitleResourceId(), (fragment.getFragmentCount() > 0));
            }

            fragment.setResourceID(getTitleResourceId());
        }
    }

    public CswFragment getCswFragment() {
        Fragment fragment = overridableGetParentFragment();
        if (fragment != null && (fragment instanceof CswFragment)) {
            return (CswFragment) fragment;
        }
        return null;
    }

    protected void consumeTouch(View view) {
        if (view == null)
            return;
        view.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }


    protected void applyParams(Configuration config, View view, int width) {
        if (width < dpToPx((int) getResources().getInteger(com.philips.cdp.registration.R.integer.reg_layout_max_width_648))) {
            applyDefaultMargin(view);
        } else {
            setMaxWidth(view);
        }
    }

    private void setMaxWidth(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = dpToPx((int) getResources().getInteger(com.philips.cdp.registration.R.integer.reg_layout_max_width_648));
        view.setLayoutParams(params);
    }

    private void applyDefaultMargin(View view) {
        ViewGroup.MarginLayoutParams mParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        mParams.leftMargin = mParams.rightMargin = dpToPx((int) getResources().getInteger(com.philips.cdp.registration.R.integer.reg_layout_margin_16));
        view.setLayoutParams(mParams);
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


    protected void handleOrientationOnView(final View view) {
        if (null == view) {
            return;
        }
        if (mWidth == 0 && mHeight == 0) {
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    if (isAdded()) {
                        Configuration config = getResources().getConfiguration();
                        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                            mWidth = view.getWidth();
                            mHeight = view.getHeight();
                        } else {
                            mWidth = view.getHeight();
                            mHeight = view.getWidth();
                        }

                        if (Build.VERSION.SDK_INT < JELLY_BEAN) {
                            view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                        setViewParams(getResources().getConfiguration(), view.getWidth());
                    }
                }
            });
        } else {
            if (isAdded()) {
                Configuration config = getResources().getConfiguration();
                if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    setViewParams(getResources().getConfiguration(), mWidth);
                } else {
                    setViewParams(getResources().getConfiguration(), mHeight);
                }
            }
        }
    }

    public void setCustomParams(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setViewParams(config, mWidth);
        } else {
            setViewParams(config, mHeight);
        }
    }

    protected void scrollViewAutomatically(final View view, final ScrollView scrollView) {
        view.requestFocus();
        if (scrollView != null) {
            scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            scrollView.scrollTo(0, view.getTop());
                        }
                    });
                    if (Build.VERSION.SDK_INT < JELLY_BEAN) {
                        view.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            });
        }
    }

    protected Fragment overridableGetParentFragment() {
        return getParentFragment();
    }
}
