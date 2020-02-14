/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.baseapp.screens.cookiesconsent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.consentmanager.PostConsentCallback;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.uappframework.launcher.FragmentLauncher;


public class CookiesConsentPresenter extends AbstractUIBasePresenter implements PostConsentCallback {
    public static String TAG = CookiesConsentPresenter.class.getSimpleName();
    private final AppFrameworkApplication appFrameworkApplication;
    private CookiesConsentFragmentView cookiesConsentFragmentView;
    private boolean CONSENT_STATUS = false;
    private AppInfraInterface appInfraInterface;

    public CookiesConsentPresenter(CookiesConsentFragmentView cookiesConsentFragmentView) {
        super(cookiesConsentFragmentView);
        this.cookiesConsentFragmentView = cookiesConsentFragmentView;
        appFrameworkApplication =
                (AppFrameworkApplication) cookiesConsentFragmentView.getFragmentActivity().getApplication();
        appInfraInterface = appFrameworkApplication.getAppInfra();
    }

    @Override
    public void onEvent(final int componentID) {
        BaseFlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
        BaseState baseState = null;
        ConsentDefinition abTestingConsentDefinition = appInfraInterface.getConsentManager().getConsentDefinitionForType(appInfraInterface.getAbTesting().getAbTestingConsentIdentifier());
        ConsentDefinition taggingDefinition = appInfraInterface.getConsentManager().getConsentDefinitionForType(appInfraInterface.getTagging().getClickStreamConsentIdentifier());
        try {
            switch (componentID) {
                case R.id.usr_cookiesConsentScreen_accept_button:
                    CONSENT_STATUS = true;
                    String EVENT_ALLOW = "accept";
                    appInfraInterface.getConsentManager().storeConsentState(abTestingConsentDefinition,true, getPostConsentTypeCallback());
                    appInfraInterface.getConsentManager().storeConsentState(taggingDefinition,true, getPostConsentTypeCallback());
                    baseState = getNextState(targetFlowManager, EVENT_ALLOW);
                    break;
                case R.id.usr_cookiesConsentScreen_Reject_button:
                    CONSENT_STATUS = false;
                    String EVENT_REJECT = "reject";
                    baseState = getNextState(targetFlowManager, EVENT_REJECT);
                    appInfraInterface.getConsentManager().storeConsentState(abTestingConsentDefinition,false, getPostConsentTypeCallback());
                    appInfraInterface.getConsentManager().storeConsentState(taggingDefinition,false, getPostConsentTypeCallback());
                    break;
                case R.id.usr_cookiesConsentScreen_info_weblink_label:
                    launchWhatDoesItMeanFragment(cookiesConsentFragmentView.getFragmentActivity());
                    break;

            }
        } catch (Exception e) {
            Toast.makeText(cookiesConsentFragmentView.getFragmentActivity(),
                    cookiesConsentFragmentView.getFragmentActivity().getString(R.string.RA_something_wrong),
                    Toast.LENGTH_SHORT).show();

        }
        if (baseState != null) {
            baseState.init(cookiesConsentFragmentView.getFragmentActivity().getApplicationContext());
            baseState.navigate(getUiLauncher());
        }
    }

    @NonNull
    FragmentLauncher getUiLauncher() {
        return new FragmentLauncher(cookiesConsentFragmentView.getFragmentActivity(),
                cookiesConsentFragmentView.getContainerId(), cookiesConsentFragmentView.getActionBarListener());
    }

    @NonNull
    BaseState getNextState(BaseFlowManager targetFlowManager, String event) {
        return targetFlowManager.getNextState(event);
    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) cookiesConsentFragmentView.getFragmentActivity().getApplicationContext();
    }

    @NonNull
    protected FragmentLauncher getFragmentLauncher() {
        return new FragmentLauncher(cookiesConsentFragmentView.getFragmentActivity(),
                cookiesConsentFragmentView.getContainerId(), cookiesConsentFragmentView.getActionBarListener());
    }

    @NonNull
    PostConsentCallback getPostConsentTypeCallback() {
        return this;
    }

    @Override
    public void onPostConsentFailed(ConsentError error) {
        RALog.d(getClass().getSimpleName(), "error while saving ab-testing consent ");
    }

    @Override
    public void onPostConsentSuccess() {
        if (CONSENT_STATUS) {
            appFrameworkApplication.getAppInfra().getAbTesting().updateCache(new ABTestClientInterface.OnRefreshListener() {
                @Override
                public void onSuccess() {
                    RALog.d(getClass().getSimpleName(), "abtesting cache updated successfully");
                    RALog.d("FireBase instance id - ", FirebaseInstanceId.getInstance().getToken());
                }

                @Override
                public void onError(ERRORVALUE error) {
                    RALog.d(getClass().getSimpleName(), "abtesting update failed");
                }
            });
        }
    }

    private void launchWhatDoesItMeanFragment(FragmentActivity fragmentActivity) {
        FragmentManager mFragmentManager = fragmentActivity.
                getSupportFragmentManager();
        Fragment whatDoesItMeanFragment = getWhatDoesItMeanFragment();
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.replace(cookiesConsentFragmentView.getContainerId(),
                whatDoesItMeanFragment,
                CookiesConsentInfoFragment.TAG);
        fragmentTransaction.addToBackStack(CookiesConsentInfoFragment.TAG);
        fragmentTransaction.commitAllowingStateLoss();
    }

    public Fragment getWhatDoesItMeanFragment() {
        return new CookiesConsentInfoFragment();
    }
}
