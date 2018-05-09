/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.aikm.model;


import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import java.util.Map;

@SuppressWarnings("unchecked")
public class AIKMService extends ServiceDiscoveryService {

    private String serviceId;
    private Map aiKMap;

    private AIKMapError aiKMapError;

    public Map<?,?> getAIKMap() {
        return aiKMap;
    }

    public void setAIKMap(Map aikmap) {
        this.aiKMap = aikmap;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public AIKMapError getAIKMapError() {
        return aiKMapError;
    }

    public void setAIKMapError(AIKMapError AIKMapError) {
        this.aiKMapError = AIKMapError;
    }

    public enum AIKMapError {

        INVALID_INDEX_URL("Invalid index url found from service discovery"),
        INDEX_NOT_FOUND("Index not found exception"),
        INVALID_JSON("AIKMap.json is an invalid JSON"),
        NO_SERVICE_FOUND("No Service Found From ServiceDiscovery"),
        NO_URL_FOUND("No URL found"),
        CONVERT_ERROR("Error while converting the value");

        private final String description;

        AIKMapError(final String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

}
