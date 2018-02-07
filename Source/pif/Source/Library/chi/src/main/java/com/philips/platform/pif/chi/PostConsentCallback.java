/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.pif.chi;

import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

public interface PostConsentCallback {
    void onPostConsentFailed(ConsentDefinition definition, ConsentError error);

    void onPostConsentSuccess(Consent consent);
}
