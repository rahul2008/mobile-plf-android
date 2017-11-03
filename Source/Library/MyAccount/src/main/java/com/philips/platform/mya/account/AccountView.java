/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.platform.mya.account;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.csw.CswInterface;
import com.philips.platform.csw.CswLaunchInput;
import com.philips.platform.mya.MyaBaseFragment;
import com.philips.platform.mya.R;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uid.view.widget.Button;

public class AccountView extends MyaBaseFragment implements
        AccountInterface, View.OnClickListener {

    private static final String BUNDLE_KEY_APPLICATION_NAME = "appName";
    private static final String BUNDLE_KEY_PROPOSITION_NAME = "propName";

    private static final boolean ADD_TO_BACKSTACK = true;
    private static final boolean DONT_ADD_TO_BACKSTACK = false;

    private Button myaFragmentLaunch;

    private AccountPresenter accountPresenter;

    public String applicationName;
    public String propositionName;

    public AccountView() {
    }

    @Override
    protected void setViewParams(Configuration config, int width) {

    }

    @Override
    protected void handleOrientation(View view) {
    }

    @Override
    public int getTitleResourceId() {
        return R.string.mya_account;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mya_fragment_account, container, false);

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        accountPresenter = new AccountPresenter(this, getContext());

        myaFragmentLaunch = (Button) getView().findViewById(R.id.mya_account_permissions);
        myaFragmentLaunch.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.mya_account_permissions) {
            launchCswFragment();

        }
    }

    private void launchCswFragment() {
        FragmentLauncher fragmentLauncher = new FragmentLauncher(getActivity(), this.getParentFragment().getView().getId(), getMyaFragment().getUpdateTitleListener());
        new CswInterface().launch(fragmentLauncher, buildLaunchInput(ADD_TO_BACKSTACK));
    }

    private CswLaunchInput buildLaunchInput(boolean addToBackStack) {
        CswLaunchInput cswLaunchInput = new CswLaunchInput();
        cswLaunchInput.setPropositionName(propositionName);
        cswLaunchInput.setApplicationName(applicationName);
        cswLaunchInput.addToBackStack(addToBackStack);
        cswLaunchInput.setContext(getContext());
        cswLaunchInput.setFragmentManager(this.getParentFragment().getChildFragmentManager());
        return cswLaunchInput;
    }

    public void setArguments(String applicationName, String propositionName) {
        Bundle b = new Bundle();
        b.putString(BUNDLE_KEY_APPLICATION_NAME, applicationName);
        b.putString(BUNDLE_KEY_PROPOSITION_NAME, propositionName);
        this.setArguments(b);
    }

}
