/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.neura;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.FragmentView;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.webview.WebViewStateData;
import com.philips.platform.uappframework.launcher.FragmentLauncher;


public class NeuraConsentManagerPresentor extends AbstractUIBasePresenter {

    private FragmentView neuraFragmentView;


    NeuraConsentManagerPresentor(FragmentView neuraFragmentView) {
        super(neuraFragmentView);
        this.neuraFragmentView = neuraFragmentView;
    }

    @Override
    public void onEvent(int componentID) {
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) neuraFragmentView.getFragmentActivity().getApplication();
        BaseFlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
        BaseState baseState = null;
        switch (componentID) {
            case R.id.allowButton:
                String EVENT_ALLOW = "allow";
                baseState = targetFlowManager.getNextState(EVENT_ALLOW);
                break;
            case R.id.mayBeLater:
                String EVENT_MAY_BE_LATER = "mayBeLater";
                baseState = targetFlowManager.getNextState(EVENT_MAY_BE_LATER);
                break;
            case R.id.philipsPrivacy:
                String EVENT_PRIVACY = "privacyPhilips";
                baseState = targetFlowManager.getNextState(EVENT_PRIVACY);
                break;
        }

        if (baseState != null) {
            baseState.init(neuraFragmentView.getFragmentActivity().getApplicationContext());
            baseState.setUiStateData(getUiStateData(componentID));
            baseState.navigate(new FragmentLauncher(neuraFragmentView.getFragmentActivity(), neuraFragmentView.getContainerId(), neuraFragmentView.getActionBarListener()));

        }
    }


    private UIStateData getUiStateData(int viewId) {
        if (viewId == R.id.philipsPrivacy) {
            WebViewStateData webViewStateData = new WebViewStateData();
            webViewStateData.setServiceId(Constants.TERMS_AND_CONDITIONS);
            webViewStateData.setTitle(neuraFragmentView.getFragmentActivity().getString(R.string.reg_TermsAndConditionsText));
            return webViewStateData;
        }
        return null;
    }
}
