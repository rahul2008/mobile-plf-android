/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.consentmanager.FetchConsentCallback;
import com.philips.platform.csw.justintime.JustInTimeConsentDependencies;
import com.philips.platform.csw.justintime.JustInTimeTextResources;
import com.philips.platform.csw.justintime.JustInTimeWidgetHandler;
import com.philips.platform.dscdemo.activity.DSLaunchActivity;
import com.philips.platform.dscdemo.activity.JustInTimeActivity;
import com.philips.platform.dscdemo.moments.MomentFragment;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class DSDemoAppuAppInterface implements UappInterface {

    private Context context;
    private AppInfraInterface appInfra;

    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        context = uappSettings.getContext();
        DSDemoAppuAppDependencies dsDependencies = (DSDemoAppuAppDependencies) uappDependencies;
        appInfra = dsDependencies.getAppInfra();
        JustInTimeConsentDependencies.appInfra = appInfra;
        JustInTimeTextResources textResources = new JustInTimeTextResources();
        textResources.titleTextRes = R.string.DSC_CSW_JustInTime_Title;
        textResources.acceptTextRes = R.string.DSC_CSW_JustInTime_Accept;
        textResources.rejectTextRes = R.string.DSC_CSW_JustInTime_Reject;
        textResources.userBenefitsTitleRes = R.string.mya_csw_justintime_user_benefits_title;
        textResources.userBenefitsDescriptionRes = R.string.mya_csw_justintime_user_benefits_description;
        JustInTimeConsentDependencies.textResources = textResources;

        DemoAppManager mDemoAppManager = DemoAppManager.getInstance();
        mDemoAppManager.initPreRequisite(uappSettings.getContext(), appInfra);
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        if (appInfra.getRestClient().isInternetReachable()) {
            JustInTimeConsentDependencies.appInfra.getConsentManager().fetchConsentTypeState("moment", new CheckConsentsListener(uiLauncher));
        } else {
            launchUApp(uiLauncher);
        }
    }

    private void launchUApp(UiLauncher uiLauncher) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchActivity(DSLaunchActivity.class);
        } else {
            launchAsFragment(uiLauncher);
        }
    }

    private void launchJustInTimeConsent(final UiLauncher uiLauncher) {
        JustInTimeConsentDependencies.completionListener = new JustInTimeWidgetHandler() {
            @Override
            public void onConsentGiven() {
                launchUApp(uiLauncher);
            }

            @Override
            public void onConsentRejected() {
                launchUApp(uiLauncher);
            }
        };
        launchActivity(JustInTimeActivity.class);
    }

    private void launchActivity(final Class<? extends Activity> activityClass) {
        Intent intent = new Intent(context, activityClass);
        context.startActivity(intent);
    }

    private void launchAsFragment(UiLauncher uiLauncher) {
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        DSBaseFragment momentsFragment = new MomentFragment();
        momentsFragment.showFragment(momentsFragment, fragmentLauncher);
    }

    class CheckConsentsListener implements FetchConsentCallback {
        private UiLauncher uiLauncher;

        private CheckConsentsListener(UiLauncher uiLauncher) {
            this.uiLauncher = uiLauncher;
        }

        @Override
        public void onGetConsentSuccess(final ConsentDefinitionStatus consentDefinitionStatus) {
            if (isMomentConsentNotGiven(consentDefinitionStatus)) {
                JustInTimeConsentDependencies.consentDefinition = consentDefinitionStatus.getConsentDefinition();
                launchJustInTimeConsent(uiLauncher);
            } else {
                launchUApp(uiLauncher);
            }
        }

        @Override
        public void onGetConsentFailed(final ConsentError error) {
            launchUApp(uiLauncher);
        }
    }

    private boolean isMomentConsentNotGiven(ConsentDefinitionStatus status) {
        return status.getConsentState().equals(ConsentStates.inactive);
    }
}
