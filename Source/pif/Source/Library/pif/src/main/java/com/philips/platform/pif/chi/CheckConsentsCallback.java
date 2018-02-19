/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.pif.chi;

import com.philips.platform.pif.chi.datamodel.Consent;

import java.util.List;

public interface CheckConsentsCallback {
    void onGetConsentsSuccess(final List<Consent> consents);

    void onGetConsentsFailed(ConsentError error);
}
