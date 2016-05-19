/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.logging;


import com.philips.platform.appinfra.AppInfra;

/**
 * Created by 310238114 on 4/26/2016.
 */
public class LoggingWrapper extends AppInfraLogging {


    /**
     * Instantiates a new Logging wrapper.
     *
     * @param appInfra         the app infra
     * @param componentId      the component id
     * @param componentVersion the component version
     */
    public LoggingWrapper(AppInfra appInfra, String componentId, String componentVersion) {
        super(appInfra);
        mComponentID = componentId;
        mComponentVersion = componentVersion;

        createLogger(mComponentID);

    }















}
