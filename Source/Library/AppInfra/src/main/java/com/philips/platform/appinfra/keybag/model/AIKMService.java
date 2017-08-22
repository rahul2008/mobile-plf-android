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
    private Map keyBag;
    private KEY_BAG_ERROR key_bag_error;

    public enum KEY_BAG_ERROR {
        INDEX_NOT_MAPPED, INVALID_INDEX_URL, INVALID_JSON_STRUCTURE, SERVICE_DISCOVERY_RESPONSE_ERROR
    }

    public Map getKeyBag() {
        return keyBag;
    }

    public void setKeyBag(Map keyBag) {
        this.keyBag = keyBag;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public KEY_BAG_ERROR getKeyBagError() {
        return key_bag_error;
    }

    public void setKeyBagError(KEY_BAG_ERROR key_bag_error) {
        this.key_bag_error = key_bag_error;
    }

}
