/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.neura;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.FragmentView;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.baseapp.screens.webview.WebViewStateData;
import com.philips.platform.mya.catk.device.DeviceStoredConsentHandler;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

public class NeuraConsentManagerPresenter extends AbstractUIBasePresenter implements PostConsentTypeCallback {

    private FragmentView neuraFragmentView;

    NeuraConsentManagerPresenter(FragmentView neuraFragmentView) {
        super(neuraFragmentView);
        this.neuraFragmentView = neuraFragmentView;
    }

    @Override
    public void onEvent(int componentID) {
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) neuraFragmentView.getFragmentActivity().getApplication();
        BaseFlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
        BaseState baseState = null;
        NeuraConsentProvider provider = getNeuraConsentProvider(appFrameworkApplication);
        try {
            switch (componentID) {
                case R.id.rap_justInTimeView_consentOk_button:
                    String EVENT_ALLOW = "allow";
                    provider.storeConsentTypeState(true, getPostConsentTypeCallback());
                    baseState = getNextState(targetFlowManager, EVENT_ALLOW);
                    break;
                case R.id.rap_justInTimeView_consentLater_label:
                    String EVENT_MAY_BE_LATER = "mayBeLater";
                    provider.storeConsentTypeState(false, getPostConsentTypeCallback());
                    baseState = getNextState(targetFlowManager, EVENT_MAY_BE_LATER);
                    break;
                case R.id.philipsPrivacy:
                    String EVENT_PRIVACY = "privacyPhilips";
                    baseState = getNextState(targetFlowManager, EVENT_PRIVACY);
                    break;
                case R.id.RA_neura_privacy_notice_label:
                    String EVENT_NEURA_PRIVACY = "privacyNeura";
                    baseState = getNextState(targetFlowManager, EVENT_NEURA_PRIVACY);
                    break;
                case R.id.rap_justInTimeView_consentHelplink_button:
                    launchWhatDoesItMeanFragment(neuraFragmentView.getFragmentActivity());
                    break;

            }
        } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                e) {
            Toast.makeText(neuraFragmentView.getFragmentActivity(), neuraFragmentView.getFragmentActivity().getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
        }

        if (baseState != null) {
            baseState.init(neuraFragmentView.getFragmentActivity().getApplicationContext());
            baseState.setUiStateData(getUiStateData(componentID));
            baseState.navigate(getUiLauncher());

        }
    }

    private void launchWhatDoesItMeanFragment(FragmentActivity fragmentActivity) {
        FragmentManager mFragmentManager = fragmentActivity.
                getSupportFragmentManager();
        Fragment whatDoesItMeanFragment = getNeuraWhatDoesItMeanFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(neuraFragmentView.getContainerId(),
                whatDoesItMeanFragment,
                NeuraWhatMeanFragment.TAG);
        fragmentTransaction.addToBackStack(NeuraWhatMeanFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @NonNull
    PostConsentTypeCallback getPostConsentTypeCallback() {
        return this;
    }


    @NonNull
    NeuraConsentProvider getNeuraConsentProvider(AppFrameworkApplication appFrameworkApplication) {
        return new NeuraConsentProvider(new DeviceStoredConsentHandler(appFrameworkApplication.getAppInfra()));
    }

    @NonNull
    FragmentLauncher getUiLauncher() {
        return new FragmentLauncher(neuraFragmentView.getFragmentActivity(), neuraFragmentView.getContainerId(), neuraFragmentView.getActionBarListener());
    }

    @NonNull
    BaseState getNextState(BaseFlowManager targetFlowManager, String event) {
        return targetFlowManager.getNextState(event);
    }


    UIStateData getUiStateData(int viewId) {
        if (viewId == R.id.philipsPrivacy) {
            WebViewStateData webViewStateData = new WebViewStateData();
            webViewStateData.setServiceId(Constants.PRIVACY);
            webViewStateData.setTitle(neuraFragmentView.getFragmentActivity().getString(R.string.RA_privacy_policy));
            return webViewStateData;
        } else if (viewId == R.id.RA_neura_privacy_notice_label){
            WebViewStateData webViewStateData = new WebViewStateData();
            webViewStateData.setUrl(neuraFragmentView.getFragmentActivity().getString(R.string.RA_neura_policy_url));
            webViewStateData.setTitle(neuraFragmentView.getFragmentActivity().getString(R.string.RA_privacy_policy));
            return webViewStateData;
        }
        return null;
    }

    @Override
    public void onPostConsentFailed(ConsentError error) {
        RALog.d(getClass().getSimpleName(), "error while saving neura consent ");
    }

    @Override
    public void onPostConsentSuccess() {
        RALog.d(getClass().getSimpleName(), "Saved neura consent successfully");
    }

    Fragment getNeuraWhatDoesItMeanFragment() {
        return new NeuraWhatMeanFragment();
    }
}
