/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import android.support.annotation.NonNull;

import com.android.volley.VolleyError;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.catk.dto.CreateConsentDto;
import com.philips.platform.catk.dto.GetConsentDto;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.infra.InfraServiceInfoProvider;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.injection.CatkComponentFactory;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.mapper.ConsentToDtoMapper;
import com.philips.platform.catk.mapper.DtoToConsentMapper;
import com.philips.platform.catk.model.BackendConsent;
import com.philips.platform.catk.model.ConsentDefinition;
import com.philips.platform.catk.provider.AppInfraInfo;
import com.philips.platform.catk.provider.ComponentProvider;
import com.philips.platform.catk.provider.ServiceInfoProvider;
import com.philips.platform.catk.utils.CatkLogger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConsentAccessToolKit {

    private static final String PROPOSITION_CONFIG_ERROR = "Missing '%s' -> Please add the following section to AppConfig.json:\n\"hsdp\":\n" +
            "{\n" +
            "    \"appName\": \"<appName>\",\n" +
            "    \"propositionName\": \"<propName>\"\n" +
            "}";
    private static final String PROPOSITION_INIT_ERROR = "ConsentAccessToolKit is not initialized. Call ConsentAccessToolKit.getInstance().init(catkInputs); before using it";

    private static volatile ConsentAccessToolKit sSoleInstance;

    private NetworkController controller;
    private CatkComponent catkComponent;
    private String applicationName;
    private String propositionName;
    private ComponentProvider componentProvider;
    private ServiceInfoProvider serviceInfoProvider;
    private List<ConsentDefinition> consentDefinitions;

    ConsentAccessToolKit() {
    }

    public static synchronized ConsentAccessToolKit getInstance() {
        if (sSoleInstance == null) {
            sSoleInstance = new ConsentAccessToolKit();
        }
        return sSoleInstance;
    }

    public void init(CatkInputs catkInputs) {
        componentProvider = componentProvider == null ? new CatkComponentFactory() : componentProvider;
        serviceInfoProvider = serviceInfoProvider == null ? new InfraServiceInfoProvider() : serviceInfoProvider;
        catkComponent = componentProvider.getComponent(catkInputs);
        initLogging();
        extractContextNames(catkInputs);
        this.consentDefinitions = catkInputs.getConsentDefinitions();
        validateAppNameAndPropName();
    }

    private void initLogging() {
        CatkLogger.init();
        CatkLogger.enableLogging();
    }

    private void extractContextNames(CatkInputs catkInputs) {
        AppConfigurationInterface appConfigInterface = catkInputs.getAppInfra().getConfigInterface();
        AppConfigurationInterface.AppConfigurationError error = new AppConfigurationInterface.AppConfigurationError();
        this.applicationName = (String) appConfigInterface.getPropertyForKey("appName", "hsdp", error);
        this.propositionName = (String) appConfigInterface.getPropertyForKey("propositionName", "hsdp", error);
    }

    private void validateAppNameAndPropName() {
        if (this.serviceInfoProvider == null) {
            throw new IllegalStateException(PROPOSITION_INIT_ERROR);
        } else if (this.applicationName == null || "".equals(this.applicationName)) {
            throw new IllegalStateException(String.format(PROPOSITION_CONFIG_ERROR, "appName"));
        } else if (this.propositionName == null || "".equals(this.propositionName)) {
            throw new IllegalStateException(String.format(PROPOSITION_CONFIG_ERROR, "propName"));
        }
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
                listner.onConfigurationError(message);
            }
        };
        serviceInfoProvider.retrieveInfo(catkComponent.getServiceDiscoveryInterface(), responseListener);
    }

    public CatkComponent getCatkComponent() {
        return catkComponent;
    }

    public void getConsentDetails(final ConsentResponseListener consentListener) {

        validateAppNameAndPropName();

        retrieveConsentServiceInfo(new ConfigCompletionListener() {

            @Override
            public void onConfigurationError(String message) {
                consentListener.onResponseFailureConsent(new ConsentNetworkError(new VolleyError(message)));
            }

            @Override
            public void onConfigurationCompletion(@NonNull String cssUrl) {
                GetConsentsModelRequest model = new GetConsentsModelRequest(cssUrl, applicationName, propositionName, new NetworkAbstractModel.DataLoadListener() {
                    @Override
                    public void onModelDataLoadFinished(List<GetConsentDto> dtos) {
                        List<BackendConsent> consents = new ArrayList<>();
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

    public void createConsent(final List<BackendConsent> consents, final CreateConsentListener consentListener) {

        validateAppNameAndPropName();

        retrieveConsentServiceInfo(new ConfigCompletionListener() {

            @Override
            public void onConfigurationError(String message) {
                consentListener.onFailure(new ConsentNetworkError(new VolleyError(message)));
            }

            @Override
            public void onConfigurationCompletion(@NonNull String cssUrl) {
                ConsentToDtoMapper mapper = new ConsentToDtoMapper(catkComponent.getUser().getHsdpUUID(), catkComponent.getUser().getCountryCode(), propositionName,
                        applicationName);
                for (BackendConsent consent : consents) {
                    CreateConsentDto consentDto = mapper.map(consent);
                    CreateConsentModelRequest model = new CreateConsentModelRequest(cssUrl, consentDto, new NetworkAbstractModel.DataLoadListener() {
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
            }
        });
    }

    public void getStatusForConsentType(final String consentType, int version, final ConsentResponseListener consentListener) {

        getConsentDetails(new ConsentResponseListener() {

            @Override
            public void onResponseSuccessConsent(List<BackendConsent> responseData) {
                for (BackendConsent consent : responseData) {
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

    public List<ConsentDefinition> getConsentDefinitions() {
        return consentDefinitions;
    }

    void setConsentDefinitions(List<ConsentDefinition> consentDefinitions) {
        this.consentDefinitions = consentDefinitions;
    }

    interface ConfigCompletionListener {
        void onConfigurationCompletion(@NonNull String cssUrl);

        void onConfigurationError(String message);
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
