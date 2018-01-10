/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.csw;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.mya.csw.dialogs.DialogView;
import com.philips.platform.mya.csw.permission.PermissionView;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

public class CswFragment extends Fragment implements BackEventListener {

    public static final String BUNDLE_KEY_ADDTOBACKSTACK = "addToBackStack";

    private FragmentManager mFragmentManager;
    private ActionBarListener mActionBarListener;

    private boolean isAddedToBackStack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.csw_fragment_consent_widget_root, container, false);

        if (getArguments() != null) {
            isAddedToBackStack = getArguments().getBoolean(BUNDLE_KEY_ADDTOBACKSTACK, false);
        }

        mFragmentManager = getmFragmentManager();
        inflatePermissionView();
        getRestClient().isInternetReachable();
        return view;
    }

    protected RestInterface getRestClient() {
        return CswInterface.get().getDependencies().getAppInfra().getRestClient();
    }

    protected DialogView getDialogView() {
        return new DialogView();
    }

    @Override
    public void onResume() {
        super.onResume();

        if(!getRestClient().isInternetReachable()) {
            getDialogView().showDialog(getActivity());
        }
    }

    @Override
    public void onViewStateRestored(Bundle state) {
        super.onViewStateRestored(state);
        if (state != null) {
            isAddedToBackStack = state.getBoolean(BUNDLE_KEY_ADDTOBACKSTACK);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (state != null) {
            state.putBoolean(BUNDLE_KEY_ADDTOBACKSTACK, isAddedToBackStack);
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
        return new PermissionView();
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
