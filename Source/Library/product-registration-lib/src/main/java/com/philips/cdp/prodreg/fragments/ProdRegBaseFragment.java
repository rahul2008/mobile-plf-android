package com.philips.cdp.prodreg.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.philips.cdp.prodreg.alert.ModalAlertDemoFragment;
import com.philips.cdp.prodreg.launcher.FragmentLauncher;
import com.philips.cdp.prodreg.listener.ActionbarUpdateListener;
import com.philips.cdp.product_registration_lib.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class ProdRegBaseFragment extends Fragment {

    private static String TAG = ProdRegBaseFragment.class.getSimpleName();
    private static int mContainerId = 0;
    private static int mEnterAnimation = 0;
    private static int mExitAnimation = 0;
    private static FragmentActivity mFragmentActivityContext = null;
    private static FragmentActivity mActivityContext = null;
    private static ActionbarUpdateListener mActionbarUpdateListener;
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
        Log.i("testing", "Product Registration Base Fragment -- Fragment Invoke");
        mContainerId = fragmentLauncher.getParentContainerResourceID();
        mActivityContext = fragmentLauncher.getFragmentActivity();
        mActionbarUpdateListener = fragmentLauncher.getActionbarUpdateListener();

        String startAnim;
        String endAnim;

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
            fragmentTransaction.replace(mContainerId, fragment, fragment.getClass().toString());
            if (!(fragment instanceof ProdRegProcessFragment))
                fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commitAllowingStateLoss();
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
            mFragmentActivityContext = getActivity();
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
            fragmentTransaction.replace(containerId, fragment, fragment.getClass().toString());
            fragmentTransaction.hide(this);
            if (!(fragment instanceof ProdRegProcessFragment))
                fragmentTransaction.addToBackStack(fragment.getTag());
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            Log.e(TAG, "IllegalStateException" + e.getMessage());
            e.printStackTrace();
        }
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
        mActionbarUpdateListener.updateActionbar(getActionbarTitle());
    }

    protected void showAlert(final String title, final String description) {
        final ModalAlertDemoFragment modalAlertDemoFragment = new ModalAlertDemoFragment();
        modalAlertDemoFragment.show(getActivity().getSupportFragmentManager(), "dialog");
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                modalAlertDemoFragment.setTitle(title);
                modalAlertDemoFragment.setDescription(description);
            }
        }, 200);
    }

    protected boolean backStackFragment() {
        FragmentManager fragmentManager = mFragmentActivityContext.getSupportFragmentManager();
        if (fragmentManager == null && mActivityContext != null) {
            fragmentManager = mActivityContext.getSupportFragmentManager();
        } else if (fragmentManager == null) {
            fragmentManager = mFragmentActivityContext.getSupportFragmentManager();
        }
        fragmentManager.popBackStack();
        return false;
    }

    public void onBackTapped(Fragment fragment, String s) {
        if (fragment instanceof ProdRegConnectionFragment) {
            FragmentManager fragmentManager = mFragmentActivityContext.getSupportFragmentManager();
            fragmentManager.popBackStackImmediate(s, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }
}

