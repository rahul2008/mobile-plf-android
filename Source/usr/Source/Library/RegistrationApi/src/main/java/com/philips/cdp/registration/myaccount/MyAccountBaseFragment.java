/*
 * Copyright (c) Koninklijke Philips N.V. 2017
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.cdp.registration.myaccount;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

public abstract class MyAccountBaseFragment extends Fragment {

    public static final String MY_ACCOUNTS_INVOKE_TAG = "My_Accounts_invoke_tag";
    public static final String TAG = MyAccountBaseFragment.class.getSimpleName();
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

    public final boolean exitMyAccounts() {
        FragmentActivity activity;
        if (fragmentLauncher == null)
            activity = getActivity();
        else
            activity = fragmentLauncher.getFragmentActivity();

        try {
            if (activity != null && !activity.isFinishing()) {

               if (activity instanceof RegistrationActivity) {
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
