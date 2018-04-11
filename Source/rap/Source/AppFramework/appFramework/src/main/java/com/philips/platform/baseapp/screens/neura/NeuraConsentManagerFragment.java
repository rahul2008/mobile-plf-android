/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.neura;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AbstractOnboardingBaseFragment;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.webview.WebViewStateData;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.BackEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class NeuraConsentManagerFragment extends AbstractOnboardingBaseFragment implements BackEventListener, View.OnClickListener {


    public static final String TAG = NeuraConsentManagerFragment.class.getSimpleName();
    private static final long serialVersionUID = 4394954556057838520L;
    private Button allow, mayBelater, philipsPrivacy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_neura_consent_manager, container, false);
        allow = inflate.findViewById(R.id.allowButton);
        mayBelater = inflate.findViewById(R.id.mayBeLater);
        philipsPrivacy = inflate.findViewById(R.id.philipsPrivacy);
        allow.setOnClickListener(this);
        mayBelater.setOnClickListener(this);
        philipsPrivacy.setOnClickListener(this);
        return inflate;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AbstractAppFrameworkBaseActivity) getActivity()).updateActionBarIcon(true);
    }

    @Override
    public boolean handleBackEvent() {
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) getActivity().getApplication();
        BaseFlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
        targetFlowManager.getBackState(targetFlowManager.getCurrentState());
        hideActionBar();
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onClick(View view) {
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) getActivity().getApplication();
        BaseFlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
        BaseState baseState = null;
        switch (view.getId()) {
            case R.id.allowButton:
                baseState = targetFlowManager.getNextState("allow");
                break;
            case R.id.mayBeLater:
                baseState = targetFlowManager.getNextState("mayBeLater");
                break;
            case R.id.philipsPrivacy:
                baseState = targetFlowManager.getNextState("privacyPhilips");
                break;
        }

        if (baseState != null) {
            baseState.init(getFragmentActivity().getApplicationContext());
            baseState.setUiStateData(getUiStateData(view.getId()));
            baseState.navigate(new FragmentLauncher(getFragmentActivity(), getContainerId(), getActionBarListener()));

        }
    }

    private UIStateData getUiStateData(int viewId) {
        if (viewId == R.id.philipsPrivacy) {
            WebViewStateData webViewStateData = new WebViewStateData();
            webViewStateData.setServiceId(Constants.TERMS_AND_CONDITIONS);
            webViewStateData.setTitle(getString(R.string.reg_TermsAndConditionsText));
            return webViewStateData;
        }
        return null;
    }
}
