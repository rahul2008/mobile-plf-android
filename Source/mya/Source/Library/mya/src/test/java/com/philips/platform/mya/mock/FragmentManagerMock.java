/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.mock;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

public class FragmentManagerMock extends FragmentManager {

    public int backStackCount;

    FragmentTransactionMock fragmentTransactionMock;
    public boolean popBackStack_wasCalled;
    public boolean beginTransactionCalled;

    public FragmentManagerMock(FragmentTransactionMock fragmentTransaction) {
        this.fragmentTransactionMock = fragmentTransaction;
    }


    @Override
    public FragmentTransaction beginTransaction() {
        beginTransactionCalled = true;
        return fragmentTransactionMock;
    }

    @Override
    public boolean executePendingTransactions() {
        return false;
    }

    @Override
    public Fragment findFragmentById(int i) {
        return null;
    }

    @Override
    public Fragment findFragmentByTag(String s) {
        return null;
    }

    @Override
    public void popBackStack() {
        popBackStack_wasCalled = true;
    }

    @Override
    public boolean popBackStackImmediate() {
        return false;
    }

    @Override
    public void popBackStack(String s, int i) {
    }

    @Override
    public boolean popBackStackImmediate(String s, int i) {
        return false;
    }

    @Override
    public void popBackStack(int i, int i1) {

    }

    @Override
    public boolean popBackStackImmediate(int i, int i1) {
        return false;
    }

    @Override
    public int getBackStackEntryCount() {
        return backStackCount;
    }

    @Override
    public BackStackEntry getBackStackEntryAt(int i) {
        return null;
    }

    @Override
    public void addOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener) {

    }

    @Override
    public void removeOnBackStackChangedListener(OnBackStackChangedListener onBackStackChangedListener) {

    }

    @Override
    public void putFragment(Bundle bundle, String s, Fragment fragment) {

    }

    @Override
    public Fragment getFragment(Bundle bundle, String s) {
        return null;
    }

    @Override
    public List<Fragment> getFragments() {
        return null;
    }

    @Override
    public Fragment.SavedState saveFragmentInstanceState(Fragment fragment) {
        return null;
    }

    @Override
    public boolean isDestroyed() {
        return false;
    }

    @Override
    public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb, boolean recursive) {

    }

    @Override
    public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb) {

    }

    @Override
    public Fragment getPrimaryNavigationFragment() {
        return null;
    }

    @Override
    public void dump(String s, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strings) {

    }

    @Override
    public boolean isStateSaved() {
        return false;
    }
}
