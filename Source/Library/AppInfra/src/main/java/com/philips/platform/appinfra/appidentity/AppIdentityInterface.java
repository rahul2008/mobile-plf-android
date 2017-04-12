/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.appidentity;

/**
 * The Identify of App interface .
 */
public interface AppIdentityInterface {

    enum AppState {DEVELOPMENT,TEST,STAGING,ACCEPTANCE,PRODUCTION};

    /**
     * Gets app name.
     *
     * @return the app name
     */
    public String getAppName();

    /**
     * Gets app version.
     *
     * @return the app version
     */
    public String getAppVersion();

    /**
     * Gets app state.
     *
     * @return the app state
     */
    public AppState getAppState();

    /**
     * Gets app localized n ame.
     *
     * @return the app localized n ame
     */
    public String getLocalizedAppName();

    /**
     * Gets microsite id.
     *
     * @return the microsite id
     */
    public String getMicrositeId();

    /**
     * Gets sector.
     *
     * @return the sector
     */
    public String getSector();

    /**
     * Gets ServiceDiscoveryEnvironment.
     *
     * @return the ServiceDiscoveryEnvironment
     */
    public String getServiceDiscoveryEnvironment();

}
