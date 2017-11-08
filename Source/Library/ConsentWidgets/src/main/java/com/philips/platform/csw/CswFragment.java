/*
 * Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * * in whole or in part is prohibited without the prior written
 * * consent of the copyright holder.
 * /
 */

package com.philips.platform.csw;

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.csw.description.DescriptionView;
import com.philips.platform.csw.permission.PermissionView;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import android.os.Bundle;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.OnClickListener;

public class CswFragment extends Fragment implements BackEventListener {
    private FragmentManager mFragmentManager;
    private ActionBarListener mActionBarListener;

    private String applicationName;
    private String propositionName;
    private boolean isAddedToBackStack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.csw_fragment_consent_widget_root, container, false);

        if (getArguments() != null) {
            applicationName = getArguments().getString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME);
            propositionName = getArguments().getString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME);
            isAddedToBackStack = getArguments().getBoolean(CatkConstants.BUNDLE_KEY_ADDTOBACKSTACK,false);
        }

        mFragmentManager = getmFragmentManager();

            inflatePermissionView();


        return view;
    }

    @Override
    public void onViewStateRestored(Bundle state) {
        super.onViewStateRestored(state);
        if (state != null) {
            applicationName = state.getString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME);
            propositionName = state.getString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME);
            isAddedToBackStack = state.getBoolean(CatkConstants.BUNDLE_KEY_ADDTOBACKSTACK);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (state != null) {
            state.putString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
            state.putString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
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
        permissionView.setArguments(applicationName, propositionName);
        return permissionView;
    }


    public int getFragmentCount() {
        FragmentManager fragmentManager = getmFragmentManager();
        if(isAddedToBackStack){
            return fragmentManager.getBackStackEntryCount()+1;
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

    protected FragmentActivity getCurrentActivity() {
        return getActivity();
    }

    public void setArguments(String applicationName, String propositionName,boolean addToBackStack) {
        Bundle b = new Bundle();
        b.putString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
        b.putString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
        b.putBoolean(CatkConstants.BUNDLE_KEY_ADDTOBACKSTACK, addToBackStack);
        this.setArguments(b);
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getPropositionName() {
        return propositionName;
    }

    protected FragmentManager getmFragmentManager() {
        return getChildFragmentManager();
    }

    protected void setChildFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void addDiscriptionFragment() {
        try {
            if (null != mFragmentManager) {
                DescriptionView descriptionView = new DescriptionView();
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.csw_frame_layout_view_container,descriptionView,"Description");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        } catch (IllegalStateException ignore) {
        }
    }
}
