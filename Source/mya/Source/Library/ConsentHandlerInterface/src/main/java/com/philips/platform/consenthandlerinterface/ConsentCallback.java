/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.consenthandlerinterface;

import com.philips.platform.consenthandlerinterface.datamodel.Consent;

public interface ConsentCallback {
    void onGetConsentRetrieved(final Consent consent);

    void onGetConsentFailed(ConsentError error);
}
