/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;

import java.util.Map;


/**
 * The interface Ai app tagging interface.
 */
public interface AIAppTaggingInterface {

    /**
     * The enum Privacy status.
     */
    enum PrivacyStatus {OPTIN, OPTOUT, UNKNOWN};


    /**
     * Create instance for component ai app tagging interface.
     * This method to be used by all component to get their respective tagging
     * @param componentId      the component id
     * @param componentVersion the component version
     * @return the appinfra app tagging interface
     */
    public AIAppTaggingInterface createInstanceForComponent(String componentId, String componentVersion);

    /**
     * Sets privacy consent.
     *
     * @param privacyStatus the privacy status
     */
    public void setPrivacyConsent(PrivacyStatus privacyStatus);

    /**
     * Sets previous page.
     *
     * @param previousPage the previous page
     */
    public void setPreviousPage(String previousPage);

    /**
     * Gets privacy consent.
     *
     * @return the privacy consent
     */
    public PrivacyStatus getPrivacyConsent();

    /**
     * Track page with info.
     *
     * @param pageName the page name
     * @param key      the key
     * @param value    the value
     */
    public void trackPageWithInfo(String pageName, String key, String value);

    /**
     * Track page with info.
     *
     * @param pageName  the page name
     * @param paramDict set of key/value pairs to be added to the tracking entry
     */
    public void trackPageWithInfo(String pageName, Map<String, String> paramDict);

    /**
     * Track action with info.
     *
     * @param pageName the page name
     * @param key      the key
     * @param value    the value
     */
    public void trackActionWithInfo(String pageName, String key, String value);

    /**
     * Track action with info.
     *
     * @param pageName  the page name
     * @param paramDict set of key/value pairs to be added to the tracking entry
     */
    public void trackActionWithInfo(String pageName, Map<String, String> paramDict);

}


