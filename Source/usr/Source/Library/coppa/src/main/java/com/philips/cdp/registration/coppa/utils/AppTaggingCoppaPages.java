/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.utils;

/**
 * Created by 310190722 on 22-Apr-16.
 */
public interface AppTaggingCoppaPages {

    String COPPA_PARENTAL_ACCESS = "coppa:parentalaccess";

    String COPPA_AGE_VERIFICATION = "coppa:ageverification";

    String COPPA_FIRST_CONSENT = "coppa:parentalconsent";

    String COPPA_SECOND_CONSENT = "coppa:parentalconsentconfirm";

    String COPPA_THANK_AFTER_FIRST_CONSENT = "coppa:thankyouafterfirstconsent";

    String COPPA_CONSENT_PROCESS_COMPLETED = "coppa:thankyouafterconsentprocesscompleted";
}
