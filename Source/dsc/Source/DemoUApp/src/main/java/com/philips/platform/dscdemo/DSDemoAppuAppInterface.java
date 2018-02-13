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
import com.philips.platform.mya.chi.CheckConsentsCallback;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentStatus;
import com.philips.platform.mya.csw.justintime.JustInTimeDependencies;
import com.philips.platform.mya.csw.justintime.JustInTimeTextResources;
import com.philips.platform.mya.csw.justintime.JustInTimeWidgetHandler;
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
        JustInTimeDependencies.consentHandlerInterface = dsDependencies.momentConsentHandler;
        JustInTimeDependencies.consentDefinition = dsDependencies.momentConsentDefinition;
        AppInfraInterface appInfra = dsDependencies.getAppInfra();
        JustInTimeDependencies.appInfra = appInfra;
        JustInTimeDependencies.textResources = dsDependencies.textResources;

        DemoAppManager mDemoAppManager = DemoAppManager.getInstance();
        mDemoAppManager.initPreRequisite(uappSettings.getContext(), appInfra);
    }

    /**
     * @param uiLauncher - Launcher to differentiate activity or fragment
     */
    @Override
    public void launch(final UiLauncher uiLauncher, final UappLaunchInput uappLaunchInput) {
        JustInTimeDependencies.consentHandlerInterface.fetchConsentStates(Collections.singletonList(JustInTimeDependencies.consentDefinition), new CheckConsentsCallback() {
            @Override
            public void onGetConsentsSuccess(List<Consent> consents) {
                boolean showConsentScreen = false;
                for (Consent consent : consents) {
                    if (consent != null && "moment".equals(consent.getType()) && consent.getStatus() != ConsentStatus.active) {
                        showConsentScreen = true;
                        break;
                    }
                }
                if (showConsentScreen) {
                    launchJIT(uiLauncher);
                } else {
                    launch(uiLauncher);
                }
            }

            @Override
            public void onGetConsentsFailed(ConsentError error) {
                System.out.println("bla");
            }
        });
    }

    private void launch(UiLauncher uiLauncher) {
        if (uiLauncher instanceof ActivityLauncher) {
            launchActivity(DSLaunchActivity.class);
        } else {
            launchAsFragment(uiLauncher);
        }
    }

    private void launchJIT(final UiLauncher uiLauncher) {
        JustInTimeDependencies.completionListener = new JustInTimeWidgetHandler() {
            @Override
            public void onConsentGiven() {
                launch(uiLauncher);
            }

            @Override
            public void onConsentRejected() {
                launch(uiLauncher);
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
}
