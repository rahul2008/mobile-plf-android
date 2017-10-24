package com.philips.plataform.mya.model.listener;

import com.philips.plataform.mya.model.response.ConsentModel;

import java.util.List;

/**
 * Created by Maqsood on 10/13/17.
 */

public interface ConsentResponseListener {
    void onResponseSuccessConsent(List<ConsentModel> responseData);
    int onResponseFailureConsent(int consentError);
}
