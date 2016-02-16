/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.

Project           : SaecoAvanti
----------------------------------------------------------------------------*/

package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.utils.IAPLog;

public abstract class BaseNoAnimationFragment extends Fragment {

    protected abstract void updateTitle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IAPLog.d(IAPLog.FRAGMENT_LIFECYCLE, "onCreate on " + this.getClass().getSimpleName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        IAPLog.d(IAPLog.FRAGMENT_LIFECYCLE, "onCreateView on " + this.getClass().getSimpleName());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        /** overriden to avoid stateloss crash */
    }

    @Override
    public void onStart() {
        IAPLog.d(IAPLog.FRAGMENT_LIFECYCLE, "onStart on " + this.getClass().getSimpleName());
        super.onStart();
    }

    @Override
    public void onResume() {
        IAPLog.d(IAPLog.FRAGMENT_LIFECYCLE, "onResume on " + this.getClass().getSimpleName());
        super.onResume();
    }

    @Override
    public void onPause() {
        IAPLog.d(IAPLog.FRAGMENT_LIFECYCLE, "onPause on " + this.getClass().getSimpleName());
        super.onPause();
    }

    @Override
    public void onStop() {
        IAPLog.d(IAPLog.FRAGMENT_LIFECYCLE, "onStop on " + this.getClass().getSimpleName());
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        IAPLog.d(IAPLog.FRAGMENT_LIFECYCLE, "onDestroyView on " + this.getClass().getSimpleName());
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        IAPLog.d(IAPLog.FRAGMENT_LIFECYCLE, "onDestroy on " + this.getClass().getSimpleName());
        super.onDestroy();
    }

    //To avoid touch leaks to underneath fragment
    protected void consumeTouch(View view) {
        if (view == null) return;
        view.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return true;
            }
        });
    }

    protected void addChildFragment(Fragment fragment, int id) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        if (childFragmentManager == null)
            return;

        FragmentTransaction transaction = childFragmentManager.beginTransaction();
        transaction.replace(id, fragment);

        transaction.commitAllowingStateLoss();
    }
}
