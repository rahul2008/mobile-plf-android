/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import android.util.Log;

import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.catk.dto.CreateConsentModelRequest;
import com.philips.platform.catk.dto.GetConsentsModel;
import com.philips.platform.catk.dto.GetConsentsModelRequest;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.injection.CatkModule;
import com.philips.platform.catk.injection.DaggerCatkComponent;
import com.philips.platform.catk.injection.UserModule;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.network.NetworkHelper;
import com.philips.platform.catk.response.ConsentStatus;
import com.philips.platform.catk.utils.CatkLogger;

import java.util.Collections;
import java.util.List;

public class ConsentAccessToolKit {

    // This field has to remove later(url should take from service discovery)
    private static final String URL = "https://platforminfra-css-platforminfradev.cloud.pcftest.com/consent";

    private static volatile ConsentAccessToolKit sSoleInstance;

    ConsentAccessToolKit() {
    }

    public static ConsentAccessToolKit getInstance() {
        if (sSoleInstance == null) { // if there is no instance available... create new one
            synchronized (ConsentAccessToolKit.class) {
                if (sSoleInstance == null)
                    sSoleInstance = new ConsentAccessToolKit();
            }
        }

        return sSoleInstance;
    }

    // Make singleton from serialize and deserialize operation.
    protected ConsentAccessToolKit readResolve() {
        return getInstance();
    }

    private CatkComponent catkComponent;

    public void init(CatkInputs catkInputs) {
        catkComponent = initDaggerComponents(catkInputs);
        CatkLogger.init();
        this.applicationName = catkInputs.getApplicationName();
        this.propositionName = catkInputs.getPropositionName();
        CatkLogger.enableLogging();
    }

    private CatkComponent initDaggerComponents(CatkInputs catkInputs) {
        return DaggerCatkComponent.builder()
                .catkModule(new CatkModule(catkInputs.getContext(), catkInputs.getAppInfra()))
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
        GetConsentsModelRequest model = new GetConsentsModelRequest(URL, applicationName, propositionName, new NetworkAbstractModel.DataLoadListener() {
            @Override
            public void onModelDataLoadFinished(List<GetConsentsModel> consents) {
                consentListener.onResponseSuccessConsent(consents);
            }

            @Override
            public int onModelDataError(ConsentNetworkError error) {
                return consentListener.onResponseFailureConsent(error.getErrorCode());
            }
        });
        NetworkHelper.getInstance().sendRequest(model);
    }

    public void createConsent(ConsentStatus consentStatus, final CreateConsentListener consentListener) {
        getLocaleAndProceed(consentStatus, consentListener);

    }

    private void getLocaleAndProceed(final ConsentStatus consentStatus, final CreateConsentListener consentListener) {

        getCatkComponent().getServiceDiscoveryInterface().getServiceLocaleWithCountryPreference("ds.consentservice", new ServiceDiscoveryInterface.OnGetServiceLocaleListener() {
            @Override
            public void onSuccess(String s) {
                String locale = s.replace("_", "-");

                CreateConsentModelRequest model = new CreateConsentModelRequest(URL, applicationName, String.valueOf(consentStatus), propositionName, locale,
                        new NetworkAbstractModel.DataLoadListener() {
                            @Override
                            public void onModelDataLoadFinished(List<GetConsentsModel> consents) {
                                if (consents == null) {
                                    consentListener.onSuccess(CatkConstants.CONSENT_SUCCESS);
                                }
                            }

                            @Override
                            public int onModelDataError(ConsentNetworkError error) {
                                return consentListener.onFailure(error.getErrorCode());
                            }
                        });
                NetworkHelper.getInstance().sendRequest(model);
            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                Log.e("Consent access toolkit", "Could not get locale");
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

    static void setInstance(ConsentAccessToolKit sSoleInstance) {
        ConsentAccessToolKit.sSoleInstance = sSoleInstance;
    }
}
