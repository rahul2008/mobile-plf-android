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
import com.philips.platform.appinfra.consentmanager.FetchConsentCallback;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;


public class ConditionCookiesConsent extends BaseCondition implements FetchConsentCallback {


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
        ConsentDefinition consentDefinition = appInfra.getConsentManager().getConsentDefinitionForType(appInfra.getAbTesting().getAbTestingConsentIdentifier());
        if (consentDefinition != null) {
            appInfra.getConsentManager().fetchConsentState(consentDefinition,this);
        }
        return shouldLaunchAbTesting;
    }

    @Override
    public void onGetConsentSuccess(ConsentDefinitionStatus consentDefinitionStatus) {
        if (consentDefinitionStatus != null && (consentDefinitionStatus.getConsentState() == ConsentStates.active
                || consentDefinitionStatus.getConsentState() == ConsentStates.rejected)) {
            shouldLaunchAbTesting = false;
        }
    }

    @Override
    public void onGetConsentFailed(ConsentError error) {
        shouldLaunchAbTesting = true;
    }
}
