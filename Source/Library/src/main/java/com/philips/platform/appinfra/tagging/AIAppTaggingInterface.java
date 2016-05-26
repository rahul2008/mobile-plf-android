/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.tagging;

import java.util.Map;

/**
 * Created by 310238655 on 5/17/2016.
 */
public interface AIAppTaggingInterface {

    enum PrivacyStatus {OPTIN, OPTOUT, UNKNOWN};


    public AIAppTaggingInterface createInstanceForComponent(String componentId, String componentVersion);

    public void configureAnalyticsWithFilePath(String configFilePath);
    public void setPrivacyConsent(PrivacyStatus privacyStatus);
    public PrivacyStatus getPrivacyConsent();
    public void trackPageWithInfo(String pageName, String key, String value);
    public void trackPageWithInfo(String pageName, Map<String, String> paramDict);
    public void trackActionWithInfo(String pageName, String key, String value);
    public void trackActionWithInfo(String pageName, Map<String, String> paramDict);

    public void collectLifecycleData();
    public void pauseCollectingLifecycleData();
}


