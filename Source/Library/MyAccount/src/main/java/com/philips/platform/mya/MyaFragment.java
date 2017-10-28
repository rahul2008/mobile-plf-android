/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.platform.mya;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.philips.platform.mya.account.AccountView;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;


public class MyaFragment extends Fragment implements
        OnClickListener, BackEventListener {
    private FragmentManager mFragmentManager;
    private ActionBarListener mActionBarListener;

    private String applicationName;
    private String propositionName;

    static String BACK_STACK_ID = MyaFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_fragment_my_account_root, container, false);
        mFragmentManager = getChildFragmentManager();
        if (mFragmentManager.getBackStackEntryCount() < 1) {
            inflateAccountView();
        }

        if (getArguments() != null) {
            applicationName = getArguments().getString(BUNDLE_KEY_APPLICATION_NAME);
            propositionName = getArguments().getString(BUNDLE_KEY_PROPOSITION_NAME);
        }

        return view;
    }

    @Override
    public void onViewStateRestored(Bundle state) {
        super.onViewStateRestored(state);
        if (state != null) {
            applicationName = state.getString(BUNDLE_KEY_APPLICATION_NAME);
            propositionName = state.getString(BUNDLE_KEY_PROPOSITION_NAME);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle state) {
        super.onSaveInstanceState(state);
        if (state != null) {
            state.putString(BUNDLE_KEY_APPLICATION_NAME, applicationName);
            state.putString(BUNDLE_KEY_PROPOSITION_NAME, propositionName);
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

/*    public void inflateIntroView() {
        try {
            if (null != mFragmentManager) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.csw_frame_layout_view_container, new IntroMenuView());
                fragmentTransaction.commitAllowingStateLoss();
            }
        } catch (IllegalStateException ignore) {
        }
    }*/

    private void inflateAccountView() {
        try {
            if (null != mFragmentManager) {
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.csw_frame_layout_view_container, buildAccountView());
                fragmentTransaction.commitAllowingStateLoss();
            }
        } catch (IllegalStateException ignore) {
        }
    }

    private AccountView buildAccountView() {
        AccountView accountView = new AccountView();
        accountView.setArguments(applicationName, propositionName);
        return accountView;
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

    private static final String BUNDLE_KEY_APPLICATION_NAME = "appName";
    private static final String BUNDLE_KEY_PROPOSITION_NAME = "propName";

    public void setArguments(String applicationName, String propositionName) {
        Bundle b = new Bundle();
        b.putString(BUNDLE_KEY_APPLICATION_NAME, applicationName);
        b.putString(BUNDLE_KEY_PROPOSITION_NAME, propositionName);
        this.setArguments(b);
    }
}
