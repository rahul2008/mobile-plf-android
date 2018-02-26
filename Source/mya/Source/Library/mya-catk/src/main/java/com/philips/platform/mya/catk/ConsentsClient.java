/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.android.volley.VolleyError;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.mya.catk.dto.CreateConsentDto;
import com.philips.platform.mya.catk.dto.GetConsentDto;
import com.philips.platform.mya.catk.error.ConsentNetworkError;
import com.philips.platform.mya.catk.infra.InfraServiceInfoProvider;
import com.philips.platform.mya.catk.injection.CatkComponent;
import com.philips.platform.mya.catk.injection.CatkComponentFactory;
import com.philips.platform.mya.catk.listener.ConsentResponseListener;
import com.philips.platform.mya.catk.listener.CreateConsentListener;
import com.philips.platform.mya.catk.mapper.ConsentToDtoMapper;
import com.philips.platform.mya.catk.mapper.DtoToConsentMapper;
import com.philips.platform.mya.catk.provider.AppInfraInfo;
import com.philips.platform.mya.catk.provider.ComponentProvider;
import com.philips.platform.mya.catk.provider.ServiceInfoProvider;
import com.philips.platform.mya.catk.utils.CatkLogger;
import com.philips.platform.pif.chi.ConsentRegistryInterface;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import java.util.Arrays;
import android.support.annotation.NonNull;

public class ConsentsClient {

    private static final String PROPOSITION_CONFIG_ERROR = "Missing '%s' -> Please add the following section to AppConfig.json:\n\"hsdp\":\n" +
            "{\n" +
            "    \"appName\": \"<appName>\",\n" +
            "    \"propositionName\": \"<propName>\"\n" +
            "}";
    private static final String PROPOSITION_INIT_ERROR = "ConsentsClient is not initialized. Call ConsentsClient.getInstance().init(catkInputs); before using it";

    private static volatile ConsentsClient sSoleInstance;

    private NetworkController controller;
    private CatkComponent catkComponent;
    private String applicationName;
    private String propositionName;
    private ComponentProvider componentProvider;
    private ServiceInfoProvider serviceInfoProvider;
    private List<ConsentDefinition> consentDefinitionList = new ArrayList<>();
    private Boolean strictConsentCheck;
    private ConsentRegistryInterface consentRegistryInterface;
    private AppInfraInterface appInfra;

    ConsentsClient() {
    }

    public static synchronized ConsentsClient getInstance() {
        if (sSoleInstance == null) {
            sSoleInstance = new ConsentsClient();
        }
        return sSoleInstance;
    }

    public void init(CatkInputs catkInputs) {
        componentProvider = componentProvider == null ? new CatkComponentFactory() : componentProvider;
        serviceInfoProvider = serviceInfoProvider == null ? new InfraServiceInfoProvider() : serviceInfoProvider;
        catkComponent = componentProvider.getComponent(catkInputs);
        initLogging();
        this.consentRegistryInterface = catkInputs.getConsentRegistryInterface();
        appInfra = catkInputs.getAppInfra();
        extractContextNames();
        this.consentDefinitionList = catkInputs.getConsentDefinitions();
        validateAppNameAndPropName();

        registerBackendPlatformConsent();

        final AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface
                .AppConfigurationError();

        final Object strictConsentCheck = catkInputs.getAppInfra().getConfigInterface().getPropertyForKey("strictConsentCheck", "mya", configError);
        this.strictConsentCheck = (strictConsentCheck == null ? false : (Boolean) strictConsentCheck);

    }

    private void registerBackendPlatformConsent() {
        try {
            consentRegistryInterface.register(Arrays.asList("moment", "coaching", "binary", "clickstream", "research", "analytics"), new ConsentInteractor(this));
        } catch (RuntimeException exception) {
            CatkLogger.d("RuntimeException", exception.getMessage());
        }
    }

    private void initLogging() {
        CatkLogger.init();
        CatkLogger.enableLogging();
    }

    private void extractContextNames() {
        AppConfigurationInterface appConfigInterface = appInfra.getConfigInterface();
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
                CatkLogger.e("ConsentsClient", "markErrorAndGetPrevious retrieving cssUrl: " + message);
                listner.onConfigurationError(message);
            }
        };
        serviceInfoProvider.retrieveInfo(catkComponent.getServiceDiscoveryInterface(), responseListener);
    }

    public CatkComponent getCatkComponent() {
        return catkComponent;
    }

    void getConsentDetails(final ConsentResponseListener consentListener) {

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

    void createConsent(final List<BackendConsent> consents, final CreateConsentListener consentListener) {

        validateAppNameAndPropName();

        retrieveConsentServiceInfo(new ConfigCompletionListener() {

            @Override
            public void onConfigurationError(String message) {
                consentListener.onFailure(new ConsentNetworkError(new VolleyError(message)));
            }

            @Override
            public void onConfigurationCompletion(@NonNull String cssUrl) {
                ConsentToDtoMapper mapper = new ConsentToDtoMapper(catkComponent.getUser().getHsdpUUID(), catkComponent.getServiceDiscoveryInterface().getHomeCountry(), propositionName,
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

    void getStatusForConsentType(final String consentType, int version, final ConsentResponseListener consentListener) {

        getConsentDetails(new ConsentResponseListener() {

            @Override
            public void onResponseSuccessConsent(List<BackendConsent> responseData) {
                for (BackendConsent consent : responseData) {
                    if (consentType.equals(consent.getType())) {
                        consentListener.onResponseSuccessConsent(Collections.singletonList(consent));
                        return;
                    }
                }
                consentListener.onResponseSuccessConsent(strictConsentCheck ? null : Collections.singletonList(createAlwaysAcceptedBackendConsent(consentType)));
            }

            @Override
            public void onResponseFailureConsent(ConsentNetworkError consentError) {
                consentListener.onResponseFailureConsent(consentError);
            }

            @NonNull
            private BackendConsent createAlwaysAcceptedBackendConsent(final String consentType) {
                return new BackendConsent(null, ConsentStatus.active, consentType, Integer.MAX_VALUE);
            }
        });
    }

    public List<ConsentDefinition> getConsentDefinitions() {
        return Collections.unmodifiableList(consentDefinitionList);
    }

    public AppInfraInterface getAppInfra() {
        return appInfra;
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

    static void setInstance(ConsentsClient sSoleInstance) {
        ConsentsClient.sSoleInstance = sSoleInstance;
    }

    private void sendRequest(NetworkAbstractModel model) {
        if (controller == null) {
            controller = new NetworkController();
        }
        controller.sendConsentRequest(model);
    }

    void setNetworkController(NetworkController networkController) {
        controller = networkController;
    }
}
