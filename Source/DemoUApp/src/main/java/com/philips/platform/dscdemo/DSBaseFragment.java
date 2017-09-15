/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

public abstract class DSBaseFragment extends Fragment implements BackEventListener {

    private static ActionBarListener mActionbarUpdateListener;
    private static ProgressDialog mProgressDialog;
    private Context mContext;

    public abstract int getActionbarTitleResId();

    public abstract String getActionbarTitle();

    public abstract boolean getBackButtonState();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
        setActionbarTitle();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clearProgressDialog();
    }

    public void showFragment(Fragment fragment, FragmentLauncher fragmentLauncher) {
        FragmentActivity fragmentActivity = fragmentLauncher.getFragmentActivity();
        mActionbarUpdateListener = fragmentLauncher.getActionbarListener();

        int containerId = fragmentLauncher.getParentContainerResourceID();
        addFragment(fragment, fragmentActivity, containerId);
    }

    public void showFragment(DSBaseFragment fragment) {
        addFragment(fragment, getActivity(), getId());
    }

    private void setActionbarTitle() {
        if (mActionbarUpdateListener != null) {
            mActionbarUpdateListener.updateActionBar(this.getActionbarTitleResId(), this.getBackButtonState());
            mActionbarUpdateListener.updateActionBar(this.getActionbarTitle(), this.getBackButtonState());
        }
    }

    private void addFragment(Fragment fragment, FragmentActivity fragmentActivity, int containerId) {
        try {
            FragmentTransaction fragmentTransaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
            String simpleName = fragment.getClass().getSimpleName();
            fragmentTransaction.replace(containerId, fragment, simpleName);
            fragmentTransaction.addToBackStack(simpleName);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException exception) {
            exception.printStackTrace();
        }
    }

    /*public void removeCurrentFragment() {
        getFragmentManager().popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
    }*/

    /*public void showProgressDialog(final String message) {
        if (getActivity() != null && !(getActivity().isFinishing())) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mProgressDialog == null) {
                        mProgressDialog = new ProgressDialog(mContext);
                    }
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.setMessage(message);
                    if (mProgressDialog != null && !mProgressDialog.isShowing() && !(((Activity) mContext).isFinishing())) {
                        mProgressDialog.show();
                    }
                }
            });
        }
    }

    public void dismissProgressDialog() {
        if (getActivity() != null && !(getActivity().isFinishing())) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isProgressShowing() && !(((Activity) mContext).isFinishing())) {
                        mProgressDialog.dismiss();
                    }
                }
            });
        }
    }*/

    public boolean isProgressShowing() {
        return (mProgressDialog != null && mProgressDialog.isShowing());
    }

    public void clearProgressDialog() {
        if (isProgressShowing() && !(((Activity) mContext).isFinishing())) {
            mProgressDialog.dismiss();
        }
        mProgressDialog = null;
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

}