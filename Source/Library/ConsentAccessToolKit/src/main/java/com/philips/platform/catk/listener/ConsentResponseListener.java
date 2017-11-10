/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.listener;

import com.philips.platform.catk.model.GetConsentsModel;

import java.util.List;

/**
 * Created by Maqsood on 10/13/17.
 */

public interface ConsentResponseListener {
    void onResponseSuccessConsent(List<GetConsentsModel> responseData);
    int onResponseFailureConsent(int consentError);
}
