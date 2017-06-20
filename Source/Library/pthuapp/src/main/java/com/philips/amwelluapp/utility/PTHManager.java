package com.philips.amwelluapp.utility;

import android.content.Context;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.AWSDKFactory;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.amwelluapp.login.PTHAuthentication;
import com.philips.amwelluapp.login.PTHLoginCallBack;
import com.philips.amwelluapp.sdkerrors.PTHSDKError;
import com.philips.amwelluapp.welcome.PTHInitializeCallBack;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class PTHManager {
    private static PTHManager sPthManager = null;
    private AWSDK mAwsdk = null;

    public static PTHManager getInstance() {
        if (sPthManager == null) {
            sPthManager = new PTHManager();
        }
        return sPthManager;
    }


    public AWSDK getAwsdk(Context context) throws AWSDKInstantiationException {
        if (mAwsdk == null) {
            mAwsdk = AWSDKFactory.getAWSDK(context);
        }
        return mAwsdk;
    }

    public void authenticate(Context context, String username, String password, String variable, final PTHLoginCallBack pthLoginCallBack) throws AWSDKInstantiationException {
        SDKCallback<Authentication, SDKError> sdkCallback = new SDKCallback<Authentication, SDKError>() {
            @Override
            public void onResponse(Authentication authentication, SDKError sdkError) {

                PTHAuthentication pthAuthentication = new PTHAuthentication();
                pthAuthentication.setAuthentication(authentication);

                PTHSDKError pthsdkError = new PTHSDKError();
                pthsdkError.setSdkError(sdkError);

                pthLoginCallBack.onLoginResponse(pthAuthentication, pthsdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                pthLoginCallBack.onLoginFailure(throwable);
            }
        };
        getAwsdk(context).authenticate(username, password, variable, sdkCallback);
    }

    public void initializeTeleHealth(Context context, final PTHInitializeCallBack pthInitializeCallBack) throws MalformedURLException, URISyntaxException, AWSDKInstantiationException, AWSDKInitializationException {
        final Map<AWSDK.InitParam, Object> initParams = new HashMap<>();
        initParams.put(AWSDK.InitParam.BaseServiceUrl, "https://sdk.myonlinecare.com");
        initParams.put(AWSDK.InitParam.ApiKey, "62f5548a"); //client key


        getAwsdk(context).initialize(
                initParams, new SDKCallback<Void, SDKError>() {
                    @Override
                    public void onResponse(Void aVoid, SDKError sdkError) {
                        PTHSDKError pthsdkError = new PTHSDKError();
                        pthsdkError.setSdkError(sdkError);
                        pthInitializeCallBack.onInitializationResponse(aVoid, pthsdkError);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        pthInitializeCallBack.onInitializationFailure(throwable);
                    }
                });
    }

    public void setAwsdk(AWSDK awsdk) {
        this.mAwsdk = awsdk;
    }

    /* public void completeEnrollment(Context context, PTHAuthentication pthAuthentication, PTHState state, String newUserName, String newPassword, final PTHRegistrationDetailCallback pthRegistrationDetailCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().completeEnrollment(pthAuthentication.getAuthentication(), state.getState(), newUserName, newPassword, new SDKCallback<Consumer, SDKPasswordError>() {
            @Override
            public void onResponse(Consumer consumer, SDKPasswordError sdkPasswordError) {

                PTHConsumer pthConsumer = new PTHConsumer();
                pthConsumer.setConsumer(consumer);

                PTHSDKPasswordError pthsdkPasswordError = new PTHSDKPasswordError();
                pthsdkPasswordError.setSdkPasswordError(sdkPasswordError);

                pthRegistrationDetailCallback.onResponse(pthConsumer,pthsdkPasswordError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                pthRegistrationDetailCallback.onFailure(throwable);
            }
        });
    }
*/
}
