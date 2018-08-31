/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.condition;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppConditions;
import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.consentmanager.consenthandler.DeviceStoredConsentHandler;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import static com.philips.platform.appframework.abtesting.AbTestingImpl.AB_TESTING_CONSENT;


public class ConditionCookiesConsent extends BaseCondition implements FetchConsentTypeStateCallback {


    /**
     * Constructor of BaseCondition
     *
     * @since 1.1.0
     */

    private boolean shouldLaunchAbTesting = true;

    public ConditionCookiesConsent() {
        super(AppConditions.LAUNCH_COOKIES_CONSENT);
    }

    @Override
    public boolean isSatisfied(Context context) {
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        AppInfraInterface appInfra = appFrameworkApplication.getAppInfra();
        new DeviceStoredConsentHandler(appInfra).fetchConsentTypeState(AB_TESTING_CONSENT,this);
        return shouldLaunchAbTesting;
    }

    @Override
    public void onGetConsentsSuccess(ConsentStatus consentStatus) {
        if (consentStatus != null && (consentStatus.getConsentState() == ConsentStates.active
                || consentStatus.getConsentState() == ConsentStates.rejected)) {
            shouldLaunchAbTesting = false;
        }
    }

    @Override
    public void onGetConsentsFailed(ConsentError error) {
        shouldLaunchAbTesting = true;
    }
}
