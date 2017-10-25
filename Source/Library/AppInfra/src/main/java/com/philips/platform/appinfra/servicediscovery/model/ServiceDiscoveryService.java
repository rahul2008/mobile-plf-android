package com.philips.platform.appinfra.servicediscovery.model;

import java.util.Map;

/**
 * The model class of ServiceDiscoveryService.
 */

public class ServiceDiscoveryService {

    private String mLocale;
    private String mConfigUrl;
    private String mError;
    private String serviceId;
    private Map aiKMap;

    private AIKMapError aiKMapError;

    public Map getAIKMap() {
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

    public void init(String localeParam, String configUrlParam) {
        mLocale = localeParam;
        mConfigUrl = configUrlParam;
    }

    public String getmError() {
        return mError;
    }

    public void setmError(String mError) {
        this.mError = mError;
    }

    public String getLocale() {
        return mLocale;
    }

    public String getConfigUrls() {
        return mConfigUrl;
    }

    public void setConfigUrl(String mConfigUrl) {
        this.mConfigUrl = mConfigUrl;
    }

}
