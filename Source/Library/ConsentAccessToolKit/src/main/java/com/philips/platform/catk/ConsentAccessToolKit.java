/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import com.philips.cdp.registration.User;
import com.philips.platform.catk.dto.CreateConsentModelRequest;
import com.philips.platform.catk.dto.GetConsentDto;
import com.philips.platform.catk.dto.GetConsentsModelRequest;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.injection.CatkModule;
import com.philips.platform.catk.injection.DaggerCatkComponent;
import com.philips.platform.catk.injection.UserModule;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.mapper.ConsentToDtoMapper;
import com.philips.platform.catk.mapper.DtoToConsentMapper;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.network.NetworkHelper;
import com.philips.platform.catk.utils.CatkLogger;

import java.util.ArrayList;
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
            public void onModelDataLoadFinished(List<GetConsentDto> dtos) {
                List<Consent> consents = new ArrayList<>();
                DtoToConsentMapper mapper = new DtoToConsentMapper();
                for (GetConsentDto dto: dtos) {
                    consents.add(mapper.map(dto));
                }
                consentListener.onResponseSuccessConsent(consents);
            }

            @Override
            public int onModelDataError(ConsentNetworkError error) {
                return consentListener.onResponseFailureConsent(error.getErrorCode());
            }
        });
        NetworkHelper.getInstance().sendRequest(model);
    }

    public void createConsent(final Consent consent, final CreateConsentListener consentListener) {
        ConsentToDtoMapper mapper = new ConsentToDtoMapper(catkComponent.getUser().getHsdpUUID(), catkComponent.getUser().getCountryCode(), propositionName, applicationName);
        CreateConsentModelRequest model = new CreateConsentModelRequest(URL, mapper.map(consent), new NetworkAbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(List<GetConsentDto> dtos) {
                        if (dtos == null) {
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


    public void getStatusForConsentType(final String consentType, int version, final ConsentResponseListener consentListener) {
        getConsentDetails(new ConsentResponseListener() {

            @Override
            public void onResponseSuccessConsent(List<Consent> responseData) {
                for (Consent consent : responseData) {
                    if (consentType.equals(consent.getType())) {
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

    static void setInstance(ConsentAccessToolKit sSoleInstance) {
        ConsentAccessToolKit.sSoleInstance = sSoleInstance;
    }
}
