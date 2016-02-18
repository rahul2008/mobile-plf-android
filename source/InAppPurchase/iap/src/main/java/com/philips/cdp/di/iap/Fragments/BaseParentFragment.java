/*----------------------------------------------------------------------------
Copyright(c) Philips Electronics India Ltd
All rights reserved. Reproduction in whole or in part is prohibited without
the written consent of the copyright holder.

Project           : Saeco Avanti
----------------------------------------------------------------------------*/

package com.philips.cdp.di.iap.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.activity.MainActivity;
import com.philips.cdp.di.iap.utils.IAPLog;

public abstract class BaseParentFragment extends BaseAnimationSupportFragment implements
        OnBackStackChangedListener {

    public abstract boolean handleBackNavigation();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_parent_base, container, false);
        getChildFragmentManager().addOnBackStackChangedListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateBackButton(getChildFragmentManager());
    }

    protected int getFragmentContainerId() {
        return R.id.fl_child_fragment_container;
    }

    protected void addChildFragment(Fragment fragment, boolean addToBackStack,
                                    TransitionAnimation transition) {
        FragmentManager childFragmentManager = getChildFragmentManager();
        if (childFragmentManager == null)
            return;

        FragmentTransaction transaction = childFragmentManager.beginTransaction();
        addTransitionAnimationToTransaction(transaction, transition);
        transaction.add(getFragmentContainerId(), fragment, getFragmentTag(fragment));
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commitAllowingStateLoss();
        IAPLog.d(IAPLog.LOG, "addChildFragment = " + fragment.toString());
    }

    public Fragment getTopFragment() {
        FragmentManager childFragmentManager = getChildFragmentManager();
        if (childFragmentManager == null)
            return null;

        return childFragmentManager.findFragmentById(getFragmentContainerId());
    }

    protected void addTransitionAnimationToTransaction(FragmentTransaction transaction,
                                                       TransitionAnimation transition) {
        switch (transition) {
            case NONE:
                break;
            case ENTER:
                transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top, 0, 0);
                break;
            case EXIT:
                transaction.setCustomAnimations(0, 0, R.anim.slide_in_bottom, R.anim.slide_out_top);
                break;
            case ENTEREXIT:
                transaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top,
                        R.anim.slide_in_bottom, R.anim.slide_out_top);
                break;
            default:
                break;
        }
    }

    public boolean canRemoveBackButton() {
        FragmentManager childFragmentManager = getChildFragmentManager();
        if (childFragmentManager == null)
            return true;

        return (childFragmentManager.getBackStackEntryCount() <= 0);
    }

    @Override
    public void onBackStackChanged() {
        IAPLog.d(IAPLog.LOG, "BaseFragment == onBackStackChanged");
        FragmentManager childFragmentManager = getChildFragmentManager();
        if (childFragmentManager == null)
            return;
        updateBackButton(childFragmentManager);
        Fragment topFragment = childFragmentManager.findFragmentById(getFragmentContainerId());
        if (topFragment != null && topFragment instanceof BaseNoAnimationFragment) {
            ((BaseNoAnimationFragment) topFragment).updateTitle();
            IAPLog.d(IAPLog.LOG, "BaseFragment == onBackStackChanged == updateTitle");
        }
    }

    @Override
    protected AnimationType getDefaultAnimationType() {
        return AnimationType.RIGHT_TO_LEFT_67PERCENT;
    }

    protected String getFragmentTag(Fragment fragment) {
        return fragment.getClass().getSimpleName();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Fragment topFragment = getTopFragment();
        if (topFragment == null)
            return;

        topFragment.onActivityResult(requestCode, resultCode, data);
    }

    protected void updateBackButton(FragmentManager childFragmentManager) {
        MainActivity parent = getMainActivity();
        if (parent == null)
            return;

        if (childFragmentManager.getBackStackEntryCount() > 0) {
            parent.showBackButton();
        } else {
            parent.hideBackButtonIfNoMoreSubfragments();
        }
    }
}
