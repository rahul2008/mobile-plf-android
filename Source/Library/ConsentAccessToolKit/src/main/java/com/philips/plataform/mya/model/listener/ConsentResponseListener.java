package com.philips.plataform.mya.model.listener;

import com.philips.plataform.mya.model.error.ConsentError;
import com.philips.plataform.mya.model.response.ConsentModel;

import java.util.ArrayList;

/**
 * Created by Maqsood on 10/13/17.
 */

public interface ConsentResponseListener {
    void onResponseSuccessConsent(ArrayList<ConsentModel> responseData);
    void onResponseFailureConsent(ConsentError consentError);
}
