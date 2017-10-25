/*
 *
 *  * Copyright (c) Koninklijke Philips N.V. 2017
 *  * All rights are reserved. Reproduction or dissemination in whole or in part
 *  * is prohibited without the prior written consent of the copyright holder.
 *
 */

package com.philips.platform.appinfra.servicediscovery;

/**
 * Created by Yogesh on 10/25/17.
 */

public enum KError {

    INVALID_INDEX_URL("Invalid index url found from service discovery"),
    INDEX_NOT_FOUND("Index not found exception"),
    INVALID_JSON("AIKMap.json is an invalid JSON"),
    NO_SERVICE_FOUND("No Service Found From ServiceDiscovery"),
    NO_URL_FOUND("No URL found"),
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
