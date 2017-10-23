package com.philips.plataform.mya.model;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.philips.cdp.registration.User;
import com.philips.plataform.mya.model.error.ConsentNetworkError;
import com.philips.plataform.mya.model.listener.ConsentResponseListener;
import com.philips.plataform.mya.model.network.NetworkAbstractModel;
import com.philips.plataform.mya.model.network.NetworkHelper;
import com.philips.plataform.mya.model.request.ConsentModelRequest;
import com.philips.plataform.mya.model.response.ConsentModel;
import com.philips.plataform.mya.model.utils.ConsentUtil;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Maqsood on 10/17/17.
 */

public class ConsentAccessToolKit {

    private NetworkHelper networkHelper;
    private User user;

    public void getConsentDetails(Context context,String applicationName,String propositionName,final ConsentResponseListener consentListener){
        user = new User(context);
        ConsentModelRequest model = new ConsentModelRequest(applicationName,propositionName,user,new NetworkAbstractModel.DataLoadListener() {
            @Override
            public void onModelDataLoadFinished(Message msg) {
                ConsentModel[] modelResults = (ConsentModel[]) msg.obj;
                ArrayList<ConsentModel> consentModels = new ArrayList<>(Arrays.asList(modelResults));
                consentListener.onResponseSuccessConsent(consentModels);
            }

            @Override
            public int onModelDataError(Message msg) {
                if (msg.obj instanceof ConsentNetworkError) {
                    return ((ConsentNetworkError) msg.obj).getErrorCode();
                }
                return ConsentUtil.CONSENT_ERROR_UNKNOWN;
            }
        });
        getNetworkHelper().sendRequest(context,Request.Method.GET, model, model);
    }

    private NetworkHelper getNetworkHelper() {
        if (networkHelper == null) {
            networkHelper = NetworkHelper.getInstance();
        }
        return networkHelper;
    }
}
