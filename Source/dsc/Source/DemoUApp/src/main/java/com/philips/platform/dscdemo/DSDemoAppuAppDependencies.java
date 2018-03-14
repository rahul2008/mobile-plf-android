/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dscdemo;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.csw.justintime.JustInTimeTextResources;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.uappframework.uappinput.UappDependencies;

public class DSDemoAppuAppDependencies extends UappDependencies {
    public JustInTimeTextResources textResources;
    public ConsentDefinition momentConsentDefinition;
    public ConsentManagerInterface consentManager;

    public DSDemoAppuAppDependencies(final AppInfraInterface appInfra,
                                     final ConsentManagerInterface consentManager, final ConsentDefinition momentConsentDefinition,
                                     final JustInTimeTextResources textResources) {
        super(appInfra);
        this.consentManager = consentManager;
        this.momentConsentDefinition = momentConsentDefinition;
        this.textResources = textResources;
    }
}
