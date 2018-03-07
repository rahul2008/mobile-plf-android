/* Copyright (c) Koninklijke Philips N.V., 2018
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.appinfra.consentmanager;

import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.List;

public interface ConsentManagerInterface extends ConsentHandlerInterface {
    void register(List<String> consentType, ConsentHandlerInterface consentHandlerInterface);

    void deregister(List<String> consentType);

    void fetchConsentState(ConsentDefinition consentDefinition, final CheckConsentsCallback callback);

    void fetchConsentStates(List<ConsentDefinition> consentDefinitions, final CheckConsentsCallback callback);

    void storeConsentState(final ConsentDefinition definition, boolean status, PostConsentCallback callback);
}
