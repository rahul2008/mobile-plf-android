package com.philips.platform.catk;

import android.os.Message;

import com.janrain.android.utils.StringUtils;
import com.philips.cdp.registration.User;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.injection.UserLocale;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.model.CreateConsentModelRequest;
import com.philips.platform.catk.model.GetConsentsModel;
import com.philips.platform.catk.model.GetConsentsModelRequest;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.network.NetworkHelper;
import com.philips.platform.catk.response.ConsentStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by Maqsood on 10/17/17.
 */

public class ConsentAccessToolKit {

    private NetworkHelper networkHelper;
    private String applicationName;
    private String propositionName;

    @Inject
    UserLocale userLocale;

    public ConsentAccessToolKit(String applicationName,String propositionName){
        this.applicationName = applicationName;
        this.propositionName = propositionName;
        CatkInterface.getCatkComponent().inject(this);
    }

    public void getConsentDetails(final ConsentResponseListener consentListener){
        GetConsentsModelRequest model = new GetConsentsModelRequest(applicationName,propositionName,new NetworkAbstractModel.DataLoadListener() {
            @Override
            public void onModelDataLoadFinished(Message msg) {
                ArrayList<GetConsentsModel> consentModels;
                if (StringUtils.trim(msg.obj.toString()) != "") {
                    GetConsentsModel[] modelResults = (GetConsentsModel[]) msg.obj;
                    consentModels = new ArrayList<>(Arrays.asList(modelResults));
                } else{
                    consentModels = new ArrayList<>();
                }
                consentListener.onResponseSuccessConsent(consentModels);
            }

            @Override
            public int onModelDataError(Message msg) {
                if (msg.obj instanceof ConsentNetworkError) {
                   return consentListener.onResponseFailureConsent(((ConsentNetworkError) msg.obj).getErrorCode());
                }
                return CatkConstants.CONSENT_ERROR_UNKNOWN;
            }
        });
        getNetworkHelper().sendRequest(model.getMethod(), model, model);
    }


    public void createConsent(ConsentStatus consentStatus, final CreateConsentListener consentListener){
         CreateConsentModelRequest model = new CreateConsentModelRequest(applicationName,String.valueOf(consentStatus),propositionName, userLocale.locale,
                 new NetworkAbstractModel.DataLoadListener() {
            @Override
            public void onModelDataLoadFinished(Message msg) {
                if(msg.arg1==0){
                    consentListener.onSuccess(CatkConstants.CONSENT_SUCCESS);
                }
            }

            @Override
            public int onModelDataError(Message msg) {
                if (msg.obj instanceof ConsentNetworkError) {
                    return consentListener.onFailure(((ConsentNetworkError) msg.obj).getErrorCode());
                }
                return CatkConstants.CONSENT_ERROR_UNKNOWN;
            }
        });
        getNetworkHelper().sendRequest(model.getMethod(), model, model);
    }

    public void getStatusForConsentType(String consentType, int version, final ConsentResponseListener consentListener) {
        final String policyRule = buildPolicyRule(consentType, version, CatkInterface.getCatkComponent().getUser().getCountryCode(), propositionName, applicationName);
        getConsentDetails(new ConsentResponseListener() {

            @Override
            public void onResponseSuccessConsent(List<GetConsentsModel> responseData) {
                for (GetConsentsModel consent: responseData) {
                    if (policyRule.equals(consent.getPolicyRule())) {
                        consentListener.onResponseSuccessConsent(Collections.singletonList(consent));
                        return;
                    }
                }
                consentListener.onResponseSuccessConsent(null);
            }

            @Override
            public int onResponseFailureConsent(int consentError) {
                return consentListener.onResponseFailureConsent(consentError);
            }
        });
    }

    private String buildPolicyRule(String consentType, int version, String country, String propositionName, String applicationName) {
        return "urn:com.philips.consent:" + consentType + "/" + country + "/" + version + "/" + propositionName + "/" + applicationName;
    }

    private NetworkHelper getNetworkHelper() {
        if (networkHelper == null) {
            networkHelper = NetworkHelper.getInstance();
        }
        return networkHelper;
    }
}
