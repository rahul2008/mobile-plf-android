package com.philips.platform.catk;

import android.os.Message;

import com.janrain.android.utils.StringUtils;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.injection.CatkModule;
import com.philips.platform.catk.injection.DaggerCatkComponent;
import com.philips.platform.catk.injection.UserModule;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.model.CreateConsentModelRequest;
import com.philips.platform.catk.model.GetConsentsModel;
import com.philips.platform.catk.model.GetConsentsModelRequest;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.network.NetworkHelper;
import com.philips.platform.catk.response.ConsentStatus;
import com.philips.platform.catk.utils.CatkLogger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ConsentAccessToolKit {


    private static volatile ConsentAccessToolKit sSoleInstance;

    //private constructor.
    private ConsentAccessToolKit() {

        //Prevent form the reflection api.
        if (sSoleInstance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static ConsentAccessToolKit getInstance() {
        if (sSoleInstance == null) { //if there is no instance available... create new one
            synchronized (ConsentAccessToolKit.class) {
                if (sSoleInstance == null) sSoleInstance = new ConsentAccessToolKit();
            }
        }

        return sSoleInstance;
    }

    //Make singleton from serialize and deserialize operation.
    protected ConsentAccessToolKit readResolve() {
        return getInstance();
    }


    private CatkComponent catkComponent;


    public void init(CatkInputs catkInputs) {
        catkComponent = initDaggerCoponents(catkInputs);
        CatkLogger.init();
        this.applicationName = catkInputs.getApplicationName();
        this.propositionName = catkInputs.getPropositionName();
        CatkLogger.enableLogging();
    }

    private CatkComponent initDaggerCoponents(CatkInputs catkInputs) {
        return DaggerCatkComponent.builder()
                .catkModule(new CatkModule(catkInputs.getContext(),catkInputs.getAppInfra()))
                .userModule(new UserModule(new User(catkInputs.getContext())))
               .build();
    }

    public CatkComponent getCatkComponent() {
        return catkComponent;
    }

    void setCatkComponent(CatkComponent component) {
        catkComponent = component;
    }

    private String applicationName;
    private String propositionName;


    public void getConsentDetails(final ConsentResponseListener consentListener) {
        GetConsentsModelRequest model = new GetConsentsModelRequest(applicationName, propositionName, new NetworkAbstractModel.DataLoadListener() {
            @Override
            public void onModelDataLoadFinished(Message msg) {
                ArrayList<GetConsentsModel> consentModels;
                if (StringUtils.trim(msg.obj.toString()) != "") {
                    GetConsentsModel[] modelResults = (GetConsentsModel[]) msg.obj;
                    consentModels = new ArrayList<>(Arrays.asList(modelResults));
                } else {
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
        NetworkHelper.getInstance().sendRequest(model.getMethod(), model, model);
    }


    public void createConsent(ConsentStatus consentStatus, final CreateConsentListener consentListener) {
        getLocaleAndProceed(consentStatus, consentListener);

    }

    private void getLocaleAndProceed(final ConsentStatus consentStatus, final CreateConsentListener consentListener) {

        getCatkComponent().getServiceDiscoveryInterface().
                getServiceLocaleWithCountryPreference("ds.consentservice", new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
                    @Override
                    public void onSuccess(String s) {
                        String locale = s.replace("_", "-");

                        CreateConsentModelRequest model = new CreateConsentModelRequest(applicationName, String.valueOf(consentStatus), propositionName,locale,
                                new NetworkAbstractModel.DataLoadListener() {
                                    @Override
                                    public void onModelDataLoadFinished(Message msg) {
                                        if (msg.arg1 == 0) {
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
                        NetworkHelper.getInstance().sendRequest(model.getMethod(), model, model);
                    }

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {

                    }
                });
    }


    public void getStatusForConsentType(String consentType, int version, final ConsentResponseListener consentListener) {
        final String policyRule = buildPolicyRule(consentType, version, getCatkComponent().getUser().getCountryCode(), propositionName, applicationName);
        getConsentDetails(new ConsentResponseListener() {

            @Override
            public void onResponseSuccessConsent(List<GetConsentsModel> responseData) {
                for (GetConsentsModel consent : responseData) {
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

    public String buildPolicyRule(String consentType, int version, String country, String propositionName, String applicationName) {
        return "urn:com.philips.consent:" + consentType + "/" + country + "/" + version + "/" + propositionName + "/" + applicationName;
    }

}
