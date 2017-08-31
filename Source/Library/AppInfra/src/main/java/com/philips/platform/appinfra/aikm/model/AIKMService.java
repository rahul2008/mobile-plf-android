/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.aikm.model;


import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;

import java.util.Map;


public class AIKMService extends ServiceDiscoveryService {

    private String serviceId;
    private Map map;

    private MapError mapError;

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

    public MapError getMapError() {
        return mapError;
    }

    public void setMapError(MapError mapError) {
        this.mapError = mapError;
    }

    public enum MapError {

        INVALID_INDEX_URL(1, "Invalid index url found from service discovery"),
        BEYOND_BOUND_ERROR(2, "Beyond bounds exception"),
        INVALID_JSON(3, "AIKeyBag.json is an invalid JSON"),
        NO_SERVICE_FOUND(4, "No Service Found From ServiceDiscovery"),
        EMPTY_ARGUMENT_URL(5, "Empty URL argument"),
        DECODE_ERROR(6, "Error while decoding the value");

        private final int id;
        private final String description;

        MapError(final int id, final String description) {
            this.id = id;
            this.description = description;
        }

        public int getCode() {
            return id;
        }

        public String getDescription() {
            return description;
        }
    }

}
