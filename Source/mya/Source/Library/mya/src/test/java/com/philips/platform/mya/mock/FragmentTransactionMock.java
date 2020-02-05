/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.mock;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.View;

public class FragmentTransactionMock extends FragmentTransaction {

    public int replace_containerId;
    public Fragment replace_fragment;
    public String replace_tag;
    public String addToBackStack_backStackId;
    public boolean commitAllowingStateLossWasCalled;

    @Override
    public FragmentTransaction add(Fragment fragment, String s) {
        return null;
    }

    @Override
    public FragmentTransaction add(int i, Fragment fragment) {
        return null;
    }

    @Override
    public FragmentTransaction add(int i, Fragment fragment, String s) {
        return null;
    }

    @Override
    public FragmentTransaction replace(int containerId, Fragment fragment) {
        this.replace_containerId = containerId;
        this.replace_fragment = fragment;
        return this;
    }

    @Override
    public FragmentTransaction replace(int containerId, Fragment fragment, String tag) {
        this.replace_containerId = containerId;
        this.replace_fragment = fragment;
        this.replace_tag = tag;
        return this;
    }

    @Override
    public FragmentTransaction remove(Fragment fragment) {
        return null;
    }

    @Override
    public FragmentTransaction hide(Fragment fragment) {
        return null;
    }

    @Override
    public FragmentTransaction show(Fragment fragment) {
        return null;
    }

    @Override
    public FragmentTransaction detach(Fragment fragment) {
        return null;
    }

    @Override
    public FragmentTransaction attach(Fragment fragment) {
        return null;
    }

    @Override
    public FragmentTransaction setPrimaryNavigationFragment(Fragment fragment) {
        return null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public FragmentTransaction setCustomAnimations(int i, int i1) {
        return null;
    }

    @Override
    public FragmentTransaction setCustomAnimations(int i, int i1, int i2, int i3) {
        return null;
    }

    @Override
    public FragmentTransaction setTransition(int i) {
        return null;
    }

    @Override
    public FragmentTransaction addSharedElement(View view, String s) {
        return null;
    }

    @Override
    public FragmentTransaction setTransitionStyle(int i) {
        return null;
    }

    @Override
    public FragmentTransaction addToBackStack(String backStackId) {
        this.addToBackStack_backStackId = backStackId;
        return this;
    }

    @Override
    public boolean isAddToBackStackAllowed() {
        return false;
    }

    @Override
    public FragmentTransaction disallowAddToBackStack() {
        return null;
    }

    @Override
    public FragmentTransaction setBreadCrumbTitle(int i) {
        return null;
    }

    @Override
    public FragmentTransaction setBreadCrumbTitle(CharSequence charSequence) {
        return null;
    }

    @Override
    public FragmentTransaction setBreadCrumbShortTitle(int i) {
        return null;
    }

    @Override
    public FragmentTransaction setBreadCrumbShortTitle(CharSequence charSequence) {
        return null;
    }

    @Override
    public FragmentTransaction setReorderingAllowed(boolean reorderingAllowed) {
        return null;
    }

    @Override
    public FragmentTransaction setAllowOptimization(boolean allowOptimization) {
        return null;
    }

    @Override
    public FragmentTransaction runOnCommit(Runnable runnable) {
        return null;
    }

    @Override
    public int commit() {
        return 0;
    }

    @Override
    public int commitAllowingStateLoss() {
        this.commitAllowingStateLossWasCalled = true;
        return 0;
    }

    @Override
    public void commitNow() {

    }

    @Override
    public void commitNowAllowingStateLoss() {

    }
}
