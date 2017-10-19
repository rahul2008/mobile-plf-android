package com.philips.platform.csw;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import org.junit.Before;
import org.junit.Test;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.List;

import static org.junit.Assert.*;


public class CswInterfaceTest {

    @Before
    public void setup() {
        fragmentTransactionMock = new FragmentTransactionMock();
        fragmentManagerMock = new FragmentManagerMock(fragmentTransactionMock);
        fragmentActivityMock = new FragmentActivityMock(fragmentManagerMock);
        cswInterface = new CswInterface();

    }

    @Test
    public void launchReplacesWithCswFragmentOnParentContainer() {
        givenParentContainerId(A_SPECIFIC_CONTAINER_ID);
        whenCallingLaunch(fragmentLauncherMock, new CswLaunchInput());
        thenReplaceWasCalledWith(A_SPECIFIC_CONTAINER_ID, CswFragment.class, CSWFRAGMENT);
        thenAddToBackStackIsCalled(CSWFRAGMENT);
    }

    public void givenParentContainerId(int parentContainerId) {
        fragmentLauncherMock = new FragmentLauncherMock(fragmentActivityMock, parentContainerId, null);
    }

    private void whenCallingLaunch(FragmentLauncherMock fragmentLauncherMock, CswLaunchInput cswLaunchInput) {
        cswInterface.launch(fragmentLauncherMock, cswLaunchInput);
    }

    private void thenReplaceWasCalledWith(int expectedParentContainerId, Class<?> expectedFragmentClass, String expectedTag) {
        assertEquals(expectedParentContainerId, fragmentTransactionMock.replace_containerId);
        assertTrue(fragmentTransactionMock.replace_fragment.getClass().isAssignableFrom(expectedFragmentClass));
        assertEquals(expectedTag, fragmentTransactionMock.replace_tag);
    }

    private void thenAddToBackStackIsCalled(String expectedBackStackId) {
        assertEquals(expectedBackStackId, fragmentTransactionMock.addToBackStack_backStackId);
    }

    public static class FragmentLauncherMock extends FragmentLauncher {

        FragmentActivityMock fragmentActivity;

        public FragmentLauncherMock(FragmentActivityMock fragmentActivity, int containerResId, ActionBarListener actionBarListener) {
            super(fragmentActivity, containerResId, actionBarListener);
            this.fragmentActivity = fragmentActivity;
        }
    }

    public static class FragmentActivityMock extends FragmentActivity {

        FragmentManagerMock fragmentManagerMock;

        public FragmentActivityMock(FragmentManagerMock fragmentManagerMock) {
            this.fragmentManagerMock = fragmentManagerMock;
        }

        @Override
        public FragmentManager getSupportFragmentManager() {
            return fragmentManagerMock;
        }

    }

    public static class FragmentManagerMock extends FragmentManager {

        FragmentTransactionMock fragmentTransactionMock;

        public FragmentManagerMock(FragmentTransactionMock fragmentTransaction) {
            this.fragmentTransactionMock = fragmentTransaction;
        }


        @Override
        public FragmentTransaction beginTransaction() {
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
            return 0;
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
        public void dump(String s, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strings) {

        }
    }

    public static class FragmentTransactionMock extends FragmentTransaction {

        public int replace_containerId;
        public Fragment replace_fragment;
        public String replace_tag;
        public String addToBackStack_backStackId;

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
            return null;
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
        public FragmentTransaction setAllowOptimization(boolean allowOptimization) {
            return null;
        }

        @Override
        public int commit() {
            return 0;
        }

        @Override
        public int commitAllowingStateLoss() {
            return 0;
        }

        @Override
        public void commitNow() {

        }

        @Override
        public void commitNowAllowingStateLoss() {

        }
    }

    private FragmentLauncherMock fragmentLauncherMock;
    private FragmentActivityMock fragmentActivityMock;
    private FragmentManagerMock fragmentManagerMock;
    private FragmentTransactionMock fragmentTransactionMock;

    public static final String CSWFRAGMENT = "CSWFRAGMENT";

    private CswInterface cswInterface;

    private static final int A_SPECIFIC_CONTAINER_ID = 938462837;
}