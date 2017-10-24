package com.philips.platform.mya.catk.listener;

import com.philips.platform.mya.catk.model.GetConsentsModel;

import java.util.List;

/**
 * Created by Maqsood on 10/13/17.
 */

public interface ConsentResponseListener {
    void onResponseSuccessConsent(List<GetConsentsModel> responseData);
    int onResponseFailureConsent(int consentError);
}
