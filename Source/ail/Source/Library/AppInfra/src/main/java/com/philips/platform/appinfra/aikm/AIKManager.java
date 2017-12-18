/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.aikm;

import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.aikm.exception.AIKMJsonFileNotFoundException;
import com.philips.platform.appinfra.aikm.model.AIKMService;
import com.philips.platform.appinfra.aikm.model.OnGetServicesListener;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AIKMResponse;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AIKManager implements AIKMInterface {

    private static final long serialVersionUID = -2356261564236731323L;
    private final AppInfra appInfra;
    private final AiKmHelper aiKmHelper;

    public enum KError {

        INVALID_INDEX_URL("Invalid index url found from service discovery"),
        DATA_NOT_FOUND("Data not found for provided index in AIKMAP.json"),
        INVALID_JSON("AIKMap.json is an invalid JSON"),
        NO_KINDEX_URL_FOUND("No Kindex URL found"),
        CONVERT_ERROR("Error while converting the value"),
        JSON_FILE_NOT_FOUND("AIKMap.json file not found in assets folder");

        private final String description;

        KError(final String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public AIKManager(AppInfra mAppInfra) {
        this.appInfra = mAppInfra;
        aiKmHelper = new AiKmHelper(mAppInfra);
    }

    @Override
    public void getValueForServiceIds(@NonNull final ArrayList<String> serviceIds, @NonNull AISDResponse.AISDPreference aiSdPreference,
                                      Map<String, String> replacement,
                                      @NonNull final OnGetServicesListener onGetServicesListener) throws AIKMJsonFileNotFoundException, JSONException {

        aiKmHelper.init(appInfra);
        final ArrayList<AIKMService> aiKmServices = new ArrayList<>();
        ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener = getSDUrlsListener(serviceIds, aiKmServices, aiSdPreference, onGetServicesListener);
        getServiceDiscoveryUrlMap(serviceIds, aiSdPreference, replacement, serviceUrlMapListener);
    }

    @NonNull
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener getKMappedGroomListener(final OnGetServicesListener onGetServicesListener, final List<AIKMService> aikmServices, final Map<String, ServiceDiscoveryService> serviceDiscoveryUrlMap) {
        return new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                aiKmHelper.mapResponse(urlMap, aikmServices, serviceDiscoveryUrlMap);
                onGetServicesListener.onSuccess(aikmServices);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                onGetServicesListener.onError(error, message);
            }
        };
    }

    @NonNull
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener getSDUrlsListener(final List<String> serviceIds, final List<AIKMService> aiKmServices,
                                                                           final AISDResponse.AISDPreference aiSdPreference,
                                                                           final OnGetServicesListener onGetServicesListener) {
        return new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                ServiceDiscoveryInterface.OnGetServiceUrlMapListener onGetServiceUrlMapListener = getKMappedGroomListener(onGetServicesListener, aiKmServices, urlMap);
                ArrayList<String> appendedServiceIds = aiKmHelper.getAppendedGrooms(serviceIds);
                getServiceDiscoveryUrlMap(appendedServiceIds, aiSdPreference, null, onGetServiceUrlMapListener);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                onGetServicesListener.onError(error, message);
            }
        };
    }

    void getServiceDiscoveryUrlMap(ArrayList<String> serviceIds, AISDResponse.AISDPreference aiSdPreference,
                                   Map<String, String> replacement,
                                   ServiceDiscoveryInterface.OnGetServiceUrlMapListener serviceUrlMapListener) {
        if (replacement != null) {
            if (aiSdPreference == AISDResponse.AISDPreference.AISDCountryPreference)
                getServiceDiscovery().getServicesWithCountryPreference(serviceIds, serviceUrlMapListener, replacement);
            else if (aiSdPreference == AISDResponse.AISDPreference.AISDLanguagePreference)
                getServiceDiscovery().getServicesWithLanguagePreference(serviceIds, serviceUrlMapListener, replacement);
        } else {
            if (aiSdPreference == AISDResponse.AISDPreference.AISDCountryPreference)
                getServiceDiscovery().getServicesWithCountryPreference(serviceIds, serviceUrlMapListener);
            else if (aiSdPreference == AISDResponse.AISDPreference.AISDLanguagePreference)
                getServiceDiscovery().getServicesWithLanguagePreference(serviceIds, serviceUrlMapListener);
        }
    }

    ServiceDiscoveryInterface getServiceDiscovery() {
        return appInfra.getServiceDiscovery();
    }


    @Override
    public AIKMResponse getServiceExtension(String serviceId, String url) {
        AIKMResponse aikmResponse = new AIKMResponse();
        try {
            aiKmHelper.init(appInfra);
        } catch (AIKMJsonFileNotFoundException | JSONException e) {
            aikmResponse.setkError(KError.JSON_FILE_NOT_FOUND);
            return aikmResponse;
        }
        return aiKmHelper.getServiceExtension(serviceId, url, aikmResponse);
    }

}

