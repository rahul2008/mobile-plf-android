package com.philips.cdp.digitalcare.homefragment;


import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.digitalcare.CcConsentProvider;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies;
import com.philips.platform.mya.csw.justintime.JustInTimeConsentFragment;
import com.philips.platform.mya.csw.justintime.JustInTimeTextResources;
import com.philips.platform.mya.csw.justintime.JustInTimeWidgetHandler;
import com.philips.platform.uappframework.listener.ActionBarListener;

class JustInTimeLauncher {
    HomeFragmentContract.View viewContract;

    void addJustInTimeConsentDependencies(Context context, HomeFragmentContract.View viewContract) {
        this.viewContract = viewContract;
        JustInTimeConsentDependencies.appInfra = DigitalCareConfigManager.getInstance().getAPPInfraInstance();
        JustInTimeConsentDependencies.consentDefinition = CcConsentProvider.fetchLocationConsentDefinition(context);
        JustInTimeConsentDependencies.consentManager = CcConsentProvider.fetchConsentManager();
        JustInTimeConsentDependencies.textResources = getJustInTimeTextResources();
        JustInTimeConsentDependencies.completionListener = getJustInTimeWidgetHandler();
    }

    JustInTimeConsentFragment getJustInTimeFragment(int containerID, ActionBarListener actionBarListener) {
        JustInTimeConsentFragment justInTimeConsentFragment = JustInTimeConsentFragment.newInstance(containerID);
        justInTimeConsentFragment.setUpdateTitleListener(actionBarListener);
        return justInTimeConsentFragment;
    }

    @NonNull
    private JustInTimeWidgetHandler getJustInTimeWidgetHandler() {
        return new JustInTimeWidgetHandler() {
            @Override
            public void onConsentGiven() {
                viewContract.onConsentProvided(true);
            }

            @Override
            public void onConsentRejected() {
                viewContract.onConsentProvided(false);
            }
        };
    }

    @NonNull
    private JustInTimeTextResources getJustInTimeTextResources() {
        JustInTimeTextResources resources = new JustInTimeTextResources();
        resources.titleTextRes = R.string.dcc_location_consent_title;
        resources.rejectTextRes = R.string.dcc_location_consent_reject;
        resources.acceptTextRes = R.string.dcc_location_consent_accept;
        return resources;
    }
}
