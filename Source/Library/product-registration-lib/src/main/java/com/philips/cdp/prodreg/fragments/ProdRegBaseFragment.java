package com.philips.cdp.prodreg.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.prodreg.listener.ActionbarUpdateListener;
import com.philips.cdp.prodreg.ui.FragmentLauncher;
import com.philips.cdp.prodreg.util.ProdRegConstants;
import com.philips.cdp.product_registration_lib.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class ProdRegBaseFragment extends Fragment {

    private static String TAG = InitialFragment.class.getSimpleName();
    private static int mContainerId = 0;
    private static int mEnterAnimation = 0;
    private static int mExitAnimation = 0;
    private static FragmentActivity mFragmentActivityContext = null;
    private static FragmentActivity mActivityContext = null;
    private FragmentManager fragmentManager;
    private ActionbarUpdateListener mActionbarUpdateListener;
    private FragmentLauncher mFragmentLauncher;
    private ImageView mBackToHome = null;
    private ImageView mHomeIcon = null;

    public abstract String getActionbarTitle();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        setActionbarTitle();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideKeyboard();
    }

    public void showFragment(Fragment fragment, FragmentLauncher fragmentLauncher,
                             int startAnimation, int endAnimation) {
        Log.i("testing", "DigitalCare Base Fragment -- Fragment Invoke");
        mFragmentLauncher = fragmentLauncher;
        mContainerId = fragmentLauncher.getParentContainerResourceID();
        mActivityContext = fragmentLauncher.getFragmentActivity();
        mActionbarUpdateListener = fragmentLauncher.getActionbarUpdateListener();

        String startAnim = null;
        String endAnim = null;

        if ((startAnimation != 0) && (endAnimation != 0)) {
            startAnim = mActivityContext.getResources().getResourceName(startAnimation);
            endAnim = mActivityContext.getResources().getResourceName(endAnimation);

            String packageName = mActivityContext.getPackageName();
            mEnterAnimation = mActivityContext.getResources().getIdentifier(startAnim,
                    "anim", packageName);
            mExitAnimation = mActivityContext.getResources().getIdentifier(endAnim, "anim",
                    packageName);
        }

        try {
            FragmentTransaction fragmentTransaction = mActivityContext
                    .getSupportFragmentManager().beginTransaction();
            if (mEnterAnimation != 0 && mExitAnimation != 0) {
                fragmentTransaction.setCustomAnimations(mEnterAnimation,
                        mExitAnimation, mEnterAnimation, mExitAnimation);
            }
            fragmentTransaction.replace(mContainerId, fragment, ProdRegConstants.PROD_REG_FRAGMENT_TAG);
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    protected void showFragment(Fragment fragment) {
        int containerId = R.id.mainContainer;

        if (mContainerId != 0) {
            containerId = mContainerId;
            mFragmentActivityContext = mActivityContext;
        } else {
            enableActionBarLeftArrow();
            InputMethodManager imm = (InputMethodManager) mFragmentActivityContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (mFragmentActivityContext.getWindow() != null
                    && mFragmentActivityContext.getWindow().getCurrentFocus() != null) {
                imm.hideSoftInputFromWindow(mFragmentActivityContext
                        .getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        try {
            FragmentTransaction fragmentTransaction = mFragmentActivityContext
                    .getSupportFragmentManager().beginTransaction();
            if (mEnterAnimation != 0 && mExitAnimation != 0) {
                fragmentTransaction.setCustomAnimations(mEnterAnimation,
                        mExitAnimation, mEnterAnimation, mExitAnimation);
            }
            fragmentTransaction.replace(containerId, fragment, ProdRegConstants.PROD_REG_FRAGMENT_TAG);
            fragmentTransaction.hide(this);
            fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            Log.e(TAG, "IllegalStateException" + e.getMessage());
            e.printStackTrace();
        }
    }

    protected void enableActionBarLeftArrow(ImageView hambergermenu, ImageView backarrow) {
        Log.d(TAG, "BackArrow Enabled");
        if (hambergermenu != null && backarrow != null) {
            backarrow.setVisibility(View.VISIBLE);
            backarrow.bringToFront();
        }
    }

    protected void hideActionBarIcons(ImageView hambergermenu, ImageView backarrow) {
        Log.d(TAG, "Hide menu & arrow icons");
        if (hambergermenu != null && backarrow != null) {
            hambergermenu.setVisibility(View.GONE);
            backarrow.setVisibility(View.GONE);
        }
    }

    protected boolean backStackFragment() {
        if (fragmentManager == null && mActivityContext != null) {
            fragmentManager = mActivityContext.getSupportFragmentManager();
        } else if (fragmentManager == null) {
            fragmentManager = mFragmentActivityContext.getSupportFragmentManager();
        }
        fragmentManager.popBackStack();
        return false;
    }

    protected void hideKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * Updating action bar title. The text has to be updated at each fragment
     * seletion/creation.
     */
    private void setActionbarTitle() {
        if (mContainerId == 0) {
            ((TextView) getActivity().findViewById(
                    R.id.action_bar_title)).setText(getActionbarTitle());
        } else {
            updateActionbar();
        }
    }

    private void updateActionbar() {
        if (this.getClass().getSimpleName()
                .equalsIgnoreCase(InitialFragment.class.getSimpleName())) {
            mActionbarUpdateListener.updateActionbar(getActionbarTitle(), true);
        } else {
            mActionbarUpdateListener.updateActionbar(getActionbarTitle(), false);
        }
    }

    private void enableActionBarLeftArrow() {
        mBackToHome = (ImageView) mFragmentActivityContext
                .findViewById(R.id.back_to_home_img);
        mHomeIcon = (ImageView) mFragmentActivityContext
                .findViewById(R.id.home_icon);
        mHomeIcon.setVisibility(View.GONE);
        mBackToHome.setVisibility(View.VISIBLE);
        mBackToHome.bringToFront();
    }
}

