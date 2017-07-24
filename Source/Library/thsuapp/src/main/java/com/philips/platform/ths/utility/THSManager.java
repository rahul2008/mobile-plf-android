package com.philips.platform.ths.utility;

import android.content.Context;
import android.support.annotation.VisibleForTesting;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.AWSDKFactory;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.Language;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKPasswordError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.ConsumerUpdate;
import com.americanwell.sdk.entity.consumer.RemindOptions;
import com.americanwell.sdk.entity.health.Condition;
import com.americanwell.sdk.entity.health.Medication;
import com.americanwell.sdk.entity.insurance.HealthPlan;
import com.americanwell.sdk.entity.legal.LegalText;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.practice.OnDemandSpecialty;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.AvailableProviders;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.entity.visit.Vitals;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.SDKCallback;
import com.americanwell.sdk.manager.SDKValidatedCallback;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.appointment.THSAvailableProviderCallback;
import com.philips.platform.ths.appointment.THSAvailableProviderList;
import com.philips.platform.ths.appointment.THSAvailableProvidersBasedOnDateCallback;
import com.philips.platform.ths.intake.THSConditions;
import com.philips.platform.ths.intake.THSConditionsCallBack;
import com.philips.platform.ths.intake.THSConditionsList;
import com.philips.platform.ths.intake.THSMedication;
import com.philips.platform.ths.intake.THSMedicationCallback;
import com.philips.platform.ths.intake.THSNoppCallBack;
import com.philips.platform.ths.intake.THSSDKValidatedCallback;
import com.philips.platform.ths.intake.THSUpdateConditionsCallback;
import com.philips.platform.ths.intake.THSUpdateConsumerCallback;
import com.philips.platform.ths.intake.THSUpdateVitalsCallBack;
import com.philips.platform.ths.intake.THSVisitContext;
import com.philips.platform.ths.intake.THSVisitContextCallBack;
import com.philips.platform.ths.intake.THSVitalSDKCallback;
import com.philips.platform.ths.intake.THSVitals;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.login.THSGetConsumerObjectCallBack;
import com.philips.platform.ths.login.THSLoginCallBack;
import com.philips.platform.ths.pharmacy.THSConsumerShippingAddressCallback;
import com.philips.platform.ths.pharmacy.THSGetPharmaciesCallback;
import com.philips.platform.ths.pharmacy.THSPreferredPharmacyCallback;
import com.philips.platform.ths.pharmacy.THSUpdatePharmacyCallback;
import com.philips.platform.ths.pharmacy.THSUpdateShippingAddressCallback;
import com.philips.platform.ths.practice.THSPracticeList;
import com.philips.platform.ths.practice.THSPracticesListCallback;
import com.philips.platform.ths.providerdetails.THSProviderDetailsCallback;
import com.philips.platform.ths.providerslist.THSOnDemandSpeciality;
import com.philips.platform.ths.providerslist.THSOnDemandSpecialtyCallback;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.providerslist.THSProvidersListCallback;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKPasswordError;
import com.philips.platform.ths.welcome.THSInitializeCallBack;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class THSManager {
    private static THSManager sTHSManager = null;
    private AWSDK mAwsdk = null;
    private THSConsumer mTHSConsumer = null;
    private THSVisitContext mVisitContext = null;

    public THSVisitContext getPthVisitContext() {
        return mVisitContext;
    }

    public void setVisitContext(THSVisitContext mVisitContext) {
        this.mVisitContext = mVisitContext;
    }


    public THSConsumer getPTHConsumer() {
        return mTHSConsumer;
    }

    public void setPTHConsumer(THSConsumer mTHSConsumer) {
        this.mTHSConsumer = mTHSConsumer;
    }


    public static THSManager getInstance() {
        if (sTHSManager == null) {
            sTHSManager = new THSManager();
        }
        return sTHSManager;
    }


    public AWSDK getAwsdk(Context context) throws AWSDKInstantiationException {
        if (mAwsdk == null) {
            mAwsdk = AWSDKFactory.getAWSDK(context);
        }
        return mAwsdk;
    }

    public void authenticate(Context context, String username, String password, String variable, final THSLoginCallBack THSLoginCallBack) throws AWSDKInstantiationException {
        AmwellLog.i(AmwellLog.LOG,"Login - SDK API Called");
        getAwsdk(context).authenticate(username, password, variable, new SDKCallback<Authentication, SDKError>() {
            @Override
            public void onResponse(Authentication authentication, SDKError sdkError) {
                AmwellLog.i(AmwellLog.LOG,"Login - On Response");
                THSAuthentication THSAuthentication = new THSAuthentication();
                THSAuthentication.setAuthentication(authentication);

                THSSDKError THSSDKError = new THSSDKError();
                THSSDKError.setSdkError(sdkError);
                THSLoginCallBack.onLoginResponse(THSAuthentication, THSSDKError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                THSLoginCallBack.onLoginFailure(throwable);
            }
        });
    }

    public void initializeTeleHealth(Context context, final THSInitializeCallBack THSInitializeCallBack) throws MalformedURLException, URISyntaxException, AWSDKInstantiationException, AWSDKInitializationException {
        final Map<AWSDK.InitParam, Object> initParams = new HashMap<>();
       initParams.put(AWSDK.InitParam.BaseServiceUrl, "https://sdk.myonlinecare.com");
        initParams.put(AWSDK.InitParam.ApiKey, "62f5548a"); //client key

         /*initParams.put(AWSDK.InitParam.BaseServiceUrl, "https://ec2-54-172-152-160.compute-1.amazonaws.com");
        initParams.put(AWSDK.InitParam.ApiKey, "3c0f99bf"); //client key*/

        AmwellLog.i(AmwellLog.LOG,"Initialize - SDK API Called");
        getAwsdk(context).initialize(
                initParams, new SDKCallback<Void, SDKError>() {
                    @Override
                    public void onResponse(Void aVoid, SDKError sdkError) {
                        AmwellLog.i(AmwellLog.LOG,"Initialize - onResponse from Amwell SDK");
                        THSSDKError THSSDKError = new THSSDKError();
                        THSSDKError.setSdkError(sdkError);
                        THSInitializeCallBack.onInitializationResponse(aVoid, THSSDKError);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        THSInitializeCallBack.onInitializationFailure(throwable);
                    }
                });
    }

    public void getOnDemandSpecialities(Context context, PracticeInfo practiceInfo, String searchItem, final THSOnDemandSpecialtyCallback thsOnDemandSpecialtyCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getPracticeProvidersManager().getOnDemandSpecialties(getPTHConsumer().getConsumer(), practiceInfo, searchItem, new SDKCallback<List<OnDemandSpecialty>, SDKError>() {
            @Override
            public void onResponse(List<OnDemandSpecialty> onDemandSpecialties, SDKError sdkError) {


                List<THSOnDemandSpeciality> listOfThsSpecialities = new ArrayList();

                for (OnDemandSpecialty onDemandSpeciality:onDemandSpecialties
                     ) {
                    THSOnDemandSpeciality thsOnDemandSpeciality = new THSOnDemandSpeciality();
                    thsOnDemandSpeciality.setOnDemandSpecialty(onDemandSpeciality);
                    listOfThsSpecialities.add(thsOnDemandSpeciality);
                }

                THSSDKError thssdkError = new THSSDKError();
                thssdkError.setSdkError(sdkError);
                thsOnDemandSpecialtyCallback.onResponse(listOfThsSpecialities,thssdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    public void getVisitContextWithOnDemandSpeciality(Context context, final THSOnDemandSpeciality thsOnDemandSpeciality, final THSVisitContextCallBack thsVisitContextCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getVisitManager().getVisitContext(getPTHConsumer().getConsumer(), thsOnDemandSpeciality.getOnDemandSpecialty(), new SDKCallback<VisitContext, SDKError>() {
            @Override
            public void onResponse(VisitContext visitContext, SDKError sdkError) {
                THSVisitContext thsVisitContext = new THSVisitContext();
                thsVisitContext.setVisitContext(visitContext);

                setVisitContext(thsVisitContext);

                THSSDKError thsSDKError = new THSSDKError();
                thsSDKError.setSdkError(sdkError);

                thsVisitContextCallback.onResponse(thsVisitContext,thsSDKError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsVisitContextCallback.onFailure(throwable);
            }
        });
    }

    public void getVisitContext(Context context, final THSProviderInfo thsProviderInfo, final THSVisitContextCallBack THSVisitContextCallBack) throws MalformedURLException, URISyntaxException, AWSDKInstantiationException, AWSDKInitializationException {

        getAwsdk(context).getVisitManager().getVisitContext(getPTHConsumer().getConsumer(), thsProviderInfo.getProviderInfo(), new SDKCallback<VisitContext, SDKError>() {
                    @Override
                    public void onResponse(VisitContext visitContext, SDKError sdkError) {

                        THSVisitContext THSVisitContext = new THSVisitContext();
                        THSVisitContext.setVisitContext(visitContext);

                        THSSDKError THSSDKError = new THSSDKError();
                        THSSDKError.setSdkError(sdkError);

                        THSVisitContextCallBack.onResponse(THSVisitContext, THSSDKError);
                        setVisitContext(THSVisitContext);

                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        THSVisitContextCallBack.onFailure(throwable);
                    }
                });
    }

    //TODO: What happens when getConsumer is null
    public void getVitals(Context context, final THSVitalSDKCallback thsVitalCallBack) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getVitals(getPTHConsumer().getConsumer(),getPthVisitContext().getVisitContext(), new SDKCallback<Vitals, SDKError>() {
            @Override
            public void onResponse(Vitals vitals, SDKError sdkError) {
                THSVitals thsVitals = new THSVitals();
                thsVitals.setVitals(vitals);

                THSSDKError THSSDKError = new THSSDKError();
                THSSDKError.setSdkError(sdkError);

                thsVitalCallBack.onResponse(thsVitals, THSSDKError);

            }

            @Override
            public void onFailure(Throwable throwable) {
                thsVitalCallBack.onFailure(throwable);

            }
        });
    }

    /*public void createVisit(Context context, THSVisitContext pthVisitContext, final THSSDKValidatedCallback pthsdkValidatedCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getVisitManager().createVisit(pthVisitContext.getPthVisitContext(), new SDKValidatedCallback<Visit, SDKError>() {
            @Override
            public void onValidationFailure(Map<String, ValidationReason> map) {
                pthsdkValidatedCallback.onValidationFailure(map);
            }

            @Override
            public void onResponse(Visit visit, SDKError sdkError) {
                THSSDKError pthSDKError = new THSSDKError();
                pthSDKError.setSdkError(sdkError);
                pthsdkValidatedCallback.onResponse(visit,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                pthsdkValidatedCallback.onFailure(throwable);
            }
        });
    }*/


    public void getConsumerObject(Context context,Authentication authentication,final THSGetConsumerObjectCallBack THSGetConsumerObjectCallBack) throws AWSDKInstantiationException {

        getAwsdk(context).getConsumerManager().getConsumer(authentication, new SDKCallback<Consumer, SDKError>() {
            @Override
            public void onResponse(Consumer consumer, SDKError sdkError) {
                THSGetConsumerObjectCallBack.onReceiveConsumerObject(consumer,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                THSGetConsumerObjectCallBack.onError(throwable);
            }
        });
    }

    public void getConditions(Context context, final THSConditionsCallBack thsConditionsCallBack) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getConditions(getPTHConsumer().getConsumer(), new SDKCallback<List<Condition>, SDKError>() {
            @Override
            public void onResponse(List<Condition> conditions, SDKError sdkError) {
                THSConditionsList thsConditions = new THSConditionsList();
                thsConditions.setConditions(conditions);

                THSSDKError THSSDKError = new THSSDKError();
                THSSDKError.setSdkError(sdkError);

                thsConditionsCallBack.onResponse(thsConditions, THSSDKError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsConditionsCallBack.onFailure(throwable);
            }
        });
    }


    public void getPractices(Context context,Consumer consumer, final THSPracticesListCallback THSPracticesListCallback) throws AWSDKInstantiationException {


        getAwsdk(context).getPracticeProvidersManager().getPractices(consumer, new SDKCallback<List<Practice>, SDKError>() {
            @Override
            public void onResponse(List<Practice> practices, SDKError sdkError) {
                THSPracticeList pTHPractice = new THSPracticeList();
                pTHPractice.setPractices(practices);
                THSPracticesListCallback.onPracticesListReceived(pTHPractice,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                THSPracticesListCallback.onPracticesListFetchError(throwable);
            }
        });
    }


    public void getProviderList(Context context, Consumer consumer, Practice practice,final THSProvidersListCallback THSProvidersListCallback) throws AWSDKInstantiationException {

        getAwsdk(context).getPracticeProvidersManager().findProviders(consumer, practice, null, null, null, null, null, null, null, new SDKCallback<List<ProviderInfo>, SDKError>() {
            @Override
            public void onResponse(List<ProviderInfo> providerInfos, SDKError sdkError) {
                List thsProvidersList = new ArrayList();
                for (ProviderInfo providerInfo:providerInfos) {
                    THSProviderInfo thsProviderInfo = new THSProviderInfo();
                    thsProviderInfo.setTHSProviderInfo(providerInfo);
                    thsProvidersList.add(thsProviderInfo);
                }
                THSProvidersListCallback.onProvidersListReceived(thsProvidersList, sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                THSProvidersListCallback.onProvidersListFetchError(throwable);
            }
        });

    }

    public void getProviderDetails(Context context, THSProviderInfo thsProviderInfo, final THSProviderDetailsCallback THSProviderDetailsCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getPracticeProvidersManager().getProvider(thsProviderInfo.getProviderInfo(), getPTHConsumer().getConsumer(), new SDKCallback<Provider, SDKError>() {
            @Override
            public void onResponse(Provider provider, SDKError sdkError) {
                THSProviderDetailsCallback.onProviderDetailsReceived(provider,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {

                THSProviderDetailsCallback.onProviderDetailsFetchError(throwable);
            }
        });
    }

    public void updateConsumer(Context context, String updatedPhone, final THSUpdateConsumerCallback pthUpdateConsumer) throws AWSDKInstantiationException {
        ConsumerUpdate consumerUpdate = getAwsdk(context).getConsumerManager().getNewConsumerUpdate(getPTHConsumer().getConsumer());
        consumerUpdate.setPhone(updatedPhone);
        getAwsdk(context).getConsumerManager().updateConsumer(consumerUpdate, new SDKValidatedCallback<Consumer, SDKPasswordError>() {
            @Override
            public void onValidationFailure(Map<String, ValidationReason> map) {
                pthUpdateConsumer.onUpdateConsumerValidationFailure(map);
            }

            @Override
            public void onResponse(Consumer consumer, SDKPasswordError sdkPasswordError) {

                THSConsumer THSConsumer = new THSConsumer();
                THSConsumer.setConsumer(consumer);

                THSSDKPasswordError pthSDKError = new THSSDKPasswordError();
                pthSDKError.setSdkPasswordError(sdkPasswordError);

                pthUpdateConsumer.onUpdateConsumerResponse(THSConsumer,pthSDKError);
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

    public void getMedication(Context context ,  final THSMedicationCallback.PTHGetMedicationCallback pTHGetMedicationCallback ) throws AWSDKInstantiationException{
        getAwsdk(context).getConsumerManager().getMedications(getPTHConsumer().getConsumer(), new SDKCallback<List<Medication>, SDKError>() {
            @Override
            public void onResponse(List<Medication> medications, SDKError sdkError) {
                AmwellLog.i("onGetMedicationReceived","success");

                    THSMedication pTHMedication = new THSMedication();
                    pTHMedication.setMedicationList(medications);
                    pTHGetMedicationCallback.onGetMedicationReceived(pTHMedication, sdkError);

            }

            @Override
            public void onFailure(Throwable throwable) {
                AmwellLog.i("onGetMedicationReceived","failure");
            }
        });

    }

    public void searchMedication(Context context , String medicineName,  final THSSDKValidatedCallback pTHSDKValidatedCallback ) throws AWSDKInstantiationException{
        getAwsdk(context).getConsumerManager().searchMedications(getPTHConsumer().getConsumer(),medicineName, new SDKValidatedCallback<List<Medication>, SDKError>() {
            @Override
            public void onValidationFailure(Map<String, ValidationReason> map) {
                pTHSDKValidatedCallback.onValidationFailure(map);
            }

            @Override
            public void onResponse(List<Medication> medications, SDKError sdkError) {
                //Log.v("onSearchMedication","sucess");

                    THSMedication pTHMedication = new THSMedication();
                    pTHMedication.setMedicationList(medications);
                    pTHSDKValidatedCallback.onResponse(pTHMedication, sdkError);

            }

            @Override
            public void onFailure(Throwable throwable) {
                AmwellLog.i("onSearchMedication","failure");
                pTHSDKValidatedCallback.onFailure(throwable);
            }
        });


    }

    public void updateVitals(Context context, THSVitals thsVitals, final THSUpdateVitalsCallBack thsUpdateVitalsCallBack) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().updateVitals(getPTHConsumer().getConsumer(), thsVitals.getVitals(), null , new SDKValidatedCallback<Void, SDKError>() {
            @Override
            public void onValidationFailure(Map<String, ValidationReason> map) {
                thsUpdateVitalsCallBack.onUpdateVitalsValidationFailure(map);
            }

            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {
                thsUpdateVitalsCallBack.onUpdateVitalsResponse(sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsUpdateVitalsCallBack.onUpdateVitalsFailure(throwable);
            }
        });
    }

    public void updateConditions(Context context, THSConsumer THSConsumer, List<THSConditions> pthConditionList, final THSUpdateConditionsCallback thsUpdateConditionsCallback) throws AWSDKInstantiationException {

        List<Condition> conditionList = new ArrayList<>();
        for (THSConditions pthcondition:pthConditionList
             ) {
            conditionList.add(pthcondition.getCondition());
        }
        
        getAwsdk(context).getConsumerManager().updateConditions(THSConsumer.getConsumer(), conditionList, new SDKCallback<Void, SDKError>() {
            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {
                THSSDKError pthSDKError = new THSSDKError();
                pthSDKError.setSdkError(sdkError);

                thsUpdateConditionsCallback.onUpdateConditonResponse(aVoid,pthSDKError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsUpdateConditionsCallback.onUpdateConditionFailure(throwable);
            }
        });
    }

    public void updateMedication(Context context , THSMedication pTHMedication, final THSMedicationCallback.PTHUpdateMedicationCallback pTHUpdateMedicationCallback) throws AWSDKInstantiationException{
        getAwsdk(context).getConsumerManager().updateMedications(getPTHConsumer().getConsumer(), pTHMedication.getMedicationList(), new SDKCallback<Void, SDKError>() {
            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {
                // sdkError comes null even after successfully updating the medication
                AmwellLog.i("onUpdateMedication","success");
                pTHUpdateMedicationCallback.onUpdateMedicationSent(aVoid,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                AmwellLog.i("onUpdateMedication","failure");

            }
        });
    }

    public void getLegaltext(Context context , LegalText legalText, final THSNoppCallBack tHSNoppCallBack) throws AWSDKInstantiationException{
        getAwsdk(context).getLegalText(legalText, new SDKCallback<String, SDKError>() {
            @Override
            public void onResponse(String s, SDKError sdkError) {
                tHSNoppCallBack.onNoppReceivedSuccess(s,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                tHSNoppCallBack.onNoppReceivedFailure(throwable);
            }
        });
    }

    public void getPharmacies(Context context, final THSConsumer thsConsumer, String city, State state, String zipCode, final THSGetPharmaciesCallback thsGetPharmaciesCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getPharmacies(thsConsumer.getConsumer(), null,city, state, zipCode, new SDKValidatedCallback<List<Pharmacy>, SDKError>() {
            @Override
            public void onValidationFailure(Map<String, ValidationReason> map) {
                thsGetPharmaciesCallback.onValidationFailure(map);
            }

            @Override
            public void onResponse(List<Pharmacy> pharmacies, SDKError sdkError) {
                thsGetPharmaciesCallback.onPharmacyListReceived(pharmacies,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsGetPharmaciesCallback.onFailure(throwable);
            }
        });
    }

    public void getPharmacies(Context context, final THSConsumer thsConsumer, float latitude, float longitude, int radius, final THSGetPharmaciesCallback thsGetPharmaciesCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getPharmacies(thsConsumer.getConsumer(), latitude, longitude, radius, true, new SDKCallback<List<Pharmacy>, SDKError>() {
            @Override
            public void onResponse(List<Pharmacy> pharmacies, SDKError sdkError) {
                thsGetPharmaciesCallback.onPharmacyListReceived(pharmacies,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsGetPharmaciesCallback.onFailure(throwable);
            }
        });
    }

    public void getConsumerPreferredPharmacy(Context context, final THSConsumer thsConsumer, final THSPreferredPharmacyCallback thsPreferredPharmacyCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getConsumerPharmacy(thsConsumer.getConsumer(), new SDKCallback<Pharmacy, SDKError>() {
            @Override
            public void onResponse(Pharmacy pharmacy, SDKError sdkError) {
                thsPreferredPharmacyCallback.onPharmacyReceived(pharmacy, sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsPreferredPharmacyCallback.onFailure(throwable);
            }
        });
    }

    public void updateConsumerPreferredPharmacy(Context context, final THSConsumer thsConsumer, final Pharmacy pharmacy, final THSUpdatePharmacyCallback thsUpdatePharmacyCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().updateConsumerPharmacy(thsConsumer.getConsumer(), pharmacy, new SDKCallback<Void, SDKError>() {
            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {
                thsUpdatePharmacyCallback.onUpdateSuccess(sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsUpdatePharmacyCallback.onUpdateFailure(throwable);
            }
        });
    }

    public void getConsumerShippingAddress(Context context, final THSConsumer thsConsumer, final THSConsumerShippingAddressCallback thsConsumerShippingAddressCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getShippingAddress(thsConsumer.getConsumer(), new SDKCallback<Address, SDKError>() {
            @Override
            public void onResponse(Address address, SDKError sdkError) {
                thsConsumerShippingAddressCallback.onSuccessfulFetch(address, sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsConsumerShippingAddressCallback.onFailure(throwable);
            }
        });
    }

    public void updatePreferredShippingAddress(Context context,final THSConsumer thsConsumer,final Address address,final THSUpdateShippingAddressCallback thsUpdateShippingAddressCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().updateShippingAddress(thsConsumer.getConsumer(), address, new SDKValidatedCallback<Address, SDKError>() {
            @Override
            public void onValidationFailure(Map<String, ValidationReason> map) {
                thsUpdateShippingAddressCallback.onAddressValidationFailure(map);
            }

            @Override
            public void onResponse(Address address, SDKError sdkError) {
                thsUpdateShippingAddressCallback.onUpdateSuccess(address,sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsUpdateShippingAddressCallback.onUpdateFailure(throwable);
            }
        });
    }

    public void getAvailableProvidersBasedOnDate(Context context, Practice thsPractice,
                                                 String searchItem, Language languageSpoken, Date appointmentDate,
                                                 Integer maxresults,
                                                 final THSAvailableProvidersBasedOnDateCallback thsAvailableProviderCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getPracticeProvidersManager().findFutureAvailableProviders(getPTHConsumer().getConsumer(), thsPractice,
                searchItem, languageSpoken, appointmentDate, maxresults, new SDKCallback<AvailableProviders, SDKError>() {
                    @Override
                    public void onResponse(AvailableProviders availableProviders, SDKError sdkError) {
                        THSAvailableProviderList thsAvailableProviderList = new THSAvailableProviderList();
                        thsAvailableProviderList.setAvailableProviders(availableProviders);

                        THSSDKError thsSDKError = new THSSDKError();
                        thsSDKError.setSdkError(sdkError);

                        thsAvailableProviderCallback.onResponse(thsAvailableProviderList,thsSDKError);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        thsAvailableProviderCallback.onFailure(throwable);
                    }
                });

    }

    public void getProviderAvailability(Context context, Provider provider, Date date, final THSAvailableProviderCallback<List,THSSDKError> thsAvailableProviderCallback) throws AWSDKInstantiationException {
        try {
            getAwsdk(context).getPracticeProvidersManager().getProviderAvailability(getPTHConsumer().getConsumer(), provider,
                    date, new SDKCallback<List<Date>, SDKError>() {
                        @Override
                        public void onResponse(List<Date> dates, SDKError sdkError) {
                            THSSDKError thssdkError = new THSSDKError();
                            thssdkError.setSdkError(sdkError);
                            thsAvailableProviderCallback.onResponse(dates,thssdkError);
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsAvailableProviderCallback.onFailure(throwable);
                        }
                    });
        }catch (Exception ex){
            thsAvailableProviderCallback.onFailure(ex);
        }

    }

    public List<HealthPlan> getHealthPlans(Context context){
        List<HealthPlan> healthplans = null;
        try {
            healthplans = getAwsdk(context).getConsumerManager().getHealthPlans();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
        return healthplans;

    }

    public void scheduleAppointment(Context context, final THSProviderInfo thsProviderInfo, Date appointmentDate, final THSSDKValidatedCallback thssdkValidatedCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().scheduleAppointment(getPTHConsumer().getConsumer(), thsProviderInfo.getProviderInfo(),
                appointmentDate, null, RemindOptions.ONE_DAY, RemindOptions.EIGHT_HOURS, new SDKValidatedCallback<Void, SDKError>() {
                    @Override
                    public void onValidationFailure(Map<String, ValidationReason> map) {
                        thssdkValidatedCallback.onValidationFailure(map);
                    }

                    @Override
                    public void onResponse(Void aVoid, SDKError sdkError) {
                        thssdkValidatedCallback.onResponse(aVoid,sdkError);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        thssdkValidatedCallback.onFailure(throwable);
                    }
                });

    }


}
