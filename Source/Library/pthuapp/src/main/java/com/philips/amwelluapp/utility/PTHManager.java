package com.philips.amwelluapp.utility;

import android.content.Context;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.AWSDKFactory;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.practice.Practice;
<<<<<<< HEAD
=======
import com.americanwell.sdk.entity.provider.ProviderInfo;
>>>>>>> 95411b51c0c33beef7ab8444f653ced3f4515c55
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.SDKCallback;
import com.philips.amwelluapp.login.PTHAuthentication;
import com.philips.amwelluapp.login.PTHLoginCallBack;
<<<<<<< HEAD
import com.philips.amwelluapp.practice.PTHGetConsumerObjectCallBack;
import com.philips.amwelluapp.practice.PTHPracticesListCallback;
=======
import com.philips.amwelluapp.providerslist.PTHGetConsumerObjectCallBack;
import com.philips.amwelluapp.providerslist.PTHPracticesListCallback;
import com.philips.amwelluapp.providerslist.PTHProvidersListCallback;
>>>>>>> 95411b51c0c33beef7ab8444f653ced3f4515c55
import com.philips.amwelluapp.welcome.PTHInitializeCallBack;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
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
        getAwsdk(context).authenticate(username, password, variable, new SDKCallback<Authentication, SDKError>() {
            @Override
            public void onResponse(Authentication authentication, SDKError sdkError) {
                PTHAuthentication pthAuthentication = new PTHAuthentication();
                pthAuthentication.setAuthentication(authentication);
                pthLoginCallBack.onLoginResponse(pthAuthentication, sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                pthLoginCallBack.onLoginFailure(throwable);
            }
        });
    }

    public void initializeTeleHealth(Context context, final PTHInitializeCallBack pthInitializeCallBack) throws MalformedURLException, URISyntaxException, AWSDKInstantiationException, AWSDKInitializationException {
        final Map<AWSDK.InitParam, Object> initParams = new HashMap<>();
        /*initParams.put(AWSDK.InitParam.BaseServiceUrl, "https://sdk.myonlinecare.com");
        initParams.put(AWSDK.InitParam.ApiKey, "62f5548a"); //client key*/
        initParams.put(AWSDK.InitParam.BaseServiceUrl, "https://ec2-54-172-152-160.compute-1.amazonaws.com");
        initParams.put(AWSDK.InitParam.ApiKey, "3c0f99bf"); //client key


        getAwsdk(context).initialize(
                initParams, new SDKCallback<Void, SDKError>() {
                    @Override
                    public void onResponse(Void aVoid, SDKError sdkError) {
                        pthInitializeCallBack.onInitializationResponse(aVoid, sdkError);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        pthInitializeCallBack.onInitializationFailure(throwable);
                    }
                });
    }

<<<<<<< HEAD

=======
>>>>>>> 95411b51c0c33beef7ab8444f653ced3f4515c55
    public void getConsumerObject(Context context,Authentication authentication,final PTHGetConsumerObjectCallBack pthGetConsumerObjectCallBack) throws AWSDKInstantiationException {

        getAwsdk(context).getConsumerManager().getConsumer(authentication, new SDKCallback<Consumer, SDKError>() {
            @Override
            public void onResponse(Consumer consumer, SDKError sdkError) {
                pthGetConsumerObjectCallBack.onReceiveConsumerObject(consumer,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                pthGetConsumerObjectCallBack.onError(throwable);
            }
        });
    }

<<<<<<< HEAD
    public void getPractices(Context context, Consumer consumer, final PTHPracticesListCallback pthPracticesListCallback) throws AWSDKInstantiationException {
=======
    public void getPractices(Context context,Consumer consumer, final PTHPracticesListCallback pthPracticesListCallback) throws AWSDKInstantiationException {
>>>>>>> 95411b51c0c33beef7ab8444f653ced3f4515c55

        getAwsdk(context).getPracticeProvidersManager().getPractices(consumer, new SDKCallback<List<Practice>, SDKError>() {
            @Override
            public void onResponse(List<Practice> practices, SDKError sdkError) {
                pthPracticesListCallback.onPracticesListReceived(practices,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                pthPracticesListCallback.onPracticesListFetchError(throwable);
            }
        });
    }
<<<<<<< HEAD
=======

    public void getProviderList(Context context, Consumer consumer, Practice practice,final PTHProvidersListCallback pthProvidersListCallback) throws AWSDKInstantiationException {

        getAwsdk(context).getPracticeProvidersManager().findProviders(consumer, practice, null, null, null, null, null, null, null, new SDKCallback<List<ProviderInfo>, SDKError>() {
            @Override
            public void onResponse(List<ProviderInfo> providerInfos, SDKError sdkError) {
                pthProvidersListCallback.onProvidersListReceived(providerInfos, sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });

    }
>>>>>>> 95411b51c0c33beef7ab8444f653ced3f4515c55
}
