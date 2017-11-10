/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.ui.utils.RLog;

import java.util.HashMap;
import java.util.Map;

public abstract class RegistrationBaseFragment extends Fragment {

    protected int mLeftRightMarginPort;

    protected int mLeftRightMarginLand;

    protected abstract void setViewParams(Configuration config, int width);

    protected abstract void handleOrientation(final View view);

    public abstract int getTitleResourceId();

    public String getTitleResourceText() {
        return null;
    }

    private int mPrevTitleResourceId = -99;

    protected static int mWidth = 0;
    protected static int mHeight = 0;

    private final int JELLY_BEAN = 16;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //  mPrevTitleResourceId = getRegistrationFragment().getResourceID();
        super.onCreate(savedInstanceState);
        setCustomLocale();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onCreate");
        mLeftRightMarginPort = (int) getResources().getDimension(R.dimen.reg_layout_margin_port);
        mLeftRightMarginLand = (int) getResources().getDimension(R.dimen.reg_layout_margin_land);
    }

    protected void setCustomLocale() {
      /* Locale.setDefault(RegistrationHelper.getInstance().getLocale(getContext()));
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = RegistrationHelper.getInstance().getLocale(getContext());
        if(isAdded()) {
            getContext().getResources().updateConfiguration(config,
                    getActivity().getResources().getDisplayMetrics());
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onStart");
    }

    @Override
    public void onResume() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onResume");
        super.onResume();
        setCurrentTitle();
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onDestroy");
        setPrevTiltle();
        super.onDestroy();
    }

    private void setPrevTiltle() {
        RegistrationFragment fragment = (RegistrationFragment) getParentFragment();

        if (null != fragment && null != fragment.getUpdateTitleListener()
                && mPrevTitleResourceId != -99) {
            RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : getFragmentCount" + fragment.getFragmentCount());

            if (fragment.getFragmentCount() > 2) {
                fragment.getUpdateTitleListener().updateActionBar(
                        mPrevTitleResourceId, true);
               /* fragment.getUpdateTitleListener().updateRegistrationTitleWithBack(
                        mPrevTitleResourceId);*/
                fragment.setCurrentTitleResource(mPrevTitleResourceId);
            } else {
                fragment.getUpdateTitleListener().updateActionBar(
                        mPrevTitleResourceId, false);
             /*   fragment.getUpdateTitleListener().updateRegistrationTitle(mPrevTitleResourceId);*/
                fragment.setCurrentTitleResource(mPrevTitleResourceId);
            }

            trackBackActionPage();
            fragment.setResourceID(mPrevTitleResourceId);
        }
    }

    private void trackBackActionPage() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "RegistrationBaseFragment : onDetach");
    }

    private void setCurrentTitle() {

        RegistrationFragment fragment = (RegistrationFragment) getParentFragment();
        if (null != fragment && null != fragment.getUpdateTitleListener()
                && -99 != fragment.getResourceID()) {
            mPrevTitleResourceId = fragment.getResourceID();
        }

        if (null != fragment) {
            if (fragment.getFragmentCount() > 1) {
                if (this instanceof HomeFragment && null != fragment.
                        getUpdateTitleListener()) {
                    fragment.getUpdateTitleListener().updateActionBar(
                            getTitleResourceId(), false);
                   /* fragment.getUpdateTitleListener().updateRegistrationTitle(getTitleResourceId());*/
                } else {
                    if (null != fragment.getUpdateTitleListener()) {
                        fragment.getUpdateTitleListener().updateActionBar(
                                getTitleResourceId(), true);
                        String titleText = getTitleResourceText();
                        if (titleText != null && titleText.length() > 0) {
                            fragment.getUpdateTitleListener().updateActionBar(titleText, false);
                        }
                    }
                /*     fragment.getUpdateTitleListener().updateRegistrationTitleWithBack(
                            getTitleResourceId());*/
                }
            } else {
                if (null != fragment.getUpdateTitleListener()) {
                    fragment.getUpdateTitleListener().updateActionBar(
                            getTitleResourceId(), false);
                    String titleText = getTitleResourceText();
                    if (titleText != null && titleText.length() > 0) {
                        fragment.getUpdateTitleListener().updateActionBar(titleText, false);
                    }
                }
                /*fragment.getUpdateTitleListener().updateRegistrationTitle(getTitleResourceId());*/
            }
            fragment.setResourceID(getTitleResourceId());
            fragment.setCurrentTitleResource(getTitleResourceId());
        }
    }

    public RegistrationFragment getRegistrationFragment() {
        Fragment fragment = getParentFragment();
        if (fragment != null && (fragment instanceof RegistrationFragment)) {
            return (RegistrationFragment) fragment;
        }
        return null;
    }

    protected void consumeTouch(View view) {
        if (view == null)
            return;
        view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }


    protected void applyParams(Configuration config, View view, int width) {
        if (width < dpToPx((int) getResources().getInteger(R.integer.reg_layout_max_width_648))) {
            if (view instanceof RecyclerView) {
                return;
            }
            applyDefaultMargin(view);
        } else {
            setMaxWidth(view);
        }
    }

    private void setMaxWidth(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.width = dpToPx((int) getResources().getInteger(R.integer.reg_layout_max_width_648));
        view.setLayoutParams(params);
    }

    private void applyDefaultMargin(View view) {
        ViewGroup.MarginLayoutParams mParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        mParams.leftMargin = mParams.rightMargin = dpToPx((int) getResources().getInteger(R.integer.reg_layout_margin_16));
        view.setLayoutParams(mParams);
    }

    private int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    protected void trackPage(String currPage) {
        AppTagging.trackPage(currPage);
    }

    protected void trackActionStatus(String state, String key, String value) {
        AppTagging.trackAction(state, key, value);
    }

    protected void trackActionForRemarkettingOption(String state) {
        AppTagging.trackAction(state, null, null);
    }

    protected void trackActionForAcceptTermsOption(String state) {
        AppTagging.trackAction(state, null, null);
    }

    protected void trackMultipleActionsRegistration() {
        Map<String, String> map = new HashMap<String, String>();
        map.put(AppTagingConstants.REGISTRATION_CHANNEL, AppTagingConstants.MY_PHILIPS);
        map.put(AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.START_USER_REGISTRATION);
        AppTagging.trackMultipleActions(AppTagingConstants.SEND_DATA, map);
    }

    protected void trackMultipleActionsLogin(String providerName) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(AppTagingConstants.LOGIN_CHANNEL, providerName);
        map.put(AppTagingConstants.SPECIAL_EVENTS, AppTagingConstants.LOGIN_START);
        AppTagging.trackMultipleActions(AppTagingConstants.SEND_DATA, map);
    }

    protected void trackMultipleActionsMap(String state, HashMap<String, String> map) {
        AppTagging.trackMultipleActions(state, map);
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


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setCustomLocale();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
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

}
