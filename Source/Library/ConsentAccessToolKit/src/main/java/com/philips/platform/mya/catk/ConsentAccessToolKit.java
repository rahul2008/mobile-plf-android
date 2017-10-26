package com.philips.platform.mya.catk;

import android.content.Context;
import android.os.Message;

import com.philips.cdp.registration.User;
import com.philips.platform.mya.catk.error.ConsentNetworkError;
import com.philips.platform.mya.catk.listener.ConsentResponseListener;
import com.philips.platform.mya.catk.listener.CreateConsentListener;
import com.philips.platform.mya.catk.model.CreateConsentModelRequest;
import com.philips.platform.mya.catk.model.GetConsentsModel;
import com.philips.platform.mya.catk.model.GetConsentsModelRequest;
import com.philips.platform.mya.catk.network.NetworkAbstractModel;
import com.philips.platform.mya.catk.network.NetworkHelper;
import com.philips.platform.mya.catk.utils.ConsentUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Maqsood on 10/17/17.
 */

public class ConsentAccessToolKit {

    private NetworkHelper networkHelper;
    private User user;
    private Context context;
    private String applicationName;
    private String propositionName;

    public ConsentAccessToolKit(Context context,String applicationName,String propositionName){
        this.context = context;
        this.applicationName = applicationName;
        this.propositionName = propositionName;
        this.user = new User(context);
    }

    public void getConsentDetails(final ConsentResponseListener consentListener){
        GetConsentsModelRequest model = new GetConsentsModelRequest(applicationName,propositionName,user,new NetworkAbstractModel.DataLoadListener() {
            @Override
            public void onModelDataLoadFinished(Message msg) {
                GetConsentsModel[] modelResults = (GetConsentsModel[]) msg.obj;
                ArrayList<GetConsentsModel> consentModels = new ArrayList<>(Arrays.asList(modelResults));
                consentListener.onResponseSuccessConsent(consentModels);
            }

            @Override
            public int onModelDataError(Message msg) {
                if (msg.obj instanceof ConsentNetworkError) {
                   return consentListener.onResponseFailureConsent(((ConsentNetworkError) msg.obj).getErrorCode());
                }
                return ConsentUtil.CONSENT_ERROR_UNKNOWN;
            }
        });
        getNetworkHelper().sendRequest(context,model.getMethod(), model, model);
    }


    public void createConsent(String consentStatus,final CreateConsentListener consentListener){
         CreateConsentModelRequest model = new CreateConsentModelRequest(applicationName,consentStatus,propositionName,user,
                 new NetworkAbstractModel.DataLoadListener() {
            @Override
            public void onModelDataLoadFinished(Message msg) {
                if(msg.arg1==0){
                    consentListener.onSuccess(ConsentUtil.CONSENT_SUCCESS);
                }
            }

            @Override
            public int onModelDataError(Message msg) {
                if (msg.obj instanceof ConsentNetworkError) {
                    return consentListener.onFailure(((ConsentNetworkError) msg.obj).getErrorCode());
                }
                return ConsentUtil.CONSENT_ERROR_UNKNOWN;
            }
        });
        getNetworkHelper().sendRequest(context,model.getMethod(), model, model);
    }

    public void getStatusForConsentType(String consentType, int version, final ConsentResponseListener consentListener) {
        final String policyRule = buildPolicyRule(consentType, version, user.getCountryCode(), propositionName, applicationName);
        getConsentDetails(new ConsentResponseListener() {

            @Override
            public void onResponseSuccessConsent(List<GetConsentsModel> responseData) {
                for (GetConsentsModel consent: responseData) {
                    if (policyRule.equals(consent.getPolicyRule())) {
                        consentListener.onResponseSuccessConsent(Collections.singletonList(consent));
                        return;
                    }
                    consentListener.onResponseSuccessConsent(null);
                }
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
