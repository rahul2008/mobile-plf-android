/*
 *
 *  * Copyright (c) Koninklijke Philips N.V. 2017
 *  * All rights are reserved. Reproduction or dissemination in whole or in part
 *  * is prohibited without the prior written consent of the copyright holder.
 *
 */

package com.philips.platform.appinfra.servicediscovery.model;


import java.util.Map;
@SuppressWarnings("unchecked")
public class AIKMResponse {

    private Map<?,?> kMap;
    private KError kError;

    public Map<?,?> getkMap() {
        return kMap;
    }

    public void setkMap(Map<?,?> kMap) {
        this.kMap = kMap;
    }

    public KError getkError() {
        return kError;
    }

    public void setkError(KError kError) {
        this.kError = kError;
    }

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
}
