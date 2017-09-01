/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.logging;


import com.philips.platform.appinfra.AppInfra;

/**
 * The Wrapper class for AppInfraLogging.
 */
public class LoggingWrapper extends AppInfraLogging {



    public LoggingWrapper(AppInfra appInfra, String componentId, String componentVersion) {
        super(appInfra);
        mComponentID = componentId;
        mComponentVersion = componentVersion;

        createLogger(mComponentID, mComponentVersion);

    }















}
