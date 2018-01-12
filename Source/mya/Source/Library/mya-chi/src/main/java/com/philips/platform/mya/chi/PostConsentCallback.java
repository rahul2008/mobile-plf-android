/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.chi;

import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;

public interface PostConsentCallback {
    void onPostConsentFailed(ConsentDefinition definition, ConsentError error);

    void onPostConsentSuccess(Consent consent);
}
