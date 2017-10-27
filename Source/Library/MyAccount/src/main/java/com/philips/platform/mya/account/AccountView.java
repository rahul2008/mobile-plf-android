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

    private Button myaFragmentLaunch;
    private Button myaActivityLaunch;

    private AccountPresenter accountPresenter;

    private String applicationName;
    private String propositionName;

    public AccountView() {}

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

        Bundle state = savedInstanceState != null ? savedInstanceState : getArguments();
        applicationName = state.getString(BUNDLE_KEY_APPLICATION_NAME);
        propositionName = state.getString(BUNDLE_KEY_PROPOSITION_NAME);

        Log.i("onCreateView", "applicationName:" + applicationName + "propositionsName:" + propositionName);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(BUNDLE_KEY_APPLICATION_NAME, applicationName);
        bundle.putString(BUNDLE_KEY_PROPOSITION_NAME, propositionName);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        accountPresenter = new AccountPresenter(this, getContext());

        myaFragmentLaunch = (Button) getView().findViewById(R.id.mya_account_permissions);
        myaFragmentLaunch.setOnClickListener(this);

        myaActivityLaunch = (Button) getView().findViewById(R.id.mya_account_launch_activity);
        myaActivityLaunch.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.mya_account_permissions) {
            launchCswFragment();

        } else if (view.getId() == R.id.mya_account_launch_activity) {
            launchCswActivity();
        }
    }

    private void launchCswFragment() {
        CswLaunchInput cswLaunchInput = new CswLaunchInput();
        cswLaunchInput.addToBackStack(true);
        FragmentLauncher fragmentLauncher =
                new FragmentLauncher(getActivity(),
                        R.id.mya_frame_layout_fragment_container, getMyaFragment().getUpdateTitleListener());
        CswInterface cswInterface = new CswInterface();
        cswInterface.launch(fragmentLauncher, cswLaunchInput);
    }

    private void launchCswActivity() {
        CswInterface uAppInterface;
        uAppInterface = new CswInterface();
        CswLaunchInput cswLaunchInput = new CswLaunchInput();
        cswLaunchInput.setContext(getContext());
        uAppInterface.launch(new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, 0), cswLaunchInput);
    }

    public void setArguments(String applicationName, String propositionName) {
        Bundle b = new Bundle();
        b.putString(BUNDLE_KEY_APPLICATION_NAME, applicationName);
        b.putString(BUNDLE_KEY_PROPOSITION_NAME, propositionName);
        this.setArguments(b);
    }

}
