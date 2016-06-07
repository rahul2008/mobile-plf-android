package com.philips.cdp.prodreg.launcher;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.prodreg.listener.ActionbarUpdateListener;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class FragmentLauncher extends UiLauncher {
    protected int mParentContainerResourceID;
    protected ActionbarUpdateListener mActionbarUpdateListener = null;
    protected FragmentActivity mFragmentActivity = null;
    private Bundle arguments;

    public FragmentLauncher(FragmentActivity fragmentActivity, int parentContainerResId, ActionbarUpdateListener actionbarUpdateListener) {
        this.mParentContainerResourceID = parentContainerResId;
        this.mActionbarUpdateListener = actionbarUpdateListener;
        this.mFragmentActivity = fragmentActivity;
    }

    public int getParentContainerResourceID() {
        return this.mParentContainerResourceID;
    }

    public ActionbarUpdateListener getActionbarUpdateListener() {
        return this.mActionbarUpdateListener;
    }

    public FragmentActivity getFragmentActivity() {
        return this.mFragmentActivity;
    }

    public Bundle getArguments() {
        return arguments;
    }

    public void setArguments(final Bundle arguments) {
        this.arguments = arguments;
    }
}
