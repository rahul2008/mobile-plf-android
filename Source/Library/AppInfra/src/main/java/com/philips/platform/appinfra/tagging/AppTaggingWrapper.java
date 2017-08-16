package com.philips.platform.appinfra.tagging;

/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

import com.philips.platform.appinfra.AppInfra;

/**
 * The Wrapper class for AppTagging.
 */
class AppTaggingWrapper extends AppTagging {

    AppTaggingWrapper(AppInfra aAppInfra, String componentId, String componentVersion) {
        super(aAppInfra);
        mComponentID = componentId;
        mComponentVersion = componentVersion;
        setComponentIdAndVersion(mComponentID, mComponentVersion);
    }
}