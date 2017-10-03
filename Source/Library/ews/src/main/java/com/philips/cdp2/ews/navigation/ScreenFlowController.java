/**
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews.navigation;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.view.EWSProductSupportFragment;

import javax.inject.Inject;
import javax.inject.Singleton;

@SuppressWarnings("WeakerAccess")
@Singleton
public class ScreenFlowController {

    @DrawableRes
    public static final int NAVIGATION_BACK_ICON = R.drawable.ic_navigate_left;

    private int containerFrameId;
    private FragmentManager.OnBackStackChangedListener listener;
    FragmentManager fragmentManager;
    private Toolbar toolbar;
    private TextView toolbarTitleView;
    AppCompatActivity activity;
    private boolean started;

    @Inject
    public ScreenFlowController() {
    }

    public void start(@NonNull AppCompatActivity activity,
                      @IdRes final int contentFrame,
                      @NonNull final Fragment rootFragment) {
        if (started) {
            return;
        }
        this.started = true;
        this.activity = activity;
        this.fragmentManager = activity.getSupportFragmentManager();
        this.containerFrameId = contentFrame;

        addBackStackListener();
        initToolbar();
        addFragment(rootFragment);
    }

    private void initToolbar() {
        toolbar = (Toolbar) activity.findViewById(R.id.ews_toolbar);
        toolbarTitleView = ((TextView) toolbar.findViewById(R.id.toolbar_title));

        activity.setSupportActionBar(toolbar);

        final ActionBar actionBar = activity.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
        }
    }

    void updateToolbar() {
        if (hasBackStack()) {
            Fragment fragment = getFragmentAtTopOfBackStack();
            if (fragment instanceof ScreenFlowParticipant) {
                ScreenFlowParticipant flowParticipant = (ScreenFlowParticipant) fragment;
                if (flowParticipant.getNavigationIconId() > 0) {
                    toolbar.setNavigationIcon(flowParticipant.getNavigationIconId());
                } else {
                    toolbar.setNavigationIcon(null);
                }
                setToolbarTitle(flowParticipant.getToolbarTitle());
            }
        }
    }

    private void addBackStackListener() {
        listener = new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                int backStackEntryCount = fragmentManager.getBackStackEntryCount();
                if (backStackEntryCount == 0) {
                    activity.finish();
                } else {
                    updateToolbar();
                }
            }
        };
        fragmentManager.addOnBackStackChangedListener(listener);
    }

    private void addFragment(final Fragment rootFragment) {
        if (!hasBackStack()) {
            showFragment(rootFragment);
        }
    }

    public void stop() {
        if (started) {
            fragmentManager.removeOnBackStackChangedListener(listener);
            listener = null;
            toolbar = null;
            fragmentManager = null;
            started = false;
        }
    }

    public void showFragment(Fragment fragment) {
        if (!started) {
            return;
        }

        if (isAlreadyShowing(fragment)) {
            return;
        }

        if (popFragmentWhenPresentOnBackStack(fragment)) {
            return;
        }

        setBackStackToHierarchyLevel(getHierarchyLevel(fragment));

        replaceFragment(fragment);
    }

    private int getHierarchyLevel(final Fragment fragment) {
        return ((ScreenFlowParticipant) fragment).getHierarchyLevel();
    }

    private void setBackStackToHierarchyLevel(int hierarchyLevel) {
        hierarchyLevel = Math.max(1, hierarchyLevel);
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();
        int popCount = backStackEntryCount - hierarchyLevel;
        popBackStack(popCount);
    }

    private void popBackStack(final int popCount) {
        for (int i = 0; i < popCount; i++) {
            popBackStack();
        }
    }

    private void replaceFragment(final Fragment fragment) {
        if (started) {
            final String name = fragment.getClass().getName();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(containerFrameId, fragment, name);
            fragmentTransaction.addToBackStack(name);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    //use full when you click on 'X' kind of buttons to just close the current fragment
    public void popBackStack() {
        if (hasBackStack()) {
            fragmentManager.popBackStack();
        }
    }

    private boolean hasBackStack() {
        return fragmentManager != null && fragmentManager.getBackStackEntryCount() > 0;
    }

    private boolean isAlreadyShowing(final Fragment fragment) {
        return hasBackStack() && fragment.equals(getFragmentAtTopOfBackStack());
    }

    @NonNull
    private Fragment getFragmentAtTopOfBackStack() {
        return getFragmentAtBackStackIndex(fragmentManager.getBackStackEntryCount() - 1);
    }

    private boolean popFragmentWhenPresentOnBackStack(final Fragment fragment) {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); i++) {
            int reversedBackStackIndex = fragmentManager.getBackStackEntryCount() - 1 - i;
            Fragment backStackFragment = getFragmentAtBackStackIndex(reversedBackStackIndex);
            String backFragmentName = getFragmentName(backStackFragment);
            if (getFragmentName(fragment).equals(backFragmentName)) {
                popBackStack(backFragmentName);
                return true;
            }
        }
        return false;
    }

    @NonNull
    private Fragment getFragmentAtBackStackIndex(final int index) {
        final FragmentManager.BackStackEntry backStackEntry = fragmentManager.getBackStackEntryAt(index);
        final String name = backStackEntry.getName();
        return fragmentManager.findFragmentByTag(name);
    }

    private void popBackStack(final String backFragmentName) {
        fragmentManager.popBackStack(backFragmentName, 0);
    }

    private String getFragmentName(final Fragment fragment) {
        return fragment.getClass().getName();
    }

    public boolean onBackPressed() {
        if (hasBackStack()) {
            Fragment fragment = getFragmentAtTopOfBackStack();
            if (fragment instanceof ScreenFlowParticipant) {
                return ((ScreenFlowParticipant) fragment).onBackPressed();
            } else if (isConsumerCareScreen()) {
                fragmentManager.popBackStackImmediate();
            }
        }

        return false;
    }

    public void homeButtonPressed() {
        if (!hasBackStack()) {
            return;
        }
        Fragment fragment = getFragmentAtTopOfBackStack();
        if (fragment instanceof ScreenFlowParticipant) {
            ScreenFlowParticipant flowParticipant = (ScreenFlowParticipant) getFragmentAtTopOfBackStack();
            if (flowParticipant.getHierarchyLevel() == 1) {
                flowParticipant.onBackPressed();
            } else {
                setBackStackToHierarchyLevel(flowParticipant.getHierarchyLevel() - 1);
            }
        } else if (isConsumerCareScreen()) {
            setBackStackToHierarchyLevel(EWSProductSupportFragment.PRODUCT_SUPPORT_HIERARCHY_LEVEL - 1);
        } else {
            popBackStack();
        }
    }

    public void finish() {
        activity.finish();
    }

    public void setToolbarTitle(final String toolbarTitle) {
        toolbarTitleView.setText(toolbarTitle);
    }

    public void setToolbarTitle(final int toolbarTitleId) {
        toolbarTitleView.setText(toolbarTitleId);
    }

    public boolean isConsumerCareScreen() {
        return EWSProductSupportFragment.PRODUCT_SUPPORT_HIERARCHY_LEVEL ==
                (fragmentManager.getBackStackEntryCount() - 1);
    }
}
