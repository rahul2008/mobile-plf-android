/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag.model;


import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import java.util.Map;


public class AIKMService extends ServiceDiscoveryService {

    private String serviceId;
    private Map map;
    private MAP_ERROR mapError;

    public enum MAP_ERROR {
        INDEX_NOT_MAPPED, INVALID_INDEX_URL, INVALID_JSON_STRUCTURE, SERVICE_DISCOVERY_RESPONSE_ERROR
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public MAP_ERROR getMapError() {
        return mapError;
    }

    public void setMapError(MAP_ERROR MAP_error) {
        this.mapError = MAP_error;
    }

}
