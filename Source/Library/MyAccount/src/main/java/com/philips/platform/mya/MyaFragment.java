/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catk.CatkConstants;
import com.philips.platform.csw.CswConstants;
import com.philips.platform.csw.CswFragment;
import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswLaunchInput;
import com.philips.platform.mya.profile.MyaProfileFragment;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

// TODO: Deepthi, can we remove this class and we should not have dependency on CSW
public class MyaFragment extends Fragment {

    public static String TAG = MyaFragment.class.getSimpleName();

    private FragmentManager mFragmentManager;
    private ActionBarListener mActionBarListener;

    // TODO: Deepthi, check below can be removed
    public String applicationName;
    public String propositionName;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_fragment_my_account_root, container, false);
        mFragmentManager = overridableGetActivity().getSupportFragmentManager();

        if (getArguments() != null) {
            applicationName = getArguments().getString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME);
            propositionName = getArguments().getString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME);
        }

        if (overridableGetActivity().findViewById(R.id.mya_frame_layout_view_container) == null) {
            inflateAccountView();
        }

        return view;
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

    private Fragment getCswFragment() {
        return getActivity().getSupportFragmentManager().findFragmentByTag(CswConstants.CSWFRAGMENT);
    }

    private void inflateAccountView() {
        try {
            if (null != mFragmentManager) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mya_frame_layout_view_container, buildAccountView());
                fragmentTransaction.commitAllowingStateLoss();
            }
        } catch (IllegalStateException ignore) {
        }
    }

    private Fragment buildAccountView() {
        return new MyaProfileFragment();
    }

    public int getFragmentCount() {
        //First Check is Csw fragment is present then return count from it +1
        int cswChildFragmentCount = 0;

        Fragment fragment = getCswFragment();
        if (fragment != null) {
            cswChildFragmentCount = ((CswFragment) fragment).getFragmentCount();
            cswChildFragmentCount = cswChildFragmentCount + 1;
        }

        int fragmentCount = mFragmentManager.getFragments().size() - 1;
        fragmentCount = fragmentCount + cswChildFragmentCount;
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

    protected void setChildFragmentManager(FragmentManager fragmentManager) {
        mFragmentManager = fragmentManager;
    }

    public void setArguments(String applicationName, String propositionName) {
        Bundle b = new Bundle();
        b.putString(CatkConstants.BUNDLE_KEY_APPLICATION_NAME, applicationName);
        b.putString(CatkConstants.BUNDLE_KEY_PROPOSITION_NAME, propositionName);
        this.setArguments(b);
    }


    public void launchCswFragment() {
        FragmentLauncher fragmentLauncher = new FragmentLauncher(getActivity(), R.id.mya_frame_layout_view_container, mActionBarListener);
        new CswInterface().launch(fragmentLauncher, buildLaunchInput(true));
    }

    private CswLaunchInput buildLaunchInput(boolean addToBackStack) {
        CswLaunchInput cswLaunchInput = new CswLaunchInput();
        cswLaunchInput.setPropositionName(propositionName);
        cswLaunchInput.setApplicationName(applicationName);
        cswLaunchInput.addToBackStack(addToBackStack);
        cswLaunchInput.setContext(getContext());
        return cswLaunchInput;
    }

    protected FragmentActivity overridableGetActivity() {
        return getActivity();
    }
}
