/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.cookiesconsent;

import android.support.annotation.NonNull;

import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.SharedPreferenceUtility;
import com.philips.platform.uappframework.launcher.FragmentLauncher;


public class CookiesConsentInfoPresenter extends AbstractUIBasePresenter {
    public static String TAG = CookiesConsentInfoPresenter.class.getSimpleName();

    private final int MENU_OPTION_HOME = 0;
    private SharedPreferenceUtility sharedPreferenceUtility;
    private BaseState baseState;
    private CookiesConsentInfoFragmentView cookiesConsentFragmentView;

    public CookiesConsentInfoPresenter(CookiesConsentInfoFragmentView cookiesConsentFragmentView) {
        super(cookiesConsentFragmentView);
        this.cookiesConsentFragmentView = cookiesConsentFragmentView;
    }

    @Override
    public void onEvent(final int componentID) {


    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) cookiesConsentFragmentView.getFragmentActivity().getApplicationContext();
    }

    @NonNull
    protected FragmentLauncher getFragmentLauncher() {
        return new FragmentLauncher(cookiesConsentFragmentView.getFragmentActivity(), cookiesConsentFragmentView.getContainerId(), cookiesConsentFragmentView.getActionBarListener());
    }

}
