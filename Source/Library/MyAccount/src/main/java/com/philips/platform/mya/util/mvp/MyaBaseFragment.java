/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.util.mvp;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.mya.MyaConstants;
import com.philips.platform.mya.activity.MyaActivity;
import com.philips.platform.mya.util.MYALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

// TODO: Deepthi, change package name to base
public abstract class MyaBaseFragment extends Fragment implements MyaBaseView {


    private int mEnterAnimation = 0;
    private int mExitAnimation = 0;
    private String TAG = MyaBaseFragment.class.getSimpleName();
    private ActionBarListener mActionbarUpdateListener;

    public abstract int getActionbarTitleResId();

    public abstract String getActionbarTitle();

    public abstract boolean getBackButtonState();

    private Context context;

    @Override
    public void onStart() {
        super.onStart();
        setActionbarTitle();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = getActivity();
    }

    private void initAnimation(final int startAnimation, final int endAnimation, final FragmentActivity fragmentActivity) {
        final String startAnim;
        final String endAnim;
        if ((startAnimation != 0) && (endAnimation != 0)) {
            startAnim = fragmentActivity.getResources().getResourceName(startAnimation);
            endAnim = fragmentActivity.getResources().getResourceName(endAnimation);

            String packageName = fragmentActivity.getPackageName();
            mEnterAnimation = fragmentActivity.getResources().getIdentifier(startAnim,
                    "anim", packageName);
            mExitAnimation = fragmentActivity.getResources().getIdentifier(endAnim, "anim",
                    packageName);
        }
    }

    public void showFragment(Fragment fragment, FragmentLauncher fragmentLauncher) {
        final FragmentActivity fragmentActivity = fragmentLauncher.getFragmentActivity();
        mActionbarUpdateListener = fragmentLauncher.getActionbarListener();
        int containerId = fragmentLauncher.getParentContainerResourceID();
        if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
            initAnimation(fragmentLauncher.getEnterAnimation(), fragmentLauncher.getExitAnimation(), fragmentActivity);
            addFragment(fragment, fragmentActivity, containerId);
        }
    }

    public void showFragment(Fragment fragment) {
        final FragmentActivity fragmentActivity = getActivity();
        if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
            addFragment(fragment, fragmentActivity, getId());
        }
    }

    private void addFragment(final Fragment fragment, final FragmentActivity fragmentActivity, final int containerId) {
        try {
            FragmentTransaction fragmentTransaction = fragmentActivity
                    .getSupportFragmentManager().beginTransaction();
            if (mEnterAnimation != 0 && mExitAnimation != 0) {
                fragmentTransaction.setCustomAnimations(mEnterAnimation,
                        mExitAnimation, mEnterAnimation, mExitAnimation);
            }
            final String simpleName = fragment.getClass().getSimpleName();
            fragmentTransaction.replace(containerId, fragment, simpleName);
            Fragment currentFrag = fragmentActivity.getSupportFragmentManager()
                    .findFragmentById(getId());

            if (!(currentFrag instanceof MyaBaseFragment))
                fragmentTransaction.addToBackStack(MyaConstants.MY_ACCOUNTS_CALLEE_TAG);
            else
                fragmentTransaction.addToBackStack(simpleName);

            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            MYALog.e(TAG, e.getMessage());
        }
    }

    /**
     * Updating action bar title. The text has to be updated at each fragment
     * seletion/creation.
     */
    private void setActionbarTitle() {
        if (mActionbarUpdateListener != null) {
            mActionbarUpdateListener.updateActionBar(getActionbarTitleResId(), getBackButtonState());
            mActionbarUpdateListener.updateActionBar(getActionbarTitle(), getBackButtonState());
        }
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    public boolean exitMyAccounts() {
        final FragmentActivity activity = getActivity();
        try {
            if (activity != null && !activity.isFinishing()) {
                if (activity instanceof MyaActivity) {
                    activity.finish();
                } else {
                    FragmentManager fragManager = activity.getSupportFragmentManager();
                    return fragManager.popBackStackImmediate(MyaConstants.MY_ACCOUNTS_CALLEE_TAG,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        } catch (IllegalStateException e) {
            MYALog.e(TAG, e.getMessage());
        }
        return false;
    }
}
