/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.platform.csw;

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.csw.permission.PermissionView;
import com.philips.platform.mya.consentwidgets.R;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;

import android.os.Bundle;
import android.support.v4.app.*;
import android.view.*;
import android.view.View.OnClickListener;

public class CswFragment extends Fragment implements
        OnClickListener, BackEventListener {
    private FragmentManager mFragmentManager;
    private ActionBarListener mActionBarListener;

    private String applicationName;
    private String propositionName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.csw_fragment_consent_widget_root, container, false);

        if (getArguments() != null) {
            applicationName = getArguments().getString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME);
            propositionName = getArguments().getString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME);
        }

        mFragmentManager = getmFragmentManager();
        if (mFragmentManager.getBackStackEntryCount() < 1) {
            inflatePermissionView();
        }

        return view;
    }

    protected FragmentManager getmFragmentManager() {
        return getChildFragmentManager();
    }

    @Override
    public void onViewStateRestored(Bundle state) {
        super.onViewStateRestored(state);
        if (state != null) {
            applicationName = state.getString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME);
            propositionName = state.getString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (state != null) {
            state.putString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
            state.putString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
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
        } else {
            getActivity().finish();
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

    @Override
    public void onClick(View v) {
        if (v.getId() == com.philips.cdp.registration.R.id.iv_reg_back) {
            onBackPressed();
        }
    }

    public int getFragmentCount() {
        FragmentManager fragmentManager = getChildFragmentManager();
        int fragmentCount = fragmentManager.getFragments().size();
        return fragmentCount;
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

    protected void setChildFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void setArguments(String applicationName, String propositionName) {
        Bundle b = new Bundle();
        b.putString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
        b.putString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
        this.setArguments(b);
    }

    public String getApplicationName() {
        return applicationName;
    }

    public String getPropositionName() {
        return propositionName;
    }
}
