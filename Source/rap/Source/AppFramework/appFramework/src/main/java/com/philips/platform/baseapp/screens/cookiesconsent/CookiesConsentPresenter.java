/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.cookiesconsent;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.baseapp.screens.utility.SharedPreferenceUtility;
import com.philips.platform.uappframework.launcher.FragmentLauncher;


public class CookiesConsentPresenter extends AbstractUIBasePresenter {
    public static String TAG = CookiesConsentPresenter.class.getSimpleName();

    private final int MENU_OPTION_HOME = 0;
    private SharedPreferenceUtility sharedPreferenceUtility;
    private BaseState baseState;
    private CookiesConsentFragmentView cookiesConsentFragmentView;
 
    public CookiesConsentPresenter(CookiesConsentFragmentView cookiesConsentFragmentView) {
        super(cookiesConsentFragmentView);
        this.cookiesConsentFragmentView = cookiesConsentFragmentView;
    }

    @Override
    public void onEvent(final int componentID) {
        AppFrameworkApplication appFrameworkApplication =
                (AppFrameworkApplication) cookiesConsentFragmentView.getFragmentActivity().getApplication();
        BaseFlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
        BaseState baseState = null;
        try {
            switch (componentID) {
                case R.id.usr_cookiesConsentScreen_accept_button:
                    String EVENT_ALLOW = "accept";
                    baseState = getNextState(targetFlowManager, EVENT_ALLOW);
                    break;


                case R.id.usr_cookiesConsentScreen_Reject_button:
                    String EVENT_REJECT = "reject";
                    baseState = getNextState(targetFlowManager, EVENT_REJECT);
                    break;



            }
        } catch (Exception e){
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

}
