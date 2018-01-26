/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.launcher;

import java.util.ArrayList;
import java.util.List;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.mya.chi.ConsentConfiguration;
import com.philips.platform.uappframework.uappinput.UappDependencies;

/**
 * This class is used to provide dependencies for myaccount.
 *
 * @since 2017.5.0
 */
public class MyaDependencies extends UappDependencies {

    private final List<ConsentConfiguration> consentConfigurationList;
    private final String privacyNoticeURL;

    public MyaDependencies(AppInfraInterface appInfra, List<ConsentConfiguration> consentConfigurationList, String privacyNoticeURL) {
        super(appInfra);
        this.consentConfigurationList = consentConfigurationList == null ? new ArrayList<ConsentConfiguration>() : consentConfigurationList;
        this.privacyNoticeURL = privacyNoticeURL;
    }

    public List<ConsentConfiguration> getConsentConfigurationList() {
        return consentConfigurationList;
    }
}
