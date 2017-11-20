/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.csw.permission.PermissionView;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import java.util.ArrayList;

public class CswFragment extends Fragment implements BackEventListener {
    private FragmentManager mFragmentManager;
    private ActionBarListener mActionBarListener;

    private ConsentBundleConfig config;

    private boolean isAddedToBackStack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.csw_fragment_consent_widget_root, container, false);

        if (getArguments() != null) {
            config = new ConsentBundleConfig(getArguments());
            isAddedToBackStack = getArguments().getBoolean(CatkConstants.BUNDLE_KEY_ADDTOBACKSTACK, false);
        }

        mFragmentManager = getmFragmentManager();

        inflatePermissionView();

        return view;
    }

    @Override
    public void onViewStateRestored(Bundle state) {
        super.onViewStateRestored(state);
        if (state != null) {
            config = new ConsentBundleConfig(state);
            isAddedToBackStack = state.getBoolean(CatkConstants.BUNDLE_KEY_ADDTOBACKSTACK);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (state != null) {
            state.putAll(config.toBundle());
            state.putBoolean(CatkConstants.BUNDLE_KEY_ADDTOBACKSTACK, isAddedToBackStack);
        }
    }

    public boolean onBackPressed() {
        return handleBackStack();
    }

    private boolean handleBackStack() {
        if (mFragmentManager != null) {
            int count = mFragmentManager.getBackStackEntryCount();
            if (count == 0) {
                return true;
            }
            mFragmentManager.popBackStack();
        }
        return false;
    }

    private void inflatePermissionView() {
        try {
            if (null != mFragmentManager) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.csw_frame_layout_view_container, buildPermissionView());
                fragmentTransaction.commitAllowingStateLoss();
            }
        } catch (IllegalStateException ignore) {
        }
    }

    private PermissionView buildPermissionView() {
        PermissionView permissionView = new PermissionView();
        permissionView.setArguments(config.toBundle());
        return permissionView;
    }

    public int getFragmentCount() {
        FragmentManager fragmentManager = getmFragmentManager();
        if (isAddedToBackStack) {
            return fragmentManager.getBackStackEntryCount() + 1;
        }
        return fragmentManager.getBackStackEntryCount();
    }

    public ActionBarListener getUpdateTitleListener() {
        return mActionBarListener;
    }

    public void setOnUpdateTitleListener(ActionBarListener listener) {
        this.mActionBarListener = listener;
    }

    private int titleResourceID = -99;

    public void setResourceID(int titleResourceId) {
        titleResourceID = titleResourceId;
    }

    public int getResourceID() {
        return titleResourceID;
    }

    @Override
    public boolean handleBackEvent() {
        return !(onBackPressed());
    }

    public String getApplicationName() {
        return config.getApplicationName();
    }

    public String getPropositionName() {
        return config.getPropositionName();
    }

    public boolean getIsAddedToBackStack() {
        return isAddedToBackStack;
    }

    protected FragmentManager getmFragmentManager() {
        return getChildFragmentManager();
    }

    protected void setChildFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }
}
