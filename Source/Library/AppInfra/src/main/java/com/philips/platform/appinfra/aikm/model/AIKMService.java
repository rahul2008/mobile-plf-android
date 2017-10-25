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


}
