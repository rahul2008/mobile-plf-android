/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.utility;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.AWSDKFactory;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.Authentication;
import com.americanwell.sdk.entity.FileAttachment;
import com.americanwell.sdk.entity.Language;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKLocalDate;
import com.americanwell.sdk.entity.SDKPasswordError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.UploadAttachment;
import com.americanwell.sdk.entity.billing.CreatePaymentRequest;
import com.americanwell.sdk.entity.billing.PaymentMethod;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.americanwell.sdk.entity.consumer.ConsumerUpdate;
import com.americanwell.sdk.entity.consumer.DependentUpdate;
import com.americanwell.sdk.entity.consumer.DocumentRecord;
import com.americanwell.sdk.entity.consumer.RemindOptions;
import com.americanwell.sdk.entity.enrollment.ConsumerEnrollment;
import com.americanwell.sdk.entity.enrollment.DependentEnrollment;
import com.americanwell.sdk.entity.health.Condition;
import com.americanwell.sdk.entity.health.Medication;
import com.americanwell.sdk.entity.insurance.HealthPlan;
import com.americanwell.sdk.entity.insurance.Relationship;
import com.americanwell.sdk.entity.insurance.Subscription;
import com.americanwell.sdk.entity.insurance.SubscriptionUpdateRequest;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.practice.OnDemandSpecialty;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.AvailableProviders;
import com.americanwell.sdk.entity.provider.EstimatedVisitCost;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.entity.visit.Appointment;
import com.americanwell.sdk.entity.visit.ChatReport;
import com.americanwell.sdk.entity.visit.Visit;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.entity.visit.VisitReport;
import com.americanwell.sdk.entity.visit.VisitReportDetail;
import com.americanwell.sdk.entity.visit.VisitSummary;
import com.americanwell.sdk.entity.visit.Vitals;
import com.americanwell.sdk.exception.AWSDKInitializationException;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.MatchmakerCallback;
import com.americanwell.sdk.manager.SDKCallback;
import com.americanwell.sdk.manager.SDKValidatedCallback;
import com.americanwell.sdk.manager.StartVisitCallback;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.configuration.URConfigurationConstants;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProviderCallback;
import com.philips.platform.ths.appointment.THSAvailableProviderList;
import com.philips.platform.ths.appointment.THSAvailableProvidersBasedOnDateCallback;
import com.philips.platform.ths.cost.ApplyCouponCallback;
import com.philips.platform.ths.cost.CreateVisitCallback;
import com.philips.platform.ths.cost.THSVisit;
import com.philips.platform.ths.insurance.THSInsuranceCallback;
import com.philips.platform.ths.insurance.THSSubscription;
import com.philips.platform.ths.insurance.THSSubscriptionUpdateRequest;
import com.philips.platform.ths.intake.THSCondition;
import com.philips.platform.ths.intake.THSConditionsCallBack;
import com.philips.platform.ths.intake.THSConditionsList;
import com.philips.platform.ths.intake.THSMedication;
import com.philips.platform.ths.intake.THSMedicationCallback;
import com.philips.platform.ths.intake.THSSDKCallback;
import com.philips.platform.ths.intake.THSSDKValidatedCallback;
import com.philips.platform.ths.intake.THSUpdateConditionsCallback;
import com.philips.platform.ths.intake.THSUpdateConsumerCallback;
import com.philips.platform.ths.intake.THSUpdateVitalsCallBack;
import com.philips.platform.ths.intake.THSVisitContextCallBack;
import com.philips.platform.ths.intake.THSVitalSDKCallback;
import com.philips.platform.ths.intake.THSVitals;
import com.philips.platform.ths.intake.selectimage.THSDeleteDocumentCallback;
import com.philips.platform.ths.intake.selectimage.THSUploadDocumentCallback;
import com.philips.platform.ths.login.THSAuthentication;
import com.philips.platform.ths.login.THSGetConsumerObjectCallBack;
import com.philips.platform.ths.login.THSLoginCallBack;
import com.philips.platform.ths.payment.THSAddress;
import com.philips.platform.ths.payment.THSCreatePaymentRequest;
import com.philips.platform.ths.payment.THSPaymentCallback;
import com.philips.platform.ths.payment.THSPaymentMethod;
import com.philips.platform.ths.pharmacy.THSConsumerShippingAddressCallback;
import com.philips.platform.ths.pharmacy.THSGetPharmaciesCallback;
import com.philips.platform.ths.pharmacy.THSPreferredPharmacyCallback;
import com.philips.platform.ths.pharmacy.THSUpdatePharmacyCallback;
import com.philips.platform.ths.pharmacy.THSUpdateShippingAddressCallback;
import com.philips.platform.ths.practice.THSPracticeCallback;
import com.philips.platform.ths.practice.THSPracticeList;
import com.philips.platform.ths.practice.THSPracticesListCallback;
import com.philips.platform.ths.providerdetails.THSFetchEstimatedCostCallback;
import com.philips.platform.ths.providerdetails.THSMatchMakingCallback;
import com.philips.platform.ths.providerdetails.THSProviderDetailsCallback;
import com.philips.platform.ths.providerslist.THSOnDemandSpeciality;
import com.philips.platform.ths.providerslist.THSOnDemandSpecialtyCallback;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.providerslist.THSProvidersListCallback;
import com.philips.platform.ths.registration.THSCheckConsumerExistsCallback;
import com.philips.platform.ths.registration.THSEditUserDetailsCallBack;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKPasswordError;
import com.philips.platform.ths.settings.THSGetAppointmentsCallback;
import com.philips.platform.ths.settings.THSVisitReportAttachmentCallback;
import com.philips.platform.ths.settings.THSVisitReportDetailCallback;
import com.philips.platform.ths.settings.THSVisitReportListCallback;
import com.philips.platform.ths.uappclasses.THSCompletionProtocol;
import com.philips.platform.ths.visit.THSCancelVisitCallBack;
import com.philips.platform.ths.visit.THSStartVisitCallback;
import com.philips.platform.ths.visit.THSVisitSummary;
import com.philips.platform.ths.visit.THSVisitSummaryCallbacks;
import com.philips.platform.ths.welcome.THSInitializeCallBack;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static com.philips.platform.ths.utility.THSConstants.THS_SDK_SERVICE_ID;


public class THSManager{
    private static THSManager sTHSManager = null;
    private AWSDK mAwsdk = null;
    private VisitContext mVisitContext = null;
    private boolean isMatchMakingVisit;
    private THSConsumer mThsConsumer;
    private THSConsumer mThsParentConsumer;
    private boolean mIsReturningUser = true;
    private String onBoradingABFlow = "";
    private String providerListABFlow = "";
    private String ServerURL=null;
    private String mCountry="";
    ConsentDefinition mConsentDefinition;
    private Provider providerObject;
    private Consumer mConsumer;
    private THSRefreshToken mRefreshToken = new THSRefreshToken();

    //private Boolean gdprEnabled =false;

    /*public Boolean isGdprEnabled() {
        return gdprEnabled;
    }
*/


    public Provider getProviderObject() {
        return providerObject;
    }

    public void setProviderObject(Provider providerObject) {
        this.providerObject = providerObject;
    }



    public ConsentDefinition getConsentDefinition() {
        return mConsentDefinition;
    }

    public void setConsentDefinition(ConsentDefinition mConsentDefinition) {
        this.mConsentDefinition = mConsentDefinition;
    }



    public String getCountry() {
        return mCountry;
    }

    public String getServerURL() {
        return ServerURL;
    }

    public void setServerURL(String serverURL) {
        ServerURL = serverURL;
    }

    private ArrayList<DocumentRecord> documentRecordList;

    public THSCompletionProtocol getThsCompletionProtocol() {
        return thsCompletionProtocol;
    }

    public void setThsCompletionProtocol(THSCompletionProtocol thsCompletionProtocol) {
        this.thsCompletionProtocol = thsCompletionProtocol;
    }

    THSCompletionProtocol thsCompletionProtocol;


    @VisibleForTesting
    private User mUser;

    private AppInfraInterface mAppInfra;
    AppTaggingInterface mAppTaggingInterface;
    LoggingInterface mLoggingInterface;


    public AppTaggingInterface getThsTagging() {
        return mAppTaggingInterface;
    }

    public LoggingInterface getLoggingInterface() {
        return mLoggingInterface;
    }




    @VisibleForTesting
    public boolean TEST_FLAG = false;

    public boolean isMatchMakingVisit() {
        return isMatchMakingVisit;
    }

    public void setMatchMakingVisit(boolean matchMakingVisit) {
        isMatchMakingVisit = matchMakingVisit;
    }


    public VisitContext getVisitContext() {
        return mVisitContext;
    }

    public void setVisitContext(VisitContext mVisitContext) {
        this.mVisitContext = mVisitContext;
    }


    public Consumer getConsumer(Context context) {
        if(getThsConsumer(context)!=null) {
            mConsumer = getThsConsumer(context).getConsumer();
        }
       return mConsumer;
    }

    public void setConsumer(Consumer consumer) {
        this.mConsumer = consumer;
    }

    public void setTHSDocumentList(ArrayList<DocumentRecord> documentRecordList){
        this.documentRecordList = documentRecordList;
    }

    public ArrayList<DocumentRecord> getTHSDocumentList(){
        return documentRecordList;
    }

    public static THSManager getInstance() {
        if (sTHSManager == null) {
            sTHSManager = new THSManager();
        }
        return sTHSManager;
    }

    private THSManager(){

    }

    public void resetTHSManagerData(){
         setVisitContext(null);
         setMatchMakingVisit(false);
    }


    public AWSDK getAwsdk(Context context) throws AWSDKInstantiationException {
        if (mAwsdk == null) {
            mAwsdk = AWSDKFactory.getAWSDK(context);
        }
        return mAwsdk;
    }

    public void authenticate(final Context context, final String username, final String password, final String variable, final THSLoginCallBack<THSAuthentication, THSSDKError> THSLoginCallBack) throws AWSDKInstantiationException {
        AmwellLog.i(AmwellLog.LOG,"Login - SDK API Called");
        getAwsdk(context).authenticate(username, password, variable, new SDKCallback<Authentication, SDKError>() {
            @Override
            public void onResponse(final Authentication authentication, SDKError sdkError) {
                AmwellLog.i(AmwellLog.LOG,"Login - On Response");
                THSAuthentication THSAuthentication = new THSAuthentication();
                THSAuthentication.setAuthentication(authentication);

                THSSDKError THSSDKError = new THSSDKError();
                THSSDKError.setSdkError(sdkError);

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                authenticate(context,username,password,variable,THSLoginCallBack);
                            } catch (AWSDKInstantiationException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            THSLoginCallBack.onLoginFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    THSLoginCallBack.onLoginResponse(THSAuthentication, THSSDKError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                THSLoginCallBack.onLoginFailure(throwable);
            }
        });
    }

    public void authenticateMutualAuthToken(final Context context, final THSLoginCallBack<THSAuthentication, THSSDKError> THSLoginCallBack) throws AWSDKInstantiationException {

        AmwellLog.i(AmwellLog.LOG,"Inside authenticateMutualAuthToken");

        String hsdpUUID = getThsParentConsumer(context).getHsdpUUID();
        if(hsdpUUID == null || hsdpUUID.isEmpty()){
            hsdpUUID = getUser(context).getHsdpUUID();
        }

        String hsdpToken = getThsParentConsumer(context).getHsdpToken();
        if(hsdpToken == null || hsdpToken.isEmpty()){
            hsdpToken = getUser(context).getHsdpAccessToken();
        }

        final String appName = getAppName();
        if(appName == null){
            THSLoginCallBack.onLoginFailure(new Exception(context.getString(R.string.ths_something_went_wrong)));
            return;
        }

        String token = hsdpUUID +":" + appName +":"+ hsdpToken;

        getAwsdk(context).authenticateMutual(token, new SDKCallback<Authentication, SDKError>() {
            @Override
            public void onResponse(Authentication authentication, SDKError sdkError) {
                AmwellLog.i(AmwellLog.LOG,"authenticateMutualAuth Login - On Response");

                THSAuthentication THSAuthentication = new THSAuthentication();
                THSAuthentication.setAuthentication(authentication);

                THSSDKError THSSDKError = new THSSDKError();
                THSSDKError.setSdkError(sdkError);

                mRefreshToken.setContext(context);

                if(mRefreshToken.checkIfAuthSuccess(THSSDKError)){
                    AmwellLog.i(AmwellLog.LOG,"authenticateMutualAuth Auth Success");
                    THSLoginCallBack.onLoginResponse(THSAuthentication, THSSDKError);
                }else {
                    AmwellLog.i(AmwellLog.LOG,"authenticateMutualAuth callRefreshUserTokenInSeparateThread");
                    mRefreshToken.refreshToken(THSLoginCallBack);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                THSLoginCallBack.onLoginFailure(throwable);
            }
        });
    }

    public void completeEnrollment(final Context context, final THSAuthentication thsAuthentication, final THSGetConsumerObjectCallBack thsGetConsumerObjectCallBack) throws AWSDKInstantiationException {
        THSConsumer thsConsumer = getThsConsumer(context);
        State state = null;
        String username = null;
        if(thsConsumer!=null){
            state = thsConsumer.getState();
            username = thsConsumer.getFirstName();
        }
        getAwsdk(context).getConsumerManager().completeEnrollment(thsAuthentication.getAuthentication(),state,username,null, new SDKCallback<Consumer, SDKPasswordError>() {
            @Override
            public void onResponse(Consumer consumer, SDKPasswordError sdkPasswordError) {

                if(sdkPasswordError!=null && sdkPasswordError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                completeEnrollment(context,thsAuthentication,thsGetConsumerObjectCallBack);
                            } catch (AWSDKInstantiationException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsGetConsumerObjectCallBack.onError(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    mConsumer = consumer;
                    getThsParentConsumer(context).setConsumer(consumer);
                    thsGetConsumerObjectCallBack.onReceiveConsumerObject(consumer,sdkPasswordError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsGetConsumerObjectCallBack.onError(throwable);
            }
        });
    }

    public void checkConsumerExists(final Context context, final THSCheckConsumerExistsCallback<Boolean, THSSDKError> thsCheckConsumerExistsCallback) throws AWSDKInstantiationException {

        getAwsdk(context).getConsumerManager().checkConsumerExists(new THSHashingFunction().md5(getThsConsumer(context).getHsdpUUID()), new SDKCallback<Boolean, SDKError>() {

            @Override
            public void onResponse(Boolean aBoolean, SDKError sdkError) {

                THSSDKError thssdkError = new THSSDKError();
                thssdkError.setSdkError(sdkError);

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                checkConsumerExists(context,thsCheckConsumerExistsCallback);
                            } catch (AWSDKInstantiationException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsCheckConsumerExistsCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    if(!getThsConsumer(context).isDependent()) {
                        setIsReturningUser(aBoolean);
                    }
                    thsCheckConsumerExistsCallback.onResponse(aBoolean,thssdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsCheckConsumerExistsCallback.onFailure(throwable);
            }
        });
    }

    @Nullable
    private THSConsumer getThsConsumerIfNotSet(Context context) {
        final User user = getUser(context);
        THSConsumer thsConsumer = new THSConsumer();

        if (mConsumer != null) {
            thsConsumer.setConsumer(mConsumer);
        }

        thsConsumer.setFirstName(user.getGivenName());
        thsConsumer.setEmail(user.getEmail());
        thsConsumer.setDob(user.getDateOfBirth());
        thsConsumer.setGender(user.getGender());
        thsConsumer.setHsdpToken(user.getHsdpAccessToken());
        thsConsumer.setHsdpUUID(user.getHsdpUUID());
        thsConsumer.setLastName(user.getFamilyName());
        setThsConsumer(thsConsumer);

        return thsConsumer;
    }

    @NonNull
    public User getUser(Context context) {
        if(TEST_FLAG){
            return mUser;
        }else {
            return new User(context);
        }
    }

    public void enrollConsumer(final Context context, final Date dateOfBirth, final String firstName, final String lastName, final String gender, final State state, final THSSDKValidatedCallback<Consumer, SDKError> thssdkValidatedCallback) throws AWSDKInstantiationException {
        final ConsumerEnrollment newConsumerEnrollment = getConsumerEnrollment(context, dateOfBirth, firstName, lastName, gender, state);

        getAwsdk(context).getConsumerManager().enrollConsumer(newConsumerEnrollment,
                new SDKValidatedCallback<Consumer, SDKPasswordError>() {
                    @Override
                    public void onValidationFailure(@NonNull Map<String, String> map) {
                        AmwellLog.i(AmwellLog.LOG,"validationFail");
                        thssdkValidatedCallback.onValidationFailure(map);
                    }

                    @Override
                    public void onResponse(Consumer consumer, SDKPasswordError sdkPasswordError) {

                        if(sdkPasswordError!=null && sdkPasswordError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                            AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                            mRefreshToken.setContext(context);
                            mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                                @Override
                                public void onSuccess() {
                                    try {
                                        enrollConsumer(context,dateOfBirth,firstName,lastName,gender,state,thssdkValidatedCallback);
                                    } catch (AWSDKInstantiationException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    thssdkValidatedCallback.onFailure(throwable);
                                }
                            });

                            refreshAmwellToken(context);

                        }else {
                            setIsReturningUser(true);
                            getThsParentConsumer(context).setConsumer(consumer);
                            getThsParentConsumer(context).setState(state);
                            getThsConsumer(context).setState(state);
                            getThsConsumer(context).setConsumer(consumer);
                            AmwellLog.i(AmwellLog.LOG,"onGetPaymentSuccess");
                            mConsumer = consumer;
                            thssdkValidatedCallback.onResponse(consumer,sdkPasswordError);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        AmwellLog.i(AmwellLog.LOG,"onFail");
                        thssdkValidatedCallback.onFailure(throwable);
                    }
                });
    }

    @NonNull
    private ConsumerEnrollment getConsumerEnrollment(Context context, Date dateOfBirth, String firstName, String lastName, String gender, State state) throws AWSDKInstantiationException {
        final ConsumerEnrollment newConsumerEnrollment = getAwsdk(context).getConsumerManager().getNewConsumerEnrollment();
        newConsumerEnrollment.setAcceptedDisclaimer(true);

        newConsumerEnrollment.setSourceId(new THSHashingFunction().md5(getThsConsumer(context).getHsdpUUID()));
        newConsumerEnrollment.setConsumerAuthKey(getThsConsumer(context).getHsdpUUID());

        newConsumerEnrollment.setEmail(getThsConsumer(context).getEmail());
        newConsumerEnrollment.setPassword(generatePasswordRandomly());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateOfBirth);
        SDKLocalDate sdkLocalDate = new SDKLocalDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        newConsumerEnrollment.setDob(sdkLocalDate);

        newConsumerEnrollment.setFirstName(firstName);
        newConsumerEnrollment.setGender(gender);
        newConsumerEnrollment.setLastName(lastName);

        newConsumerEnrollment.setLegalResidence(state);
        return newConsumerEnrollment;
    }

    protected String generatePasswordRandomly() {

        Random rnd = new Random();
        int length = rnd.nextInt(16 - 8) + 8;

        String firstThreeChars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder password = new StringBuilder();

        while (password.length() < length) { // length of the random string.
            int index = (int) (rnd.nextFloat() * firstThreeChars.length());
            password.append(firstThreeChars.charAt(index));
        }

        String randomPassword = password.toString();
        if(validatePassword(randomPassword)){
            return randomPassword;
        }else {
            return generatePasswordRandomly();
        }
    }

    protected boolean validatePassword(String password){
        if(password.matches(".*[a-z].*") && password.matches(".*[A-Z].*") && password.matches(".*[0-9].*")){
            return true;
        }
        return false;
    }

    public void enrollDependent(final Context context, final Date dateOfBirth, final String firstName, final String lastName, final String gender, final State state, final THSSDKValidatedCallback<Consumer, SDKError> thssdkValidatedCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().enrollDependent(getDependantEnrollment(context, dateOfBirth, firstName, lastName, gender), new SDKValidatedCallback<Consumer, SDKError>() {
            @Override
            public void onValidationFailure(@NonNull Map<String, String> map) {
                thssdkValidatedCallback.onValidationFailure(map);
            }

            @Override
            public void onResponse(Consumer consumer, SDKError sdkError) {

                THSSDKError thssdkError = new THSSDKError();
                thssdkError.setSdkError(sdkError);

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                enrollDependent(context,dateOfBirth,firstName,lastName,gender,state,thssdkValidatedCallback);
                            } catch (AWSDKInstantiationException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thssdkValidatedCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    mConsumer = consumer;
                    getThsConsumer(context).setConsumer(consumer);
                    getThsConsumer(context).setState(state);
                    thssdkValidatedCallback.onResponse(mConsumer,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
               thssdkValidatedCallback.onFailure(throwable);
            }
        });
    }

    @NonNull
    private DependentEnrollment getDependantEnrollment(Context context, Date dateOfBirth, String firstName, String lastName, String gender) throws AWSDKInstantiationException {
        final DependentEnrollment newConsumerEnrollment = getAwsdk(context).getConsumerManager().getNewDependentEnrollment(getThsParentConsumer(context).getConsumer());

        newConsumerEnrollment.setSourceId(new THSHashingFunction().md5(getThsConsumer(context).getHsdpUUID()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateOfBirth);
        SDKLocalDate sdkLocalDate = new SDKLocalDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));

        newConsumerEnrollment.setDob(sdkLocalDate);

        newConsumerEnrollment.setFirstName(firstName);
        newConsumerEnrollment.setGender(gender);
        newConsumerEnrollment.setLastName(lastName);


        return newConsumerEnrollment;
    }


    public void initializeTeleHealth(final Context context, final THSInitializeCallBack<Void, THSSDKError> THSInitializeCallBack) throws MalformedURLException, URISyntaxException, AWSDKInstantiationException, AWSDKInitializationException {
        final Map<Integer, Object> initParams = new HashMap<>();
       /*initParams.put(AWSDK.InitParam.BaseServiceUrl, "https://sdk.myonlinecare.com");
        initParams.put(AWSDK.InitParam.ApiKey, "62f5548a"); //client key*/

       /*initParams.put(AWSDK.InitParam.BaseServiceUrl, "https://stagingOC169.mytelehealth.com");
        initParams.put(AWSDK.InitParam.ApiKey, "dc573250"); //client key*/

        //This is required to be reset in case of logout login case
        updateParentConsumer(context);

        if(getAppInfra() == null || getAppInfra().getServiceDiscovery() == null){
            Log.e(AmwellLog.LOG, "App infra is null");
            THSInitializeCallBack.onInitializationFailure(new Exception(context.getString(R.string.ths_something_went_wrong)));
            return;
        }

        AppConfigurationInterface.AppConfigurationError getConfigError= new AppConfigurationInterface.AppConfigurationError();
        final String APIKey = (String) getAppInfra().getConfigInterface().getPropertyForKey("apiKey","ths",getConfigError);
     //   gdprEnabled =  (Boolean) getAppInfra().getConfigInterface().getPropertyForKey("gdprEnabled", "ths", getConfigError);

        getAppInfra().getServiceDiscovery().getServiceUrlWithCountryPreference(THS_SDK_SERVICE_ID, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                initParams.put(AWSDK.InitParam.BaseServiceUrl, url.toString());
                initParams.put(AWSDK.InitParam.ApiKey, APIKey); //client key
                setServerURL(url.toString());
                AmwellLog.i(AmwellLog.LOG,"Initialize - SDK API Called");
                try {
                    getAwsdk(context).initialize(
                            initParams, new SDKCallback<Void, SDKError>() {
                                @Override
                                public void onResponse(Void aVoid, SDKError sdkError) {
                                    AmwellLog.i(AmwellLog.LOG,"Initialize - onGetPaymentSuccess from Amwell SDK");
                                    THSSDKError THSSDKError = new THSSDKError();
                                    THSSDKError.setSdkError(sdkError);
                                    THSInitializeCallBack.onInitializationResponse(aVoid, THSSDKError);
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    THSInitializeCallBack.onInitializationFailure(throwable);
                                }
                            });
                }catch (Exception e){
                    THSInitializeCallBack.onInitializationFailure(e);
                }
            }

            @Override
            public void onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues, String s) {
                Throwable serviceDiscoveryError= new Throwable(s);
                THSInitializeCallBack.onInitializationFailure(serviceDiscoveryError);
            }
        });



    }

    public void getOnDemandSpecialities(final Context context, final PracticeInfo practiceInfo, final String searchItem, final THSOnDemandSpecialtyCallback<List<THSOnDemandSpeciality>, THSSDKError> thsOnDemandSpecialtyCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getPracticeProvidersManager().getOnDemandSpecialties(getConsumer(context), practiceInfo, searchItem, new SDKCallback<List<OnDemandSpecialty>, SDKError>() {
            @Override
            public void onResponse(List<OnDemandSpecialty> onDemandSpecialties, SDKError sdkError) {

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getOnDemandSpecialities(context,practiceInfo,searchItem,thsOnDemandSpecialtyCallback);
                            } catch (AWSDKInstantiationException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsOnDemandSpecialtyCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    List<THSOnDemandSpeciality> listOfThsSpecialities = new ArrayList<>();

                    for (OnDemandSpecialty onDemandSpeciality:onDemandSpecialties
                            ) {
                        //setMatchMakingVisit(true);
                        THSOnDemandSpeciality thsOnDemandSpeciality = new THSOnDemandSpeciality();
                        thsOnDemandSpeciality.setOnDemandSpecialty(onDemandSpeciality);
                        listOfThsSpecialities.add(thsOnDemandSpeciality);
                    }

                    THSSDKError thssdkError = new THSSDKError();
                    thssdkError.setSdkError(sdkError);
                    thsOnDemandSpecialtyCallback.onResponse(listOfThsSpecialities,thssdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsOnDemandSpecialtyCallback.onFailure(throwable);
            }
        });
    }

    public void getVisitContextWithOnDemandSpeciality(final Context context, final THSOnDemandSpeciality thsOnDemandSpeciality, final THSVisitContextCallBack<VisitContext, THSSDKError> thsVisitContextCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getVisitManager().getVisitContext(getConsumer(context), thsOnDemandSpeciality.getOnDemandSpecialty(), new SDKCallback<VisitContext, SDKError>() {
            @Override
            public void onResponse(VisitContext visitContext, SDKError sdkError) {

                THSSDKError thsSDKError = new THSSDKError();
                thsSDKError.setSdkError(sdkError);

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getVisitContextWithOnDemandSpeciality(context,thsOnDemandSpeciality,thsVisitContextCallback);
                            } catch (AWSDKInstantiationException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsVisitContextCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    setVisitContext(visitContext);
                    thsVisitContextCallback.onResponse(visitContext,thsSDKError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsVisitContextCallback.onFailure(throwable);
            }
        });
    }

    public void getVisitContext(final Context context, final THSProviderInfo thsProviderInfo, final THSVisitContextCallBack<VisitContext, THSSDKError> thsvisitcontextcallback) throws MalformedURLException, URISyntaxException, AWSDKInstantiationException, AWSDKInitializationException {

        getAwsdk(context).getVisitManager().getVisitContext(getThsConsumer(context).getConsumer(), thsProviderInfo.getProviderInfo(), new SDKCallback<VisitContext, SDKError>() {
            @Override
            public void onResponse(VisitContext visitContext, SDKError sdkError) {

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                getVisitContext(context, thsProviderInfo, thsvisitcontextcallback);
                            } catch (MalformedURLException e) {
                                thsvisitcontextcallback.onFailure(e);
                            } catch (URISyntaxException e) {
                                thsvisitcontextcallback.onFailure(e);
                            } catch (AWSDKInitializationException e) {
                                thsvisitcontextcallback.onFailure(e);
                            } catch (AWSDKInstantiationException e) {
                                thsvisitcontextcallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsvisitcontextcallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    onVisitContextResponse(visitContext, sdkError, thsvisitcontextcallback);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsvisitcontextcallback.onFailure(throwable);
            }
        });
    }

    private void onVisitContextResponse(VisitContext visitContext, SDKError sdkError, THSVisitContextCallBack<VisitContext, THSSDKError> thsvisitcontextcallback) {


            THSSDKError THSSDKError = new THSSDKError();
            THSSDKError.setSdkError(sdkError);

            thsvisitcontextcallback.onResponse(visitContext, THSSDKError);
            setVisitContext(visitContext);
    }

    public void getVisitContext(final Context context, final Appointment appointment, final THSVisitContextCallBack<VisitContext, THSSDKError> thsvisitcontextcallback) throws MalformedURLException, URISyntaxException, AWSDKInstantiationException, AWSDKInitializationException{
        getAwsdk(context).getVisitManager().getVisitContext(appointment, new SDKCallback<VisitContext, SDKError>() {
            @Override
            public void onResponse(VisitContext visitContext, SDKError sdkError) {

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                try {
                                    getVisitContext(context,appointment,thsvisitcontextcallback);
                                } catch (MalformedURLException e) {
                                    thsvisitcontextcallback.onFailure(e);
                                } catch (URISyntaxException e) {
                                    thsvisitcontextcallback.onFailure(e);
                                } catch (AWSDKInitializationException e) {
                                    thsvisitcontextcallback.onFailure(e);
                                }
                            } catch (AWSDKInstantiationException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsvisitcontextcallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    onVisitContextResponse(visitContext, sdkError, thsvisitcontextcallback);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsvisitcontextcallback.onFailure(throwable);
            }
        });
    }

    //TODO: What happens when getConsumer is null
    public void getVitals(final Context context, final THSVitalSDKCallback<THSVitals, THSSDKError> thsVitalCallBack) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getVitals(getConsumer(context), getVisitContext(), new SDKCallback<Vitals, SDKError>() {
            @Override
            public void onResponse(Vitals vitals, SDKError sdkError) {
                THSVitals thsVitals = new THSVitals();
                thsVitals.setVitals(vitals);

                THSSDKError THSSDKError = new THSSDKError();
                THSSDKError.setSdkError(sdkError);

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getVitals(context,thsVitalCallBack);
                            } catch (AWSDKInstantiationException e) {
                                thsVitalCallBack.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsVitalCallBack.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    thsVitalCallBack.onResponse(thsVitals, THSSDKError);
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                thsVitalCallBack.onFailure(throwable);

            }
        });
    }

    /*public void createVisit(Context context, visitContext pthVisitContext, final THSGetPaymentMethodValidatedCallback pthsdkValidatedCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getVisitManager().createVisit(pthVisitContext.getVisitContext(), new SDKValidatedCallback<Visit, SDKError>() {
            @Override
            public void onValidationFailure(Map<String, ValidationReason> map) {
                pthsdkValidatedCallback.onValidationFailure(map);
            }

            @Override
            public void onGetPaymentSuccess(Visit visit, SDKError sdkError) {
                THSSDKError pthSDKError = new THSSDKError();
                pthSDKError.setSdkError(sdkError);
                pthsdkValidatedCallback.onGetPaymentSuccess(visit,sdkError);
            }

            @Override
            public void onGetPaymentFailure(Throwable throwable) {
                pthsdkValidatedCallback.onGetPaymentFailure(throwable);
            }
        });
    }*/

    //TODO: No SDKError is sent by backend.
    public void getAppointments(final Context context, final SDKLocalDate sdkLocalDate, final THSGetAppointmentsCallback<List<Appointment>, THSSDKError> thsGetAppointmentsCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getAppointments(getConsumer(context),sdkLocalDate,new SDKCallback<List< Appointment >, SDKError>(){

            @Override
            public void onResponse(List<Appointment> appointments, SDKError sdkError) {

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getAppointments(context,sdkLocalDate,thsGetAppointmentsCallback);
                            } catch (AWSDKInstantiationException e) {
                                thsGetAppointmentsCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsGetAppointmentsCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    thsGetAppointmentsCallback.onResponse(appointments,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsGetAppointmentsCallback.onFailure(throwable);
            }
        });

    }


    //TODO: No SDKError is sent by backend.
    public void getConsumerObject(final Context context, final Authentication authentication, final THSGetConsumerObjectCallBack THSGetConsumerObjectCallBack) throws AWSDKInstantiationException {

        getAwsdk(context).getConsumerManager().getConsumer(authentication, new SDKCallback<Consumer, SDKError>() {
            @Override
            public void onResponse(Consumer consumer, SDKError sdkError) {

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getConsumerObject(context,authentication,THSGetConsumerObjectCallBack);
                            } catch (AWSDKInstantiationException e) {
                                THSGetConsumerObjectCallBack.onError(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            THSGetConsumerObjectCallBack.onError(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    mConsumer = consumer;
                    //The mThsConsumer object is set to Parent as when-ever the control is in welcome screen, only the parent is the active person
                    setThsConsumer(getThsParentConsumer(context));
                    getThsConsumer(context).setConsumer(consumer);
                    THSGetConsumerObjectCallBack.onReceiveConsumerObject(consumer,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                THSGetConsumerObjectCallBack.onError(throwable);
            }
        });
    }

    //TODO: No SDKError is sent by backend.
    public void getConditions(final Context context, final THSConditionsCallBack<THSConditionsList,THSSDKError> thsConditionsCallBack) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getConditions(getConsumer(context), new SDKCallback<List<Condition>, SDKError>() {
            @Override
            public void onResponse(List<Condition> conditions, SDKError sdkError) {
                THSConditionsList thsConditions = new THSConditionsList();
                thsConditions.setConditions(conditions);

                THSSDKError THSSDKError = new THSSDKError();
                THSSDKError.setSdkError(sdkError);

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getConditions(context,thsConditionsCallBack);
                            } catch (AWSDKInstantiationException e) {
                                thsConditionsCallBack.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsConditionsCallBack.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    thsConditionsCallBack.onResponse(thsConditions, THSSDKError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsConditionsCallBack.onFailure(throwable);
            }
        });
    }


    //Done error codes
    public void getPractices(final Context context, final THSPracticesListCallback THSPracticesListCallback) throws AWSDKInstantiationException {

        AmwellLog.i(AmwellLog.LOG,"getPractices in THSManager Called");
        if(getConsumer(context)==null){
            THSPracticesListCallback.onPracticesListFetchError(null);
        }
        getAwsdk(context).getPracticeProvidersManager().getPractices(getConsumer(context), new SDKCallback<List<Practice>, SDKError>() {
            @Override
            public void onResponse(List<Practice> practices, SDKError sdkError) {

                AmwellLog.i(AmwellLog.LOG,"onResponse of getPractices inside callback");

                THSPracticeList pTHPractice = new THSPracticeList();
                pTHPractice.setPractices(practices);


                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getPractices(context,THSPracticesListCallback);
                            } catch (AWSDKInstantiationException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            THSPracticesListCallback.onPracticesListFetchError(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    THSPracticesListCallback.onPracticesListReceived(pTHPractice,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                THSPracticesListCallback.onPracticesListFetchError(throwable);
            }
        });
    }


    //Done error codes
    public void getProviderList(final Context context, final Practice practice, final String searchTerm, final THSProvidersListCallback THSProvidersListCallback) throws AWSDKInstantiationException{
        getAwsdk(context).getPracticeProvidersManager().findProviders(getConsumer(context), practice, null, searchTerm, null, null, null, null, null, new SDKCallback<List<ProviderInfo>, SDKError>() {
            @Override
            public void onResponse(List<ProviderInfo> providerInfos, SDKError sdkError) {

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getProviderList(context,practice,searchTerm,THSProvidersListCallback);
                            } catch (AWSDKInstantiationException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            THSProvidersListCallback.onProvidersListFetchError(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    List<THSProviderInfo> thsProvidersList = new ArrayList<>();
                    if(providerInfos!=null && providerInfos.size()>0) {
                        for (ProviderInfo providerInfo : providerInfos) {
                            THSProviderInfo thsProviderInfo = new THSProviderInfo();
                            thsProviderInfo.setTHSProviderInfo(providerInfo);
                            thsProvidersList.add(thsProviderInfo);
                        }
                    }
                    THSProvidersListCallback.onProvidersListReceived(thsProvidersList, sdkError);
                }


            }

            @Override
            public void onFailure(Throwable throwable) {
                THSProvidersListCallback.onProvidersListFetchError(throwable);
            }
        });
    }

    private void refreshAmwellToken(Context context) {
        try {
            AmwellLog.i(AmwellLog.LOG,"Calling authenticateMutualAuth API from refresh and thread goes to wait");
            authenticateMutualAuthToken(context,mRefreshToken);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    public void getProviderList(Context context, Practice practice, final THSProvidersListCallback THSProvidersListCallback) throws AWSDKInstantiationException {
        getProviderList(context, practice,null,THSProvidersListCallback);
    }


    //Done error codes
    public void getProviderDetails(final Context context, final THSProviderInfo thsProviderInfo, final THSProviderDetailsCallback THSProviderDetailsCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getPracticeProvidersManager().getProvider(thsProviderInfo.getProviderInfo(), getConsumer(context), new SDKCallback<Provider, SDKError>() {
            @Override
            public void onResponse(Provider provider, SDKError sdkError) {

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getProviderDetails(context,thsProviderInfo,THSProviderDetailsCallback);
                            } catch (AWSDKInstantiationException e) {
                                THSProviderDetailsCallback.onProviderDetailsFetchError(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            THSProviderDetailsCallback.onProviderDetailsFetchError(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    THSProviderDetailsCallback.onProviderDetailsReceived(provider,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

                THSProviderDetailsCallback.onProviderDetailsFetchError(throwable);
            }
        });
    }

    public void updateConsumer(final Context context, final String updatedPhone, final THSUpdateConsumerCallback<Consumer, THSSDKPasswordError> pthUpdateConsumer) throws AWSDKInstantiationException {
        ConsumerUpdate consumerUpdate;
        if(getConsumer(context).isDependent()){
            consumerUpdate = getAwsdk(context).getConsumerManager().getNewConsumerUpdate(getThsParentConsumer(context).getConsumer());
            consumerUpdate.setPhone(updatedPhone);
        }else {
            consumerUpdate = getAwsdk(context).getConsumerManager().getNewConsumerUpdate(getConsumer(context));
            consumerUpdate.setPhone(updatedPhone);
        }
        getAwsdk(context).getConsumerManager().updateConsumer(consumerUpdate, new SDKValidatedCallback<Consumer, SDKPasswordError>() {
            @Override
            public void onValidationFailure(@NonNull Map<String, String> map) {
                pthUpdateConsumer.onUpdateConsumerValidationFailure(map);
            }

            @Override
            public void onResponse(Consumer consumer, SDKPasswordError sdkPasswordError) {

                THSSDKPasswordError pthSDKError = new THSSDKPasswordError();
                pthSDKError.setSdkPasswordError(sdkPasswordError);

                if(sdkPasswordError!=null && sdkPasswordError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                updateConsumer(context,updatedPhone,pthUpdateConsumer);
                            } catch (AWSDKInstantiationException e) {
                                pthUpdateConsumer.onUpdateConsumerFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            pthUpdateConsumer.onUpdateConsumerFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    mConsumer=consumer;
                    getThsParentConsumer(context).setConsumer(consumer);
                    pthUpdateConsumer.onUpdateConsumerResponse(consumer,pthSDKError);
                }
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

    //Done error codes
    public void getMedication(final Context context , final THSMedicationCallback.PTHGetMedicationCallback thsGetMedicationCallback ) throws AWSDKInstantiationException{
        getAwsdk(context).getConsumerManager().getMedications(getConsumer(context), new SDKCallback<List<Medication>, SDKError>() {
            @Override
            public void onResponse(List<Medication> medications, SDKError sdkError) {
                AmwellLog.i("onGetMedicationReceived", "success");

                THSMedication thsMedication = new THSMedication();
                thsMedication.setMedicationList(medications);

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getMedication(context,thsGetMedicationCallback);
                            } catch (AWSDKInstantiationException e) {
                                thsGetMedicationCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsGetMedicationCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    thsGetMedicationCallback.onGetMedicationReceived(thsMedication, sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                AmwellLog.i("onGetMedicationReceived", "failure");
                thsGetMedicationCallback.onFailure(throwable);
            }
        });

    }

    //Done error codes
    public void searchMedication(final Context context, final String medicineName, final THSSDKValidatedCallback<THSMedication, SDKError> thsSDKValidatedCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().searchMedications(getConsumer(context), medicineName, new SDKValidatedCallback<List<Medication>, SDKError>() {
            @Override
            public void onValidationFailure(@NonNull Map<String, String> map) {
                thsSDKValidatedCallback.onValidationFailure(map);
            }


            @Override
            public void onResponse(List<Medication> medications, SDKError sdkError) {

                THSMedication pTHMedication = new THSMedication();
                pTHMedication.setMedicationList(medications);

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                searchMedication(context,medicineName,thsSDKValidatedCallback);
                            } catch (AWSDKInstantiationException e) {
                                thsSDKValidatedCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsSDKValidatedCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    thsSDKValidatedCallback.onResponse(pTHMedication, sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                AmwellLog.i("onSearchMedication", "failure");
                thsSDKValidatedCallback.onFailure(throwable);
            }
        });


    }

    //Done error codes
    public void updateVitals(final Context context, final THSVitals thsVitals, final THSUpdateVitalsCallBack thsUpdateVitalsCallBack) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().updateVitals(getConsumer(context), thsVitals.getVitals(), null , new SDKValidatedCallback<Void, SDKError>() {
            @Override
            public void onValidationFailure(@NonNull Map<String, String> map) {
                thsUpdateVitalsCallBack.onUpdateVitalsValidationFailure(map);
            }

            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                updateVitals(context,thsVitals,thsUpdateVitalsCallBack);
                            } catch (AWSDKInstantiationException e) {
                                thsUpdateVitalsCallBack.onUpdateVitalsFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsUpdateVitalsCallBack.onUpdateVitalsFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    thsUpdateVitalsCallBack.onUpdateVitalsResponse(sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsUpdateVitalsCallBack.onUpdateVitalsFailure(throwable);
            }
        });
    }

    public void updateConditions(final Context context, final List<THSCondition> pthConditionList, final THSUpdateConditionsCallback<Void, THSSDKError> thsUpdateConditionsCallback) throws AWSDKInstantiationException {

        List<Condition> conditionList = new ArrayList<>();
        for (THSCondition pthcondition:pthConditionList
             ) {
            conditionList.add(pthcondition.getCondition());
        }
        
        getAwsdk(context).getConsumerManager().updateConditions(getConsumer(context), conditionList, new SDKCallback<Void, SDKError>() {
            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {
                THSSDKError pthSDKError = new THSSDKError();
                pthSDKError.setSdkError(sdkError);

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                updateConditions(context,pthConditionList,thsUpdateConditionsCallback);
                            } catch (AWSDKInstantiationException e) {
                                thsUpdateConditionsCallback.onUpdateConditionFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsUpdateConditionsCallback.onUpdateConditionFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    thsUpdateConditionsCallback.onUpdateConditonResponse(aVoid,pthSDKError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsUpdateConditionsCallback.onUpdateConditionFailure(throwable);
            }
        });
    }

    //Done error codes
    public void updateMedication(final Context context , final THSMedication pTHMedication, final THSMedicationCallback.PTHUpdateMedicationCallback thsUpdateMedicationCallback) throws AWSDKInstantiationException{
        getAwsdk(context).getConsumerManager().updateMedications(getConsumer(context), pTHMedication.getMedicationList(), new SDKCallback<Void, SDKError>() {
            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {
                // sdkError comes null even after successfully updating the medication
                AmwellLog.i("onUpdateMedication","success");

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                updateMedication(context,pTHMedication,thsUpdateMedicationCallback);
                            } catch (AWSDKInstantiationException e) {
                                thsUpdateMedicationCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsUpdateMedicationCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    thsUpdateMedicationCallback.onUpdateMedicationSent(aVoid,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                AmwellLog.i("onUpdateMedication","failure");
                thsUpdateMedicationCallback.onFailure(throwable);

            }
        });
    }

    /*public void getLegaltext(Context context, LegalText legalText, final THSNoticeOfPrivacyPracticesCallBack thsNoticeOfPrivacyPracticesCallBack) throws AWSDKInstantiationException {
        getAwsdk(context).getLegalText(legalText, new SDKCallback<String, SDKError>() {
            @Override
            public void onResponse(String s, SDKError sdkError) {
                thsNoticeOfPrivacyPracticesCallBack.onNoticeOfPrivacyPracticesReceivedSuccess(s, sdkError);
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsNoticeOfPrivacyPracticesCallBack.onNoticeOfPrivacyPracticesReceivedFailure(throwable);
            }
        });
    }
*/
    public void getPharmacies(final Context context, final Consumer thsConsumerWrapper, final String city, final State state, final String zipCode, final THSGetPharmaciesCallback thsGetPharmaciesCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getPharmacies(thsConsumerWrapper, null,city, state, zipCode, new SDKValidatedCallback<List<Pharmacy>, SDKError>() {
            @Override
            public void onValidationFailure(@NonNull Map<String, String> map) {
                thsGetPharmaciesCallback.onValidationFailure(map);
            }

            @Override
            public void onResponse(List<Pharmacy> pharmacies, SDKError sdkError) {

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getPharmacies(context,thsConsumerWrapper,city,state,zipCode,thsGetPharmaciesCallback);
                            } catch (AWSDKInstantiationException e) {
                                thsGetPharmaciesCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsGetPharmaciesCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    thsGetPharmaciesCallback.onPharmacyListReceived(pharmacies,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsGetPharmaciesCallback.onFailure(throwable);
            }
        });
    }

    public void getPharmacies(final Context context, final Consumer thsConsumerWrapper, final float latitude, final float longitude, final int radius, final THSGetPharmaciesCallback thsGetPharmaciesCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getPharmacies(thsConsumerWrapper, latitude, longitude, radius, false, new SDKCallback<List<Pharmacy>, SDKError>() {
            @Override
            public void onResponse(List<Pharmacy> pharmacies, SDKError sdkError) {

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getPharmacies(context,thsConsumerWrapper,latitude,longitude,radius,thsGetPharmaciesCallback);
                            } catch (AWSDKInstantiationException e) {
                                thsGetPharmaciesCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsGetPharmaciesCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    thsGetPharmaciesCallback.onPharmacyListReceived(pharmacies,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsGetPharmaciesCallback.onFailure(throwable);
            }
        });
    }

    /**
    Error handling not done here. If there is not pharmacy returned, then the logic of the app takes to suer to select a pharmacy. Heance SDK error which
    throws Primary pharmacy not found is not required.*/

    public void getConsumerPreferredPharmacy(final Context context, final THSPreferredPharmacyCallback thsPreferredPharmacyCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getConsumerPharmacy(getConsumer(context), new SDKCallback<Pharmacy, SDKError>() {
            @Override
            public void onResponse(Pharmacy pharmacy, SDKError sdkError) {

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getConsumerPreferredPharmacy(context,thsPreferredPharmacyCallback);
                            } catch (AWSDKInstantiationException e) {
                                thsPreferredPharmacyCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsPreferredPharmacyCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    thsPreferredPharmacyCallback.onPharmacyReceived(pharmacy, sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsPreferredPharmacyCallback.onFailure(throwable);
            }
        });
    }

    /**
     * Error handling not done here as the sdk error returned here has no ENUM from Amwell server.
     * @param context
     * @param pharmacy
     * @param thsUpdatePharmacyCallback
     * @throws AWSDKInstantiationException
     */
    public void updateConsumerPreferredPharmacy(final Context context, final Pharmacy pharmacy, final THSUpdatePharmacyCallback thsUpdatePharmacyCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().updateConsumerPharmacy(getConsumer(context), pharmacy, new SDKCallback<Void, SDKError>() {
            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                updateConsumerPreferredPharmacy(context,pharmacy,thsUpdatePharmacyCallback);
                            } catch (AWSDKInstantiationException e) {
                                thsUpdatePharmacyCallback.onUpdateFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsUpdatePharmacyCallback.onUpdateFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    thsUpdatePharmacyCallback.onUpdateSuccess(sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsUpdatePharmacyCallback.onUpdateFailure(throwable);
            }
        });
    }

    /**
     ** Error handling not done here as the sdk error returned here has no ENUM from Amwell server
     * @param context
     * @param thsConsumerShippingAddressCallback
     * @throws AWSDKInstantiationException
     */
    public void getConsumerShippingAddress(final Context context, final THSConsumerShippingAddressCallback thsConsumerShippingAddressCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getShippingAddress(getConsumer(context), new SDKCallback<Address, SDKError>() {
            @Override
            public void onResponse(Address address, SDKError sdkError) {

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getConsumerShippingAddress(context,thsConsumerShippingAddressCallback);
                            } catch (AWSDKInstantiationException e) {
                                thsConsumerShippingAddressCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsConsumerShippingAddressCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    thsConsumerShippingAddressCallback.onSuccessfulFetch(address, sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsConsumerShippingAddressCallback.onFailure(throwable);
            }
        });
    }

    /**
     ** Error handling not done here as the sdk error returned here has no ENUM from Amwell server
     * @param context
     * @param address
     * @param thsUpdateShippingAddressCallback
     * @throws AWSDKInstantiationException
     */
    public void updatePreferredShippingAddress(final Context context, final Address address, final THSUpdateShippingAddressCallback thsUpdateShippingAddressCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().updateShippingAddress(getConsumer(context), address, new SDKValidatedCallback<Address, SDKError>() {
            @Override
            public void onValidationFailure(@NonNull Map<String, String> map) {
                thsUpdateShippingAddressCallback.onAddressValidationFailure(map);
            }

            @Override
            public void onResponse(final Address address, SDKError sdkError) {

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                updatePreferredShippingAddress(context,address,thsUpdateShippingAddressCallback);
                            } catch (AWSDKInstantiationException e) {
                                thsUpdateShippingAddressCallback.onUpdateFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsUpdateShippingAddressCallback.onUpdateFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    thsUpdateShippingAddressCallback.onUpdateSuccess(address,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsUpdateShippingAddressCallback.onUpdateFailure(throwable);
            }
        });
    }

    public void getAvailableProvidersBasedOnDate(final Context context, final Practice thsPractice,
                                                 final String searchItem, final Language languageSpoken, final Date appointmentDate,
                                                 final Integer maxresults,
                                                 final THSAvailableProvidersBasedOnDateCallback<THSAvailableProviderList, THSSDKError> thsAvailableProviderCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getPracticeProvidersManager().findFutureAvailableProviders(getConsumer(context), thsPractice,
                searchItem, languageSpoken, appointmentDate, maxresults,null, new SDKCallback<AvailableProviders, SDKError>() {
                    @Override
                    public void onResponse(AvailableProviders availableProviders, SDKError sdkError) {
                        THSAvailableProviderList thsAvailableProviderList = new THSAvailableProviderList();
                        thsAvailableProviderList.setAvailableProviders(availableProviders);

                        THSSDKError thsSDKError = new THSSDKError();
                        thsSDKError.setSdkError(sdkError);

                        if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                            AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                            mRefreshToken.setContext(context);
                            mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                                @Override
                                public void onSuccess() {
                                    try {
                                        getAvailableProvidersBasedOnDate(context,thsPractice,searchItem,languageSpoken,appointmentDate,maxresults,thsAvailableProviderCallback);
                                    } catch (AWSDKInstantiationException e) {
                                        thsAvailableProviderCallback.onFailure(e);
                                    }
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    thsAvailableProviderCallback.onFailure(throwable);
                                }
                            });

                            refreshAmwellToken(context);

                        }else {
                            thsAvailableProviderCallback.onResponse(thsAvailableProviderList,thsSDKError);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        thsAvailableProviderCallback.onFailure(throwable);
                    }
                });

    }

    public void getProviderAvailability(final Context context, final Provider provider, final Date date, final THSAvailableProviderCallback<List<Date>,THSSDKError> thsAvailableProviderCallback) throws AWSDKInstantiationException {
        try {
            getAwsdk(context).getPracticeProvidersManager().getProviderAvailability(getConsumer(context), provider,
                    date, null,new SDKCallback<List<Date>, SDKError>() {
                        @Override
                        public void onResponse(List<Date> dates, SDKError sdkError) {
                            THSSDKError thssdkError = new THSSDKError();
                            thssdkError.setSdkError(sdkError);

                            if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                                AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                                mRefreshToken.setContext(context);
                                mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                                    @Override
                                    public void onSuccess() {
                                        try {
                                            getProviderAvailability(context,provider,date,thsAvailableProviderCallback);
                                        } catch (AWSDKInstantiationException e) {
                                            thsAvailableProviderCallback.onFailure(e);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Throwable throwable) {
                                        thsAvailableProviderCallback.onFailure(throwable);
                                    }
                                });

                                refreshAmwellToken(context);

                            }else {
                                thsAvailableProviderCallback.onResponse(dates,thssdkError);
                            }
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

        }
        return healthplans;

    }

    public List<Relationship> getSubscriberRelationships(Context context) throws AWSDKInstantiationException {
        List<Relationship> relationships;

        relationships = getAwsdk(context).getConsumerManager().getRelationships();

        return relationships;
    }


    public void getExistingSubscription(final Context context, final THSInsuranceCallback.THSgetInsuranceCallBack<THSSubscription, THSSDKError> tHSSDKCallBack) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getInsuranceSubscription(getConsumer(context), new SDKCallback<Subscription, SDKError>() {
            @Override
            public void onResponse(Subscription subscription, SDKError sdkError) {
                THSSubscription tHSSubscription = new THSSubscription();
                tHSSubscription.setSubscription(subscription);
                THSSDKError tHSSDKError = new THSSDKError();
                tHSSDKError.setSdkError(sdkError);

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getExistingSubscription(context,tHSSDKCallBack);
                            } catch (AWSDKInstantiationException e) {
                                tHSSDKCallBack.onGetInsuranceFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            tHSSDKCallBack.onGetInsuranceFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    tHSSDKCallBack.onGetInsuranceResponse(tHSSubscription, tHSSDKError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                tHSSDKCallBack.onGetInsuranceFailure(throwable);
            }
        });

    }

    public THSSubscriptionUpdateRequest getNewSubscriptionUpdateRequest(Context context) throws AWSDKInstantiationException {
        THSSubscriptionUpdateRequest thsSubscriptionUpdateRequest = new THSSubscriptionUpdateRequest();
        SubscriptionUpdateRequest subscriptionUpdateRequest = getAwsdk(context).getConsumerManager().getNewSubscriptionUpdateRequest(getConsumer(context), false);
        thsSubscriptionUpdateRequest.setSubscriptionUpdateRequest(subscriptionUpdateRequest);
        return thsSubscriptionUpdateRequest;
    }

    public void updateInsuranceSubscription(final Context context, final THSSubscriptionUpdateRequest thsSubscriptionUpdateRequest, final THSSDKValidatedCallback<Void, SDKError> tHSSDKValidatedCallback) throws AWSDKInstantiationException {

        getAwsdk(context).getConsumerManager().updateInsuranceSubscription(thsSubscriptionUpdateRequest.getSubscriptionUpdateRequest(), new SDKValidatedCallback<Void, SDKError>() {
            @Override
            public void onValidationFailure(@NonNull Map<String, String> map) {
                tHSSDKValidatedCallback.onValidationFailure(map);
            }

            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                updateInsuranceSubscription(context,thsSubscriptionUpdateRequest,tHSSDKValidatedCallback);
                            } catch (AWSDKInstantiationException e) {
                                tHSSDKValidatedCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            tHSSDKValidatedCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    tHSSDKValidatedCallback.onResponse(aVoid, sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                tHSSDKValidatedCallback.onFailure(throwable);
            }
        });

    }

    public void validateSubscriptionUpdateRequest(Context context, THSSubscriptionUpdateRequest thsSubscriptionUpdateRequest,@NonNull Map<String, String> errors) throws AWSDKInstantiationException {


        getAwsdk(context).getConsumerManager().validateSubscriptionUpdateRequest(thsSubscriptionUpdateRequest.getSubscriptionUpdateRequest(), errors);
    }

    public void createVisit(final Context context, final VisitContext thsVisitContext, final CreateVisitCallback<THSVisit, THSSDKError> createVisitCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getVisitManager().createOrUpdateVisit(thsVisitContext, new SDKValidatedCallback<Visit, SDKError>() {
            @Override
            public void onValidationFailure(@NonNull Map<String, String> map) {
                createVisitCallback.onCreateVisitValidationFailure(map);
            }

            @Override
            public void onResponse(Visit visit, SDKError sdkError) {
                THSVisit thsVisit = new THSVisit();
                thsVisit.setVisit(visit);
                THSSDKError thssdkError = new THSSDKError();
                thssdkError.setSdkError(sdkError);

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                createVisit(context,thsVisitContext,createVisitCallback);
                            } catch (AWSDKInstantiationException e) {
                                createVisitCallback.onCreateVisitFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            createVisitCallback.onCreateVisitFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    createVisitCallback.onCreateVisitResponse(thsVisit, thssdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                createVisitCallback.onCreateVisitFailure(throwable);
            }
        });

    }

    public void getPaymentMethod(final Context context, final THSPaymentCallback.THSGetPaymentMethodCallBack<THSPaymentMethod, THSSDKError> tHSSDKCallBack) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getPaymentMethod(getConsumer(context), new SDKCallback<PaymentMethod, SDKError>() {
            @Override
            public void onResponse(PaymentMethod paymentMethod, SDKError sdkError) {
                THSPaymentMethod tHSPaymentMethod = new THSPaymentMethod();
                tHSPaymentMethod.setPaymentMethod(paymentMethod);
                THSSDKError tHSSDKError = new THSSDKError();
                tHSSDKError.setSdkError(sdkError);

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                getPaymentMethod(context,tHSSDKCallBack);
                            } catch (AWSDKInstantiationException e) {
                                tHSSDKCallBack.onGetPaymentFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            tHSSDKCallBack.onGetPaymentFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    tHSSDKCallBack.onGetPaymentSuccess(tHSPaymentMethod, tHSSDKError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                tHSSDKCallBack.onGetPaymentFailure(throwable);
            }
        });
    }


    public THSCreatePaymentRequest getNewCreatePaymentRequest(Context context) throws AWSDKInstantiationException {
        CreatePaymentRequest createPaymentRequest = getAwsdk(context).getConsumerManager().getNewCreatePaymentRequest(getConsumer(context));
        THSCreatePaymentRequest tHSCreatePaymentRequest = new THSCreatePaymentRequest();
        tHSCreatePaymentRequest.setCreatePaymentRequest(createPaymentRequest);
        return tHSCreatePaymentRequest;
    }

    public THSAddress getAddress(Context context) throws AWSDKInstantiationException {
        THSAddress thsAddress = new THSAddress();
        Address address = getAwsdk(context).getNewAddress();
        thsAddress.setAddress(address);
        return thsAddress;
    }

    public boolean isCreditCardNumberValid(Context context, String cardNumber) throws AWSDKInstantiationException {
        return THSManager.getInstance().getAwsdk(context).getCreditCardUtil().isCreditCardNumberValid(cardNumber);
    }

    public boolean isSecurityCodeValid(Context context, String cardNumber, String cvv) throws AWSDKInstantiationException {
        return THSManager.getInstance().getAwsdk(context).getCreditCardUtil().isSecurityCodeValid(cardNumber, cvv);
    }


    public void validateCreatePaymentRequest(Context context, THSCreatePaymentRequest thsCreatePaymentRequest, Map<String, String> errors) throws AWSDKInstantiationException {

        getAwsdk(context).getConsumerManager().validateCreatePaymentRequest(thsCreatePaymentRequest.getCreatePaymentRequest(),errors );


    }
    public void updatePaymentMethod(final Context context, final THSCreatePaymentRequest thsCreatePaymentRequest, final THSPaymentCallback.THSUpdatePaymentMethodValidatedCallback<THSPaymentMethod, THSSDKError> tHSSDKValidatedCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().updatePaymentMethod(thsCreatePaymentRequest.getCreatePaymentRequest(), new SDKValidatedCallback<PaymentMethod, SDKError>() {
            @Override
            public void onValidationFailure(@NonNull Map<String, String> map) {
                tHSSDKValidatedCallback.onValidationFailure(map);
            }

            @Override
            public void onResponse(PaymentMethod paymentMethod, SDKError sdkError) {
                THSPaymentMethod tHSPaymentMethod = new THSPaymentMethod();
                tHSPaymentMethod.setPaymentMethod(paymentMethod);
                THSSDKError tHSSDKError = new THSSDKError();
                tHSSDKError.setSdkError(sdkError);

                if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                    AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                        @Override
                        public void onSuccess() {
                            try {
                                updatePaymentMethod(context,thsCreatePaymentRequest,tHSSDKValidatedCallback);
                            } catch (AWSDKInstantiationException e) {
                                tHSSDKValidatedCallback.onUpdatePaymentFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            tHSSDKValidatedCallback.onUpdatePaymentFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                }else {
                    tHSSDKValidatedCallback.onUpdatePaymentSuccess(tHSPaymentMethod, tHSSDKError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                tHSSDKValidatedCallback.onUpdatePaymentFailure(throwable);
            }
        });
    }

    public void scheduleAppointment(final Context context, final THSProviderInfo thsProviderInfo, final Date appointmentDate, final String consumerRemindOptions, final THSSDKValidatedCallback<Void, SDKError> thssdkValidatedCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().scheduleAppointment(getConsumer(context), thsProviderInfo.getProviderInfo(),
                appointmentDate, null,consumerRemindOptions, RemindOptions.NO_REMINDER, new SDKValidatedCallback<Void, SDKError>() {
                    @Override
                    public void onValidationFailure(@NonNull Map<String, String> map) {
                        thssdkValidatedCallback.onValidationFailure(map);
                    }

                    @Override
                    public void onResponse(Void aVoid, SDKError sdkError) {

                        if(sdkError!=null && sdkError.getHttpResponseCode()== HttpURLConnection.HTTP_UNAUTHORIZED){

                            AmwellLog.i(AmwellLog.LOG,"getPractices got 401 and called refresh in separate thread");

                            mRefreshToken.setContext(context);
                            mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack(){

                                @Override
                                public void onSuccess() {
                                    try {
                                        scheduleAppointment(context,thsProviderInfo,appointmentDate,consumerRemindOptions,thssdkValidatedCallback);
                                    } catch (AWSDKInstantiationException e) {
                                        thssdkValidatedCallback.onFailure(e);
                                    }
                                }

                                @Override
                                public void onFailure(Throwable throwable) {
                                    thssdkValidatedCallback.onFailure(throwable);
                                }
                            });

                            refreshAmwellToken(context);

                        }else {
                            thssdkValidatedCallback.onResponse(aVoid,sdkError);
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        thssdkValidatedCallback.onFailure(throwable);
                    }
                });

    }

    public void uploadHealthDocument(final Context context, final UploadAttachment uploadAttachment, final THSUploadDocumentCallback thsUploadDocumentCallback) throws AWSDKInstantiationException, IOException {

        getAwsdk(context).getConsumerManager().addHealthDocument(getConsumer(context), uploadAttachment, new SDKValidatedCallback<DocumentRecord, SDKError>() {
            @Override
            public void onValidationFailure(@NonNull Map<String, String> map) {
                thsUploadDocumentCallback.onUploadValidationFailure(map);
            }

            @Override
            public void onResponse(DocumentRecord documentRecord, SDKError sdkError) {

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                uploadHealthDocument(context, uploadAttachment, thsUploadDocumentCallback);
                            } catch (IOException e) {
                                thsUploadDocumentCallback.onError(e);
                            } catch (AWSDKInstantiationException e) {
                                thsUploadDocumentCallback.onError(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsUploadDocumentCallback.onError(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    thsUploadDocumentCallback.onUploadDocumentSuccess(documentRecord, sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsUploadDocumentCallback.onError(throwable);
            }
        });
    }

    public void deletedHealthDocument(final Context context, final DocumentRecord documentRecord, final THSDeleteDocumentCallback thsDeleteDocumentCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().removeHealthDocumentRecord(getConsumer(context), documentRecord, new SDKCallback<Void, SDKError>() {
            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                deletedHealthDocument(context,documentRecord,thsDeleteDocumentCallback);
                            }  catch (AWSDKInstantiationException e) {
                                thsDeleteDocumentCallback.onError(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsDeleteDocumentCallback.onError(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    thsDeleteDocumentCallback.onDeleteSuccess(aVoid,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsDeleteDocumentCallback.onError(throwable);
            }
        });

    }
    public void startVisit(final Context context , final Visit visit, final Intent intent, final THSStartVisitCallback thsStartVisitCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getVisitManager().startVisit(visit, visit.getConsumer().getAddress(), intent,new StartVisitCallback() {

            @Override
            public void onValidationFailure(@NonNull Map<String, String> map) {
                thsStartVisitCallback.onValidationFailure(map);
            }

            @Override
            public void onProviderEntered(@NonNull Intent intent) {
                thsStartVisitCallback.onProviderEntered(intent);
            }

            @Override
            public void onStartVisitEnded(@NonNull String s) {
                thsStartVisitCallback.onStartVisitEnded(s);
            }

            @Override
            public void onPatientsAheadOfYouCountChanged(int i) {
                thsStartVisitCallback.onPatientsAheadOfYouCountChanged(i);
            }

            @Override
            public void onSuggestedTransfer() {
                thsStartVisitCallback.onSuggestedTransfer();
            }

            @Override
            public void onChat(@NonNull ChatReport chatReport) {
                thsStartVisitCallback.onChat(chatReport);
            }

            @Override
            public void onPollFailure(@NonNull Throwable throwable) {
                thsStartVisitCallback.onPollFailure(throwable);
            }

            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                startVisit(context,visit,intent,thsStartVisitCallback);
                            }  catch (AWSDKInstantiationException e) {
                                thsStartVisitCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsStartVisitCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    thsStartVisitCallback.onResponse(aVoid,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsStartVisitCallback.onFailure(throwable);
            }
        });
    }

    public void cancelVisit(final Context context, final Visit visit, final THSCancelVisitCallBack.SDKCallback <Void, SDKError> tHSSDKCallback)  throws AWSDKInstantiationException {
        getAwsdk(context).getVisitManager().cancelVisit(visit, new SDKCallback<Void, SDKError>() {
            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                cancelVisit(context,visit,tHSSDKCallback);
                            }  catch (AWSDKInstantiationException e) {
                                tHSSDKCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            tHSSDKCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    tHSSDKCallback.onResponse(aVoid, sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                tHSSDKCallback.onFailure(throwable);
            }
        });

    }

    public void cancelAppointment(final Context context, final Appointment appointment, final THSInitializeCallBack<Void, THSSDKError> thsInitializeCallBack) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().cancelAppointment(getConsumer(context), appointment, new SDKCallback<Void, SDKError>() {
            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {
                THSSDKError thssdkError = new THSSDKError();
                thssdkError.setSdkError(sdkError);

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                cancelAppointment(context,appointment,thsInitializeCallBack);
                            }  catch (AWSDKInstantiationException e) {
                                thsInitializeCallBack.onInitializationFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsInitializeCallBack.onInitializationFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    thsInitializeCallBack.onInitializationResponse(aVoid,thssdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsInitializeCallBack.onInitializationFailure(throwable);
            }
        });
    }

    public void abandonCurrentVisit(Context context)throws AWSDKInstantiationException{
        getAwsdk(context).getVisitManager().abandonCurrentVisit();
    }


    //TODO : error code :No enum sent by amwell for sdkerror code handling
    public void fetchEstimatedVisitCost(final Context context, final Provider provider, final THSFetchEstimatedCostCallback thsFetchEstimatedCostCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getPracticeProvidersManager().getEstimatedVisitCost(getConsumer(context), provider, new SDKCallback<EstimatedVisitCost, SDKError>() {
            @Override
            public void onResponse(EstimatedVisitCost estimatedVisitCost, SDKError sdkError) {

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                fetchEstimatedVisitCost(context,provider,thsFetchEstimatedCostCallback);
                            }  catch (AWSDKInstantiationException e) {
                                thsFetchEstimatedCostCallback.onError(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsFetchEstimatedCostCallback.onError(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    thsFetchEstimatedCostCallback.onEstimatedCostFetchSuccess(estimatedVisitCost,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsFetchEstimatedCostCallback.onError(throwable);
            }
        });
    }

    @VisibleForTesting
    public void setUser(User user){
        mUser = user;
    }

    public String getAppName() {
        AppConfigurationInterface.AppConfigurationError configError = new
                AppConfigurationInterface.AppConfigurationError();
        Object propertyForKey = (getAppInfra().getConfigInterface().getPropertyForKey(URConfigurationConstants.HSDP_CONFIGURATION_APPLICATION_NAME,
                URConfigurationConstants.UR, configError));
        if(propertyForKey instanceof Map<?,?>){
            HashMap<?,?> map = (HashMap<?,?>) propertyForKey;
            if (map.get("default") != null) {
                return map.get("default").toString();
            }else {
                return " ";
            }
        }
        if(propertyForKey!=null) {
            return propertyForKey.toString();
        }else {
            return " ";
        }
    }

    public AppInfraInterface getAppInfra() {
        return mAppInfra;
    }

    public void setAppInfra(AppInfraInterface mAppInfra) {
        this.mAppInfra = mAppInfra;
        this.mAppTaggingInterface = mAppInfra.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME);// initialize tagging for ths
        this.mLoggingInterface = mAppInfra.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME);
        this.mCountry = getAppInfra().getServiceDiscovery().getHomeCountry();
    }

    //TODO : error code :No enum sent by amwell for sdkerror code handling
    public void getVisitHistory(final Context context, final SDKLocalDate date, final THSVisitReportListCallback<List<VisitReport>, SDKError> visitReportListCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getVisitReports(getConsumer(context), date, null, new SDKCallback<List<VisitReport>, SDKError>() {

            @Override
            public void onResponse(List<VisitReport> visitReports, SDKError sdkError) {

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                getVisitHistory(context,date,visitReportListCallback);
                            }  catch (AWSDKInstantiationException e) {
                                visitReportListCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            visitReportListCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    visitReportListCallback.onResponse(visitReports,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                visitReportListCallback.onFailure(throwable);
            }
        });
    }

    //TODO : error code :No enum sent by amwell for sdkerror code handling
    public void getVisitReportDetail(final Context context, final VisitReport visitReport, final THSVisitReportDetailCallback<VisitReportDetail, SDKError> thsVisitReportDetailCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getVisitReportDetail(getConsumer(context), visitReport, new SDKCallback<VisitReportDetail, SDKError>() {
            @Override
            public void onResponse(VisitReportDetail visitReportDetail, SDKError sdkError) {

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                getVisitReportDetail(context,visitReport,thsVisitReportDetailCallback);
                            }  catch (AWSDKInstantiationException e) {
                                thsVisitReportDetailCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsVisitReportDetailCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    thsVisitReportDetailCallback.onResponse(visitReportDetail,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsVisitReportDetailCallback.onFailure(throwable);
            }
        });
    }
    //TODO : error code :No enum sent by amwell for sdkerror code handling

    public void getVisitSummary(final Context context, final Visit visit, final THSVisitSummaryCallbacks.THSVisitSummaryCallback<THSVisitSummary, THSSDKError> thsVisitSummaryCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getVisitManager().getVisitSummary(visit, new SDKCallback<VisitSummary, SDKError>() {
            @Override
            public void onResponse(VisitSummary visitSummary, SDKError sdkError) {
                THSVisitSummary thsVisitSummary = new THSVisitSummary();
                thsVisitSummary.setVisitSummary(visitSummary);
                THSSDKError thssdkError = new THSSDKError();
                thssdkError.setSdkError(sdkError);

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                getVisitSummary(context,visit,thsVisitSummaryCallback);
                            }  catch (AWSDKInstantiationException e) {
                                thsVisitSummaryCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsVisitSummaryCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    thsVisitSummaryCallback.onResponse(thsVisitSummary,thssdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsVisitSummaryCallback.onFailure(throwable);
            }
        });
    }


    //TODO : error code :No enum sent by amwell for sdkerror code handling
    public void getVisitReportAttachment(final Context context, final VisitReport visitReport, final THSVisitReportAttachmentCallback<FileAttachment, SDKError> thsVisitReportAttachmentCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getConsumerManager().getVisitReportAttachment(getConsumer(context), visitReport, new SDKCallback<FileAttachment, SDKError>() {
            @Override
            public void onResponse(FileAttachment fileAttachment, SDKError sdkError) {

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                getVisitReportAttachment(context,visitReport,thsVisitReportAttachmentCallback);
                            }  catch (AWSDKInstantiationException e) {
                                thsVisitReportAttachmentCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsVisitReportAttachmentCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    thsVisitReportAttachmentCallback.onResponse(fileAttachment,sdkError);
                }

            }

            @Override
            public void onFailure(Throwable throwable) {
                thsVisitReportAttachmentCallback.onFailure(throwable);
            }
        });

    }

    public void sendRatings(final Context context, final Visit visit, final Integer providerRating, final Integer visitRating, final THSSDKCallback<Void, SDKError> thssdkCallback)throws AWSDKInstantiationException{
        getAwsdk(context).getVisitManager().sendRatings(visit, providerRating, visitRating, new SDKCallback<Void, SDKError>() {
            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                sendRatings(context,visit,providerRating,visitRating,thssdkCallback);
                            }  catch (AWSDKInstantiationException e) {
                                thssdkCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thssdkCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    thssdkCallback.onResponse(aVoid,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {

                thssdkCallback.onFailure(throwable);
            }
        });
    }

    public void getPractice(final Context context, final PracticeInfo practiceInfo, final THSPracticeCallback<Practice, SDKError> thsPracticeCallback) throws AWSDKInstantiationException {
        getAwsdk(context).getPracticeProvidersManager().getPractice(practiceInfo, new SDKCallback<Practice, SDKError>() {
            @Override
            public void onResponse(Practice practice, SDKError sdkError) {

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                getPractice(context,practiceInfo,thsPracticeCallback);
                            }  catch (AWSDKInstantiationException e) {
                                thsPracticeCallback.onFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsPracticeCallback.onFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    thsPracticeCallback.onResponse(practice,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsPracticeCallback.onFailure(throwable);
            }
        });
    }

    public void doMatchMaking(final Context context, final VisitContext thsVisitContext, final THSMatchMakingCallback thsMatchMakingCallback)throws AWSDKInstantiationException {
        getAwsdk(context).getVisitManager().startMatchmaking(thsVisitContext, new MatchmakerCallback() {

            @Override
            public void onProviderFound(@NonNull Provider provider) {
                thsMatchMakingCallback.onMatchMakingProviderFound(provider);
            }

            @Override
            public void onProviderListExhausted() {
                thsMatchMakingCallback.onMatchMakingProviderListExhausted();
            }

            @Override
            public void onRequestGone() {
                thsMatchMakingCallback.onMatchMakingRequestGone();
            }

            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                doMatchMaking(context,thsVisitContext,thsMatchMakingCallback);
                            }  catch (AWSDKInstantiationException e) {
                                thsMatchMakingCallback.onMatchMakingFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsMatchMakingCallback.onMatchMakingFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    thsMatchMakingCallback.onMatchMakingResponse(aVoid,sdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsMatchMakingCallback.onMatchMakingFailure(throwable);
            }
        });
    }

    public void cancelMatchMaking(final Context context, final VisitContext thsVisitContext)throws AWSDKInstantiationException {
        try {
            getAwsdk(context).getVisitManager().cancelMatchmaking(thsVisitContext, new SDKCallback<Void, SDKError>() {
                @Override
                public void onResponse(Void aVoid, SDKError sdkError) {
                    if (null == sdkError) {
                        AmwellLog.v("cancelMatchMaking", "success");
                    } else {
                        AmwellLog.v("cancelMatchMaking", "failure");
                    }

                    if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                        AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                        mRefreshToken.setContext(context);
                        mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                            @Override
                            public void onSuccess() {

                                try {
                                    cancelMatchMaking(context,thsVisitContext);
                                }  catch (AWSDKInstantiationException e) {

                                }
                            }

                            @Override
                            public void onFailure(Throwable throwable) {

                            }
                        });

                        refreshAmwellToken(context);

                    }

                }

                @Override
                public void onFailure(Throwable throwable) {
                    AmwellLog.v("cancelMatchMaking", "failure");
                }
            });
        }catch(Exception e){
            AmwellLog.v("cancelMatchMaking", "failure");
        }

    }
    public void applyCouponCode(final Context context, final THSVisit thsVisit, final String couponCode, final ApplyCouponCallback<Void, THSSDKError> applyCouponCallback) throws AWSDKInstantiationException{
        getAwsdk(context).getVisitManager().applyCouponCode(thsVisit.getVisit(), couponCode, new SDKCallback<Void, SDKError>() {
            @Override
            public void onResponse(Void aVoid, SDKError sdkError) {
                THSSDKError thssdkError = new THSSDKError();
                thssdkError.setSdkError(sdkError);

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                applyCouponCode(context,thsVisit,couponCode,applyCouponCallback);
                            }  catch (AWSDKInstantiationException e) {
                                applyCouponCallback.onApplyCouponFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            applyCouponCallback.onApplyCouponFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    applyCouponCallback.onApplyCouponResponse(aVoid,thssdkError);
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                applyCouponCallback.onApplyCouponFailure(throwable);
            }
        });

    }

    public boolean isReturningUser() {
        return mIsReturningUser;
    }

    public void setIsReturningUser(boolean firstTimeUser) {
        mIsReturningUser = firstTimeUser;
    }

    public THSConsumer getThsConsumer(Context context) {
        if(mThsConsumer == null){
            mThsConsumer = getThsConsumerIfNotSet(context);
        }
        return mThsConsumer;
    }

    public void setThsConsumer(THSConsumer mThsConsumer) {
        this.mThsConsumer = mThsConsumer;
    }

    public THSConsumer getThsParentConsumer(Context context) {
        if(mThsParentConsumer == null){
            mThsParentConsumer = getThsConsumerIfNotSet(context);
        }
        return mThsParentConsumer;
    }

    public void setThsParentConsumer(THSConsumer mThsParentConsumer) {
        this.mThsParentConsumer = mThsParentConsumer;
        this.mThsConsumer = mThsParentConsumer;
    }

    public void updateParentConsumer(Context context){
        final User user = getUser(context);
        getThsParentConsumer(context).setEmail(user.getEmail());
        getThsParentConsumer(context).setHsdpToken(user.getHsdpAccessToken());
        getThsParentConsumer(context).setHsdpUUID(user.getHsdpUUID());
    }

    public void updateConsumerData(final Context context, final String email, final Date date, final String firstname, final String lastname, final String gender, final State state, final THSEditUserDetailsCallBack thsEditUserDetailsCallBack) throws AWSDKInstantiationException {
        ConsumerUpdate consumerUpdate = getAwsdk(context).getConsumerManager().getNewConsumerUpdate(getThsParentConsumer(context).getConsumer());
        consumerUpdate.setEmail(email);
        consumerUpdate.setLegalResidence(state);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final SDKLocalDate sdkLocalDate = new SDKLocalDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1,calendar.get(Calendar.DATE));
        consumerUpdate.setDob(sdkLocalDate);
        consumerUpdate.setFirstName(firstname);
        consumerUpdate.setLastName(lastname);
        consumerUpdate.setGender(gender);
        getAwsdk(context).getConsumerManager().updateConsumer(consumerUpdate, new SDKValidatedCallback<Consumer, SDKPasswordError>() {
            @Override
            public void onValidationFailure(Map<String, String> map) {
                thsEditUserDetailsCallBack.onEditUserDataValidationFailure(map);
            }

            @Override
            public void onResponse(Consumer consumer, SDKPasswordError sdkPasswordError) {

                if (sdkPasswordError != null && sdkPasswordError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                updateConsumerData(context,email,date,firstname,lastname,gender,state,thsEditUserDetailsCallBack);
                            }  catch (AWSDKInstantiationException e) {
                                thsEditUserDetailsCallBack.onEditUserDataFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsEditUserDetailsCallBack.onEditUserDataFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    getThsParentConsumer(context).setConsumer(consumer);
                    getThsConsumer(context).setConsumer(consumer);
                    thsEditUserDetailsCallBack.onEditUserDataResponse(consumer,sdkPasswordError);
                    mConsumer = consumer;
                }
            }

            @Override
            public void onFailure(Throwable throwable) {
                thsEditUserDetailsCallBack.onEditUserDataFailure(throwable);
            }
        });
    }

    public void updateDependentConsumerData(final Context context, final Consumer consumerSent, final Date date, final String firstname, final String lastname, final String gender, final THSEditUserDetailsCallBack thsEditUserDetailsCallBack) throws AWSDKInstantiationException {

        DependentUpdate dependentUpdate = getAwsdk(context).getConsumerManager().getNewDependentUpdate(consumerSent);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final SDKLocalDate sdkLocalDate = new SDKLocalDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DATE));
        dependentUpdate.setDob(sdkLocalDate);
        dependentUpdate.setFirstName(firstname);
        dependentUpdate.setLastName(lastname);
        dependentUpdate.setGender(gender);
        getAwsdk(context).getConsumerManager().updateDependent(dependentUpdate, new SDKValidatedCallback<Consumer, SDKError>() {
            @Override
            public void onValidationFailure(@NonNull Map<String, String> map) {
                thsEditUserDetailsCallBack.onEditUserDataValidationFailure(map);
            }

            @Override
            public void onResponse(@Nullable final Consumer consumer, @Nullable SDKError sdkError) {

                if (sdkError != null && sdkError.getHttpResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                    AmwellLog.i(AmwellLog.LOG, "getPractices got 401 and called refresh in separate thread");

                    mRefreshToken.setContext(context);
                    mRefreshToken.setRefreshCallBack(new THSRefreshTokenCallBack() {

                        @Override
                        public void onSuccess() {

                            try {
                                updateDependentConsumerData(context, consumerSent, date, firstname, lastname, gender, thsEditUserDetailsCallBack);
                            } catch (AWSDKInstantiationException e) {
                                thsEditUserDetailsCallBack.onEditUserDataFailure(e);
                            }
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            thsEditUserDetailsCallBack.onEditUserDataFailure(throwable);
                        }
                    });

                    refreshAmwellToken(context);

                } else {
                    getThsConsumer(context).setConsumer(consumer);
                    mConsumer = consumer;
                    refreshConsumer(context, thsEditUserDetailsCallBack);

                }
            }

            @Override
            public void onFailure(@NonNull Throwable throwable) {
                thsEditUserDetailsCallBack.onEditUserDataFailure(throwable);
            }
        });
    }

    private void refreshConsumer(final Context context, final THSEditUserDetailsCallBack thsEditUserDetailsCallBack) {
        try {
            getAwsdk(context).getConsumerManager().refreshConsumer(getThsParentConsumer(context).getConsumer(), new SDKCallback<Consumer, SDKError>() {
                @Override
                public void onResponse(@Nullable Consumer consumer, @Nullable SDKError sdkError) {
                    getThsParentConsumer(context).setConsumer(consumer);
                    thsEditUserDetailsCallBack.onEditUserDataResponse(consumer,sdkError);
                }

                @Override
                public void onFailure(@NonNull Throwable throwable) {
                    thsEditUserDetailsCallBack.onEditUserDataFailure(throwable);
                }
            });
        } catch (AWSDKInstantiationException e) {
            thsEditUserDetailsCallBack.onEditUserDataFailure(e);
        }
    }

    public String getOnBoradingABFlow() {
        if(onBoradingABFlow.isEmpty() || onBoradingABFlow==null){
            setOnBoradingABFlow(THSConstants.THS_ONBOARDING_ABFLOW2);
        }
        return onBoradingABFlow;
    }

    public void setOnBoradingABFlow(String onBoradingABFlow) {
        this.onBoradingABFlow = onBoradingABFlow;
    }

    public String getProviderListABFlow() {
        return providerListABFlow;
    }

    public void setProviderListABFlow(String providerListABFlow) {
        this.providerListABFlow = providerListABFlow;
    }
}
