/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import com.philips.platform.catk.dto.CreateConsentModelRequest;
import com.philips.platform.catk.dto.GetConsentDto;
import com.philips.platform.catk.dto.GetConsentsModelRequest;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.infra.InfraServiceInfoProvider;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.injection.CatkComponentFactory;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.mapper.ConsentToDtoMapper;
import com.philips.platform.catk.mapper.DtoToConsentMapper;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.network.NetworkController;
import com.philips.platform.catk.provider.AppInfraInfo;
import com.philips.platform.catk.provider.ComponentProvider;
import com.philips.platform.catk.provider.ServiceInfoProvider;
import com.philips.platform.catk.utils.CatkLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConsentAccessToolKit {

    private static volatile ConsentAccessToolKit sSoleInstance;

    private NetworkController controller;
    private CatkComponent catkComponent;
    private String applicationName;
    private String propositionName;
    private ComponentProvider componentProvider;
    private ServiceInfoProvider serviceInfoProvider;

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

    public void init(CatkInputs catkInputs) {
        componentProvider = componentProvider == null ? new CatkComponentFactory() : componentProvider;
        serviceInfoProvider = serviceInfoProvider == null ? new InfraServiceInfoProvider() : serviceInfoProvider;
        catkComponent = componentProvider.getComponent(catkInputs);
        CatkLogger.init();
        CatkLogger.enableLogging();
        this.applicationName = catkInputs.getApplicationName();
        this.propositionName = catkInputs.getPropositionName();
    }

    private void retrieveConsentServiceInfo(final ConfigCompletionListener listner) {
        ServiceInfoProvider.ResponseListener responseListener = new ServiceInfoProvider.ResponseListener() {
            @Override
            public void onResponse(AppInfraInfo info) {
                listner.onConfigurationCompletion(info.getCssUrl());
            }

            @Override
            public void onError(String message) {
                CatkLogger.e("ConsentAccessToolKit", "markErrorAndGetPrevious retrieving cssUrl: " + message);
            }
        };
        serviceInfoProvider.retrieveInfo(catkComponent.getServiceDiscoveryInterface(), responseListener);
    }

    public CatkComponent getCatkComponent() {
        return catkComponent;
    }

    public void getConsentDetails(final ConsentResponseListener consentListener) {
        retrieveConsentServiceInfo(new ConfigCompletionListener() {
            @Override
            public void onConfigurationCompletion(String cssUrl) {
                GetConsentsModelRequest model = new GetConsentsModelRequest(cssUrl, applicationName, propositionName, new NetworkAbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(List<GetConsentDto> dtos) {
                        List<Consent> consents = new ArrayList<>();
                        for (GetConsentDto dto : dtos) {
                            consents.add(DtoToConsentMapper.map(dto));
                        }
                        consentListener.onResponseSuccessConsent(consents);
                    }

                    @Override
                    public void onModelDataError(ConsentNetworkError error) {
                        consentListener.onResponseFailureConsent(error);
                    }
                });
                sendRequest(model);
            }
        });
    }

    public void createConsent(final Consent consent, final CreateConsentListener consentListener) {
        retrieveConsentServiceInfo(new ConfigCompletionListener() {
            @Override
            public void onConfigurationCompletion(String cssUrl) {
                ConsentToDtoMapper mapper = new ConsentToDtoMapper(catkComponent.getUser().getHsdpUUID(), catkComponent.getUser().getCountryCode(), propositionName,
                        applicationName);
                CreateConsentModelRequest model = new CreateConsentModelRequest(cssUrl, mapper.map(consent), new NetworkAbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(List<GetConsentDto> dtos) {
                        if (dtos == null) {
                            consentListener.onSuccess();
                        }
                    }

                    @Override
                    public void onModelDataError(ConsentNetworkError error) {
                        consentListener.onFailure(error);
                    }
                });
                sendRequest(model);
            }
        });
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
            public void onResponseFailureConsent(ConsentNetworkError consentError) {
                consentListener.onResponseFailureConsent(consentError);
            }
        });
    }

    interface ConfigCompletionListener {
        void onConfigurationCompletion(String cssUrl);
    }

    void setCatkComponent(CatkComponent component) {
        catkComponent = component;
    }

    void setComponentProvider(ComponentProvider componentProvider) {
        this.componentProvider = componentProvider;
    }

    void setServiceInfoProvider(ServiceInfoProvider serviceInfoProvider) {
        this.serviceInfoProvider = serviceInfoProvider;
    }

    static void setInstance(ConsentAccessToolKit sSoleInstance) {
        ConsentAccessToolKit.sSoleInstance = sSoleInstance;
    }

    private void sendRequest(NetworkAbstractModel model) {
        if (controller == null) {
            controller = new NetworkController();
        }
        controller.sendConsentRequest(model);
    }

    public void setNetworkController(NetworkController networkController) {
        controller = networkController;
    }
}
