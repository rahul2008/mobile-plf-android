package com.philips.amwelluapp.utility;

import android.content.Context;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.AWSDKFactory;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKPasswordError;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.ConsumerUpdate;
import com.americanwell.sdk.entity.health.Medication;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.visit.Visit;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.SDKCallback;
import com.americanwell.sdk.manager.SDKValidatedCallback;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.amwelluapp.intake.PTHMedication;
import com.philips.amwelluapp.intake.PTHMedicationCallback;
import com.philips.amwelluapp.intake.PTHSDKValidatedCallback;
import com.philips.amwelluapp.intake.PTHUpdateConsumerCallback;
import com.philips.amwelluapp.intake.PTHVisitContext;
import com.philips.amwelluapp.intake.PTHVisitContextCallBack;
import com.philips.amwelluapp.login.PTHAuthentication;
import com.philips.amwelluapp.login.PTHGetConsumerObjectCallBack;
import com.philips.amwelluapp.login.PTHLoginCallBack;
import com.philips.amwelluapp.practice.PTHPractice;
import com.philips.amwelluapp.practice.PTHPracticesListCallback;
import com.philips.amwelluapp.providerdetails.PTHProviderDetailsCallback;
import com.philips.amwelluapp.providerslist.PTHProviderInfo;
import com.philips.amwelluapp.providerslist.PTHProvidersListCallback;
import com.philips.amwelluapp.registration.PTHConsumer;
import com.philips.amwelluapp.sdkerrors.PTHSDKError;
import com.philips.amwelluapp.sdkerrors.PTHSDKPasswordError;
import com.philips.amwelluapp.welcome.PTHInitializeCallBack;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PTHManager {
    private static PTHManager sPthManager = null;
    private AWSDK mAwsdk = null;
    private PTHConsumer mPTHConsumer= null;

    public PTHConsumer getPTHConsumer() {
        return mPTHConsumer;
    }

    public void setPTHConsumer(PTHConsumer mPTHConsumer) {
        this.mPTHConsumer = mPTHConsumer;
    }


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
        AmwellLog.i(AmwellLog.LOG,"Login - SDK API Called");
        getAwsdk(context).authenticate(username, password, variable, new SDKCallback<Authentication, SDKError>() {
            @Override
            public void onResponse(Authentication authentication, SDKError sdkError) {
                AmwellLog.i(AmwellLog.LOG,"Login - On Response");
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
        });
    }

    public void initializeTeleHealth(Context context, final PTHInitializeCallBack pthInitializeCallBack) throws MalformedURLException, URISyntaxException, AWSDKInstantiationException, AWSDKInitializationException {
        final Map<AWSDK.InitParam, Object> initParams = new HashMap<>();
       /*initParams.put(AWSDK.InitParam.BaseServiceUrl, "https://sdk.myonlinecare.com");
        initParams.put(AWSDK.InitParam.ApiKey, "62f5548a"); //client key*/
         initParams.put(AWSDK.InitParam.BaseServiceUrl, "https://ec2-54-172-152-160.compute-1.amazonaws.com");
        initParams.put(AWSDK.InitParam.ApiKey, "3c0f99bf"); //client key

        AmwellLog.i(AmwellLog.LOG,"Initialize - SDK API Called");
        getAwsdk(context).initialize(
                initParams, new SDKCallback<Void, SDKError>() {
                    @Override
                    public void onResponse(Void aVoid, SDKError sdkError) {
                        AmwellLog.i(AmwellLog.LOG,"Initialize - onResponse from Amwell SDK");
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

    public void getVisitContext(Context context, final PTHConsumer consumer, final PTHProviderInfo providerInfo, final PTHVisitContextCallBack pthVisitContextCallBack) throws MalformedURLException, URISyntaxException, AWSDKInstantiationException, AWSDKInitializationException {

        getAwsdk(context).getVisitManager().getVisitContext(consumer.getConsumer(), providerInfo.getProviderInfo(), new SDKCallback<VisitContext, SDKError>() {
                    @Override
                    public void onResponse(VisitContext visitContext, SDKError sdkError) {

                        PTHVisitContext pthVisitContext = new PTHVisitContext();
                        pthVisitContext.setVisitContext(visitContext);

                        PTHSDKError pthsdkError = new PTHSDKError();
                        pthsdkError.setSdkError(sdkError);

                        pthVisitContextCallBack.onResponse(pthVisitContext,pthsdkError);

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        pthVisitContextCallBack.onFailure(throwable);
                    }
                });
    }

    public void createVisit(Context context, PTHVisitContext pthVisitContext, final PTHSDKValidatedCallback pthsdkValidatedCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getVisitManager().createVisit(pthVisitContext.getVisitContext(), new SDKValidatedCallback<Visit, SDKError>() {
            @Override
            public void onValidationFailure(Map<String, ValidationReason> map) {
                pthsdkValidatedCallback.onValidationFailure(map);
            }

            @Override
            public void onResponse(Visit visit, SDKError sdkError) {
                PTHSDKError pthSDKError = new PTHSDKError();
                pthSDKError.setSdkError(sdkError);
                pthsdkValidatedCallback.onResponse(visit,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                pthsdkValidatedCallback.onFailure(throwable);
            }
        });
    }


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


    public void getPractices(Context context,Consumer consumer, final PTHPracticesListCallback pthPracticesListCallback) throws AWSDKInstantiationException {


        getAwsdk(context).getPracticeProvidersManager().getPractices(consumer, new SDKCallback<List<Practice>, SDKError>() {
            @Override
            public void onResponse(List<Practice> practices, SDKError sdkError) {
                PTHPractice pTHPractice = new PTHPractice();
                pTHPractice.setPractices(practices);
                pthPracticesListCallback.onPracticesListReceived(pTHPractice,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                pthPracticesListCallback.onPracticesListFetchError(throwable);
            }
        });
    }


    public void getProviderList(Context context, Consumer consumer, Practice practice,final PTHProvidersListCallback pthProvidersListCallback) throws AWSDKInstantiationException {

        getAwsdk(context).getPracticeProvidersManager().findProviders(consumer, practice, null, null, null, null, null, null, null, new SDKCallback<List<ProviderInfo>, SDKError>() {
            @Override
            public void onResponse(List<ProviderInfo> providerInfos, SDKError sdkError) {
                pthProvidersListCallback.onProvidersListReceived(providerInfos, sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                pthProvidersListCallback.onProvidersListFetchError(throwable);
            }
        });

    }

    public void getProviderDetails(Context context,Consumer consumer,ProviderInfo providerInfo,final PTHProviderDetailsCallback pthProviderDetailsCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getPracticeProvidersManager().getProvider(providerInfo, consumer, new SDKCallback<Provider, SDKError>() {
            @Override
            public void onResponse(Provider provider, SDKError sdkError) {
                pthProviderDetailsCallback.onProviderDetailsReceived(provider,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {

                pthProviderDetailsCallback.onProviderDetailsFetchError(throwable);
            }
        });
    }

    public void updateConsumer(Context context, String updatedPhone, final PTHUpdateConsumerCallback pthUpdateConsumer) throws AWSDKInstantiationException {
        ConsumerUpdate consumerUpdate = getAwsdk(context).getConsumerManager().getNewConsumerUpdate(getPTHConsumer().getConsumer());
        consumerUpdate.setPhone(updatedPhone);
        getAwsdk(context).getConsumerManager().updateConsumer(consumerUpdate, new SDKValidatedCallback<Consumer, SDKPasswordError>() {
            @Override
            public void onValidationFailure(Map<String, ValidationReason> map) {
                pthUpdateConsumer.onUpdateConsumerValidationFailure(map);
            }

            @Override
            public void onResponse(Consumer consumer, SDKPasswordError sdkPasswordError) {
                PTHConsumer pthConsumer = new PTHConsumer();
                pthConsumer.setConsumer(consumer);

                PTHSDKPasswordError pthSDKError = new PTHSDKPasswordError();
                pthSDKError.setSdkPasswordError(sdkPasswordError);

                pthUpdateConsumer.onUpdateConsumerResponse(pthConsumer,pthSDKError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                pthUpdateConsumer.onUpdateConsumerFailure(throwable);
            }
        });
    }

    @VisibleForTesting
    public void setAwsdk(AWSDK awsdk) {
        this.mAwsdk = awsdk;
    }

    public void getMedication(Context context , final PTHMedicationCallback.PTHGetMedicationCallback pTHGetMedicationCallback ) throws AWSDKInstantiationException{
        getAwsdk(context).getConsumerManager().getMedications(getPTHConsumer().getConsumer(), new SDKCallback<List<Medication>, SDKError>() {
            @Override
            public void onResponse(List<Medication> medications, SDKError sdkError) {
                if(null!=medications && !medications.isEmpty()) {
                    PTHMedication pTHMedication = new PTHMedication();
                    pTHMedication.setMedicationList(medications);
                    pTHGetMedicationCallback.onGetMedicationReceived(pTHMedication, sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
              //  Log.v("onGetMedicationReceived","failure");
            }
        });

    }

    public void searchMedication(Context context , String medicineName, final PTHSDKValidatedCallback pTHSDKValidatedCallback ) throws AWSDKInstantiationException{
        getAwsdk(context).getConsumerManager().searchMedications(getPTHConsumer().getConsumer(),medicineName, new SDKValidatedCallback<List<Medication>, SDKError>() {
            @Override
            public void onValidationFailure(Map<String, ValidationReason> map) {
                pTHSDKValidatedCallback.onValidationFailure(map);
            }

            @Override
            public void onResponse(List<Medication> medications, SDKError sdkError) {
                //Log.v("onSearchMedication","sucess");
                if(null!=medications ) {
                    PTHMedication pTHMedication = new PTHMedication();
                    pTHMedication.setMedicationList(medications);
                    pTHSDKValidatedCallback.onResponse(pTHMedication, sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

                pTHSDKValidatedCallback.onFailure(throwable);
            }
        });


    }

    public void updateMedication(Context context , PTHMedication pTHMedication, final PTHMedicationCallback.PTHUpdateMedicationCallback pTHUpdateMedicationCallback) throws AWSDKInstantiationException{
        getAwsdk(context).getConsumerManager().updateMedications(getPTHConsumer().getConsumer(), pTHMedication.getMedicationList(), new SDKCallback<Void, SDKError>() {
            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {
              // sdkError comes null even after successfully updating the medication
                //Log.v("onUpdateMedication","success");
                pTHUpdateMedicationCallback.onUpdateMedicationSent(aVoid,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
               // Log.v("onUpdateMedication","failure");
            }
        });
    }




}
