/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.listener;

import java.util.List;

import com.philips.platform.catk.dto.GetConsentsModel;

public interface ConsentResponseListener {
    void onResponseSuccessConsent(List<GetConsentsModel> responseData);

    int onResponseFailureConsent(int consentError);
}
