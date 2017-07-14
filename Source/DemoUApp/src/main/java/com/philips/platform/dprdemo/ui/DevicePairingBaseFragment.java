/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dprdemo.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.dprdemo.R;
import com.philips.platform.dprdemo.utils.NetworkChangeListener;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

public abstract class DevicePairingBaseFragment extends Fragment implements BackEventListener, NetworkChangeListener.INetworkChangeListener {
    private static ActionBarListener mActionbarUpdateListener;
    private ProgressDialog mProgressDialog;
    private AlertDialog.Builder mAlertDialogBuilder;
    private AlertDialog mAlertDialog;
    private Context mContext;

    DevicePairingBaseFragment() {
    }

    public abstract int getActionbarTitleResId();

    public abstract String getActionbarTitle();

    public abstract boolean getBackButtonState();

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

    public void onStop() {
        super.onStop();
    }

    public void onDestroyView() {
        super.onDestroyView();
    }

    public void showFragment(Fragment fragment, FragmentLauncher fragmentLauncher) {
        FragmentActivity fragmentActivity = fragmentLauncher.getFragmentActivity();
        mActionbarUpdateListener = fragmentLauncher.getActionbarListener();

        int containerId = fragmentLauncher.getParentContainerResourceID();
        addFragment(fragment, fragmentActivity, containerId);
    }

    public void showFragment(DevicePairingBaseFragment fragment) {
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

 /*   public boolean clearFragmentStack() {
        FragmentActivity activity = this.getActivity();
        try {
            if (activity != null && !activity.isFinishing()) {
                if (!(activity instanceof DevicePairingLaunchActivity)) {
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    return fragmentManager.popBackStackImmediate("device_pairing_vertical_tag", 1);
                }
                activity.finish();
            }
        } catch (IllegalStateException var3) {
            ProdRegLogger.e(TAG, var3.getMessage());
        }
        return false;
    }*/

    public boolean handleBackEvent() {
        return false;
    }

    @Override
    public void onConnectionAvailable() {
    }

    @Override
    public void onConnectionLost() {
        dismissProgressDialog();
        showAlertDialog("Please check your connection and try again.");
    }

    public void showProgressDialog(final String message) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressDialog = new ProgressDialog(getActivity());
                mProgressDialog.setCancelable(true);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.setMessage(message);
                if (mProgressDialog != null && !mProgressDialog.isShowing() && !(getActivity().isFinishing())) {
                    mProgressDialog.show();
                }

            }
        });
    }

    public void dismissProgressDialog() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mProgressDialog != null && mProgressDialog.isShowing() && !(getActivity().isFinishing())) {
                    mProgressDialog.dismiss();
                }

            }
        });
    }

    public void showAlertDialog(String message) {
        if (mAlertDialogBuilder == null) {
            mAlertDialogBuilder = new AlertDialog.Builder(mContext, R.style.alertDialogStyle);
            mAlertDialogBuilder.setCancelable(false);
            mAlertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
        }
        if (mAlertDialog == null)
            mAlertDialog = mAlertDialogBuilder.create();

        if (!mAlertDialog.isShowing() && !(getActivity().isFinishing())) {
            mAlertDialog.setMessage(message);
            mAlertDialog.show();
        }
    }

}
