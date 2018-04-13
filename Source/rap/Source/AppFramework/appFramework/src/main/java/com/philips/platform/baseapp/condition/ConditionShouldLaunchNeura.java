/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.baseapp.condition;

import android.content.Context;

import com.philips.platform.appframework.flowmanager.AppConditions;
import com.philips.platform.appframework.flowmanager.base.BaseCondition;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.neura.NeuraConsentProvider;
import com.philips.platform.mya.catk.device.DeviceStoredConsentHandler;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;


public class ConditionShouldLaunchNeura extends BaseCondition implements FetchConsentTypeStateCallback {


    /**
     * Constructor of BaseCondition
     *
     * @since 1.1.0
     */

    private boolean isConsentProvided;

    public ConditionShouldLaunchNeura() {
        super(AppConditions.SHOULD_LAUNCH_NEURA);
    }

    @Override
    public boolean isSatisfied(Context context) {
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        NeuraConsentProvider neuraConsentProvider = new NeuraConsentProvider(new DeviceStoredConsentHandler(appFrameworkApplication.getAppInfra()));
        neuraConsentProvider.fetchConsentHandler(this);
        return !isConsentProvided;
    }

    @Override
    public void onGetConsentsSuccess(ConsentStatus consentStatus) {
        if (consentStatus != null && consentStatus.getConsentState() == ConsentStates.active) {
            isConsentProvided = true;
        }
    }

    @Override
    public void onGetConsentsFailed(ConsentError error) {
        isConsentProvided = false;
    }
}
