/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.prodreg.tagging;

import com.philips.cdp.product_registration_lib.BuildConfig;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;
import java.util.Map;

public class ProdRegTagging {

    private static ProdRegTagging prodRegTagging;
    private static AppTaggingInterface aiAppTaggingInterface;
    private ProdRegTagging() {
    }

    public static ProdRegTagging getInstance() {
        if (prodRegTagging == null) {
            prodRegTagging = new ProdRegTagging();
        }
        return prodRegTagging;
    }

    @SuppressWarnings("deprecation")
    public static void init(AppInfra appInfra) {
        aiAppTaggingInterface = appInfra.getTagging().createInstanceForComponent("Product registration", BuildConfig.VERSION_NAME);
        aiAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);
    }

    public AppTaggingInterface getAiAppTaggingInterface() {
        return aiAppTaggingInterface;
    }

    public void trackPage(String pageName, String key, String value) {
        final Map<String, String> commonGoalsMap = new HashMap<>();
        commonGoalsMap.put(key, value);
        getAiAppTaggingInterface().trackPageWithInfo(pageName, commonGoalsMap);
    }

    public void trackAction(String event, String key, String value) {
        final Map<String, String> commonGoalsMap = new HashMap<>();
        commonGoalsMap.put(key, value);
        getAiAppTaggingInterface().trackActionWithInfo(event, commonGoalsMap);
    }
}
