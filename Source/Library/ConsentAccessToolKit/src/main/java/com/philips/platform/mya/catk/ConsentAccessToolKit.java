package com.philips.platform.mya.catk;

import android.content.Context;
import android.os.Message;

import com.android.volley.Request;
import com.philips.cdp.registration.User;
import com.philips.platform.mya.catk.error.ConsentNetworkError;
import com.philips.platform.mya.catk.listener.ConsentResponseListener;
import com.philips.platform.mya.catk.model.GetConsentsModel;
import com.philips.platform.mya.catk.network.NetworkAbstractModel;
import com.philips.platform.mya.catk.network.NetworkHelper;
import com.philips.platform.mya.catk.model.GetConsentsModelRequest;
import com.philips.platform.mya.catk.utils.ConsentUtil;

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
        getNetworkHelper().sendRequest(context,Request.Method.GET, model, model);
    }

    private NetworkHelper getNetworkHelper() {
        if (networkHelper == null) {
            networkHelper = NetworkHelper.getInstance();
        }
        return networkHelper;
    }
}
