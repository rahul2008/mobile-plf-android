/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.appidentity;

import java.io.Serializable;

/**
 * The Identify of App interface .
 */
public interface AppIdentityInterface extends Serializable {

    enum AppState {DEVELOPMENT,TEST,STAGING,ACCEPTANCE,PRODUCTION}

    /**
     * Gets app name.
     * @return the app name
     * @since 1.0.0
     */
    String getAppName();

    /**
     * Gets app version.
     * @return the app version
     * @since 1.0.0
     */
    String getAppVersion();

    /**
     * Gets app state.
     * @return the app state
     * @since 1.0.0
     */
    AppState getAppState();

    /**
     * Gets app localized n ame.
     * @return the app localized name
     * @since 1.0.0
     */
    String getLocalizedAppName();

    /**
     * Gets microsite id.
     * @return the microsite id
     * @since 1.0.0
     */
    String getMicrositeId();

    /**
     * Gets sector.
     * @return the sector
     * @since 1.0.0
     */
    String getSector();

    /**
     * Gets ServiceDiscoveryEnvironment.
     * @return the ServiceDiscoveryEnvironment
     * @since 1.0.0
     */
    String getServiceDiscoveryEnvironment();

}
