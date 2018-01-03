/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk.listener;

import com.philips.platform.mya.catk.error.ConsentNetworkError;
import com.philips.platform.mya.chi.datamodel.BackendConsent;

import java.util.List;

public interface ConsentResponseListener {
    void onResponseSuccessConsent(List<BackendConsent> responseData);

    void onResponseFailureConsent(ConsentNetworkError error);
}
