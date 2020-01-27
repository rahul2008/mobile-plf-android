/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.mya.base;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.philips.platform.mya.activity.MyaActivity;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

public abstract class MyaBaseFragment extends Fragment implements MyaBaseView {

    public static final String MY_ACCOUNTS_INVOKE_TAG = "My_Accounts_invoke_tag";
    public static final String TAG = MyaBaseFragment.class.getSimpleName();
    private int mEnterAnimation = 0;
    private int mExitAnimation = 0;
    private ActionBarListener mActionbarUpdateListener;

    public abstract int getActionbarTitleResId();

    public abstract String getActionbarTitle(Context context);

    public abstract boolean getBackButtonState();

    private Context context;

    private FragmentLauncher fragmentLauncher;


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

    public void showFragment(Fragment fragment) {
        if (fragmentLauncher == null) {
            exitMyAccounts();
            return;
        }
        final FragmentActivity fragmentActivity = fragmentLauncher.getFragmentActivity();
        int containerId = fragmentLauncher.getParentContainerResourceID();
        if (fragmentActivity != null && !fragmentActivity.isFinishing()) {
            initAnimation(fragmentLauncher.getEnterAnimation(), fragmentLauncher.getExitAnimation(), fragmentActivity);
            addFragment(fragment, fragmentActivity, containerId);
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
                fragmentTransaction.addToBackStack(MY_ACCOUNTS_INVOKE_TAG);
            else
                fragmentTransaction.addToBackStack(simpleName);

            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.getMessage();
        }
    }

    /**
     * Updating action bar title. The text has to be updated at each fragment
     * seletion/creation.
     */
    private void setActionbarTitle() {
        if (mActionbarUpdateListener != null) {
            mActionbarUpdateListener.updateActionBar(getActionbarTitleResId(), getBackButtonState());
            mActionbarUpdateListener.updateActionBar(getActionbarTitle(context), getBackButtonState());
        }
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    public final boolean exitMyAccounts() {
        FragmentActivity activity;
        if (fragmentLauncher == null)
            activity = getActivity();
        else
            activity = fragmentLauncher.getFragmentActivity();

        try {
            if (activity != null && !activity.isFinishing()) {
                if (activity instanceof MyaActivity) {
                    activity.finish();
                } else {
                    FragmentManager fragManager = activity.getSupportFragmentManager();
                    return fragManager.popBackStackImmediate(MY_ACCOUNTS_INVOKE_TAG,
                            FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }
            }
        } catch (IllegalStateException e) {
            e.getMessage();
        }
        return false;
    }

    public void setActionbarUpdateListener(ActionBarListener mActionbarUpdateListener) {
        this.mActionbarUpdateListener = mActionbarUpdateListener;
    }

    public ActionBarListener getActionbarUpdateListener() {
        return mActionbarUpdateListener;
    }

    public FragmentLauncher getFragmentLauncher() {
        return fragmentLauncher;
    }

    public void setFragmentLauncher(FragmentLauncher fragmentLauncher) {
        this.fragmentLauncher = fragmentLauncher;
    }
}
