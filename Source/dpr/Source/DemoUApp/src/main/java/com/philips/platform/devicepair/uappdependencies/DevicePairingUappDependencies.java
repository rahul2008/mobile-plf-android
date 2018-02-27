/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.uappdependencies;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.pif.chi.ConsentRegistryInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;


public class DevicePairingUappDependencies extends UappDependencies {

    private CommCentral commCentral;
    private ConsentRegistryInterface consentRegistryInterface;
    private AppInfraInterface appInfraInterface;

    public DevicePairingUappDependencies(AppInfraInterface appInfra, ConsentRegistryInterface consentRegistryInterface, CommCentral commCentral) {
        super(appInfra);
        this.commCentral = commCentral;
        this.consentRegistryInterface = consentRegistryInterface;
        this.appInfraInterface = appInfra;
    }

    public CommCentral getCommCentral() {
        return commCentral;
    }

    public ConsentRegistryInterface getConsentRegistryInterface() {
        return consentRegistryInterface;
    }

    public AppInfraInterface getAppInfra() {
        return appInfraInterface;
    }
}
