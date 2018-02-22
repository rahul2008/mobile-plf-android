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
import com.philips.platform.dscdemo.activity.DSLaunchActivity;
import com.philips.platform.dscdemo.activity.JustInTimeActivity;
import com.philips.platform.dscdemo.moments.MomentFragment;
import com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies;
import com.philips.platform.mya.csw.justintime.JustInTimeWidgetHandler;
import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;
import com.philips.platform.uappframework.UappInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.Collections;
import java.util.List;

public class DSDemoAppuAppInterface implements UappInterface {

    private Context mContext;

    /**
     * @param uappDependencies - App dependencies
     * @param uappSettings     - App settings
     */
    @Override
    public void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        mContext = uappSettings.getContext();
        DSDemoAppuAppDependencies dsDependencies = (DSDemoAppuAppDependencies) uappDependencies;
        JustInTimeConsentDependencies.consentHandlerInterface = dsDependencies.momentConsentHandler;
        JustInTimeConsentDependencies.consentDefinition = dsDependencies.momentConsentDefinition;
        AppInfraInterface appInfra = dsDependencies.getAppInfra();
        JustInTimeConsentDependencies.appInfra = appInfra;
        JustInTimeConsentDependencies.textResources = dsDependencies.textResources;

        DemoAppManager mDemoAppManager = DemoAppManager.getInstance();
        mDemoAppManager.initPreRequisite(uappSettings.getContext(), appInfra);
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        List<ConsentDefinition> consentDefinitionsToCheck = Collections.singletonList(JustInTimeConsentDependencies.consentDefinition);
        JustInTimeConsentDependencies.consentHandlerInterface.fetchConsentStates(consentDefinitionsToCheck, new CheckConsentsListener(uiLauncher));
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
        Intent intent = new Intent(mContext, activityClass);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void launchAsFragment(UiLauncher uiLauncher) {
        final FragmentLauncher fragmentLauncher = (FragmentLauncher) uiLauncher;
        DSBaseFragment momentsFragment = new MomentFragment();
        momentsFragment.showFragment(momentsFragment, fragmentLauncher);
    }

    class CheckConsentsListener implements CheckConsentsCallback {
        private UiLauncher uiLauncher;

        public CheckConsentsListener(UiLauncher uiLauncher) {
            this.uiLauncher = uiLauncher;
        }

        @Override
        public void onGetConsentsSuccess(List<Consent> consents) {
            if (isMomentConsentNotGiven(consents)) {
                launchJustInTimeConsent(uiLauncher);
            } else {
                launchUApp(uiLauncher);
            }
        }

        @Override
        public void onGetConsentsFailed(ConsentError error) {
        }
    }

    private boolean isMomentConsentNotGiven(final List<Consent> consents) {
        Consent momentConsent = null;
        for (Consent consent : consents) {
            if (consent != null && "moment".equals(consent.getType())) {
                momentConsent = consent;
                break;
            }
        }
        return momentConsent == null || momentConsent.getStatus() == ConsentStatus.inactive;
    }
}
